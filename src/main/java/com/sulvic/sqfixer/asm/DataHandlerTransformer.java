package com.sulvic.sqfixer.asm;

import static org.objectweb.asm.ClassWriter.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class DataHandlerTransformer implements IClassTransformer{
	
	private byte[] patchReputationHandler(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's ReputationHandler");
		for(MethodNode methodNode: classNode.methods) if(methodNode.name.equals("onReputationChange")){
			methodNode.instructions.clear();
			methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
			methodNode.instructions.add(new VarInsnNode(ALOAD, 1));
			methodNode.instructions.add(new VarInsnNode(ILOAD, 2));
			methodNode.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/sulvic/sqfixer/handler/SulvicReputationHandler", "onReputationChange", methodNode.desc, false));
			methodNode.instructions.add(new InsnNode(RETURN));
			break;
		}
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		FixerLoadingPlugin.logger.info("Patched Spider Queen's ReputationHandler");
		return writer.toByteArray();
	}
	
	public byte[] patchModItems(byte[] basicClass){
		System.out.println(basicClass.length);
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's ItemOffering");
		classNode.visitField(ACC_PRIVATE, "sulvicPatch", "Lcom/sulvic/sqfixer/helper/OfferingPatchHelper;", null, null).visitEnd();
		for(MethodNode methodNode: classNode.methods){
			if(methodNode.name.equals("<init>")){
				for(AbstractInsnNode insnNode: methodNode.instructions.toArray()){
					if(insnNode.getOpcode() == RETURN){
						InsnList insnList = new InsnList();
						insnList.add(new LabelNode());
						insnList.add(new VarInsnNode(ALOAD, 0));
						insnList.add(new TypeInsnNode(NEW, "com/sulvic/sqfixer/helper/OfferingPatchHelper"));
						insnList.add(new InsnNode(DUP));
						insnList.add(new MethodInsnNode(INVOKESPECIAL, "com/sulvic/sqfixer/helper/OfferingPatchHelper", "<init>", "()V", false));
						insnList.add(new FieldInsnNode(PUTFIELD, "sq/items/ItemOffering", "sulvicPatch", "Lcom/sulvic/sqfixer/helper/OfferingPatchHelper;"));
						methodNode.instructions.insertBefore(insnNode.getPrevious(), insnList);
						break;
					}
				}
			}
			if(methodNode.name.equals("onEntityItemUpdate")){
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "sulvicPatch", "Lcom/sulvic/sqfixer/helper/OfferingPatchHelper;"));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "offeringType", "Lsq/enums/EnumOfferingType;"));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sulvic/sqfixer/helper/OfferingPatchHelper", "onEntityItemUpdate", "(Lnet/minecraft/entity/item/EntityItem;Lsq/enums/EnumOfferingType;)Z", false));
				insnList.add(new InsnNode(IRETURN));
				insnList.add(new LabelNode());
				methodNode.instructions.add(insnList);
			}
		}
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		FixerLoadingPlugin.logger.info("Patched Spider Queen's ItemOffering");
		return writer.toByteArray();
	}
	
	public byte[] transform(String name, String transformedName, byte[] basicClass){
		if(transformedName.equals("sq.core.ReputationHandler")) return patchReputationHandler(basicClass);
		if(transformedName.equals("sq.items.ItemOffering")) return patchModItems(basicClass);
		return basicClass;
	}
	
}

package com.sulvic.sqfixer.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class DataHandlerTransformer implements IClassTransformer{
	
	private byte[] patchEventHooksFML(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's EventHooksFML");
		for(MethodNode methodNode: classNode.methods){
			if(methodNode.name.equals("serverTickEventHandler")){
				FixerLoadingPlugin.logger.info(methodNode.visibleAnnotations);
				methodNode.visibleAnnotations.clear();
				break;
			}
		}
		FixerLoadingPlugin.logger.info("Patched Spider Queen's EventHooksFML");
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	private byte[] patchFullWebBlock(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's BlockWebFull");
		classNode.visitField(ACC_PRIVATE, "helper", "Lcom/sulvic/sqfixer/helper/BedCheckHelper;", null, null).visitEnd();
		for(MethodNode methodNode: classNode.methods){
			if(methodNode.name.equals("<init>")) for(AbstractInsnNode insnNode: methodNode.instructions.toArray()) if(insnNode.getOpcode() == RETURN){
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new TypeInsnNode(NEW, "com/sulvic/sqfixer/helper/BedCheckHelper"));
				insnList.add(new InsnNode(DUP));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new MethodInsnNode(INVOKESPECIAL, "com/sulvic/sqfixer/helper/BedCheckHelper", "<init>", "(Lsq/blocks/BlockWebFull;)V", false));
				insnList.add(new FieldInsnNode(PUTFIELD, "sq/blocks/BlockWebFull", "helper", "Lcom/sulvic/sqfixer/helper/BedCheckHelper;"));
				methodNode.instructions.insertBefore(insnNode.getPrevious(), insnList);
				break;
			}
			if(methodNode.name.equals("checkForBed")){
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/blocks/BlockWebFull", "helper", "Lcom/sulvic/sqfixer/helper/BedCheckHelper;"));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ILOAD, 2));
				insnList.add(new VarInsnNode(ILOAD, 3));
				insnList.add(new VarInsnNode(ILOAD, 4));
				insnList.add(new VarInsnNode(ILOAD, 5));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sulvic/sqfixer/helper/BedCheckHelper", "checkForBed", "(Lnet/minecraft/world/World;IIII)V", false));
				insnList.add(new LabelNode());
				insnList.add(new InsnNode(RETURN));
				methodNode.instructions.insert(insnList);
				break;
			}
		}
		FixerLoadingPlugin.logger.info("Patched Spider Queen's BlockWebFull");
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	private byte[] patchItemOfferings(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's ItemOffering");
		classNode.visitField(ACC_PRIVATE, "sulvicPatch", "Lcom/sulvic/sqfixer/helper/OfferingPatchHelper;", null, null).visitEnd();
		for(MethodNode methodNode: classNode.methods){
			if(methodNode.name.equals("<init>")) for(AbstractInsnNode insnNode: methodNode.instructions.toArray()) if(insnNode.getOpcode() == RETURN){
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
			if(methodNode.name.equals("onEntityItemUpdate")){
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "sulvicPatch", "Lcom/sulvic/sqfixer/helper/OfferingPatchHelper;"));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "offeringType", "Lsq/enums/EnumOfferingType;"));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sulvic/sqfixer/helper/OfferingPatchHelper", "onEntityItemUpdate",
					"(Lnet/minecraft/entity/item/EntityItem;Lsq/enums/EnumOfferingType;)Z", false));
				insnList.add(new InsnNode(IRETURN));
				insnList.add(new LabelNode());
				methodNode.instructions.add(insnList);
				break;
			}
		}
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		FixerLoadingPlugin.logger.info("Patched Spider Queen's ItemOffering");
		return writer.toByteArray();
	}
	
	private byte[] patchReputationHandler(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's ReputationHandler");
		for(MethodNode methodNode: classNode.methods) if(methodNode.name.equals("onReputationChange")){
			methodNode.instructions.clear();
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(ALOAD, 0));
			insnList.add(new VarInsnNode(ALOAD, 1));
			insnList.add(new VarInsnNode(ILOAD, 2));
			insnList.add(new MethodInsnNode(INVOKESTATIC, "com/sulvic/sqfixer/handler/SulvicReputationHandler", "onReputationChange", methodNode.desc, false));
			insnList.add(new InsnNode(RETURN));
			methodNode.instructions.add(insnList);
			break;
		}
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		FixerLoadingPlugin.logger.info("Patched Spider Queen's ReputationHandler");
		return writer.toByteArray();
	}
	
	public byte[] transform(String name, String transformedName, byte[] basicClass){
		if(transformedName.equals("sq.blocks.BlockWebFull")) return patchFullWebBlock(basicClass);
		if(transformedName.equals("sq.core.ReputationHandler")) return patchReputationHandler(basicClass);
		if(transformedName.equals("sq.core.forge.EventHooksFML")) return patchEventHooksFML(basicClass);
		if(transformedName.equals("sq.items.ItemOffering")) return patchItemOfferings(basicClass);
		return basicClass;
	}
	
}

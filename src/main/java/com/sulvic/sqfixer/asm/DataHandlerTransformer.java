package com.sulvic.sqfixer.asm;

import static org.objectweb.asm.ClassWriter.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class DataHandlerTransformer implements IClassTransformer{

	private byte[] patchEventHooksFML(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's EventHooksFML");
		for(MethodNode methodNode: classNode.methods) if(methodNode.name.equals("serverTickEventHandler")){
			methodNode.visibleAnnotations.clear();
			break;
		}
		FixerLoadingPlugin.logger.info("Patched Spider Queen's EventHooksFML");
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	private byte[] patchEventHooksForge(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		FixerLoadingPlugin.logger.info("Patching Spider Queen's EventHooksForge");
		for(MethodNode methodNode: classNode.methods) if(methodNode.name.equals("onRenderPlayerPre")){
			methodNode.visibleAnnotations.clear();
			break;
		}
		FixerLoadingPlugin.logger.info("Patched Spider Queen's EventHooksForge");
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

	private byte[] patchHumanBlockWeight(byte[] basicClass){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "getBlockPathWeight", "(III)F", null, null);
		methodVisitor.visitParameter("p_70783_1_", ACC_MANDATED);
		methodVisitor.visitParameter("p_70783_2_", ACC_MANDATED);
		methodVisitor.visitParameter("p_70783_3_", ACC_MANDATED);
		methodVisitor.visitEnd();
		for(MethodNode methodNode: classNode.methods) if(methodNode.name.equals("getBlockPathWeight") && methodNode.desc.equals("(III)F")){
			InsnList insnList = new InsnList();
			LabelNode secondNode = new LabelNode();
			insnList.add(new LabelNode());
			insnList.add(new VarInsnNode(ALOAD, 0));
			insnList.add(new FieldInsnNode(GETFIELD, "sq/entity/creature/EntityHuman", "worldObj", "Lnet/minecraft/world/World;"));
			insnList.add(new VarInsnNode(ILOAD, 1));
			insnList.add(new VarInsnNode(ILOAD, 2));
			insnList.add(new InsnNode(ICONST_1));
			insnList.add(new InsnNode(ISUB));
			insnList.add(new VarInsnNode(ILOAD, 3));
			insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "getBlock", "(III)Lnet/minecraft/block/Block;", false));
			insnList.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", "grass", "Lnet/minecraft/block/BlockGrass;"));
			insnList.add(new JumpInsnNode(IF_ACMPNE, secondNode));
			insnList.add(new InsnNode(FRETURN));
			insnList.add(secondNode);
			insnList.add(new VarInsnNode(ALOAD, 0));
			insnList.add(new FieldInsnNode(GETFIELD, "sq/entity/creature/EntityHuman", "worldObj", "Lnet/minecraft/world/World;"));
			insnList.add(new VarInsnNode(ILOAD, 1));
			insnList.add(new VarInsnNode(ILOAD, 2));
			insnList.add(new VarInsnNode(ILOAD, 3));
			insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "getLightBrightness", "(III)F", false));
			insnList.add(new LdcInsnNode(0.5f));
			insnList.add(new InsnNode(FSUB));
			insnList.add(new InsnNode(FRETURN));
		}
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
		if(transformedName.equals("sq.core.forge.EventHooksForge")) return patchEventHooksForge(basicClass);
		if(transformedName.equals("sq.core.creature.EntityHuman")) return patchHumanBlockWeight(basicClass);
		if(transformedName.equals("sq.items.ItemOffering")) return patchItemOfferings(basicClass);
		return basicClass;
	}

}

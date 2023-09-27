package com.sulvic.sqfixer.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.*;

public class SpiderQueenPatcher{

	protected static void patchEventsFML(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's EventHooksFML.");
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "serverTickEventHandler":
				for(AnnotationNode annoNode: methodNode.visibleAnnotations) if(annoNode.desc.equals("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;")){
					methodNode.visibleAnnotations.remove(annoNode);
					break;
				}
			break;
		}
		SpiderFixerPlugin.logger.info("Patched Spider Queen's EventHooksFML.");
	}

	protected static void patchEventsForge(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's EventHooksForge.");
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "onRenderPlayerPre":
				for(AnnotationNode annoNode: methodNode.visibleAnnotations) if(annoNode.desc.equals("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;")){
					methodNode.visibleAnnotations.remove(annoNode);
					break;
				}
			break;
		}
		SpiderFixerPlugin.logger.info("Patching Spider Queen's EventHooksForge.");

	}

	protected static void patchItemOffering(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's ItemOffering.");
		classNode.visitField(ACC_PRIVATE, "offerPatch", "Lcom/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch;", null, null).visitEnd();
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "<init>":{
				AbstractInsnNode lastLabel = methodNode.instructions.getLast().getPrevious();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new TypeInsnNode(NEW, "com/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch"));
				insnList.add(new InsnNode(DUP));
				insnList.add(new MethodInsnNode(INVOKESPECIAL, "com/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch", methodNode.name, "()V", false));
				insnList.add(new FieldInsnNode(PUTFIELD, "sq/items/ItemOffering", "offerPatch", "Lcom/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch;"));
				methodNode.instructions.insertBefore(lastLabel, insnList);
			}
			break;
			case "getOfferingType":{
				methodNode.access = ACC_PUBLIC;
			}
			break;
			case "onEntityItemUpdate":{
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "offerPatch", "Lcom/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch;"));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new FieldInsnNode(GETFIELD, "sq/items/ItemOffering", "offeringType", "Lsq/enums/EnumOfferingType;"));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/sulvic/sqfixer/asm/FixerHandlers$OfferingPatch", methodNode.name,
					"(Lnet/minecraft/entity/item/EntityItem;Lsq/enums/EnumOfferingType;)Z", false));
				insnList.add(new InsnNode(IRETURN));
				insnList.add(new LabelNode());
				methodNode.instructions.add(insnList);
			}
			break;
		}
		SpiderFixerPlugin.logger.info("Patched Spider Queen's ItemOffering.");
	}

	protected static void patchMandCrop(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's BlockMandCrop.");
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "updateTick":
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ILOAD, 2));
				insnList.add(new VarInsnNode(ILOAD, 3));
				insnList.add(new VarInsnNode(ILOAD, 4));
				insnList.add(new VarInsnNode(ALOAD, 5));
				insnList.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/block/BlockCrops", methodNode.name, methodNode.desc, false));
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ILOAD, 2));
				insnList.add(new VarInsnNode(ILOAD, 3));
				insnList.add(new VarInsnNode(ILOAD, 4));
				insnList.add(new VarInsnNode(ALOAD, 5));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "com/sulvic/sqfixer/EventHandlers", "updateMandragoraTick", methodNode.desc, false));
				insnList.add(new LabelNode());
				insnList.add(new InsnNode(RETURN));
				methodNode.instructions.add(insnList);
			break;
		}
		SpiderFixerPlugin.logger.info("Patched Spider Queen's BlockMandCrop.");
	}

	protected static void patchReputationHandler(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's ReputationChanger.");
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "onReputationChange":{
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ILOAD, 2));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "com/sulvic/sqfixer/asm/FixerHandlers", methodNode.name, methodNode.desc, false));
				insnList.add(new InsnNode(RETURN));
				insnList.add(new LabelNode());
				methodNode.instructions.add(insnList);
			}
			break;
		}
		SpiderFixerPlugin.logger.info("Patched Spider Queen's ReputationChanger.");
	}

	protected static void patchWebFull(ClassNode classNode){
		SpiderFixerPlugin.logger.info("Patching Spider Queen's BlockWebFull.");
		for(MethodNode methodNode: classNode.methods) switch(methodNode.name){
			case "checkForBed":
				methodNode.instructions.clear();
				InsnList insnList = new InsnList();
				insnList.add(new LabelNode());
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new VarInsnNode(ALOAD, 1));
				insnList.add(new VarInsnNode(ILOAD, 2));
				insnList.add(new VarInsnNode(ILOAD, 3));
				insnList.add(new VarInsnNode(ILOAD, 4));
				insnList.add(new VarInsnNode(ILOAD, 5));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "com/sulvic/sqfixer/asm/FixerHandlers", methodNode.name, "(Lsq/blocks/BlockWebFull;Lnet/minecraft/world/World;IIII)V", false));
				insnList.add(new LabelNode());
				insnList.add(new InsnNode(RETURN));
				methodNode.instructions.add(insnList);
			break;
		}
		SpiderFixerPlugin.logger.info("Patched Spider Queen's BlockWebFull.");
	}

}

package com.sulvic.sqfixer.asm;

import static org.objectweb.asm.ClassWriter.*;

import java.util.function.Consumer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class DataTransformHandler implements IClassTransformer{

	protected static byte[] patchClass(byte[] basicClass, Consumer<ClassNode> consumer){
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(basicClass);
		reader.accept(classNode, 0);
		ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
		consumer.accept(classNode);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] transform(String name, String transformedName, byte[] basicClass){
		switch(transformedName){
			case "sq.blocks.BlockMandCrop":
				return patchClass(basicClass, SpiderQueenPatcher::patchMandCrop);
			case "sq.blocks.BlockWebFull":
				return patchClass(basicClass, SpiderQueenPatcher::patchWebFull);
			case "sq.core.forge.EventHooksFML":
				return patchClass(basicClass, SpiderQueenPatcher::patchEventsFML);
			case "sq.core.forge.EventHooksForge":
				return patchClass(basicClass, SpiderQueenPatcher::patchEventsForge);
			case "sq.core.ReputationHandler":
				return patchClass(basicClass, SpiderQueenPatcher::patchReputationHandler);
			case "sq.items.ItemOffering":
				return patchClass(basicClass, SpiderQueenPatcher::patchItemOffering);
			default:
				return basicClass;
		}
	}

}

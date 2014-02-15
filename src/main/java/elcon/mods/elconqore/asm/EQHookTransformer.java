package elcon.mods.elconqore.asm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import elcon.mods.elconqore.ElConQore;

public class EQHookTransformer implements IClassTransformer, Opcodes {

	public static class Hook {

		public String name = "";
		public String desc = "";
		
		@Override
		public String toString() {
			return "Hook[name=" + name + ", desc=" + desc + "]";
		}
	}
	
	public static Multimap<String, Hook> hooks = ArrayListMultimap.create();
	public static ArrayList<String> hookFiles = new ArrayList<String>();
	
	public static void readHooksFile(String hooksFile) {
		if(hookFiles.contains(hooksFile)) {
			return;
		}
		hookFiles.add(hooksFile);
		try {
			File file = new File(hooksFile);
			URL hooksResource;
			if(file.exists()) {
				hooksResource = file.toURI().toURL();
			} else {
				hooksResource = Resources.getResource(hooksFile);
			}
			Resources.readLines(hooksResource, Charsets.UTF_8, new LineProcessor<Void>() {
				@Override
				public Void getResult() {
					return null;
				}

				@Override
				public boolean processLine(String input) throws IOException {
					String line = Iterables.getFirst(Splitter.on('#').limit(2).split(input), "").trim();
					if(line.length() == 0) {
						return true;
					}
					List<String> parts = Lists.newArrayList(Splitter.on(" ").trimResults().split(line));
					if(parts.size() != 2) {
						throw new RuntimeException("Invalid hook file line " + input);
					}
					Hook hook = new Hook();
					String nameReference = parts.get(1);
					int parenIdx = nameReference.indexOf('(');
					if(parenIdx > 0) {
						hook.desc = nameReference.substring(parenIdx);
						hook.name = nameReference.substring(0, parenIdx);
					} else {
						hook.name = nameReference;
					}
					String className = parts.get(0).replace('/', '.');
					hooks.put(className, hook);
					return true;
				}
			});
			ElConQore.log.info("[HookAPI] Loaded hooks from file: " + hooksFile);
			ElConQore.log.info("[HookAPI] " + hooks);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		if(!hooks.containsKey(transformedName)) {
			return bytes;
		}
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		Collection<Hook> hookList = hooks.get(transformedName);
		for(Hook hook : hookList) {
			for(MethodNode method : classNode.methods) {
				if((method.name.equals(hook.name) && method.desc.equals(hook.desc))) {
					method.instructions.insert(createInstructionList(hook));					
					ElConQore.log.info("[HookAPI] Added hook: " + hook.name + " " + hook.desc + " to " + transformedName);
				}
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public InsnList createInstructionList(Hook hook) {
		InsnList list = new InsnList();
		MethodNode mv = new MethodNode();
		list.accept(mv);
		
		//TODO: 
		/*if(hook.desc.endsWith(")")) {
			Label l0 = new Label();
			mv.visitLabel(l0);
			//mv.visitLineNumber(10, l0);
			mv.visitTypeInsn(NEW, "elcon/mods/elconqore/hooks/HookEvent");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("HookAPI.test");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKESPECIAL, "elcon/mods/elconqore/hooks/HookEvent", "<init>", "(Ljava/lang/String;[Ljava/lang/Object;)V");
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			//mv.visitLineNumber(11, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "elcon/mods/elconqore/hooks/HookAPI", "post", "(Lelcon/mods/elconqore/hooks/HookEvent;)Z");
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			//mv.visitLineNumber(12, l3);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			//mv.visitLineNumber(14, l2);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"elcon/mods/elconqore/hooks/HookEvent"}, 0, null);
			mv.visitInsn(RETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Lelcon/mods/elconqore/hooks/HookAPI;", null, l0, l4, 0);
			mv.visitLocalVariable("event", "Lelcon/mods/elconqore/hooks/HookEvent;", "Lelcon/mods/elconqore/hooks/HookEvent<Ljava/lang/Void;>;", l1, l4, 1);
			mv.visitMaxs(7, 4);
		} else {
			
		}*/
		mv.visitEnd();
		return list;
	}
}

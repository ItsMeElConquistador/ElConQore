package elcon.mods.elconqore.asm;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import elcon.mods.elconqore.ElConQore;

public class EQInstanceTransformer implements IClassTransformer {

	public static class InstanceTransform {

		public String classDetectionMCP;
		public String classDetectionOBF;
		public String classMCP;
		public String classOBF;
		public String newClass;
		public int classWriterFlag;

		public InstanceTransform(String classDetectionMCP, String classDetectionOBF, String classMCP, String classOBF, String newClass, int classWriterFlag) {
			this.classDetectionMCP = classDetectionMCP;
			this.classDetectionOBF = classDetectionOBF;
			this.classMCP = classMCP;
			this.classOBF = classOBF;
			this.newClass = newClass;
			this.classWriterFlag = classWriterFlag;
		}
	}

	private static boolean checkedObfuscation;
	private static boolean isObfuscated;

	private static ArrayList<InstanceTransform> instanceTransforms = new ArrayList<InstanceTransform>();

	public EQInstanceTransformer() {
		addInstanceTransforms();
	}

	public void addInstanceTransforms() {
		if(instanceTransforms.isEmpty()) {
			ElConQore.log.info("[InstanceTransformer] Registered instance transforms");
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(!checkedObfuscation) {
			LaunchClassLoader cl = (LaunchClassLoader) EQLoadingPlugin.class.getClassLoader();
			try {
				isObfuscated = cl.getClassBytes("net.minecraft.world.World") == null;
			} catch(Exception e) {
			}
			checkedObfuscation = true;
		}
		addInstanceTransforms();
		for(InstanceTransform transform : instanceTransforms) {
			if(transform != null && (name.equals(transform.classDetectionMCP) || name.equals(transform.classDetectionOBF))) {
				ClassReader classReader = new ClassReader(bytes);
				ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);

				replaceInstance(classNode, isObfuscated ? transform.classOBF : transform.classMCP, transform.newClass);

				ClassWriter cw = new ClassWriter(transform.classWriterFlag);
				classNode.accept(cw);
				return cw.toByteArray();
			}
		}
		return bytes;
	}

	public void replaceInstance(ClassNode classNode, String oldInstance, String newInstance) {
		ElConQore.log.info("[InstanceTransformer] Class: " + classNode.name);
		ElConQore.log.info("[InstanceTransformer] Replacing " + oldInstance + " with " + newInstance);

		oldInstance = oldInstance.replace(".", "/");
		newInstance = newInstance.replace(".", "/");

		for(MethodNode methodNode : classNode.methods) {
			Iterator<AbstractInsnNode> iter = methodNode.instructions.iterator();
			TypeInsnNode previousTypeInsnNode = null;

			while(iter.hasNext()) {
				AbstractInsnNode node = (AbstractInsnNode) iter.next();
				if((node instanceof TypeInsnNode)) {
					TypeInsnNode tn = (TypeInsnNode) node;
					if(tn.desc.equals(oldInstance)) {
						previousTypeInsnNode = tn;
					}
				}
				if((node instanceof MethodInsnNode)) {
					MethodInsnNode mn = (MethodInsnNode) node;
					if((mn.owner.equals(oldInstance)) && (mn.name.equals("<init>"))) {
						mn.owner = newInstance;
						previousTypeInsnNode.desc = newInstance;
						ElConQore.log.info("[InstanceTransformer] Found injection point: method: " + methodNode.name + " typeinsn: " + methodNode.instructions.indexOf(previousTypeInsnNode) + " methodinsn: " + methodNode.instructions.indexOf(mn));
					}
				}
			}
		}
	}
}

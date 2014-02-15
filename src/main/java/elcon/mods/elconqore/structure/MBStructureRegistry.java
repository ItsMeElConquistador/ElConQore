package elcon.mods.elconqore.structure;

import java.util.HashMap;

import elcon.mods.elconqore.ElConQore;

public class MBStructureRegistry {

private static HashMap<String, MBStructure> structures = new HashMap<String, MBStructure>();
	
	public static MBStructure getStructure(String structure) {
		return structures.get(structure);
	}
	
	public static boolean hasStructure(String structure) {
		return structures.containsKey(structure);
	}
	
	public static void registerStructure(MBStructure structure) {
		if(structures.containsKey(structure.name)) {
			ElConQore.log.warn("[MultiBlockStructures] Overriding existing structure (" + structures.get(structure).name + ")");
		}
		structures.put(structure.name, structure);
	}
	
	public static void unregisterStructure(String structure) {
		if(structures.containsKey(structure)) {
			structures.remove(structure);
		}
	}

	public static MBStructurePattern getStructurePattern(String structureName, String structurePatternName) {
		MBStructure structure = getStructure(structureName);
		if(structure != null) {
			return structure.patterns.get(structurePatternName);
		}
		return null;
	}
	
	public static boolean hasStructurePattern(String structureName, String structurePatternName) {
		MBStructure structure = getStructure(structureName);
		if(structure != null) {
			return structure.patterns.containsKey(structurePatternName);
		}
		return false;
	}
}

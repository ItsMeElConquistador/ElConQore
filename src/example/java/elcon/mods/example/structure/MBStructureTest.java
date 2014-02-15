package elcon.mods.example.structure;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import elcon.mods.elconqore.structure.MBStructure;
import elcon.mods.elconqore.structure.MBStructurePattern;
import elcon.mods.example.ExampleMod;

public class MBStructureTest extends MBStructure {

	public MBStructureTest() {
		super("test");
		metaOnValid.put('B', 256);
		for(int i = 3; i < 6; i++) {
			int halfSize = (int) Math.floor(i / 2.0D);
			MBStructurePattern pattern = new MBStructurePattern(i + "x" + i, i, 3, i).setOffsets(-halfSize, 0, -halfSize);
			String patternString = "";
			for(int y = 0; y < 3; y++) {
				for(int x = 0; x < i; x++) {
					for(int z = 0; z < i; z++) {
						patternString += "A";
					}
				}
			}
			pattern.setStructure(patternString);
			pattern.structure[halfSize][1][0] = 'B';
			addPattern(pattern);
		}
	}

	@Override
	public boolean areCharAndBlockEqual(char required, Block block, int blockMetadata, TileEntity blockTileEntity) {
		return required == 'B' ? Block.getIdFromBlock(block) == Block.getIdFromBlock(ExampleMod.coloredWool) : Block.getIdFromBlock(block) == Block.getIdFromBlock(ExampleMod.structureTestBlock);
	}
}

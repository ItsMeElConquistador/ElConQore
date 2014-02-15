package elcon.mods.example.structure;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import elcon.mods.elconqore.structure.MBStructure;
import elcon.mods.elconqore.structure.MBStructurePattern;
import elcon.mods.example.ExampleMod;

public class MBStructureTest extends MBStructure {

	public MBStructureTest() {
		super("test");
		for(int i = 3; i < 6; i++) {
			MBStructurePattern pattern = new MBStructurePattern(i + "x" + i, i, 3, i);
			String patternString = "";
			for(int y = 0; y < 3; y++) {
				for(int x = 0; x < i; x++) {
					for(int z = 0; z < i; z++) {
						patternString += "A";
					}
				}
			}
			pattern.setStructure(patternString);
			pattern.structure[i / 2][1][0] = 'B';
			addPattern(pattern);
		}
	}

	@Override
	public boolean areCharAndBlockEqual(char required, Block block, int blockMetadata, TileEntity blockTileEntity) {
		return required == 'B' ? Block.getIdFromBlock(block) == Block.getIdFromBlock(ExampleMod.coloredWool) : Block.getIdFromBlock(block) == Block.getIdFromBlock(ExampleMod.structureTestBlock);
	}
}

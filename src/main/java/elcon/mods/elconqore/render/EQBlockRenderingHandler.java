package elcon.mods.elconqore.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQConfig;
import elcon.mods.elconqore.blocks.BlockFluidMetadata;
import elcon.mods.elconqore.blocks.IBlockOverlay;

@SideOnly(Side.CLIENT)
public class EQBlockRenderingHandler implements ISimpleBlockRenderingHandler {

	public static final double OVERLAY_SHIFT = 0.001D;
	public static final float LIGHT_Y_NEG = 0.5F;
	public static final float LIGHT_Y_POS = 1.0F;
	public static final float LIGHT_XZ_NEG = 0.8F;
	public static final float LIGHT_XZ_POS = 0.6F;
	public static final double RENDER_OFFSET = 0.0010000000474974513D;

	public float getFluidHeightAverage(float[] flow) {
		float total = 0;
		int count = 0;
		float end = 0;
		for(int i = 0; i < flow.length; i++) {
			if(flow[i] >= 0.875F && end != 1F) {
				end = flow[i];
			}
			if(flow[i] >= 0) {
				total += flow[i];
				count++;
			}
		}
		if(end == 0) {
			end = total / count;
		}
		return end;
	}

	public float getFluidHeightForRender(IBlockAccess blockAccess, int x, int y, int z, BlockFluidMetadata block) {
		if(Block.getIdFromBlock(blockAccess.getBlock(x, y, z)) == Block.getIdFromBlock(block)) {
			if(blockAccess.getBlock(x, y - block.getDensityDirection(blockAccess, x, y, z), z).getMaterial().isLiquid()) {
				return 1;
			}
			if(blockAccess.getBlockMetadata(x, y, z) == block.getMaxRenderHeightMeta()) {
				return 0.875F;
			}
		}
		return !blockAccess.getBlock(x, y, z).getMaterial().isSolid() && Block.getIdFromBlock(blockAccess.getBlock(x, y - block.getDensityDirection(blockAccess, x, y, z), z)) == Block.getIdFromBlock(block) ? 1 : block.getQuantaPercentage(blockAccess, x, y, z) * 0.875F;
	}
	
	@Override
	public int getRenderId() {
		return EQConfig.BLOCK_OVERLAY_RENDER_ID;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelID, RenderBlocks renderer) {
		if(modelID == EQConfig.BLOCK_OVERLAY_RENDER_ID) {
			boolean flag = renderer.renderStandardBlock(block, x, y, z);
			renderBlockOverlay(blockAccess, block, x, y, z, renderer);
			return flag;
		} else if(modelID == EQConfig.BLOCK_FLUID_RENDER_ID) {
			if(!(block instanceof BlockFluidMetadata)) {
				return false;
			}
			Tessellator tessellator = Tessellator.instance;
			int color = block.colorMultiplier(blockAccess, x, y, z);
			float red = (color >> 16 & 255) / 255.0F;
			float green = (color >> 8 & 255) / 255.0F;
			float blue = (color & 255) / 255.0F;
			red = 1.0F;
			green = 1.0F;
			blue = 1.0F;

			BlockFluidMetadata blockFluid = (BlockFluidMetadata) block;
			int meta = blockAccess.getBlockMetadata(x, y, z);

			boolean renderTop = Block.getIdFromBlock(blockAccess.getBlock(x, y - blockFluid.getDensityDirection(blockAccess, x, y, z), z)) != Block.getIdFromBlock(blockFluid);
			boolean renderBottom = block.shouldSideBeRendered(blockAccess, x, y + blockFluid.getDensityDirection(blockAccess, x, y, z), z, 0) && Block.getIdFromBlock(blockAccess.getBlock(x, y + blockFluid.getDensityDirection(blockAccess, x, y, z), z)) != Block.getIdFromBlock(blockFluid);
			boolean[] renderSides = new boolean[]{
				block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2), 
				block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3), 
				block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4), 
				block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5)
			};

			if(!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3]) {
				return false;
			} else {
				boolean rendered = false;
				double heightNW, heightSW, heightSE, heightNE;
				float flow11 = getFluidHeightForRender(blockAccess, x, y, z, blockFluid);

				if(flow11 != 1) {
					float flow00 = getFluidHeightForRender(blockAccess, x - 1, y, z - 1, blockFluid);
					float flow01 = getFluidHeightForRender(blockAccess, x - 1, y, z, blockFluid);
					float flow02 = getFluidHeightForRender(blockAccess, x - 1, y, z + 1, blockFluid);
					float flow10 = getFluidHeightForRender(blockAccess, x, y, z - 1, blockFluid);
					float flow12 = getFluidHeightForRender(blockAccess, x, y, z + 1, blockFluid);
					float flow20 = getFluidHeightForRender(blockAccess, x + 1, y, z - 1, blockFluid);
					float flow21 = getFluidHeightForRender(blockAccess, x + 1, y, z, blockFluid);
					float flow22 = getFluidHeightForRender(blockAccess, x + 1, y, z + 1, blockFluid);

					heightNW = getFluidHeightAverage(new float[]{flow00, flow01, flow10, flow11});
					heightSW = getFluidHeightAverage(new float[]{flow01, flow02, flow12, flow11});
					heightSE = getFluidHeightAverage(new float[]{flow12, flow21, flow22, flow11});
					heightNE = getFluidHeightAverage(new float[]{flow10, flow20, flow21, flow11});
				} else {
					heightNW = flow11;
					heightSW = flow11;
					heightSE = flow11;
					heightNE = flow11;
				}
				boolean rises = blockFluid.getDensityDirection(blockAccess, x, y, z) == 1;
				if(renderer.renderAllFaces || renderTop) {
					rendered = true;
					IIcon iconStill = block.getIcon(blockAccess, x, y, z, 1);
					float flowDir = (float) blockFluid.getFlowDirection(blockAccess, x, y, z);
					if(flowDir > -999.0F) {
						iconStill = block.getIcon(blockAccess, x, y, z, 2);
					}
					heightNW -= RENDER_OFFSET;
					heightSW -= RENDER_OFFSET;
					heightSE -= RENDER_OFFSET;
					heightNE -= RENDER_OFFSET;

					double u1, u2, u3, u4, v1, v2, v3, v4;
					if(flowDir < -999.0F) {
						u2 = iconStill.getInterpolatedU(0.0D);
						v2 = iconStill.getInterpolatedV(0.0D);
						u1 = u2;
						v1 = iconStill.getInterpolatedV(16.0D);
						u4 = iconStill.getInterpolatedU(16.0D);
						v4 = v1;
						u3 = u4;
						v3 = v2;
					} else {
						float xFlow = MathHelper.sin(flowDir) * 0.25F;
						float zFlow = MathHelper.cos(flowDir) * 0.25F;
						u2 = iconStill.getInterpolatedU(8.0F + (-zFlow - xFlow) * 16.0F);
						v2 = iconStill.getInterpolatedV(8.0F + (-zFlow + xFlow) * 16.0F);
						u1 = iconStill.getInterpolatedU(8.0F + (-zFlow + xFlow) * 16.0F);
						v1 = iconStill.getInterpolatedV(8.0F + (zFlow + xFlow) * 16.0F);
						u4 = iconStill.getInterpolatedU(8.0F + (zFlow + xFlow) * 16.0F);
						v4 = iconStill.getInterpolatedV(8.0F + (zFlow - xFlow) * 16.0F);
						u3 = iconStill.getInterpolatedU(8.0F + (zFlow - xFlow) * 16.0F);
						v3 = iconStill.getInterpolatedV(8.0F + (-zFlow - xFlow) * 16.0F);
					}
					tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
					tessellator.setColorOpaque_F(LIGHT_Y_POS * red, LIGHT_Y_POS * green, LIGHT_Y_POS * blue);
					if(!rises) {
						tessellator.addVertexWithUV(x + 0, y + heightNW, z + 0, u2, v2);
						tessellator.addVertexWithUV(x + 0, y + heightSW, z + 1, u1, v1);
						tessellator.addVertexWithUV(x + 1, y + heightSE, z + 1, u4, v4);
						tessellator.addVertexWithUV(x + 1, y + heightNE, z + 0, u3, v3);
					} else {
						tessellator.addVertexWithUV(x + 1, y + 1 - heightNE, z + 0, u3, v3);
						tessellator.addVertexWithUV(x + 1, y + 1 - heightSE, z + 1, u4, v4);
						tessellator.addVertexWithUV(x + 0, y + 1 - heightSW, z + 1, u1, v1);
						tessellator.addVertexWithUV(x + 0, y + 1 - heightNW, z + 0, u2, v2);
					}
				}
				if(renderer.renderAllFaces || renderBottom) {
					rendered = true;
					tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z));
					if(!rises) {
						tessellator.setColorOpaque_F(LIGHT_Y_NEG * red, LIGHT_Y_NEG * green, LIGHT_Y_NEG * blue);
						renderer.renderFaceYNeg(block, x, y + RENDER_OFFSET, z, block.getIcon(0, meta));
					} else {
						tessellator.setColorOpaque_F(LIGHT_Y_POS * red, LIGHT_Y_POS * green, LIGHT_Y_POS * blue);
						renderer.renderFaceYPos(block, x, y + RENDER_OFFSET, z, block.getIcon(1, meta));
					}
				}
				for(int side = 0; side < 4; ++side) {
					int x2 = x;
					int z2 = z;
					switch(side) {
					case 0:
						z2--;
						break;
					case 1:
						z2++;
						break;
					case 2:
						x2--;
						break;
					case 3:
						x2++;
						break;
					}
					IIcon iconFlow = block.getIcon(blockAccess, x, y, z, side + 2);
					if(renderer.renderAllFaces || renderSides[side]) {
						rendered = true;
						double ty1;
						double tx1;
						double ty2;
						double tx2;
						double tz1;
						double tz2;
						if(side == 0) {
							ty1 = heightNW;
							ty2 = heightNE;
							tx1 = x;
							tx2 = x + 1;
							tz1 = z + RENDER_OFFSET;
							tz2 = z + RENDER_OFFSET;
						} else if(side == 1) {
							ty1 = heightSE;
							ty2 = heightSW;
							tx1 = x + 1;
							tx2 = x;
							tz1 = z + 1 - RENDER_OFFSET;
							tz2 = z + 1 - RENDER_OFFSET;
						} else if(side == 2) {
							ty1 = heightSW;
							ty2 = heightNW;
							tx1 = x + RENDER_OFFSET;
							tx2 = x + RENDER_OFFSET;
							tz1 = z + 1;
							tz2 = z;
						} else {
							ty1 = heightNE;
							ty2 = heightSE;
							tx1 = x + 1 - RENDER_OFFSET;
							tx2 = x + 1 - RENDER_OFFSET;
							tz1 = z;
							tz2 = z + 1;
						}
						float u1Flow = iconFlow.getInterpolatedU(0.0D);
						float u2Flow = iconFlow.getInterpolatedU(8.0D);
						float v1Flow = iconFlow.getInterpolatedV((1.0D - ty1) * 16.0D * 0.5D);
						float v2Flow = iconFlow.getInterpolatedV((1.0D - ty2) * 16.0D * 0.5D);
						float v3Flow = iconFlow.getInterpolatedV(8.0D);
						tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x2, y, z2));
						float sideLighting = 1.0F;
						if(side < 2) {
							sideLighting = LIGHT_XZ_NEG;
						} else {
							sideLighting = LIGHT_XZ_POS;
						}
						tessellator.setColorOpaque_F(LIGHT_Y_POS * sideLighting * red, LIGHT_Y_POS * sideLighting * green, LIGHT_Y_POS * sideLighting * blue);
						if(!rises) {
							tessellator.addVertexWithUV(tx1, y + ty1, tz1, u1Flow, v1Flow);
							tessellator.addVertexWithUV(tx2, y + ty2, tz2, u2Flow, v2Flow);
							tessellator.addVertexWithUV(tx2, y + 0, tz2, u2Flow, v3Flow);
							tessellator.addVertexWithUV(tx1, y + 0, tz1, u1Flow, v3Flow);
						} else {
							tessellator.addVertexWithUV(tx1, y + 1 - 0, tz1, u1Flow, v3Flow);
							tessellator.addVertexWithUV(tx2, y + 1 - 0, tz2, u2Flow, v3Flow);
							tessellator.addVertexWithUV(tx2, y + 1 - ty2, tz2, u2Flow, v2Flow);
							tessellator.addVertexWithUV(tx1, y + 1 - ty1, tz1, u1Flow, v1Flow);
						}
					}
				}
				renderer.renderMinY = 0;
				renderer.renderMaxY = 1;
				return rendered;
			}
		}
		return false;
	}
	
	public static boolean renderBlockWithOverlay(IBlockAccess blockAcccess, Block block, int x, int y, int z, RenderBlocks renderer) {
		boolean flag = renderer.renderStandardBlock(block, x, y, z);
		renderBlockOverlay(blockAcccess, block, x, y, z, renderer);
		return flag;
	}

	private static boolean renderBlockOverlay(IBlockAccess blockAccess, Block block, int x, int y, int z, RenderBlocks renderer) {
		IBlockOverlay blockOverlay = (IBlockOverlay) block;
		int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
		IIcon overlay = null;		
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 0);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 0)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 0);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderBottomFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 1);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 1)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 1);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderTopFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 2);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 2)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 2);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderEastFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 3);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 3)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 3);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderWestFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 4);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 4)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 4);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderNorthFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		overlay = blockOverlay.getBlockOverlayTexture(blockAccess, x, y, z, 5);
		if(overlay != null && blockOverlay.shouldOverlaySideBeRendered(blockAccess, x, y, z, 5)) {
			int color = blockOverlay.getBlockOverlayColor(blockAccess, x, y, z, 5);
			float sideR = (color >> 16 & 0xFF) / 255.0F;
			float sideG = (color >> 8 & 0xFF) / 255.0F;
			float sideB = (color & 0xFF) / 255.0F;
			renderSouthFace(blockAccess, block, x, y, z, renderer, overlay, mixedBrightness, sideR, sideG, sideB);
		}
		return true;
	}

	public static int determineMixedBrightness(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, int mixedBrightness) {
		return renderer.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(world, x, y, z);
	}

	public static void renderBottomFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x, y - 1, z, 0))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x, y - 1, z, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(0.5F * r, 0.5F * g, 0.5F * b);
		renderer.renderFaceYNeg(block, x, y - OVERLAY_SHIFT, z, textureIndex);
	}

	public static void renderTopFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x, y + 1, z, 1))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x, y + 1, z, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(r, g, b);
		renderer.renderFaceYPos(block, x, y + OVERLAY_SHIFT, z, textureIndex);
	}

	public static void renderEastFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x, y, z - 1, 2))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x, y, z - 1, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(0.8F * r, 0.8F * g, 0.8F * b);
		renderer.renderFaceZNeg(block, x, y, z - OVERLAY_SHIFT, textureIndex);
	}

	public static void renderWestFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x, y, z + 1, 3))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x, y, z + 1, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(0.8F * r, 0.8F * g, 0.8F * b);
		renderer.renderFaceZPos(block, x, y, z + OVERLAY_SHIFT, textureIndex);
	}

	public static void renderNorthFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x - 1, y, z, 4))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x - 1, y, z, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(0.6F * r, 0.6F * g, 0.6F * b);
		renderer.renderFaceXNeg(block, x - OVERLAY_SHIFT, y, z, textureIndex);
	}

	public static void renderSouthFace(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon textureIndex, int mixedBrightness, float r, float g, float b) {
		if((!renderer.renderAllFaces) && (!block.shouldSideBeRendered(world, x + 1, y, z, 5))) {
			return;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.setBrightness(determineMixedBrightness(world, block, x + 1, y, z, renderer, mixedBrightness));
		tesselator.setColorOpaque_F(0.6F * r, 0.6F * g, 0.6F * b);
		renderer.renderFaceXPos(block, x + OVERLAY_SHIFT, y, z, textureIndex);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) {
		return modelID == EQConfig.BLOCK_OVERLAY_RENDER_ID;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if(modelID == EQConfig.BLOCK_OVERLAY_RENDER_ID) {
			renderItemBlockOverlay(block, metadata, modelID, renderer);
		} else if(modelID == EQConfig.BLOCK_FLUID_RENDER_ID) {
			
		}
	}
	
	public void renderItemBlockOverlay(Block block, int metadata, int modelID, RenderBlocks renderer) {
		IBlockOverlay blockOverlay = (IBlockOverlay) block;
		IIcon overlay = null;
		Tessellator tessellator = Tessellator.instance;
		if(renderer.useInventoryTint) {
			int color = block.getRenderColor(metadata);
			float r = (float) (color >> 16 & 255) / 255.0F;
			float g = (float) (color >> 8 & 255) / 255.0F;
			float b = (float) (color & 255) / 255.0F;
			GL11.glColor4f(r, g, b, 1.0F);
		}
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(0, metadata);
		if(overlay != null) {
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(1, metadata);
		if(overlay != null) {
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(2, metadata);
		if(overlay != null) {
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(3, metadata);
		if(overlay != null) {
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(4, metadata);
		if(overlay != null) {
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		overlay = blockOverlay.getBlockOverlayTexture(5, metadata);
		if(overlay != null) {
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, overlay);
		}
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}

package elcon.mods.elconqore.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiToggleButton extends GuiButton {
	
	public boolean selectOnly = false;
	public boolean toggled;
	
	public GuiToggleButton(int id, int x, int y, int textureX, int textureY, int width, int height, ResourceLocation location, String text, boolean selectOnly) {
		super(id, x, y, textureX, textureY, width, height, location, text);
		this.selectOnly = selectOnly;
		toggled = false;
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		boolean flag = super.mousePressed(par1Minecraft, par2, par3);
		if(flag) {
			if(toggled && selectOnly) {
				return flag;
			}
			toggled = !toggled;
		}
		return flag;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(visible) {
			mc.getTextureManager().bindTexture(location);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, textureX, textureY + (toggled ? height : 0), width, height);
			mouseDragged(mc, mouseX, mouseY);
			if(displayString != null && displayString.length() > 0) {
				int color = 0xE0E0E0;
				if(!enabled) {
					color = -0x5F5F60;
				} else if(field_146123_n) {
					color = 0xFFFFA0;
				}
				drawCenteredString(mc.fontRenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, color);
			}
		}
	}
}

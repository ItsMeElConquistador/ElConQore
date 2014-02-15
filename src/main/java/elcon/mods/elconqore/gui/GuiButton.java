package elcon.mods.elconqore.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButton extends net.minecraft.client.gui.GuiButton {

	public int textureX;
	public int textureY;
	public ResourceLocation location;
	
	public GuiButton(int id, int x, int y, int textureX, int textureY, int width, int height, ResourceLocation location, String text) {
		super(id, x, y, width, height, text);
		this.textureX = textureX;
		this.textureY = textureY;
		this.location = location;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(visible) {
			mc.getTextureManager().bindTexture(location);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int hoverState = getHoverState(field_146123_n);
			drawTexturedModalRect(xPosition, yPosition, textureX, textureY + hoverState * height, width, height);
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

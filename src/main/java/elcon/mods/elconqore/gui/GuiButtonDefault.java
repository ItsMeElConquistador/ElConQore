package elcon.mods.elconqore.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonDefault extends GuiButton {

	public GuiButtonDefault(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(visible) {
			mc.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int hoverState = getHoverState(field_146123_n);
			drawTexturedModalRect(xPosition, yPosition, 0, 46 + hoverState * 20, width / 2, height / 2);
			drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + hoverState * 20, width / 2, height / 2);
			drawTexturedModalRect(xPosition, yPosition + height / 2, 0, (46 + hoverState * 20) + (20 - height / 2), width / 2, height / 2);
			drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, (46 + hoverState * 20) + (20 - height / 2), width / 2, height / 2);
			mouseDragged(mc, mouseX, mouseY);
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

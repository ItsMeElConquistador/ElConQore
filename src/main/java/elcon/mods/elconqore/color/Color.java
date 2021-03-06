package elcon.mods.elconqore.color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Color {

	public static final int TEXT_COLOR_BLACK = 0x000000;
	public static final int TEXT_COLOR_DARK_BLUE = 0x0000AA;
	public static final int TEXT_COLOR_DARK_GREEN = 0x00AA00;
	public static final int TEXT_COLOR_DARK_AQUA = 0x00AAAA;
	public static final int TEXT_COLOR_DARK_RED = 0xAA0000;
	public static final int TEXT_COLOR_PURPLE = 0xAA00AA;
	public static final int TEXT_COLOR_GOLD = 0xFFAA00;
	public static final int TEXT_COLOR_GRAY = 0xAAAAAA;
	public static final int TEXT_COLOR_DARK_GRAY = 0x555555;
	public static final int TEXT_COLOR_BLUE = 0x5555FF;
	public static final int TEXT_COLOR_GREEN = 0x55FF55;
	public static final int TEXT_COLOR_AQUA = 0x55FFFF;
	public static final int TEXT_COLOR_RED = 0xFF5555;
	public static final int TEXT_COLOR_LIGHT_PURPLE = 0xFF55FF;
	public static final int TEXT_COLOR_YELLOW = 0xFFFF55;
	public static final int TEXT_COLOR_WHITE = 0xFFFFFF;

	public byte r;
	public byte g;
	public byte b;
	public byte a;

	public Color(int r, int g, int b, int a) {
		this.r = (byte) r;
		this.g = (byte) g;
		this.b = (byte) b;
		this.a = (byte) a;
	}

	public Color(Color color) {
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
	}

	@SideOnly(Side.CLIENT)
	public void glColor() {
		GL11.glColor4ub(r, g, b, a);
	}

	@SideOnly(Side.CLIENT)
	public void glColor(int a) {
		GL11.glColor4ub(r, g, b, (byte) a);
	}

	@SideOnly(Side.CLIENT)
	@Deprecated
	public void glColor(byte a) {
		GL11.glColor4ub(r, g, b, a);
	}

	public abstract int pack();

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
	}

	public Color add(Color color2) {
		a += color2.a;
		r += color2.r;
		g += color2.g;
		b += color2.b;
		return this;
	}

	public Color sub(Color color2) {
		int ia = (a & 0xFF) - (color2.a & 0xFF);
		int ir = (r & 0xFF) - (color2.r & 0xFF);
		int ig = (g & 0xFF) - (color2.g & 0xFF);
		int ib = (b & 0xFF) - (color2.b & 0xFF);
		a = (byte) (ia < 0 ? 0 : ia);
		r = (byte) (ir < 0 ? 0 : ir);
		g = (byte) (ig < 0 ? 0 : ig);
		b = (byte) (ib < 0 ? 0 : ib);
		return this;
	}

	public Color invert() {
		a = (byte) (0xFF - (a & 0xFF));
		r = (byte) (0xFF - (r & 0xFF));
		g = (byte) (0xFF - (g & 0xFF));
		b = (byte) (0xFF - (b & 0xFF));
		return this;
	}

	public Color multiply(Color color2) {
		a = (byte) ((a & 0xFF) * ((color2.a & 0xFF) / 255D));
		r = (byte) ((r & 0xFF) * ((color2.r & 0xFF) / 255D));
		g = (byte) ((g & 0xFF) * ((color2.g & 0xFF) / 255D));
		b = (byte) ((b & 0xFF) * ((color2.b & 0xFF) / 255D));
		return this;
	}

	public Color scale(double d) {
		a = (byte) ((a & 0xFF) * d);
		r = (byte) ((r & 0xFF) * d);
		g = (byte) ((g & 0xFF) * d);
		b = (byte) ((b & 0xFF) * d);
		return this;
	}

	public Color interpolate(Color color2, double d) {
		return this.add(color2.copy().sub(this).scale(d));
	}

	public Color multiplyC(double d) {
		r = (byte) Math.min(Math.max((r & 0xFF) * d, 0), 255);
		g = (byte) Math.min(Math.max((g & 0xFF) * d, 0), 255);
		b = (byte) Math.min(Math.max((b & 0xFF) * d, 0), 255);
		return this;
	}

	public abstract Color copy();

	public int rgb() {
		return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}

	public int argb() {
		return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}

	public int rgba() {
		return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
	}

	public Color set(Color color) {
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
		return this;
	}
}

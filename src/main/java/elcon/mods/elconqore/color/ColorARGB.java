package elcon.mods.elconqore.color;

public class ColorARGB extends Color {

	public ColorARGB(int color) {
		super((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
	}

	public ColorARGB(int a, int r, int g, int b) {
		super(r, g, b, a);
	}

	public ColorARGB(ColorARGB color) {
		super(color);
	}

	public ColorARGB copy() {
		return new ColorARGB(this);
	}

	public int pack() {
		return pack(this);
	}

	public static int pack(Color color) {
		return (color.a & 0xFF) << 24 | (color.r & 0xFF) << 16 | (color.g & 0xFF) << 8 | (color.b & 0xFF);
	}
}

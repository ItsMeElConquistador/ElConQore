package elcon.mods.elconqore.color;

public class ColorRGBA extends Color {

	public ColorRGBA(int color) {
		super((color >> 24) & 0xFF, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
	}

	public ColorRGBA(double r, double g, double b, double a) {
		super((int) (255 * r), (int) (255 * g), (int) (255 * b), (int) (255 * a));
	}

	public ColorRGBA(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public ColorRGBA(ColorRGBA color) {
		super(color);
	}

	public int pack() {
		return pack(this);
	}

	@Override
	public Color copy() {
		return new ColorRGBA(this);
	}

	public static int pack(Color color) {
		return (color.r & 0xFF) << 24 | (color.g & 0xFF) << 16 | (color.b & 0xFF) << 8 | (color.a & 0xFF);
	}
}

package elcon.mods.elconqore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EQUtil {

	public static String firstUpperCase(String s) {
		return Character.toString(s.charAt(0)).toUpperCase() + s.substring(1, s.length());
	}

	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if(i > p) {
			return fileName.substring(i + 1);
		}
		return "";
	}

	public static void extractZip(File zip, File dest, boolean deleteDest) {
		byte[] buffer = new byte[1024];
		try {
			if(deleteDest && dest.exists()) {
				dest.delete();
			}
			dest.mkdirs();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
			ZipEntry ze = zis.getNextEntry();

			while(ze != null) {
				String fileName = ze.getName();
				File newFile = new File(dest, fileName);
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static String coordsToString(int x, int y, int z) {
		return x + "," + y + "," + z;
	}

	public static String coordsToString(TileEntity tile) {
		return coordsToString(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public static int getFirstUncoveredBlock(World world, int x, int z) {
		int i;
		for(i = 48; !world.isAirBlock(x, i + 1, z); i++) {
		}
		return i;
	}

	public static int getHighestBlock(World world, int x, int z) {
		int i;
		for(i = 255; !world.isAirBlock(x, i + 1, z); i--) {
		}
		return i;
	}

	public static byte[] intToByte(int[] intArray) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * 4);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(intArray);
		byte[] array = byteBuffer.array();
		return array;
	}

	public static int[] byteToInt(byte[] byteArray) {
		IntBuffer intBuf = ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] array = new int[intBuf.remaining()];
		intBuf.get(array);
		return array;
	}
}

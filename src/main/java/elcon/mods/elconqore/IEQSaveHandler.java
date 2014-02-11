package elcon.mods.elconqore;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.minecraft.nbt.NBTTagCompound;

public interface IEQSaveHandler {

	public static enum SaveFileType {
		OBJECT(),
		NBT();
	}
	
	public String[] getSaveFiles();
	
	public SaveFileType getSaveFileType(String fileName);
	
	public void load(String fileName, File file, ObjectInputStream in);
	
	public void save(String fileName, File file, ObjectOutputStream out);
	
	public void loadNBT(String fileName, File file, NBTTagCompound nbt);
	
	public void saveNBT(String fileName, File file, NBTTagCompound nbt);
}

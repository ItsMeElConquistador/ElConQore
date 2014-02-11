package elcon.mods.elconqore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import elcon.mods.elconqore.IEQSaveHandler.SaveFileType;

public class EQSaveHandler {

	public ISaveHandler saveHandler;
	public World world;

	public EQSaveHandler(ISaveHandler saveHandler, World world) {
		this.saveHandler = saveHandler;
		this.world = world;
	}
	
	public void load() {
		if((world.provider.dimensionId == 0)) {
			for(EQMod mod : EQMod.mods.values()) {
				if(mod.saveHandler != null) {
					String[] saveFileNames = mod.saveHandler.getSaveFiles();
					for(int i = 0; i < saveFileNames.length; i++) {
						SaveFileType saveFileType = mod.saveHandler.getSaveFileType(saveFileNames[i]);
						if(saveFileType == SaveFileType.OBJECT) {
							try {
								File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
								if(file != null) {
									try {
										loadFile(mod.saveHandler, saveFileNames[i], file);
									} catch(Exception e) {
										e.printStackTrace();
										File fileBackup = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", true);
										if(fileBackup.exists()) {
											loadFile(mod.saveHandler, saveFileNames[i], fileBackup);
										} else {
											file.createNewFile();
											saveFile(mod.saveHandler, saveFileNames[i], file);
										}
									}
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
						} else if(saveFileType == SaveFileType.NBT) {
							try {
								File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
								if(file != null) {
									try {
										loadFileNBT(mod.saveHandler, saveFileNames[i], file);
									} catch(Exception e) {
										e.printStackTrace();
										File fileBackup = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", true);
										if(fileBackup.exists()) {
											loadFileNBT(mod.saveHandler, saveFileNames[i], fileBackup);
										} else {
											file.createNewFile();
											saveFileNBT(mod.saveHandler, saveFileNames[i], file);
										}
									}
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private void loadFile(IEQSaveHandler saveHandler, String fileName, File file) {
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
	
			saveHandler.load(fileName, file, in);
			
			in.close();
			gzis.close();
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadFileNBT(IEQSaveHandler saveHandler, String fileName, File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(fis);
			fis.close();
			saveHandler.loadNBT(fileName, file, nbt);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		if((world.provider.dimensionId == 0)) {
			for(EQMod mod : EQMod.mods.values()) {
				if(mod.saveHandler != null) {
					String[] saveFileNames = mod.saveHandler.getSaveFiles();
					for(int i = 0; i < saveFileNames.length; i++) {
						SaveFileType saveFileType = mod.saveHandler.getSaveFileType(saveFileNames[i]);
						if(saveFileType == SaveFileType.OBJECT) {
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							saveFile(mod.saveHandler, saveFileNames[i], file);
						} else if(saveFileType == SaveFileType.NBT) {
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							saveFileNBT(mod.saveHandler, saveFileNames[i], file);
						}
					}
				}
			}
		}
	}
	
	public void saveFile(IEQSaveHandler saveHandler, String fileName, File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);

			saveHandler.save(fileName, file, out);

			out.flush();
			out.close();
			gzos.close();
			fos.close();
			copyFile(file, new File(new StringBuilder().append(file.getAbsolutePath()).append(".bak").toString()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveFileNBT(IEQSaveHandler saveHandler, String fileName, File file) {
		try {
			NBTTagCompound nbt = new NBTTagCompound();
			
			saveHandler.saveNBT(fileName, file, nbt);
			
			FileOutputStream fos = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(nbt, fos);
			fos.close();
			
			copyFile(file, new File(new StringBuilder().append(file.getAbsolutePath()).append(".bak").toString()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public File getSaveFile(ISaveHandler saveHandler, World world, String name, boolean backup) {
		File worldDir = new File(saveHandler.getWorldDirectoryName());
		IChunkLoader loader = saveHandler.getChunkLoader(world.provider);
		if((loader instanceof AnvilChunkLoader)) {
			worldDir = ((AnvilChunkLoader) loader).chunkSaveLocation;
		}
		File file = new File(worldDir, new StringBuilder().append(name).append(backup ? ".bak" : "").toString());
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public void copyFile(File sourceFile, File destFile) {
		FileChannel source = null;
		FileChannel destination = null;
		try {
			if(!destFile.exists()) {
				destFile.createNewFile();
			}
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0L, source.size());
			source.close();
			destination.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

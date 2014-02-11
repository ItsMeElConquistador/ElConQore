package elcon.mods.elconqore.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PatchGenerator {

	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Can't run, not enough arguments");
			System.exit(0);
		}
		String inputDirName = args[0];
		String outputFileName = args[1];
		
		try {
			File inputDir = new File(inputDirName);
			if(!inputDir.exists() || inputDir.listFiles().length <= 0) {
				System.out.println("Can't run, input dir not found or empty");
				System.exit(0);
			}
			File outputFile = new File(outputFileName);
			if(outputFile.exists()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeInt(inputDir.listFiles().length);
			File[] files = inputDir.listFiles();
			for(int i = 0; i < files.length; i++) {
				out.writeUTF(files[i].getName().replaceAll(".class", ""));
				FileInputStream inputStream = new FileInputStream(files[i]);
				out.writeInt(inputStream.available());
				byte[] bytes = new byte[inputStream.available()];
				inputStream.read(bytes);
				out.write(bytes);
				inputStream.close();
			}
			outputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

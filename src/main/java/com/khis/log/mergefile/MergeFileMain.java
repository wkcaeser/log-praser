package com.khis.log.mergefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Optional;

public class MergeFileMain {

	public static void main(String[] args) {
		// write your code here
//	    String currentPath = Main.class.getResource("/").getPath();
//	    System.out.println(currentPath);
//
		String currentPath = "C:\\Users\\wkgui\\Desktop\\服务器巡检\\xiantao-04-24";
		File currentDirectory = new File(currentPath);
		if (!currentDirectory.isDirectory()){
			System.err.println("当前路径不合法");
			return;
		}


		File mergeFile = createMergeFile(currentPath);
		if (mergeFile == null){
			System.out.println("合并文件创建失败");
			return;
		}

		try(FileChannel mergeFileChannel = new FileOutputStream(mergeFile).getChannel()) {
			mergeFile(mergeFileChannel, new File(currentPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建合并的目标文件
	 * @param path 文件平级路径
	 * @return file
	 */
	private static File createMergeFile(String path){
		String[] dirs = path.split("\\\\");
		int lastDirLen = dirs[dirs.length-1].length();
		File mergeFile = new File(path.substring(0, path.length()-lastDirLen) + "/merge-file-" + System.currentTimeMillis() + ".txt");
		if (mergeFile.exists()){
			mergeFile.delete();
		}
		try {
			mergeFile.createNewFile();
		} catch (IOException e) {
			System.err.println("文件创建失败");
			return null;
		}
		return mergeFile;
	}

	/**
	 * 递归合并文件夹下的所有文件
	 * @param mergeFileChannel 输出文件channel
	 * @param file file
	 */
	private static void mergeFile(FileChannel mergeFileChannel, File file){
		//是文件直接添加到file
		if (file.isFile()){
			try(FileChannel fileChannelOut = new FileInputStream(file).getChannel()) {
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				while (fileChannelOut.read(buffer) != -1){
					buffer.flip();
					mergeFileChannel.write(buffer);
					buffer.clear();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (file.isDirectory()){
			Arrays.stream(Optional.ofNullable(file.listFiles()).orElse(new File[0]))
					.forEach(obj -> mergeFile(mergeFileChannel, obj));
		}
	}

}

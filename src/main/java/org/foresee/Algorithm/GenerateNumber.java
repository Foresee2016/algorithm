package org.foresee.Algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 生成随机数，写到JSON文件里，为排序使用，全是整数（为了输出好看不用格式化浮点数），文件位置E:/JavaSpace/sort.json.
 * 
 * @author Foresee
 *
 */
public class GenerateNumber {
	public static final int MAX_GENERATE_VALUE = 1000;
	public static final int TOTAL_GENERATE = 50;

	public static void generateInt(String filename) {
		File file = new File(filename);
		if (file.isDirectory()) {
			System.out.println("filename cannot be directory");
			return;
		}
		if (file.exists()) {
			System.out.println("file exist. will Quit. NOT over write");
			return;
		}
		try {
			if (!file.createNewFile()) {
				System.out.println("Fail to Create new file.");
			}
			FileWriter writer = new FileWriter(file);
			int data;
			for (int i = 0; i < TOTAL_GENERATE; i++) {
				data = (int) (Math.random() * MAX_GENERATE_VALUE);
				writer.write("" + data);
				writer.write("\r\n");
			}
			writer.flush();
			writer.close();
			System.out.println("Successful write");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	public static int[] getFileData(String filename){
		File file = new File(filename);
		if (file.isDirectory()) {
			System.out.println("filename cannot be directory");
			return null;
		}
		if (!file.exists()) {
			System.out.println("File NOT exist. will Quit.");
			return null;
		}
		int[] data=new int[TOTAL_GENERATE];
		try {
			FileReader reader=new FileReader(file);
			BufferedReader bReader=new BufferedReader(reader);
			for(int i=0; i<TOTAL_GENERATE; i++){
				String line=bReader.readLine();
				data[i]=NumberFormat.getInstance().parse(line).intValue();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}
	public static void main(String[] args) {
		String filename="E:/JavaSpace/Algorithm/sort.data";
		generateInt(filename);
		int[] data=getFileData(filename);
		for(int i=0; i<data.length; i++){
			System.out.println(data[i]);
		}
	}
}

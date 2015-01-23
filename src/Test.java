import java.util.*;
import java.io.*;


public class Test {
	public static void main(String[] args) throws FileNotFoundException{
		//Scanner input = new Scanner(System.in);
		//System.out.println("Enter input: ");
		File tetons1 = new File("tetons1.ppm");
		Scanner teton = new Scanner(tetons1);
		for (int i = 0; i < 3; i++) {
			teton.nextLine();
		}
		
		String colors = teton.nextLine();
		int[][] array = colorArray(colors);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(array[i][j]);
			}
		}
		
		teton.close();
	}
	
	public static String retrieveHeader(Scanner scanner){
		String header = "";
		for (int i = 0; i < 3; i++) {
			header += scanner.nextLine() + "\n";
		}
		scanner.close();
		return header;
	}
	
	public static int[][] colorArray(String colorLine) {
		
		/*
		 * "\s" is any whitespace, but extra slash for escape
		 * + is to match with one or more of the "\\s" expression
		 */
		
		String[] rgbArray = colorLine.split("\\s+");
		if (rgbArray.length % 3 != 0) {
			throw new IllegalArgumentException("Number of "
					+ "RGB values on line is not divisible by 3.");
		}
		
		int[][] colorArray = new int[rgbArray.length / 3][3];
		for (int i = 0; i < rgbArray.length / 3; i++) {
			for (int j = 0; j < 3; j++) {
				colorArray[i][j] = Integer.parseInt(rgbArray[3*i+j]);
			}
		}
		
		return colorArray;
		
	}

}

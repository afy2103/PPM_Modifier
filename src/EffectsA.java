import java.io.*;
import java.util.*;

public class EffectsA {	
	ArrayList<Scanner> scanners;
	String magicNumber;
	
	//Constructor
	public EffectsA() {
		scanners = new ArrayList<>();
		magicNumber = "P3";
	}
	
	public void filter(File[] files, String output) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(output);
		String header = commonHeader(scanners, files);
		
		out.println(header);
		majorityRGBGen(scanners, out);
		
		out.close();
		for (int i = 0; i < scanners.size(); i++) {
			scanners.get(i).close();
		}
	}

	//Checks if the files are valid; if so, updates scanners and returns File[]
	public void filesCheck(ArrayList<String> strings) throws FileNotFoundException {
		File[] files = new File[strings.size()];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(strings.get(i));
		}
		for (int i = 0; i < files.length; i++) {
			Scanner scan = new Scanner(files[i]);
			scanners.add(scan);
		}
	}

	//Generates the majority rules ppm file's RGB values
	private void majorityRGBGen(ArrayList<Scanner> scanners, PrintWriter out) {
		while (scanners.get(0).hasNextLine()) {
			String[] fileLines = new String[scanners.size()];
			
			for (int i = 0; i < scanners.size(); i++) {	
				fileLines[i] = scanners.get(i).nextLine();
			}
			
			int rgb_length = fileLines[0].split("\\s+").length / 3;
			int[][][] colorLines = new int[rgb_length][scanners.size()][3];
			
			for (int i = 0; i < scanners.size(); i++) {
				for (int j = 0; j < rgb_length; j++) {
					colorLines[j][i] = colorArray(fileLines[i])[j];
				}
			}
			
			for (int i = 0; i < rgb_length; i++) {
				int[][] colors = new int[scanners.size()][3];
				for (int j = 0; j < scanners.size(); j++) {
					colors[j] = colorLines[i][j];
				}
				
				int[] newRGB = majorityColor(colors);
				String textRGB = "";
				for (int k = 0; k < 3; k++) {
					textRGB += newRGB[k] + " ";
				}
				out.print(textRGB);
			}
		}
	}
	
	//Returns a File array based on a list of string inputs for filenames
	public File[] arrayMaker(ArrayList<String> strings) {
		File[] files = new File[strings.size()];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(strings.get(i));
		}
		
		return files;
	}
	
	//Returns the color from an array of RGB values that occurs most often
	private int[] majorityColor(int[][] colors) {
		int repeats = 0;
		int max = 0;
		int[] majColor = colors[0];
		for (int i = 0; i < colors.length; i++) {
			int[] current = colors[i];
			for (int k = i; k < colors.length; k++) {
				if (Arrays.equals(colors[k], current)) {
					repeats++;
				}
			}
			
			if (repeats > max) {
				max = repeats;
				majColor = current;
			}
			
			repeats = 0;
		}
		
		return majColor;
	}
	
	/*
	 * Converts a line in an image file and converts it into an array of
	 * arrays, specifically an array of RGB arrays
	 */
	private int[][] colorArray(String colorLine) {
		
		/*
		 * "\s" is any whitespace, but extra slash for escape
		 * + is to match with one or more of the "\\s" expression
		 */
		
		String[] rgbArray = colorLine.split("\\s+");
		if (rgbArray.length % 3 > 0) {
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

	//Returns header in the scanned files if they're consistent/correct
	private String commonHeader(ArrayList<Scanner> scanners, File[] files) {
		String[] headers = new String[scanners.size()];
		for (int i = 0; i < scanners.size(); i++) {
			headers[i] = retrieveHeader(scanners.get(i));
		}
		
		for (int j = 0; j < scanners.size(); j++) {
			if (checkHeader(headers[j])) {
				throw new IllegalArgumentException("Invalid header(s).");
			}
		}
		
		for (int k = 0; k < scanners.size() - 1; k++) {
			if (!headers[k].equals(headers[k+1])) {
				throw new IllegalArgumentException("Inconsistent file "
						+ "formats or dimensions.");
			}
		}
		
		return headers[0];
	}
	
	//Returns the header of a file as a string, assuming header is correct
	private String retrieveHeader(Scanner scanner){
		String header = "";
		for (int i = 0; i < 2; i++) {
			header += scanner.nextLine() + "\n";
		}
		header += scanner.nextLine();
		return header;
	}
	
	//Returns if the header is correct
	private boolean checkHeader(String header) {
		boolean error = false;
		//Splits header by line
		String[] headerElem = header.split("\\n+");
		
		//Splits line including dimensions into separate numbers
		String[] dimensions = headerElem[1].split("\\s+");
		if (!magicNumber.equals(headerElem[0])) {
			error = true;
		}

		else if (dimensions.length != 2) {
			error = true;
		}
		
		else if (!isInteger(dimensions[0]) || !isInteger(dimensions[1])) {
			error = true;
		}
		
		else if (!isInteger(headerElem[2])) {
			error = true;
		}
		
		return error;
		
	}
	
	//Returns if a string can be converted into an integer
	private boolean isInteger(String integer) {
		boolean inttest = true;
		try {
			Integer.parseInt(integer);
		}
		
		catch (NumberFormatException e) {
			inttest = false;
		}
		
		return inttest;
	}


	
	
}

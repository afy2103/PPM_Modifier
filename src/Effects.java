/*
 * Effects
 * by Alex Yu (afy2103)
 * Handles all methods needed for the various effects methods on images
 * This also includes I/O interactions with the user for these methods
 * Also takes care of exceptions/exception handling
 */
import java.io.*;
import java.util.*;

public class Effects {	
	ArrayList<Scanner> scanners;
	
	//Constructor
	public Effects() {
		scanners = new ArrayList<>();
	}
	
	//Takes an array of images and uses majority rule to create a new file
	public void filter(File[] files, String output) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(output);
		String header = commonHeader(scanners, files);
		
		//Prints header and then rest of file
		out.println(header);
		majorityRGBGen(scanners, out);
		
		//Closes all scanners
		out.close();
		for (int i = 0; i < scanners.size(); i++) {
			scanners.get(i).close();
		}
	}

	//Checks if the files are valid; if so, updates scanners
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
		while (scanners.get(0).hasNext()) {
			
			//Creates an array for a fixed pixel among all files
			int[][] colors = new int[scanners.size()][3];
			for (int i = 0; i < scanners.size(); i++) {
				for (int j = 0; j < 3; j++) {
					String integer = scanners.get(i).next();
					if (!isInteger(integer)) {
						throw new NumberFormatException("One or more RGB values are invalid.");
					}
					colors[i][j] = Integer.parseInt(integer);
				}
			}
			
			int[] newRGB = majorityColor(colors); 
			String textRGB = "";
			for (int k = 0; k < 3; k++) {
				textRGB += newRGB[k] + " ";
			}
			out.print(textRGB);
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
		
		/*
		 * "\s" is any whitespace, but extra slash for escape
		 * + is to match with one or more of the "\\s" expression
		 */
		
		//Splits header by lines
		String[] headerElem = header.split("\\n+");
		
		//Splits line including dimensions into separate numbers
		String[] dimensions = headerElem[1].split("\\s+");

		//Checks for correct magic number
		if (!"P3".equals(headerElem[0])) {
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
	
	//Retrieves width of a header
	private int retrieveWidth(String header) {
		String[] headerElem = header.split("\\n+");
		String[] dimensions = headerElem[1].split("\\s+");
		int width = Integer.parseInt(dimensions[0]);
		return width;
		}
		
	//Retrieves height of a header
	private int retrieveHeight(String header) {
		String[] headerElem = header.split("\\n+");
		String[] dimensions = headerElem[1].split("\\s+");
		int height = Integer.parseInt(dimensions[1]);
		return height;
	}

	//Flips a given image (with specified output) horizontally
	public void flipHorizontal(File file, String output) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(output);
		Scanner scan = new Scanner(file);
		String header = retrieveHeader(scan);
		if (checkHeader(header)) {
			out.close();
			throw new IllegalArgumentException("Invalid file header.");
		}
		int height = retrieveHeight(header);
		int width = retrieveWidth(header);
		out.println(header);
		
		for (int i = 0; i < height; i++) {
			int[][] colors = new int[width][3];
			
			//Traverses and fills color array backwards; this is the flip mechanism
			for (int j = width - 1; j >= 0; j--) {
				for (int k = 0; k < 3; k++) {
					String integer = scan.next();
					if (!isInteger(integer)) {
						out.close();
						throw new NumberFormatException("One or more RGB values are invalid.");
					}
					colors[j][k] = Integer.parseInt(integer);
				}
			}	
			
			for (int j = 0; j < width; j++) {
				String textRGB = "";
				for (int k = 0; k < 3; k++) {
					textRGB += colors[j][k] + " ";
				}
				out.print(textRGB);
			}
		}
		out.close();
		scan.close();
	}
	
	//Replaces a given image (with specified output) with its grey scale version
	public void greyScale(File file, String output) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(output);
		Scanner scan = new Scanner(file);
		String header = retrieveHeader(scan);
		if (checkHeader(header)) {
			out.close();
			throw new IllegalArgumentException("Invalid file header.");
		}
		out.println(header);
		
		while (scan.hasNext()) {
			int[] color = new int[3];
			for (int i = 0; i < 3; i++) {
				String integer = scan.next();
				if (!isInteger(integer)) {
					out.close();
					throw new NumberFormatException("One or more RGB values are invalid.");
				}
				color[i] = Integer.parseInt(integer);
			}
			int red = color[0];
			int green = color[1];
			int blue = color[2];
			
			//Finds average, and replaces all elements in pixel array with grey
			int grey = (int) ((red + green + blue) / 3);
			for (int i = 0; i < 3; i++) {
				color[i] = grey;
			}
			
			String textRGB = "";
			for (int i = 0; i < 3; i++) {
				textRGB += color[i] + " ";
			}
			
			out.print(textRGB);
			
		}
		
		out.close();
	}
	
	//Handles I/O for the filter method
	public void filterEffect() throws IOException {
		Scanner input = new Scanner(System.in);
		File[] files;

		//Tests for whether the files actually exist
		boolean validFiles = false;
		
		//ArrayList to store input file names
		ArrayList<String> fileNames = new ArrayList<>();
		
		while(!validFiles) {
			ArrayList<String> file_names = new ArrayList<>();
			System.out.println("Please input the images you would like to use.");
			System.out.println("When you are done inputting files, enter \"Done\":");
			while (input.hasNext()){
				String next_input = input.next();
				if (next_input.equals("Done")) {
					break;
				}
				
				else {
					file_names.add(next_input);
				}
			}
		
			try {
				filesCheck(file_names);
				validFiles = true;
				fileNames = file_names;
			}
			
			catch (FileNotFoundException e) {
				System.out.println("One or more invalid file names; check again.");
			}
		}
		
		files = arrayMaker(fileNames);
		System.out.println("Now please input the name of the new image you would like to create:");
			String output = input.next();
			try {
				filter(files, output);
				System.out.println("New image complete!");
			} 
			
			catch (NoSuchElementException e) {
				System.out.println("One or more file contents invalid.");
				
			}
			
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		
		input.close();
		
	}
	
	//Handles I/O for the flipHorizontal method
	public void flipHorizontalEffect() throws IOException {
		Scanner input = new Scanner(System.in);
		String file_name = null;
		
		//Tests for whether the files actually exist
		boolean validFile = false;
		
		while (!validFile) {
			ArrayList<String> fileName = new ArrayList<>();
			System.out.println("Please input the image you would like to flip:");
			fileName.add(input.next());
			try {
				filesCheck(fileName);
				file_name = fileName.get(0);
				validFile = true;
			}
			
			catch (FileNotFoundException e) {
				System.out.println("Invalid file name; please try again.");
			}
		}
		
		File file = new File(file_name);
		
		System.out.println("Now please input the name of the new image you would like to create:");
		String output = input.next();
		try {
			flipHorizontal(file, output);
			System.out.println("New image complete!");
		} 
		
		catch (NoSuchElementException e) {
			System.out.println("File contents invalid.");
			
		}
		
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	
		input.close();
		
	}
	
	//Handles I/O for the greyScale method
	public void greyScaleEffect() throws IOException {
		Scanner input = new Scanner(System.in);
		String file_name = null;
		
		//Tests for whether the files actually exist
		boolean validFile = false;
		
		while (!validFile) {
			ArrayList<String> fileName = new ArrayList<>();
			System.out.println("Please input the image you would like to grey:");
			fileName.add(input.next());
			try {
				filesCheck(fileName);
				file_name = fileName.get(0);
				validFile = true;
			}
			
			catch (FileNotFoundException e) {
				System.out.println("Invalid file name; please try again.");
			}
		}
		
		File file = new File(file_name);
		
		System.out.println("Now please input the name of the new image you would like to create:");
		String output = input.next();
		try {
			greyScale(file, output);
			System.out.println("New image complete!");
		} 
		
		catch (NoSuchElementException e) {
			System.out.println("File contents invalid.");
			
		}
		
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	
		input.close();
		
	}
}

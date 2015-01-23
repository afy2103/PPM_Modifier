import java.util.*;
import java.io.*;


public class EffectsTestA {
	
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		File[] files;
		EffectsA photoshop = new EffectsA();
		boolean validFiles = false;
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
				photoshop.filesCheck(file_names);
				validFiles = true;
				fileNames = file_names;
			}
			
			catch (FileNotFoundException e) {
				System.out.println("One or more invalid file names; check again.");
			}
		}
		
		files = photoshop.arrayMaker(fileNames);
		System.out.println("Now please input the name of the new image you would like to create:");
			String output = input.next();
			try {
				photoshop.filter(files, output);
			} 
			
			catch (NoSuchElementException e) {
				System.out.println("File contents invalid.");
			}
			
			
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		input.close();
		System.out.println("New image complete!");
		
	}

}

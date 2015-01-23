1. Effects Explanation
	Instructions for Use:
		1. To create an Effects object, simply declare Effects().
		
		2. For a given Effects (call it photoshop), photoshop.filter() takes an array
		of image files and creates a new one where each pixel is decided using the majority rule.
		
		3. photoshop.flipHorizontal() takes a single image and creates a new one that is flipped
		horizontally.
		
		4. photoshop.greyScale() takes a single image and creates a new one that is grey scaled
		by taking the average of the RGB values of each pixel.
		
		5. photoshop.filesCheck() checks if each of the file names given actually points to an
		existing file, and if they all do, then creates a list of scanners.
		
		6. photoshop.arrayMaker() takes a list of input file names and creates a File array
		
	Explanation of Code:
		Instance variables: scanners was used as a list of scanners, one for each file. Additionally,
		magicNumber was used to decide the file type (the magic number in PPMs).
		
		Constructor: Here I initialized scanners and also set magicNumber as P3, since this is the 
		PPM file being used.
		
		filter(): This method first creates a new PrinterWriter to write to the given output file 
		specified, then calls upon commonHeader to both check for consistency and correctness in headers,
		and also to create the header for the output file if all the files pass the check. Then the header
		is printed, and majorityRGBGen is called upon that prints in the actual pixels/RGB values. Then,
		all of the scanners are systematically closed (the ones in scanners are done so via for loop).
		
		filesCheck(): This checks for all of the files, by first creating a File array, and then setting up
		scanners for each one; if any throw an exception, then the method is designed to throw it, and otherwise
		the scanners list is updated.
		
		majorityRGBGen(): This method actually writes in each pixel for the PPM file; to do this, while the
		scanners all have remaining RGB values (and throws an appropriate exception if any of the values are not
		possible numbers), and then meanwhile creates an array of pixels (for a fixed position over all scanners),
		which is then passed through majorityColor to create the new pixel via majority rule, the elements of which
		are then added to the file being written.
		
		arrayMaker(): This is rather straightforward; a simple for loop is created, in which a File[] is filled up with
		the appropriate files.
		
		majorityColor(): This finds the relative majority by finding the pixel (by looking at each row of the two
		dimensional array) via majority rule; each successive pixel is chosen as the current pixel, and then a for loop
		iterates over all subsequent pixels, in which a counter is updated; if it goes over the stored max, then the max
		is replaced and the majority color is replaced, as well, until the loop terminates; starting with i as the initial index
		in the nested for loop is to save on comparisons; this doesn't leave out anything because if there was a previous element
		with the same pixel, then the previous iteration would have already accounted for that; the concern is not about covering
		every pixel, but about accounting for every distinct pixel.
		
		commonHeader(): This checks for consistency among headers, and returns the consistent header if the test passes. First it
		creates an array of headers, after which they are all tested using the checkHeader method (for proper format) and then
		another loop is used to see if they are all equal to each other; either one failing results in an exception being thrown. 
		Otherwise, an arbitrary header from the array (since they are all identical) is returned.
		
		retrieveHeader(): This is straightforward, as it returns the header for a given file by simply iterating nextLine() three times
		and returning the resultant concatenated string.
		
		checkHeader(): This checks a given string Header for 4 things: for the proper magic number, whether the dimensions line
		only has two elements, if both of those elements are integers, and if the maximum color line is also an integer. This is done
		by splitting the header by line (already assuming that the header string is in proper spaced format), and then takes the 
		dimensions line (the second line) and splits it up by white space, and subsequent if/else if statements check for the proper
		conditions, calling upon isInteger if necessary. The boolean error is set as true if any of these conditions fail, and is returned.
		
		isInteger(): This simply checks if a given string can be converted into an integer; if parseInt succeeds, then the initial boolean,
		initialized as true, is returned; otherwise, an exception is thrown, and the catch clause results in the boolean being set as false,
		which is consequently returned.
		
		flipHorizontal(): This takes an input file and an output file name, and flips the image horizontally into a new image with the output
		name. To do this, it first creates the same header (as the file type and dimensions should not be changed), and to flip the image, 
		for each line of the image, makes a two-dimensional array (an array of pixels), and simply fills in the line in reverse, and then each
		value in the pixel is added to the output file.
		
		greyScale(): This takes an input file and an output file name, and turns the image into a grey-scaled version with the output file name.
		To do this, it first creates the same header (as the file type and dimensions should not be changed), and then scans the rest, creating
		a pixel every three values scanned, and replaces each value in the pixel with the average of the three values scanned, and then adds the
		values to the output file.
		
		filterEffect(): This method handled the I/O if the filter effect was selected. Here a while loop was used, so that the user would be
		forced to input valid files (once this was done, validFiles was changed, but otherwise an exception is thrown with an error message). 
		If valid files are entered, then a file array is constructed and an output file name is asked; here additional exceptions from within
		the filter method are caught, and the filter method is called upon.
		
		flipHorizontalEffect(): This method handled the I/O if the flipHorizontal effect was selected. Here similarly a while loop was used to
		check for valid files, and if passed the file name given would be assigned to a String (otherwise an exception would be thrown), and a 
		file was constructed out of this file name, after which the output would be asked, and the flipHorizontal method would be called upon,
		and possible exceptions caught and handled appropriately.
		
		greyScaleEffect(): This method handled the I/O if the greyScale effect was selected. This operates almost identically to the
		flipHorizontalEffect method, except instead of calling upon the flipHorizontal method, it calls upon the greyScale method. 
		
2. EffectsTest Explanation
	Instructions for Use:
		1. To choose which effect to use, simply type in the given choices in the parentheses.
		
			filter:
			1. To input any given number of files, simply type them in and type "Done" when finished.
		
			2. Then input the name of the desired output file, and the filter method should go to work, displaying a completion message.
			
			flipHorizontal:
			1. Simply type in the name of the file to be flipped.
			
			2. Then input the name of the desired output file, and the flipHorizontal method should go to work, displaying a completion message.
			
			greyScale:
			1. Simply type in the name of the filter to be flipped.
			
			2. Then input the name of the desired output file, and the greyScale method should go to work, displaying a completion message.
			
	Explanation of Code:
		main method: This method handled the I/O for choosing the appropriate effect (and then calls upon the additional I/O method for the
		chosen effect). An Effects object was declared, and then in addition to a scanner for input, a boolean valid_input was set as true; a while
		loop was set up to ensure that the user entered in the possible responses for the effects inquiry; if any incorrect input was entered, then 
		the loop would continue until it equalled one of the possible given responses, valid_input would be set as true, and the appropriate I/O
		method would be called upon, with the loop then ending and a completion message stated if no exceptions were thrown by the called methods.

		


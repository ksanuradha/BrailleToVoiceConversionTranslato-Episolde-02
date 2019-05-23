package com.edu.msc.sliit.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.msc.sliit.service.BinaryValueCalculation;
import com.edu.msc.sliit.service.BraileeDetect;
import com.edu.msc.sliit.service.CountWhireSpots;
import com.edu.msc.sliit.service.FindXCordinates;

@RestController
@RequestMapping("/api/brailleapplication")
@ComponentScan("com.edu.msc.sliit")
public class BrailleApplicationRestController {
	
	@Autowired
	FindXCordinates findXCordinates;
	
	@Autowired
	CountWhireSpots countWhireSpots;

	@PostMapping("/uploadFile")
	public void uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		try {
			// Get the filename and build the local file path (be sure that the
			// application have write permissions on such directory)
			String filename = uploadfile.getOriginalFilename();
			String directory = "./src/main/resources/static/img";
			String filepath = Paths.get(directory, "brailee.jpg").toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}// method uploadFile

	@PostMapping("/scan")
	public void scanImage() {
		Mat imgGrayscale = new Mat();
		Mat image = Imgcodecs.imread("./src/main/resources/static/img/brailee.jpg", 1);
		HashMap<Integer, double[]> findRectangle = new BraileeDetect(image).findRectangle();
		HashMap<Integer, ArrayList<Double>> findAverageXCordinates = findXCordinates.findAverageXCordinates(findRectangle);
		try {
			HashMap<Integer, int[]> calculatCordinates = BinaryValueCalculation.calculateBanayValuesForEachCordinates(findAverageXCordinates);
			printHashMap(calculatCordinates);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		File directory = new File("./src/main/resources/static/img/dilate.jpg");	
//		try {
//			FileUtils.forceDelete(directory);
//		}
//		catch(Exception e){
//			System.out.println(e.fillInStackTrace());
//		}
		
		
    	//File f3 = new File("./src/main/resources/static/img/dilate.jpg");
    	//System.out.println(f3.delete());
		//countWhireSpots.drawALine(findAverageXCordinates);   
	}
	
	private void printHashMap(HashMap<Integer, int[]> calculateBanayValuesForEachCordinates) {
		Iterator hmIterator = calculateBanayValuesForEachCordinates.entrySet().iterator();
		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry) hmIterator.next();
			 int[] cordinates = ((int[]) mapElement.getValue());
			 System.out.println(Arrays.toString(cordinates));
		}
	}

}

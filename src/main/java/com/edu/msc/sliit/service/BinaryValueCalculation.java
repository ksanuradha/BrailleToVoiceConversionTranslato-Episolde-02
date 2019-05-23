package com.edu.msc.sliit.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BinaryValueCalculation {
	
	    public static HashMap<Integer, int[]> calculateBanayValuesForEachCordinates(HashMap<Integer, ArrayList<Double>> findAverageXCordinates) 
	            throws FileNotFoundException, UnsupportedEncodingException {
	        
	        ArrayList<Double> yCordinates = findAverageXCordinates.get(0);
	        ArrayList<Double> xCordinates = findAverageXCordinates.get(1);
	        Workbook workbook = new XSSFWorkbook();//Write to the Exel Sheet
			CreationHelper createHelper = workbook.getCreationHelper();
			Sheet sheet = workbook.createSheet("BraileeOCR");
			createRows(sheet,xCordinates.size()/3);
			URL url;
	        BufferedImage image = null;
	        boolean whitePixel = false;	        
	        double binaryValue=0;
	        try {
	            image = ImageIO.read(new FileInputStream("./src/main/resources/static/img/dilate.jpg"));
	        } catch (Exception ex) {
	            Logger.getLogger(BinaryValueCalculation.class.getName()).log(Level.SEVERE, null, ex);
	        }//Math.round(x)
	        int cellNo = 0;
	        for (int i = 0; i < yCordinates.size(); i++) {
	            for (int j = 0; j < xCordinates.size(); j++) {
	            	 		             
	                int clr = image.getRGB(Integer.parseInt(Math.round(yCordinates.get(i)) + ""), Integer.parseInt(Math.round(xCordinates.get(j)) + ""));
	                if (isWhitePixel(clr)) {
	                    whitePixel = true;
	                    //System.out.println("");
	                } else {
	                    //System.out.println("whitePixel Before : "+whitePixel);
	                    whitePixel = isWhitePixelFurther(Integer.parseInt(Math.round(yCordinates.get(i)) + ""), Integer.parseInt(Math.round(xCordinates.get(j)) + ""), image);
//	                    if(whitePixel){
//	                        System.out.println("i : "+i+" j : "+j);
//	                        System.out.println("whitePixel After : "+whitePixel);
//	                    }
	                    
	                }
	                //Calculate binary Values
	                //Write to Exel
	                if(i%2==0){
	                    if(j%3==0 && whitePixel ){
	                       binaryValue=binaryValue+1;
	                    }
	                    else if(j%3==1 && whitePixel ){
	                        binaryValue=binaryValue+2;
	                    }
	                    else if(j%3==2){
	                        if(whitePixel){
	                            binaryValue=binaryValue+4;
	                        }
	                        Row row1=sheet.getRow(cellNo);
	                        Cell cell1=row1.createCell(i);
	                        cell1.setCellValue(binaryValue);
	                        cellNo++;
	                        //PoI Write to Exel	                       
	                        binaryValue=0;
	                    }
	                }
	                else if(i%2==1){
	                    if(j%3==0 && whitePixel){
	                        binaryValue=binaryValue+8;
	                    }
	                    else if(j%3==1 && whitePixel){
	                        binaryValue=binaryValue+16;
	                    }
	                    else if(j%3==2){
	                        if(whitePixel){
	                             binaryValue=binaryValue+32;
	                        }
	                        Row row1=sheet.getRow(cellNo);
	                        Cell cell1=row1.createCell(i);
	                        cell1.setCellValue(binaryValue);
	                        cellNo++;
	                        //PoI Write to Exel	                       
	                        binaryValue=0;
	                    }
	                }                   
	                whitePixel = false;
	            }
	            cellNo=0;
	        }
	        FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream("./src/main/resources/static/img/poi-generated-BinaryValuesforDilatedImage.xlsx");
				workbook.write(fileOut);
				fileOut.close();
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
   
	        return readExelSheet(workbook,yCordinates.size(),xCordinates.size()/3);
	    }
	    
	    private static HashMap<Integer, int[]> readExelSheet(Workbook workbook,int width,int height) {//Calculate Brailee values
	    	Sheet braileeOCR = workbook.getSheet("BraileeOCR");
	    	Row row;
	    	HashMap<Integer, int[]> braileeLetters =new HashMap<Integer, int[]>();
	    	for(int i=0;i<height;i++) {
	    		row = braileeOCR.getRow(i);
	    		int values[]=new int[width/2];
	    		int letterNo=0;
	    		int sum=0;
	    		int count=0;
	    		inner: for (Cell cell : row) {
	    			if(cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	    				break inner;
	    			}
	    			else {
	    				DataFormatter formatter = new DataFormatter();
    					String str = formatter.formatCellValue(cell);
    					int parseInt = Integer.parseInt(str);
    					
	    				if(count%2==1) {
	    					sum=sum+parseInt;
	    					values[letterNo]=sum;
	    					//save to Array
	    					sum=0;
	    					count=0;
	    					letterNo++;
	    				}
	    				else {
	    					sum=sum+parseInt;
	    					count++;
	    				}
	    				
	    			}
	    			braileeLetters.put(i, values);
	    		}
	    	}
	    	//delete images and Exel files
	    	File f1 = new File("./src/main/resources/static/img/poi-generated-BinaryValuesforDilatedImage.xlsx");
//	    	File f2 = new File("./src/main/resources/static/img/brailee.jpg");
//	    	File f3 = new File("./src/main/resources/static/img/dilate.jpg");
	    	boolean delete = f1.delete();
//	    	boolean delete2 = f2.delete();
//	    	boolean delete3 = f3.delete();
	    	return braileeLetters;
	    }

	 	private static void createRows(Sheet sheet,int size) {
	 		for(int i = 0; i <size; i++) {
	        	sheet.createRow(i);        
	        }
	 	}
	 
	    public static boolean isWhitePixel(int clr) {
	        int red = (clr & 0x00ff0000) >> 16;
	        int green = (clr & 0x0000ff00) >> 8;
	        int blue = clr & 0x000000ff;
	        //System.out.println("red: " + red + " green: " + green + " blue: " + blue);
	        if (red > 235 && green > 235 && blue > 235) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    public static boolean isWhitePixelFurther(int x, int y, BufferedImage image) {
	        boolean whitePixel = false;
	        outer:
	        for (int i = x - 1; i <= x + 1; i++) {
	            for (int j = y - 1; j <= y + 1; j++) {
	                whitePixel = isWhitePixel(image.getRGB(i, j));
	                //System.out.println("####### x : " + j + " y : " + i);
	                if (whitePixel) {
	                    break outer;
	                }

	            }
	        }
	        return whitePixel;
	    }
	    
	    
}

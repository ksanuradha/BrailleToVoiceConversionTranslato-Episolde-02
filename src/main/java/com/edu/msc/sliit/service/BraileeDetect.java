package com.edu.msc.sliit.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@Service
public class BraileeDetect {

    private Mat image;
    private Mat originalImage;
    private List<MatOfPoint> contours;
    private Mat hierarchy;
    private int HEIGHT;
    private int WIDTH;
    private List<Rect> rects = new ArrayList<Rect>();
    private Mat imgGrayscale;
    private Random rng = new Random(12345);
    
    public BraileeDetect(){}

    public BraileeDetect(Mat input) {
        this.originalImage = input;
        this.image = new Mat();
        this.hierarchy = new Mat();
        this.contours = new ArrayList<MatOfPoint>();
        this.HEIGHT = input.height();
        this.WIDTH = input.width();
        this.imgGrayscale = new Mat();
    }

    private void setFilter() {
        //Apply gaussian blur to remove noise
        Imgproc.cvtColor(originalImage, imgGrayscale, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imgGrayscale, imgGrayscale, new Size(3, 3), 0);
        Imgproc.adaptiveThreshold(imgGrayscale, imgGrayscale, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
        Imgproc.medianBlur(imgGrayscale, imgGrayscale, 3);
        Imgproc.threshold(imgGrayscale, imgGrayscale, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(imgGrayscale, imgGrayscale, new Size(3, 3), 0);
        Imgproc.threshold(imgGrayscale, imgGrayscale, 0, 255, Imgproc.THRESH_OTSU);
        
       // Imgcodecs.imwrite("./src/main/resources/static/img/grayscale.jpg", imgGrayscale);
        
        //Threshold
        // Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 7, 1);

        //Invert the image
        //Core.bitwise_not(imgGrayscale, imgGrayscale);
        //Core.bitwise_not(imgGrayscale, imgGrayscale);
        //Erosion
        double erosion_size = 0.9; //initialy 0.5
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
        Imgproc.erode(imgGrayscale, imgGrayscale, kernel);
        //Imgcodecs.imwrite("./src/main/resources/static/img/Erosion2.jpg", imgGrayscale);
        
        //Dilate
        double dilation_size = 1;
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
        // Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(3, 3), new Point(1, 1));
        Imgproc.dilate(imgGrayscale, imgGrayscale, element1);

        //print image
        Imgcodecs.imwrite("./src/main/resources/static/img/dilate.jpg", imgGrayscale);
                
    }
    
    public HashMap<Integer, double[]> findRectangle() {
        setFilter();
        this.rects.clear();
        Imgproc.findContours(imgGrayscale, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        HashMap<Integer, double[]> capitalCities = new HashMap<Integer, double[]>();
        List<MatOfPoint> ret = new ArrayList<>();
        int i = 1;
        int y = 0;
        int merge = contours.size() + 1;
        double maxX = 0, maxY = 0, minX = 0, minY = 0;
        //Mat image2 = Imgcodecs.imread(".\\src\\com\\edu\\sliit\\img\\dilation.jpg", 3);
        for (MatOfPoint cnt : contours) {
            int x = 1;
            //System.out.println("MatOfPoint no : " + i+" Area : "+Imgproc.contourArea(cnt));
            for (Point p : cnt.toList()) {
                if (x == 1) {
                    maxX = p.x;
                    minX = p.x;
                    maxY = p.y;
                    minY = p.y;
                }
                if (maxX < p.x) {
                    maxX = p.x;
                }
                if (minX > p.x) {
                    minX = p.x;
                }
                if (maxY < p.y) {
                    maxY = p.y;
                }
                if (minY > p.y) {
                    minY = p.y;
                }
                x++;
            }
            if (!(i == 41 || i == 40)) {
                //System.out.println("MatOfPoint no : " + i);
                if (Imgproc.contourArea(cnt) > 20) { //Eliminate Merge                    
                    double averageXYCordinates[] = new double[2];
                    averageXYCordinates[0] = (minX + (((maxX + minX) / 2) - 0.8)) / 2;//Put difference between colums
                    averageXYCordinates[1] = (maxY + minY) / 2;
                    capitalCities.put(i, averageXYCordinates);
                    //Merge recover
                    double averageXYCordinates2[] = new double[2];
                    averageXYCordinates2[0] = ((((maxX + minX) / 2) + 0.8) + maxX) / 2;//Put difference between colums
                    averageXYCordinates2[1] = (maxY + minY) / 2;
                    capitalCities.put(merge, averageXYCordinates2);
                    merge++;
                    //Merge recover
                } else {
                    double averageXYCordinates[] = new double[2];
                    averageXYCordinates[0] = (maxX + minX) / 2;
                    averageXYCordinates[1] = (maxY + minY) / 2;
                    capitalCities.put(i, averageXYCordinates);
                }
            }
            x = 0;
            i++;
        }
        return capitalCities;
    }
}

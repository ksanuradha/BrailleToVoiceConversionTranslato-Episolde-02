package com.edu.msc.sliit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import nu.pattern.OpenCV;

@SpringBootApplication
public class BrailleToVoiceConversionTranslatorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BrailleToVoiceConversionTranslatorApplication.class, args);		
		OpenCV.loadShared();
	}
}

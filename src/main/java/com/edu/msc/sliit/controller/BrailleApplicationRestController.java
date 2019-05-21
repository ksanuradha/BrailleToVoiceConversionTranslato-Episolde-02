package com.edu.msc.sliit.controller;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/brailleapplication")
@ComponentScan("com.edu.msc.sliit")
public class BrailleApplicationRestController {

	
	@PostMapping("/import")
	public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile,@RequestParam("pc") String projectName,
			@RequestParam("vew") String projectVersion){
		//Versioncontrol versioncontrol= new Versioncontrol();
		//versioncontrol.setProjectName(projectName);
		//versioncontrol.setProjectVersion(projectVersion);
		//	bcMatrixService.saveBCMatrix(reapExcelDataFile,versioncontrol);		

	}
	
	
}

package com.telino.limsdataextractor;

import com.telino.limsdataextractor.service.LIMSWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LimsDataExtractorApplication {

	private static LIMSWebService object;

	@Autowired
	public void setLIMSWebService(LIMSWebService object){
		LimsDataExtractorApplication.object = object;
	}


	public static void main(String[] args) {
		SpringApplication.run(LimsDataExtractorApplication.class, args);
		object.printUsername();
	}

}

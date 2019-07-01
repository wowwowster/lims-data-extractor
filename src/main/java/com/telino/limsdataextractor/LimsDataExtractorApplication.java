package com.telino.limsdataextractor;

import com.telino.limsdataextractor.fakemodel.apiexterne.ImportApiExterne;
import com.telino.limsdataextractor.fakemodel.apiexterne.ParametreApiExterne;
import com.telino.limsdataextractor.importt.LIMSImporter;
import com.telino.limsdataextractor.service.LIMSWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class LimsDataExtractorApplication {

	private static LIMSWebService limsWsForTest;
	private static LIMSImporter importer;

	@Autowired
	public void setLIMSWebService(LIMSWebService limsWs){
		LimsDataExtractorApplication.limsWsForTest = limsWs;
	}

	@Autowired
	public void setImporter(LIMSImporter importer) {
		this.importer = importer;
	}

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(LimsDataExtractorApplication.class, args);
		limsWsForTest.printUsername();

		ImportApiExterne importWsExterne = new ImportApiExterne();
		importWsExterne.setCode("LeCodebbbbb");
		importWsExterne.setTypeImportExterne("LIMS");
		importWsExterne.addOrUpdateParametre(new ParametreApiExterne("ALL"));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
		Date dateLastFinishedAt = df.parse("2017/03/01 04:00:00.000");
		Date dateFin = df.parse("2017/04/15 17:00:00.000");
		importWsExterne.setLastFinishedAt(dateLastFinishedAt);
		importer.doImport(importWsExterne, dateFin);
	}

}

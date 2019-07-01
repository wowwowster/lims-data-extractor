package com.telino.limsdataextractor;

import com.telino.limsdataextractor.fakemodel.apiexterne.ImportApiExterne;
import com.telino.limsdataextractor.fakemodel.apiexterne.ParametreApiExterne;
import com.telino.limsdataextractor.importt.LIMSImporter;
import com.telino.limsdataextractor.service.LIMSWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
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

	private static final Logger logger = LoggerFactory.getLogger(LimsDataExtractorApplication.class);

	public static void main(String[] args) throws ParseException {
		/*File logsFolder = new File("logs");
		logsFolder.mkdir(); */
		logger.info("Début extraction");
		SpringApplication.run(LimsDataExtractorApplication.class, args);
		System.out.println("jourDeBut avant argumentation="+limsWsForTest.getJourDeBut());
		logger.info("jourDeBut avant argumentation="+limsWsForTest.getJourDeBut());
		limsWsForTest.setJourDeBut(args.length > 0 ? args[0]:limsWsForTest.getJourDeBut());
		logger.info("jourDeBut="+limsWsForTest.getJourDeBut());
		limsWsForTest.printUsername();

		ImportApiExterne importWsExterne = new ImportApiExterne();
		importWsExterne.setCode("LeCodebbbbb");
		importWsExterne.setTypeImportExterne("LIMS");
		importWsExterne.addOrUpdateParametre(new ParametreApiExterne("ALL"));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
		Date dateLastFinishedAt = df.parse("2017/03/01 04:00:00.000");
		Date dateFin = df.parse("2017/04/05 17:00:00.000");
		importWsExterne.setLastFinishedAt(dateLastFinishedAt);
		importer.doImport(importWsExterne, dateFin);
		logger.info("Fin extraction");

	}

}

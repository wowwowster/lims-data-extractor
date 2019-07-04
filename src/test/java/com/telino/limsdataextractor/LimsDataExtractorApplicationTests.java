package com.telino.limsdataextractor;

import com.telino.limsdataextractor.fakemodel.apiexterne.ImportApiExterne;
import com.telino.limsdataextractor.fakemodel.apiexterne.ParametreApiExterne;
import com.telino.limsdataextractor.importt.LIMSImporter;
import com.telino.limsdataextractor.service.LIMSWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LimsDataExtractorApplicationTests {

	@Test
	public void contextLoads() {
	}

	private ImportApiExterne importWsExterne;

/*	@Autowired
	private LIMSWebService webService;  */

	@Autowired
	private LIMSImporter importer;

	@Test
	public void testImportPagination() throws ParseException {
		//importer.setLimsWS(webService);
		importWsExterne = new ImportApiExterne();
		importWsExterne.setCode("LeCodebbbbb");
		importWsExterne.setTypeImportExterne("LIMS");
		importWsExterne.addOrUpdateParametre(new ParametreApiExterne("ALL"));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
		Date dateLastFinishedAt = df.parse("2017/03/01 04:00:00.000");
		Date dateFin = df.parse("2017/04/05 17:00:00.000");
		//importWsExterne.setLastFinishedAt(dateLastFinishedAt);
		importWsExterne.setLastFinishedAt(null);
		//importer.doImport(importWsExterne, dateFin);
		importer.doImport(importWsExterne, null);

	}

}

package com.telino.limsdataextractor.service;

import com.telino.limsdataextractor.bean.LIMSReponseBean;
import com.telino.limsdataextractor.constante.Const;
import com.telino.limsdataextractor.exception.ApplicationException;
import com.telino.limsdataextractor.fakemodel.apiexterne.ImportApiExterne;
import com.telino.limsdataextractor.fakemodel.apiexterne.ParametreApiExterne;
import com.telino.limsdataextractor.utils.LimsFileUtils;
import com.telino.limsdataextractor.utils.RestTemplateUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("LIMSWebService")
public class LIMSWebService {

    @Value("${lims.username}")
    private String username;

    @Value("${lims.password}")
    private String password;

    @Value("${lims.url}")
    private String url;

    @Value("${lims.jourDeBut}")
    private String jourDeBut;

    @Value("${lims.jourFin}")
    private String jourFin;

    static int compteur;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJourDeBut() {
        return jourDeBut;
    }

    public void setJourDeBut(String jourDeBut) {
        this.jourDeBut = jourDeBut;
    }

    public String getJourFin() {
        return jourFin;
    }

    public void setJourFin(String jourFin) {
        this.jourFin = jourFin;
    }

    private Logger logger = LogManager.getLogger(LIMSWebService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    public void printUsername()
    {
        System.out.println(username);
    }

    public LIMSReponseBean getPage(ImportApiExterne importExterne, Date dateDebut, Date dateFin) {
        if (null==importExterne.getParametres()){
            throw new ApplicationException("L'appel LIMS n'a pas de paramètres en entrée");
        }

        ParametreApiExterne parametre = importExterne.getParametres().stream().filter(param ->
                param.getNom().equalsIgnoreCase(Const.IMPORT_LIMS_NOM_PARAMETRE) && param.getImportApiExterne().equals(importExterne)).findFirst().get();
        String entites = parametre != null
                ? parametre.getValeur()
                : null;
        logger.debug("Username LIMS:"+username);
        return getPage(url, username, password, entites, dateDebut, dateFin);
    }

    public LIMSReponseBean  getPage(String url, String user, String password, String entites, Date dateDebut, Date dateFin) {
        logger.debug("Début d'exécution de la méthode getPage");
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (entites != null) {
                uriBuilder.addParameter("entities", entites);
            } else {
                uriBuilder.addParameter("entities", "ALL");
            }
            String startDateParameter = dateDebut == null ? "ALL" : dateFormat.format(dateDebut);
            uriBuilder.addParameter("start-date", startDateParameter);

            if (dateFin != null) {
                uriBuilder.addParameter("end-date", dateFormat.format(dateFin));
            }
            RestTemplate restTemplate = new RestTemplate();

          /*  ObjectMapper objectMapper = new ObjectMapper();
           // objectMapper.registerModule(new JodaModule());
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
            objectMapper.setDateFormat(new ISO8601DateFormat());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonMessageConverter.setObjectMapper(objectMapper);
            messageConverters.add(jsonMessageConverter);
            restTemplate.setMessageConverters(messageConverters);  */


            HttpHeaders headers = RestTemplateUtils.addBasicAuth(new HttpHeaders(), user, password);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<LIMSReponseBean> result = restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, LIMSReponseBean.class);

            File outputFolder = new File("output");
            outputFolder.mkdir();
            LimsFileUtils.cleanFolder(outputFolder);
            HttpEntity<String> entityBis = new HttpEntity<String>("parameters", headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    uriBuilder.build(),
                    HttpMethod.GET, entityBis, byte[].class);

            compteur = compteur + 1;
            if (response.getStatusCode() == HttpStatus.OK) {
                Files.write(Paths.get("output/lims-entites-"+entites+" - "+  compteur +".json"), response.getBody());
            }

            logger.debug("Fin d'exécution de la méthode getPage");
            return result.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);

        } catch (HttpClientErrorException ex) {
            throw new ApplicationException("Le service LIMS n'est pas disponible.");
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    public LIMSReponseBean getPage(String url, String user, String password) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = RestTemplateUtils.addBasicAuth(new HttpHeaders(), user, password);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<LIMSReponseBean> result = restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, LIMSReponseBean.class);

            HttpEntity<String> entityBis = new HttpEntity<String>("parameters", headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    uriBuilder.build(),
                    HttpMethod.GET, entityBis, byte[].class);


            compteur = compteur + 1;

            List<NameValuePair> params = URLEncodedUtils.parse(uriBuilder.build(), Charset.forName("UTF-8"));
            String entites="";
            for (NameValuePair param : params) {
                if (param.getName().equalsIgnoreCase("entities")) {
                 entites = param.getValue();
                };
            }

            if (response.getStatusCode() == HttpStatus.OK) {
                Files.write(Paths.get("output/lims-entites-"+entites+" - "+  compteur +".json"), response.getBody());
            }

            return result.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    public LIMSReponseBean getNextPage(LIMSReponseBean previousResponse) {
        return getPage(previousResponse.getNextUrl(),  username, password);
    }

}

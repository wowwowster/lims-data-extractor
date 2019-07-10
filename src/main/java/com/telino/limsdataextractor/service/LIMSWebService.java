package com.telino.limsdataextractor.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    String pathJson = "";
    String startDateParameter;
    String endDateParameter;

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

    // pour tester l'autowiring en mode statique
    public void printUsername() {
        System.out.println(username);
    }

    public LIMSReponseBean getPage(ImportApiExterne importExterne, Date dateDebut, Date dateFin) {
        if (null == importExterne.getParametres()) {
            throw new ApplicationException("L'appel LIMS n'a pas de paramètres en entrée");
        }

        ParametreApiExterne parametre = importExterne.getParametres().stream().filter(param ->
                param.getNom().equalsIgnoreCase(Const.IMPORT_LIMS_NOM_PARAMETRE) && param.getImportApiExterne().equals(importExterne)).findFirst().get();
        String entites = parametre != null
                ? parametre.getValeur()
                : null;
        logger.debug("Username LIMS:" + username);
        return getFirstPage(url, username, password, entites, dateDebut, dateFin);
    }

    public LIMSReponseBean getFirstPage(String url, String user, String password, String entites, Date dateDebut, Date dateFin) {
        logger.debug("Début d'exécution de la méthode getPage");
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (entites != null) {
                uriBuilder.addParameter("entities", entites);
            } else {
                uriBuilder.addParameter("entities", "ALL");
            }
            startDateParameter = dateDebut == null ? "ALL" : dateFormat.format(dateDebut);
            uriBuilder.addParameter("start-date", startDateParameter);
            endDateParameter = dateFin == null ? "ALL" : dateFormat.format(dateFin);
            uriBuilder.addParameter("end-date", endDateParameter);

            // gestion du certificat
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
//            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new ResponseErrorHandler() {

                @Override
                public boolean hasError(ClientHttpResponse arg0) throws IOException {

                    logger.info("StatusCode from remote http resource:"+arg0.getStatusCode());
                    logger.info("RawStatusCode from remote http resource:"+arg0.getRawStatusCode());
                    logger.info("StatusText from remote http resource:"+arg0.getStatusText());

            //        String body = new BufferedReader(new InputStreamReader(arg0.getBody()))
            //                .lines().collect(Collectors.joining("\n"));

                    //logger.fatal("Error body from remote http resource:"+body);
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse arg0) throws IOException {
                }
            });
            HttpHeaders headers = RestTemplateUtils.addBasicAuth(new HttpHeaders(), user, password);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<LIMSReponseBean> result = restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, LIMSReponseBean.class);
            pathJson = "output";
            File outputFolder = new File(pathJson);
            if (!outputFolder.exists()) {
                if (!outputFolder.mkdir()) {
                    logger.fatal("création du répertoire " + outputFolder + "impossible");
                    throw new ApplicationException("création du répertoire " + outputFolder + "impossible");
                }
            }

            List<String> listStartDateParameter = Lists.newArrayList(Splitter.on("-").split(startDateParameter));
            int indexFinListStartDateParameter = listStartDateParameter.size()>3 ? 3:listStartDateParameter.size();
            listStartDateParameter = listStartDateParameter.subList(0,indexFinListStartDateParameter);
            Lists.reverse(listStartDateParameter);
            startDateParameter= listStartDateParameter.stream().collect(Collectors.joining(""));

            List<String> listEndDateParameter = Lists.newArrayList(Splitter.on("-").split(endDateParameter));
            int indexFinListEndDateParameter = listEndDateParameter.size()>3 ? 3:listEndDateParameter.size();
            listEndDateParameter = listEndDateParameter.subList(0,indexFinListEndDateParameter );
            Lists.reverse(listEndDateParameter);
            endDateParameter= listEndDateParameter.stream().collect(Collectors.joining(""));
            pathJson = "output/" + entites + "-" + startDateParameter + "-" + endDateParameter;
            outputFolder = new File(pathJson);
            if (!outputFolder.exists()) {
                if (!outputFolder.mkdir()) {
                    logger.fatal("création du répertoire " + outputFolder + "impossible");
                    throw new ApplicationException("création du répertoire " + outputFolder + "impossible");
                }
            } else {
                LimsFileUtils.cleanFolder(outputFolder);

            }
            HttpEntity<String> entityBis = new HttpEntity<String>("parameters", headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    uriBuilder.build(),
                    HttpMethod.GET, entityBis, byte[].class);
            logger.info("Appel WS : " + uriBuilder.build());
            compteur = compteur + 1;

            String nomFichier = "";
            if (response.getStatusCode() == HttpStatus.OK) {
               nomFichier = outputFolder + "/lims-entites-" + entites + "-" + startDateParameter+ "-" + endDateParameter + "-" + compteur + ".json";
                Files.write(Paths.get(nomFichier), response.getBody());
            }
            logger.info("Fichier créé : " + nomFichier);
            logger.debug("Fin d'exécution de la méthode getPage");
            return result.getBody();
        } catch (HttpClientErrorException ex) {
            logger.fatal("Le service LIMS n'est pas disponible", ex);
            throw new ApplicationException("Le service LIMS n'est pas disponible.");
        } catch (Exception ex) {
            logger.fatal(ex);
            throw new ApplicationException(ex);
        }
    }

    public LIMSReponseBean getPage(String url, String user, String password) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            // gestion du certificat
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.setErrorHandler(new ResponseErrorHandler() {

                @Override
                public boolean hasError(ClientHttpResponse arg0) throws IOException {

                    logger.info("StatusCode from remote http resource:"+arg0.getStatusCode());
                    logger.info("RawStatusCode from remote http resource:"+arg0.getRawStatusCode());
                    logger.info("StatusText from remote http resource:"+arg0.getStatusText());

                    //        String body = new BufferedReader(new InputStreamReader(arg0.getBody()))
                    //                .lines().collect(Collectors.joining("\n"));

                    //logger.fatal("Error body from remote http resource:"+body);
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse arg0) throws IOException {
                }
            });
            HttpHeaders headers = RestTemplateUtils.addBasicAuth(new HttpHeaders(), user, password);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<LIMSReponseBean> result = restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, LIMSReponseBean.class);
            logger.info("Appel WS : " + uriBuilder.build());
            HttpEntity<String> entityBis = new HttpEntity<String>("parameters", headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    uriBuilder.build(),
                    HttpMethod.GET, entityBis, byte[].class);
            compteur = compteur + 1;

            List<NameValuePair> params = URLEncodedUtils.parse(uriBuilder.build(), Charset.forName("UTF-8"));
            String entites = "";
            for (NameValuePair param : params) {
                if (param.getName().equalsIgnoreCase("entities")) {
                    entites = param.getValue();
                }
            }
            String nomFichier = "";
            if (response.getStatusCode() == HttpStatus.OK) {
                nomFichier = pathJson + "/lims-entites-" + entites + "-" + startDateParameter + "-" + endDateParameter + "-" + compteur + ".json";
                Files.write(Paths.get(nomFichier), response.getBody());
            }
            logger.info("Fichier créé : " + nomFichier);
            return result.getBody();
        } catch (Exception ex) {
            logger.fatal(ex);
            throw new ApplicationException(ex);
        }
    }

    public LIMSReponseBean getNextPage(LIMSReponseBean previousResponse) {
        return getPage(previousResponse.getNextUrl(), username, password);
    }

}

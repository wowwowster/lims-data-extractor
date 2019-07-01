package com.telino.limsdataextractor.importt;

import com.telino.limsdataextractor.bean.LIMSAnalyseBean;
import com.telino.limsdataextractor.bean.LIMSReponseBean;
import com.telino.limsdataextractor.fakemodel.ResultatAnalyse;
import com.telino.limsdataextractor.fakemodel.ResultatImportExport;
import com.telino.limsdataextractor.fakemodel.apiexterne.ImportApiExterne;
import com.telino.limsdataextractor.service.LIMSWebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LIMSImporter {

    private static final Logger logger = LogManager.getLogger(LIMSImporter.class);

    @Autowired
    private LIMSWebService limsWS;

    public ResultatImportExport doImport(ImportApiExterne importExterne, Date dateFin) {
        System.out.println("Début d'exécution de la méthode doImport");
        ResultatImportExport resultat = new ResultatImportExport();
        LIMSReponseBean page = limsWS.getPage(importExterne, importExterne.getLastFinishedAt(), dateFin);

        if (page == null || page.getAnalyses().isEmpty()) {
            System.out.println("alarmNoData");
        } else {

            do {
                doImport(page.getAnalyses(), resultat);
                page = limsWS.getNextPage(page);

                // Le WS LIMS renvoie à la pagination suivante même si celle-ci ne contient pas de données
            } while (page.hasNext() && !page.getAnalyses().isEmpty());
        }


        logger.info("Fin d'exécution de la méthode doImport");
        return resultat;
    }

    public void doImport(List<LIMSAnalyseBean> analyses, ResultatImportExport resultat) {
        analyses.forEach(a -> doImport(a, resultat));
    }

    public void doImport(LIMSAnalyseBean analyse, ResultatImportExport resultat) {
        Integer donneeVariableId;
        ResultatAnalyse resultatAnalyse;
  /*        try {
          logger.info(String.format("Récupération de la définition de variable à partir de code sandre ouvrage : %s " +
                            " -  code sandre point mesure : %s - code sandre parametre : %s - code sandre fraction %s " +
                            " -  source id : %s ",
                    analyse.getCodeSandreOuvrage(),
                    analyse.getCodeSandrePointMesure(),
                    analyse.getCodeSandreParametre(),
                    analyse.getCodeSandreFraction(),
                    analyse.getSourceId()
            ));
            DefinitionVariable definitionVariable = getDefinitionVariable(
                    analyse.getCodeSandreOuvrage(),
                    analyse.getCodeSandrePointMesure(),
                    analyse.getCodeSandreParametre(),
                    analyse.getCodeSandreFraction()
            );

            if (definitionVariable == null) {
                // Signaler analyse ignoree
                resultat.error(
                        DeserializationErrorHandler.ErrorCode.RULE,
                        DeserializationErrorHandler.ErrorGravity.WARNING,
                        String.format("Pas de variable trouvée pour le point de mesure %s/%s parametre %s fraction %s : le résultat %s est ignoré",
                                analyse.getCodeSandreOuvrage(),
                                analyse.getCodeSandrePointMesure(),
                                analyse.getCodeSandreParametre(),
                                analyse.getCodeSandreFraction(),
                                analyse.getSourceId()
                        ),
                        analyse);
                return;
            }

            if (analyse.getResultat() == null) {
                resultat.error(
                        DeserializationErrorHandler.ErrorCode.RULE,
                        DeserializationErrorHandler.ErrorGravity.WARNING,
                        String.format("Pas de résultat pour l'analyse %s => ignorée",
                                analyse.getSourceId()
                        ),
                        analyse);
            }

            Date dateMesure = analyse.getDatePrelevement();
            if (dateMesure == null) {
                resultat.error(
                        DeserializationErrorHandler.ErrorCode.RULE,
                        DeserializationErrorHandler.ErrorGravity.WARNING,
                        String.format("Pas de date de prélèvement pour l'analyse %s => ignorée",
                                analyse.getSourceId()
                        ),
                        analyse);
            }

            UniteMesure uniteMesure = getUniteMesure(analyse.getCodeSandreUniteMesure());
            if (uniteMesure == null) {
                resultat.error(
                        DeserializationErrorHandler.ErrorCode.RULE,
                        DeserializationErrorHandler.ErrorGravity.WARNING,
                        String.format("Code SANDRE d'unité de mesure %s inconnu : le résultat %s est ignoré",
                                analyse.getCodeSandreUniteMesure(),
                                analyse.getSourceId()
                        ),
                        analyse);
                return;
            }
            DonneeVariable donnee = gestionnaireDonneeVariable.getDonneeVariable(definitionVariable, dateMesure);
            if (donnee == null) {
                resultatAnalyse = convert(analyse.getResultatAnalyse());
                gestionnaireDonneeVariable.createDonneeVariable(definitionVariable, dateMesure, false, resultatAnalyse, uniteMesure, false);
            } else {
                donneeVariableId = donnee.getDonneeVariableId();
                if (null != persistanceDonneeVariable.findResultatAnalyseById(donneeVariableId)) {
                    analyse.getResultatAnalyse().setDonneeVariableId(donneeVariableId);
                }
                resultatAnalyse = convert(analyse.getResultatAnalyse());
                gestionnaireDonneeVariable.modifier(donnee, resultatAnalyse, "Modifié dans LIMS", uniteMesure, definitionVariable.getUniteMesure());
            }
        } catch (ApplicationException ex) {
            resultat.error(
                    DeserializationErrorHandler.ErrorCode.RULE,
                    DeserializationErrorHandler.ErrorGravity.WARNING,
                    String.format("analyse %s : erreur %s",
                            analyse.getSourceId(),
                            ex.getMessage()
                    ),
                    analyse);

            LOGGER.error(ex);
        }*/
    }


}

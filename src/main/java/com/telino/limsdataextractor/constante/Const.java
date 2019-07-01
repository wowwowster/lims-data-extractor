package com.telino.limsdataextractor.constante;

public interface Const {

    String  CODE_APPLICATION_MAV_STANDARD = "mav_standard";
    String  CODE_APPLICATION_SEM_STANDARD = "sem_standard";

    String  CONVERSION_TYPE_MULTIPLICATION = "multiplication";
    String  CONVERSION_TYPE_DIVISION = "division";
    String  CONVERSION_TYPE_REGLE_DE_TROIS = "regle_de_trois";

    String  CODE_AGENCE_SANDRE = "SANDRE";

    String FORMAT_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    String FORMAT_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**** GESTION DES IMPORT ET APIS EXTERNES ****/

        /* Lors de l'implémentation d'une api externe autre que LIMS, les deux constantes suivantes seront supprimées et
         remplacées par un enum */
    String IMPORT_LIMS_TYPE = "LIMS";
    String IMPORT_LIMS_NOM_PARAMETRE = "entites";

    // Quartz
    String WS_LIMS_JOB_PREFIX = "JobApiExterne_";
    String WS_LIMS_TRIGGER_PREFIX = "TriggerApiExterne_";
    //String LIMS_MINIMAL_LAST_RUN_DATE = "2018/06/20 00:00:00.000";

    static Long compteur = new Long(1);

}

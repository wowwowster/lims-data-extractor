package com.telino.limsdataextractor.fakemodel.apiexterne;

import com.telino.limsdataextractor.constante.Const;

public class ParametreApiExterne implements java.io.Serializable {

    private String nom;
    private String valeur;
    private ImportApiExterne importApiExterne;

    public ParametreApiExterne() {
        this.nom = Const.IMPORT_LIMS_NOM_PARAMETRE;
    }

    public ParametreApiExterne(String valeur) {
        this.nom = Const.IMPORT_LIMS_NOM_PARAMETRE;
        this.valeur = valeur;
    }

    public ImportApiExterne getImportApiExterne() {
        return importApiExterne;
    }

    public void setImportApiExterne(ImportApiExterne importApiExterne) {
        this.importApiExterne = importApiExterne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}

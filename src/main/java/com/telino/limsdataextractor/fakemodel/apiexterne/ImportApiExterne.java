package com.telino.limsdataextractor.fakemodel.apiexterne;

import com.telino.limsdataextractor.constante.Const;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImportApiExterne {

    private String typeImportExterne = Const.IMPORT_LIMS_TYPE;
    private String code;
    private Date lastFinishedAt;
    private Set<ParametreApiExterne> parametres = new HashSet<>();

    public ImportApiExterne() {
        this.typeImportExterne = Const.IMPORT_LIMS_TYPE;
    }

    public Set<ParametreApiExterne> getParametres() {
        return parametres;
    }

    public Date getLastFinishedAt() {
        return lastFinishedAt;
    }

    public void setLastFinishedAt(Date lastFinishedAt) {
        this.lastFinishedAt = lastFinishedAt;
    }

    public void setParametres(Set<ParametreApiExterne> parametres) {
        this.parametres = parametres;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeImportExterne() {
        return Const.IMPORT_LIMS_TYPE;
    }

    public void setTypeImportExterne(String typeImportExterne) {
        this.typeImportExterne = typeImportExterne;
    }

    public void addOrUpdateParametre(ParametreApiExterne param){
        boolean paramFound = false;
        String nomAControler = param.getNom();
        if (null == parametres) {
            parametres = new HashSet<>();
        } else {
            Iterator<ParametreApiExterne> it = parametres.iterator();
            while (it.hasNext()) {
                ParametreApiExterne parametreApiExterne = it.next();
                if (parametreApiExterne.getNom().equalsIgnoreCase(nomAControler)) {
                    parametreApiExterne.setValeur(param.getValeur());
                    paramFound = true;
                }
            }
        }
        if(!paramFound) {
            parametres.add(param);
        }
        if (param.getImportApiExterne() != this) {
            param.setImportApiExterne(this);
        }
    }

}

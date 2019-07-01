package com.telino.limsdataextractor.fakemodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import com.telino.sandre.Methode;
import java.io.Serializable;
import java.util.Date;

public class ResultatAnalyse implements Serializable {
    private Integer id;
  //  private DonneeVariableNumeric donneeVariable;
    private Date datePrelevement;
    private Date dateAnalyse;
    private String codePreleveur;
    private Boolean conformitePrelevement;
    private Boolean accreditationPrelevement;
    private Date dateReceptionEchantillon;
    private Boolean inSitu;
    private String statutAnalyse;
    private String qualificationAnalyse;
    //private Methode methodeAnalyse;
    private String codeLaboratoire;
    private String nomLaboratoire;
    private String finaliteAnalyse;
    private Double ld;
    private Double lq;
    private Boolean accreditationAnalyse;
    private Boolean agrementAnalyse;
    private Double incertitudeAnalytique;
    private String commentaireAnalyse;
    private Double resultat;
    private Boolean inferieur = false;
    private Integer dureePrelevement;
    private String sourceId;

    public ResultatAnalyse() {
        statutAnalyse = "A";
        qualificationAnalyse = "4";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

  /*  public DonneeVariableNumeric getDonneeVariable() {
        return donneeVariable;
    }

    public void setDonneeVariable(DonneeVariableNumeric donneeVariable) {
        this.donneeVariable = donneeVariable;
    }*/

    public Date getDateAnalyse() {
        return dateAnalyse;
    }

    public void setDateAnalyse(Date dateAnalyse) {
        this.dateAnalyse = dateAnalyse;
    }

    public Date getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(Date datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public String getCodePreleveur() {
        return codePreleveur;
    }

    public void setCodePreleveur(String codePreleveur) {
        this.codePreleveur = codePreleveur;
    }

    public Boolean getConformitePrelevement() {
        return conformitePrelevement;
    }

    public void setConformitePrelevement(Boolean conformitePrelevement) {
        this.conformitePrelevement = conformitePrelevement;
    }

    public Boolean getAccreditationPrelevement() {
        return accreditationPrelevement;
    }

    public void setAccreditationPrelevement(Boolean accreditationPrelevement) {
        this.accreditationPrelevement = accreditationPrelevement;
    }

    public Date getDateReceptionEchantillon() {
        return dateReceptionEchantillon;
    }

    public void setDateReceptionEchantillon(Date dateReceptionEchantillon) {
        this.dateReceptionEchantillon = dateReceptionEchantillon;
    }

    public Boolean getInSitu() {
        return inSitu;
    }

    public void setInSitu(Boolean inSitu) {
        this.inSitu = inSitu;
    }

    public String getStatutAnalyse() {
        return statutAnalyse;
    }

    public void setStatutAnalyse(String statutAnalyse) {
        this.statutAnalyse = statutAnalyse;
    }

    public String getQualificationAnalyse() {
        return qualificationAnalyse;
    }

    public void setQualificationAnalyse(String qualificationAnalyse) {
        this.qualificationAnalyse = qualificationAnalyse;
    }


/*    public Methode getMethodeAnalyse() {
        return methodeAnalyse;
    }

    public String getCdMethodeAnalyse() {
        return methodeAnalyse != null
                ? methodeAnalyse.getCdMethode()
                : null;
    }

    public void setMethodeAnalyse(Methode methodeAnalyse) {
        this.methodeAnalyse = methodeAnalyse;
    }*/

    public String getCodeLaboratoire() {
        return codeLaboratoire;
    }

    public void setCodeLaboratoire(String codeLaboratoire) {
        this.codeLaboratoire = codeLaboratoire;
    }

    public String getNomLaboratoire() {
        return nomLaboratoire;
    }

    public void setNomLaboratoire(String nomLaboratoire) {
        this.nomLaboratoire = nomLaboratoire;
    }

    public String getFinaliteAnalyse() {
        return finaliteAnalyse;
    }

    public void setFinaliteAnalyse(String finaliteAnalyse) {
        this.finaliteAnalyse = finaliteAnalyse;
    }

    public Double getLd() {
        return ld;
    }

    public void setLd(Double ld) {
        this.ld = ld;
    }

    public Double getLq() {
        return lq;
    }

    public void setLq(Double lq) {
        this.lq = lq;
    }

    public Boolean getAccreditationAnalyse() {
        return accreditationAnalyse;
    }

    public void setAccreditationAnalyse(Boolean accreditationAnalyse) {
        this.accreditationAnalyse = accreditationAnalyse;
    }

    public Boolean getAgrementAnalyse() {
        return agrementAnalyse;
    }

    public void setAgrementAnalyse(Boolean agrementAnalyse) {
        this.agrementAnalyse = agrementAnalyse;
    }

    public Double getIncertitudeAnalytique() {
        return incertitudeAnalytique;
    }

    public void setIncertitudeAnalytique(Double incertitudeAnalytique) {
        this.incertitudeAnalytique = incertitudeAnalytique;
    }

    public String getCommentaireAnalyse() {
        return commentaireAnalyse;
    }

    public void setCommentaireAnalyse(String commentaireAnalyse) {
        this.commentaireAnalyse = commentaireAnalyse;
    }

    public Double getResultat() {
        return resultat;
    }

    public void setResultat(Double resultat) {
        this.resultat = resultat;
    }

    public Boolean getInferieur() {
        return inferieur;
    }

    public void setInferieur(Boolean inferieur) {
        this.inferieur = inferieur;
    }

    public Integer getDureePrelevement() {
        return dureePrelevement;
    }

    public String getDureePrelevementString() {
        return this.dureePrelevement == null
                ? null
                : String.format("%d:%02d:%02d", getDureePrelevementHeure(), getDureePrelevementMinute(), getDureePrelevementSecond());
    }

    public void setDureePrelevement(Integer dureePrelevement) {
        this.dureePrelevement = dureePrelevement;
    }

    public Integer getDureePrelevementSecond() {
        return this.dureePrelevement == null
                ? null
                : this.dureePrelevement % 60;
    }

    public Integer getDureePrelevementMinute() {
        return this.dureePrelevement == null
                ? null
                : (this.dureePrelevement / 60) % 60;
    }

    public Integer getDureePrelevementHeure() {
        return this.dureePrelevement == null
                ? null
                : this.dureePrelevement / (60*60);
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}


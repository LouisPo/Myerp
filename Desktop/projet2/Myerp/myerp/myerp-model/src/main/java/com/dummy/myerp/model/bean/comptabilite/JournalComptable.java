package com.dummy.myerp.model.bean.comptabilite;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Bean représentant un Journal Comptable
 */
public class JournalComptable {

    // ==================== Attributs ====================
    /** code */
    @NotNull
    @Size(min = 1, max = 5)
    private String code;

    /** libelle */
    @NotNull
    @Size(min = 1, max = 150)
    private String libelle;

    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Journal comptable.
     */
    public JournalComptable() {
    }

    /**
     * Instantiates a new Journal comptable.
     *
     * @param pCode    the p code
     * @param pLibelle the p libelle
     */
    public JournalComptable(String pCode, String pLibelle) {
	code = pCode;
	libelle = pLibelle;
    }

    // ==================== Getters/Setters ====================
    public String getCode() {
	return code;
    }

    public void setCode(String pCode) {
	code = pCode;
    }

    public String getLibelle() {
	return libelle;
    }

    public void setLibelle(String pLibelle) {
	libelle = pLibelle;
    }

    // ==================== Méthodes ====================
    @Override
    public String toString() {
	final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
	final String vSEP = ", ";
	vStB.append("{").append("code='").append(code).append('\'').append(vSEP).append("libelle='").append(libelle)
		.append('\'').append("}");
	return vStB.toString();
    }

    /**
     * HashCode and equals override to allow JournalComptable instantiate class
     * comparison
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	JournalComptable other = (JournalComptable) obj;
	if (code == null) {
	    if (other.code != null)
		return false;
	} else if (!code.equals(other.code))
	    return false;
	if (libelle == null) {
	    if (other.libelle != null)
		return false;
	} else if (!libelle.equals(other.libelle))
	    return false;
	return true;
    }

    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link JournalComptable} de code {@code pCode} s'il est présent
     * dans la liste
     *
     * @param pList la liste où chercher le {@link JournalComptable}
     * @param pCode le code du {@link JournalComptable} à chercher
     * @return {@link JournalComptable} ou {@code null}
     */
    // FIXME Delete this comment: same question as ComptecompTable static getlist.
    // Same reason
    public static JournalComptable getByCode(List<? extends JournalComptable> pList, String pCode) {
	JournalComptable vRetour = null;
	for (JournalComptable vBean : pList) {
	    if (vBean != null && Objects.equals(vBean.getCode(), pCode)) {
		vRetour = vBean;
		break;
	    }
	}
	return vRetour;
    }
}

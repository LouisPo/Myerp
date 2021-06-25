package com.dummy.myerp.model.bean.comptabilite;

import javax.validation.constraints.NotNull;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

	// ==================== Attributs ====================
	/** L'année */
	@NotNull
	private Integer annee;
	/** La dernière valeur utilisée */
	@NotNull
	private Integer derniereValeur;
	/**Journal comptable */
	private JournalComptable journalComptable;

	// ==================== Constructeurs ====================
	/**
	 * Constructeur
	 */
	public SequenceEcritureComptable() {
	}

	/**
	 *
	 * @param annee
	 * @param derniereValeur
	 * @param journalComptable
	 */
	public SequenceEcritureComptable(Integer annee, Integer derniereValeur, JournalComptable journalComptable) {
		this.annee = annee;
		this.derniereValeur = derniereValeur;
		this.journalComptable = journalComptable;
	}

	// ==================== Getters/Setters ====================
	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer pAnnee) {
		annee = pAnnee;
	}

	public Integer getDerniereValeur() {
		return derniereValeur;
	}

	public void setDerniereValeur(Integer pDerniereValeur) {
		derniereValeur = pDerniereValeur;
	}

	public JournalComptable getJournalComptable() {
		return journalComptable;
	}

	public void setJournalComptable(JournalComptable journalComptable) {
		this.journalComptable = journalComptable;
	}

	// ==================== Méthodes ====================
	@Override
	public String toString() {
		final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
		final String vSEP = ", ";
		vStB.append("{").append("journal comptable=").append(journalComptable).append(vSEP).append("annee=").append(annee).append(vSEP).append("derniereValeur=").append(derniereValeur)
				.append("}");
		return vStB.toString();
	}


/**
 * HashCode and equals override to allow SequenceEcritureComptable instantiate class comparison
 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annee == null) ? 0 : annee.hashCode());
		result = prime * result + ((derniereValeur == null) ? 0 : derniereValeur.hashCode());
		result = prime * result + ((journalComptable == null) ? 0 : journalComptable.hashCode());
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
		SequenceEcritureComptable other = (SequenceEcritureComptable) obj;
		if (annee == null) {
			if (other.annee != null)
				return false;
		} else if (!annee.equals(other.annee))
			return false;
		if (derniereValeur == null) {
			if (other.derniereValeur != null)
				return false;
		} else if (!derniereValeur.equals(other.derniereValeur))
			return false;
		if (journalComptable == null) {
			if (other.journalComptable != null)
				return false;
		} else if (!journalComptable.equals(other.journalComptable))
			return false;
		return true;
	}


}

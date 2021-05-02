package com.dummy.myerp.business.impl.manager;

//import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

 
/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {  
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }
    /**
     * Ajoute une référence à l'écriture comptable.
     *
     * <strong> RG_Compta_5 : </strong>
     * La référence d'une écriture comptable est composée du code du journal dans lequel figure l'écriture
     * suivi de l'année et d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté automatiquement
     * à chaque écriture. Le formatage de la référence est : XX-AAAA/#####.
     * <br>
     * Ex : Journal de banque (BQ), écriture au 31/12/2016
     * <pre>BQ-2016/00001</pre>
     *
     * <p><strong>Attention :</strong> l'écriture n'est pas enregistrée en persistance</p>
     * @param pEcritureComptable L'écriture comptable concernée
     */
    /**
     * {@inheritDoc}
     * @throws NotFoundException 
     */
    // TODO à tester 22 mars
    /*
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable)  {
        String ref; 
        StringBuilder valSeq = new StringBuilder();

        //Récupération de l'année de l'écriture
        Calendar date = Calendar.getInstance();
        date.setTime(pEcritureComptable.getDate());
        int anneeEcriture = date.get(Calendar.YEAR);
        //Chargement de la séquence avec le code journal de l'écriture comptable
        
        SequenceEcritureComptable seq = null;
		try {
			seq = getDaoProxy().getComptabiliteDao().getSequenceEcritureComptable(pEcritureComptable.getJournal().getCode(), anneeEcriture);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if(seq != null) {
            if(anneeEcriture == seq.getAnnee()) {
                valSeq.append((seq.getDerniereValeur() + 1));
                ref = pEcritureComptable.getJournal().getCode() + "-" + anneeEcriture + "/";
                while (valSeq.length() != 5) {
                    valSeq.insert(0, '0');
                }
                ref = ref.concat(valSeq.toString());
                pEcritureComptable.setReference(ref);
                seq.setDerniereValeur(seq.getDerniereValeur() + 1);
                updateSequenceEcritureComptable(seq, pEcritureComptable.getJournal().getCode());
            }
        } else {
            seq = new SequenceEcritureComptable();
            seq.setAnnee(anneeEcriture);
            seq.setDerniereValeur(1);
            pEcritureComptable.setReference(pEcritureComptable.getJournal().getCode() + "-" + anneeEcriture + "/" + "00001");
            System.out.println(pEcritureComptable);
            insertSequenceEcritureComptable(seq, pEcritureComptable.getJournal().getCode());
        }
        
        //fin rajout 22/03*************************************************************
        
        // TODO à implémenter
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */
   // }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        //this.checkEcritureComptableContext(pEcritureComptable);
    }
    /**
     * Virifie le contenu et le format de la référence
     * @param pEcritureComptable l'écritire dont il faut vérifier la référence
     * @throws FunctionalException en cas de référence invalide
     */
    public void checkReference(EcritureComptable pEcritureComptable) throws FunctionalException {
        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        String[] parts = pEcritureComptable.getReference().split("\\p{Punct}");
        String codeJournal = parts[0];
        String date = parts[1];

        if(!codeJournal.equals(pEcritureComptable.getJournal().getCode()))
            throw new FunctionalException("Le code journal de la référence est différent du code journal.");

        Calendar cal = Calendar.getInstance();
        cal.setTime(pEcritureComptable.getDate());
        String annee = Integer.toString(cal.get(Calendar.YEAR));

        if(!date.equals(annee))
            throw new FunctionalException("L'année de la référence est différente de l'année d'écriture.");
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    
    
    
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                               "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        
        // ===== RG_Compta_1 : affichage de la nature du solde montant des debits moins montant des credits
        pEcritureComptable.natureSolde();

        


        
        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
     /*   if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }
*/
        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            } 
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
    	System.out.println("vous etes rentres dans appel de insertEcritureComptable YESSS" );
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        

        
        //essai
      
            this.insertEcritureComptable(pEcritureComptable);

        
        
        /*commenté le 27/04 pour essai
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
        *fin essai 27/04*/
    } 

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    } 
  
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
   
    @Override
    public void insertSequenceEcritureComptable(final SequenceEcritureComptable seq, String code) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(seq, code);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
    @Override
    public void updateSequenceEcritureComptable(final SequenceEcritureComptable seq, String code) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(seq, code);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }


	@Override
	public void addReference(EcritureComptable pEcritureComptable) {
		// TODO Auto-generated method stub
		
	}


/*	@Override
	public void addReference(EcritureComptable pEcritureComptable) {
		// TODO Auto-generated method stub
		
	}
*/

/*	@Override
	public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		// TODO Auto-generated method stub
		
	}

*/
	
	/*
	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		// TODO Auto-generated method stub
		
	}

*/
	
	/*
	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		// TODO Auto-generated method stub
		
	}
*/
	
	/*

	@Override
	public void updateSequenceEcritureComptable(SequenceEcritureComptable seq, String code) {
		// TODO Auto-generated method stub
		
	}
*/
	
	/*

	@Override
	public void insertSequenceEcritureComptable(SequenceEcritureComptable seq, String code) {
		// TODO Auto-generated method stub
		
	}
	*/
}

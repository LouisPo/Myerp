package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;
//louis 22/04
import java.util.List;
import java.util.Calendar;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;

//import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
//louis 22/04
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import static org.junit.Assert.assertTrue;

public class ComptabiliteManagerImplTest   {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    //louis 22/04
    private EcritureComptable vEcritureComptable = new EcritureComptable();

    @Before
    public void initEcritureComptable() {
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
    }
    
    
    
    
    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        
        //louis 1 ligne
        //vEcritureComptable.setReference("2019");
        
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
       // System.out.println("Achat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //test
        BigDecimal solde;
        String etatsolde="";
    	BigDecimal A = new BigDecimal("-60000000000");
    	BigDecimal B = new BigDecimal("20000000000");
    	BigDecimal C = new BigDecimal("30000000000");
    	solde = C.subtract(A);
    	//System.out.println("Achat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+solde);
    	if (solde.compareTo(BigDecimal.ZERO) > 0) {
            etatsolde="debiteur";
     	}
     	if (solde.compareTo(BigDecimal.ZERO) < 0) {
             etatsolde="crediteur";
      	}
     	//System.out.println("etatsolde!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+etatsolde);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }
/*
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }*/

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkReference() {
    	EcritureComptable ec= new EcritureComptable();
    	ec.setReference(null);
    	Boolean flag = false;
    	try{
    	manager.checkReference(ec);
    	}catch(Exception e){
    	flag = true;
    	}
    	assertTrue(flag);    }
    
    //a prendre comme exemple
    
    @Test
    public void checkReferenceInvalidCode(){
    EcritureComptable ec = new EcritureComptable();
    JournalComptable j=new JournalComptable();
    j.setCode("vrai");
    ec.setJournal(j);
    ec.setReference("referenceValide//2020-2020");
    boolean flag = false;
    try {
    manager.checkReference(ec);
    }catch(FunctionalException e){
    flag = true;
    }
    assertTrue(flag);
    }
    //louis
    
    
    /*
    @Test
    public void insertEcritureComptable() throws Exception {
    Date currentDate = new Date();
    int currentYear = 0;  
    vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
    vEcritureComptable.setDate(new Date());
    vEcritureComptable.setLibelle("Libelle");
    vEcritureComptable.setReference("AC-2019/00001");
    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
            null, new BigDecimal(123),
            null));
    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
            null, null,
            new BigDecimal(123)));
   // vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),null, new BigDecimal(123),null));
    //vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),null, new BigDecimal(123), null));
    //manager.checkReference(vEcritureComptable);
    
    System.out.println("vous etes rentres dans appel de insertEcritureComptable depuis les tests" );
   // manager.checkEcritureComptableContext(vEcritureComptable);
    manager.insertEcritureComptable(vEcritureComptable);
    }
    */
    @Test
    public void insertEcritureComptable() throws Exception {
    	System.out.println("vous etes rentres dans appel de insertEcritureComptable new depuis les tests" );
        manager.insertEcritureComptable(vEcritureComptable);
        EcritureComptable eb =
                getDaoProxy().getComptabiliteDao()
                        .getEcritureComptableByRef("AC-2019/00001");
        Assert.assertEquals("AC-2019/00001", eb.getReference());
        manager.deleteEcritureComptable(eb.getId());
    }



  /*  @Test
    public void insertEcritureComptable() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null, new BigDecimal(1234)));
       JournalComptable j=new JournalComptable();
       j.setCode("vrai");
       vEcritureComptable.setJournal(j);
       vEcritureComptable.setReference("referenceValide//2020-2020");                                                        
       vEcritureComptable.setLibelle("Equilibrée");
       manager.insertEcritureComptable(vEcritureComptable);

    }
*/

    /*
   
    @Test
    public void checkEcritureComptableContext() throws FunctionalException {
        vEcritureComptable.setReference("AC-2019/00001");
        manager.checkReference(vEcritureComptable);
    }


    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextViolation() throws FunctionalException {
        vEcritureComptable.setReference("AC-2010/00001");
        manager.checkReference(vEcritureComptable);
    }

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> pList = getDaoProxy().getComptabiliteDao().getListCompteComptable();
        Assert.assertTrue(pList.size() >= 1);
    }

    @Test
    public void getListJournalComptable() {
        List<CompteComptable> pList = getDaoProxy().getComptabiliteDao().getListCompteComptable();
        Assert.assertTrue(pList.size() >= 1);
    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> pList = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        Assert.assertTrue(pList.size() >= 1);
    }

    @Test
    public void addReferenceSS() throws NotFoundException {
        SequenceEcritureComptable seq = new SequenceEcritureComptable();
        seq.setAnnee(2019);

        manager.addReference(vEcritureComptable);
        Assert.assertNotNull(vEcritureComptable.getReference());
        Assert.assertEquals("AC-2019/00001", vEcritureComptable.getReference());

        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(seq, "AC");

    }

    @Test
    public void addReferenceAS() {
        SequenceEcritureComptable seq = new SequenceEcritureComptable();
        seq.setAnnee(2019);
        StringBuilder valSeq = new StringBuilder();
        String ref;

        valSeq.append(43);
        ref = "AC-2019/";
        while (valSeq.length() != 5) {
            valSeq.insert(0, '0');
        }
        ref = ref.concat(valSeq.toString());
        vEcritureComptable.setReference(ref);
        Assert.assertEquals("AC-2019/00043", vEcritureComptable.getReference());

    }



    @Test
    public void updateEcritureComptable() throws Exception {
        manager.insertEcritureComptable(vEcritureComptable);
        EcritureComptable eb =
                getDaoProxy().getComptabiliteDao()
                        .getEcritureComptableByRef("AC-2019/00001");
        Assert.assertEquals("AC-2019/00001", eb.getReference());
        eb.setReference("AC-2022/00055");
        manager.updateEcritureComptable(eb);
        Assert.assertEquals("AC-2022/00055", eb.getReference());
        manager.deleteEcritureComptable(eb.getId());
    }

    @Test
    public void deleteEcritureComptable() throws Exception {
        manager.insertEcritureComptable(vEcritureComptable);
        EcritureComptable eb =
                getDaoProxy().getComptabiliteDao()
                        .getEcritureComptableByRef("AC-2019/00001");
        manager.deleteEcritureComptable(eb.getId());
    }

    @Test
    public void insertSequenceEcritureComptable() throws NotFoundException {
        SequenceEcritureComptable vSeq = new SequenceEcritureComptable();
        vSeq.setDerniereValeur(32);
        vSeq.setAnnee(1994);
        manager.insertSequenceEcritureComptable(vSeq, "OD");
        SequenceEcritureComptable seqBis =
                getDaoProxy().getComptabiliteDao().getSequenceEcritureComptable("OD", 1994);
        Assert.assertEquals(1994, (int) seqBis.getAnnee());
        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(seqBis, "OD");
    }

    @Test
    public void updateSequenceEcritureComptable() throws NotFoundException {
        SequenceEcritureComptable vSeq = new SequenceEcritureComptable();
        vSeq.setDerniereValeur(32);
        vSeq.setAnnee(1994);
        manager.insertSequenceEcritureComptable(vSeq, "OD");
        SequenceEcritureComptable seqBis =
                getDaoProxy().getComptabiliteDao().getSequenceEcritureComptable("OD", 1994);
        seqBis.setDerniereValeur(65);
        manager.updateSequenceEcritureComptable(seqBis, "OD");
        Assert.assertEquals(65, (int) seqBis.getDerniereValeur());
        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(seqBis, "OD");
    }
    
   */

}

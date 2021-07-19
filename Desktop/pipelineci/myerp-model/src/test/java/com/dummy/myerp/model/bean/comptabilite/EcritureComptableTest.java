package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EcritureComptableTest {

    private EcritureComptable vEcritureComptable = new EcritureComptable();

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
	BigDecimal vDebit = pDebit == null ? BigDecimal.ZERO : new BigDecimal(pDebit);
	BigDecimal vCredit = pCredit == null ? BigDecimal.ZERO : new BigDecimal(pCredit);
	String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
		.subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
	LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
		vLibelle, vDebit, vCredit);
	return vRetour;
    }

    @Nested
    @Tag("MethodInBean")
    @DisplayName("Test getTotal methods")
    public class TestTotal {

	@BeforeEach
	private void initListLigneEcriture() {
	    vEcritureComptable = new EcritureComptable();
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, "10", null));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, "20.25", null));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, "30", null));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, "40.25", null));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, "15.50"));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, "25.25"));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, "35"));
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, "45.25"));
	    
	    
	}

	@Test
	@DisplayName("Test total debit is a BigDecimal and =0")
	public void getTotalDebitTestOnNull() {
	    vEcritureComptable.getListLigneEcriture().clear();
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, null));
	    assertThat(vEcritureComptable.getTotalDebit()).isInstanceOf(BigDecimal.class);
	    assertThat(vEcritureComptable.getTotalDebit()).isEqualTo(new BigDecimal("0.00"));

	}

	@Test
	@DisplayName("Test total Credit is a BigDecimal and =0")
	public void getTotalCreditTestOnNull() {
	    vEcritureComptable.getListLigneEcriture().clear();
	    vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, null));
	    assertThat(vEcritureComptable.getTotalCredit()).isInstanceOf(BigDecimal.class);
	    assertThat(vEcritureComptable.getTotalCredit()).isEqualTo(new BigDecimal("0.00"));
	}

	@Test
	@DisplayName("Test total debit is a BigDecimal and =100.50")
	public void getTotalDebitTestOnValues() {
	    assertThat(vEcritureComptable.getTotalDebit()).isEqualTo(new BigDecimal("100.50"));

	}

	@Test
	@DisplayName("Test total Credit is a BigDecimal and =121.00")
	public void getTotalCreditTestOnValues() {
	    assertThat(vEcritureComptable.getTotalCredit()).isEqualTo(new BigDecimal("121.00"));
	}

    }


    
    
    
    @Test
    public void isEquilibree() {
	EcritureComptable vEcriture;
	vEcriture = new EcritureComptable();

	vEcriture.setLibelle("Equilibrée");
	vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
	vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
	vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
	vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
	assertThat(vEcriture.isEquilibree()).isTrue();

	vEcriture.getListLigneEcriture().clear();
	vEcriture.setLibelle("Non équilibrée");
	vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
	vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
	vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
	vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
	assertThat(vEcriture.isEquilibree()).isFalse();
    }
 
    @Test
    @Tag("CustomToString")
    @DisplayName("check override toString()")
    public void toStringCheck() {
	vEcritureComptable = new EcritureComptable(1, new JournalComptable("AA", null), "AA-2021/00001", new Date(),
		"libelle");
	vEcritureComptable.getListLigneEcriture().add(createLigne(1, "10", null));
	vEcritureComptable.getListLigneEcriture().add(createLigne(1, null, "20"));
	final String vExpected = vEcritureComptable.getClass().getSimpleName()
		.concat("{id=1" + ", journal=" + vEcritureComptable.getJournal().toString()
			+ ", reference='AA-2021/00001'" + ", date=" + new Date().toString() + ", libelle='libelle'"
			+ ", totalDebit=10.00" + ", totalCredit=20.00" + ", listLigneEcriture=[\n"
			+ vEcritureComptable.getListLigneEcriture().get(0).toString() + "\n"
			+ vEcritureComptable.getListLigneEcriture().get(1).toString() + "\n]" + "}");
	assertThat(vExpected.equals(vEcritureComptable.toString())).isTrue();
    }

}

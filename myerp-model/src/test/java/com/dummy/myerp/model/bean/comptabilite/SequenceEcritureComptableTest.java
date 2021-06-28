package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SequenceEcritureComptableTest {

	private SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();

	JournalComptable journalComptable = new JournalComptable("AA","journal");

	@BeforeEach
	public void initSequenceEcritureComptable() {
		vSequenceEcritureComptable = new SequenceEcritureComptable(2021, 52, journalComptable );
	}

	@Test
	@Tag("CustomTostring")
	@DisplayName("check override toString()")
	public void toStringCheck() {
		final String vExpected = vSequenceEcritureComptable.getClass().getSimpleName()
				.concat("{journal comptable="+journalComptable.toString()+", annee=2021, derniereValeur=52}");
		assertThat(vExpected.equals(vSequenceEcritureComptable.toString())).isTrue();
	}

}

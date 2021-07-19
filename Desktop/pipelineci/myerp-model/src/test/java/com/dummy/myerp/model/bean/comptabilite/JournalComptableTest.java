package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JournalComptableTest {

	private JournalComptable vJournalComptable = new JournalComptable();
	private List<JournalComptable> vListJournalComptable;

	@BeforeEach
	public void initListJournalComptable() {
		vListJournalComptable = new ArrayList<>();
		vListJournalComptable.add(0, null);
		vListJournalComptable.add(1, new JournalComptable("AA", "alpha"));
		vListJournalComptable.add(2, new JournalComptable("BB", "beta"));

	}

	@SuppressWarnings("static-access")
	@ParameterizedTest
	@ValueSource(strings = { "AA", "BB" })
	@Tag("MethodInBean")
	@DisplayName("Get JournalComptable from list pass")
	public void getByCodeTestPass(String pCode) {
		assertThatCode(() -> vJournalComptable.getByCode(vListJournalComptable, pCode)).doesNotThrowAnyException();
		assertThat(vJournalComptable.getByCode(vListJournalComptable, pCode)).isNotNull();
	}

	@SuppressWarnings("static-access")
	@ParameterizedTest
	@ValueSource(strings = { "", "CC" })
	@Tag("MethodInBean")
	@DisplayName("Get JournalComptable from list fail")
	public void getByCodeTestFail(String pCode) {
		assertThatCode(() -> vJournalComptable.getByCode(vListJournalComptable, pCode)).doesNotThrowAnyException();
		assertThat(vJournalComptable.getByCode(vListJournalComptable, pCode)).isNull();
	}

	@Test
	@Tag("CustomToString")
	@DisplayName("check override toString()")
	public void toStringCheck() {
		vJournalComptable = new JournalComptable("ZZ", "Omega");
		final String vExpected = vJournalComptable.getClass().getSimpleName().concat("{code='ZZ', libelle='Omega'}");
		assertThat(vExpected.equals(vJournalComptable.toString())).isTrue();
	}

}

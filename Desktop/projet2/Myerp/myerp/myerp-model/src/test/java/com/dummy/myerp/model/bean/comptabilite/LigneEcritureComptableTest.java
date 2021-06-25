package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LigneEcritureComptableTest {

	private LigneEcritureComptable vligneEcritureComptable = new LigneEcritureComptable();
	private final static BigDecimal SCALED_ZERO = BigDecimal.ZERO.setScale(2);

	@ParameterizedTest
	@ValueSource(strings = { "0", "1", "10", "1.12" })
	@Tag("MethodInBean")
	@DisplayName("Set scale to the fixed value pass")
	public void fixedScalePass(String vBigDecimal) {
		vligneEcritureComptable = new LigneEcritureComptable(new CompteComptable(1), "alpha",
				new BigDecimal(vBigDecimal), new BigDecimal(vBigDecimal));
		assertThat(vligneEcritureComptable.getDebit()).isEqualTo(new BigDecimal(vBigDecimal).setScale(2));
		assertThat(vligneEcritureComptable.getCredit()).isEqualTo(new BigDecimal(vBigDecimal).setScale(2));
	}

	@ParameterizedTest
	@ValueSource(strings = { "0.123", "1.2345" })
	@Tag("MethodInBean")
	@DisplayName("Set scale to the fixed value fail")
	public void fixedScaleFail(String vBigDecimal) {
		assertThatThrownBy(() -> vligneEcritureComptable = new LigneEcritureComptable(new CompteComptable(1), "alpha",
				new BigDecimal(vBigDecimal), new BigDecimal(vBigDecimal))).isInstanceOf(ArithmeticException.class);

	}

	@Test
	@Tag("MethodInBean")
	@DisplayName("Parse to Bigdecimal if null on null pass")
	public void parseIfNullToBigDecimalTestOnNullPass() {
		vligneEcritureComptable = new LigneEcritureComptable(new CompteComptable(1), "alpha", null, null);
		assertThat(vligneEcritureComptable.getCredit()).isInstanceOf(BigDecimal.class);
		assertThat(vligneEcritureComptable.getDebit()).isInstanceOf(BigDecimal.class);
		assertThat(vligneEcritureComptable.getCredit()).isEqualTo(SCALED_ZERO);
		assertThat(vligneEcritureComptable.getDebit()).isEqualTo(SCALED_ZERO);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1.00", "1.11", "12.00", "12.34" })
	@Tag("MethodInBean")
	@DisplayName("Parse to Bigdecimal if null on not null pass")
	public void parseIfNullToBigDecimalTestOnNotNullPass(String vdouble) {
		vligneEcritureComptable = new LigneEcritureComptable(new CompteComptable(1), "alpha", new BigDecimal(vdouble),
				new BigDecimal(vdouble));
		assertThat(vligneEcritureComptable.getCredit()).isEqualTo(new BigDecimal(vdouble));
		assertThat(vligneEcritureComptable.getDebit()).isEqualTo(new BigDecimal(vdouble));
	}

	@Test
	@Tag("CustomToString")
	@DisplayName("check override toString()")
	public void toStringCheck() {
		vligneEcritureComptable = new LigneEcritureComptable(new CompteComptable(1), "Omega", null, null);
		final String vExpected = vligneEcritureComptable.getClass().getSimpleName()
				.concat("{compteComptable=" + vligneEcritureComptable.getCompteComptable().toString()
						+ ", libelle='Omega', debit=0.00, credit=0.00}");
		assertThat(vExpected.equals(vligneEcritureComptable.toString())).isTrue();
	}

}

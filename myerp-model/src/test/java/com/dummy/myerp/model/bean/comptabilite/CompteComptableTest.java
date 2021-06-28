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

public class CompteComptableTest {
	private CompteComptable vCompteComptable = new CompteComptable();;
	private List<CompteComptable> listCompteComptable;

	@BeforeEach
	private void initListCompteComptable() {
		listCompteComptable = new ArrayList<>();
		listCompteComptable.add(0, null);
		listCompteComptable.add(1, new CompteComptable(1, "alpha"));
		listCompteComptable.add(2, new CompteComptable(2, "beta"));

	}

	@SuppressWarnings("static-access")
	@ParameterizedTest
	@ValueSource(ints = { 1, 2 })
	@Tag("MethodInBean")
	@DisplayName("Get CompteComptable from list pass")
	public void getByNumeroTestPass(Integer pNumero) {
		assertThatCode(() -> vCompteComptable = vCompteComptable.getByNumero(listCompteComptable, pNumero))
				.doesNotThrowAnyException();
		assertThat(vCompteComptable).isNotNull();
	}

	@SuppressWarnings("static-access")
	@ParameterizedTest
	@ValueSource(ints = { 0, 3, 4 })
	@Tag("MethodInBean")
	@DisplayName("Get CompteComptable from list fail")
	public void getByNumeroTestFail(Integer pNumero) {
		assertThatCode(() -> vCompteComptable = vCompteComptable.getByNumero(listCompteComptable, pNumero))
				.doesNotThrowAnyException();
		assertThat(vCompteComptable).isNull();
	}

	@Test
	@Tag("CustomToString")
	@DisplayName("check override toString()")
	public void toStringCheck() {
		vCompteComptable = new CompteComptable(0, "Omega");
		final String vExpected = vCompteComptable.getClass().getSimpleName().concat("{numero=0, libelle='Omega'}");
		assertThat(vExpected.equals(vCompteComptable.toString())).isTrue();
	}

}

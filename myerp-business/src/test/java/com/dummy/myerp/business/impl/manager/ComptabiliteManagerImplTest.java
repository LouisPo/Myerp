package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

public class ComptabiliteManagerImplTest {

	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
	private EcritureComptable vEcritureComptable;

	// ======================= FLE @Test start ==================================

	/**
	 * Set a EcritureComptable object with full constrains respected before each
	 * tests .
	 */
	@BeforeEach
	public void initEcritureComptable() {
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", null));
		vEcritureComptable.setReference("AC-2021/00001");
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Equilibrée");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), "LigneDebit", new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), "LigneCredit", null, new BigDecimal(123)));
	}

	/**
	 *
	 * JUnit test on RG_Compta_0 ecriture comptable constrain.
	 *
	 */
	@Nested
	@Tag("CheckRGCompta")
	@DisplayName("check RG_Compta_0")
	public class ComptabiliteManagerImplTestCheckRGCompta0Test {

		@Test
		@DisplayName("RG_Compta_0 full constrains Test_Pass")
		public void ConstrainsOkTest() {
			assertRGCompta0DoNotThrowConstrainException();
			assertCheckEcritureComptableUnitDoNotThrowAnyException();
		}

		@Test
		@DisplayName("RG_Compta_0 on empty EcritureComptable Test_Fail")
		public void ConstrainsKoTest() {
			vEcritureComptable = new EcritureComptable();
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@DisplayName("RG_Compta_0 on EcritureComptable Id NotNull constrain Test_Fail")
		public void idTest() {
			vEcritureComptable.setId(null);
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@ParameterizedTest
		@ValueSource(strings = { "-2021/0001", "ABCDEFG-2021/0001", "AB-1/0001", "AB-20210/0001", "AB-2021/0",
				"AB-20210/00001" })
		@Tag("ModelConstrains")
		@DisplayName("RG_Compta_0 on EcritureComptable Reference Regex constrain Test_Fail")
		public void referenceRegexTest(String str) {
			vEcritureComptable.setReference(str.concat("-2021/00001"));
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@DisplayName("RG_Compta_0 on EcritureComptable Date NotNull constrain Test_Fail")
		public void dateNullTest() {
			vEcritureComptable.setDate(null);
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@DisplayName("RG_Compta_0 on EcritureComptable Journal NotNull constrain Test_Fail")
		public void journalNullTest() {
			vEcritureComptable.setJournal(null);
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@Tag("ModelConstrains")
		@DisplayName("RG_Compta_0 on EcritureComptable List<LigneEcritureComptable> Size>=2 constrain Test_Fail on Size=0")
		public void listSizeZeroTest() {
			vEcritureComptable.getListLigneEcriture().clear();
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@DisplayName("RG_Compta_0 on EcritureComptable List<LigneEcritureComptable> Size>=2 constrain Test_Fail on Size=1")
		public void listSizeOneTest() {
			vEcritureComptable.getListLigneEcriture().remove(1);
			assertRGCompta0ThrowConstrainException();
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Nested
		@DisplayName("RG_Compta_0 on EcritureComptable libelle constrains")
		public class RGCompta0EcritureComptableLibelleTest {

			@Test
			@DisplayName("RG_Compta_0 on EcritureComptable libelle NotNull constrain Test_Fail")
			public void libelleNullTest() {
				vEcritureComptable.setLibelle(null);
				assertRGCompta0ThrowConstrainException();
				assertCheckEcritureComptableUnitThrowFunctionalException();
			}

			@ParameterizedTest
			@ValueSource(ints = { 1, 20, 200 })
			@DisplayName("RG_Compta_0 on EcritureComptable Libelle Size constrain Test_Pass")
			public void libelleSizeOkTest(int size) {
				vEcritureComptable.setLibelle(libelleSized(size));
				assertRGCompta0DoNotThrowConstrainException();
				assertCheckEcritureComptableUnitDoNotThrowAnyException();
			}

			@ParameterizedTest
			@ValueSource(ints = { 0, 201, 2000 })
			@DisplayName("RG_Compta_0 on EcritureComptable Libelle Size constrain Test_Fail")
			public void libelleSizeKoTest(int size) {
				vEcritureComptable.setLibelle(libelleSized(size));
				assertRGCompta0ThrowConstrainException();
				assertCheckEcritureComptableUnitThrowFunctionalException();
			}

		}

		@Nested
		@DisplayName("RG_Compta_0 on EcritureComptable List<LigneEcritureComptable> LigneEcritureComptable constrains")
		public class LigneEcritureComptableTest {

			@Test
			@DisplayName("LigneEcritureComptable libelle, null constrain Test_Fail ")
			public void ligneEcritureComptableLibelleNull() {
				vEcritureComptable.getListLigneEcriture().get(0).setLibelle(null);
				assertRGCompta0ThrowConstrainException();
			}

			@ParameterizedTest
			@ValueSource(ints = { 1, 20, 200 })
			@DisplayName("LigneEcritureComptable libelle, size constrain Test_Pass ")
			public void ligneEcritureComptableLibelleSizeOkTest(int size) {
				vEcritureComptable.getListLigneEcriture().get(0).setLibelle(libelleSized(size));
				assertRGCompta0DoNotThrowConstrainException();
			}

			@ParameterizedTest
			@ValueSource(ints = { 0, 201, 2000 })
			@DisplayName("LigneEcritureComptable libelle, size constrain Test_Fail ")
			public void ligneEcritureComptableLibelleSizeKoTest(int size) {
				vEcritureComptable.getListLigneEcriture().get(0).setLibelle(libelleSized(size));
				assertRGCompta0ThrowConstrainException();
				assertCheckEcritureComptableUnitThrowFunctionalException();

			}

			@ParameterizedTest
			@ValueSource(strings = { "1.23", "1234567891012" })
			@DisplayName("RG_Compta_7, LigneEcritureComptable @MontantComptable constrain check Pass ")
			public void ligneEcritureComptableMontantComptableOkTest(String vBigDecimal) {
				vEcritureComptable.getListLigneEcriture().get(0).setDebit(new BigDecimal(vBigDecimal));
				vEcritureComptable.getListLigneEcriture().get(1).setCredit(new BigDecimal(vBigDecimal));
				assertRGCompta0DoNotThrowConstrainException();
				assertCheckEcritureComptableUnitDoNotThrowAnyException();
			}

			@ParameterizedTest
			@ValueSource(strings = { "1.234", "123456789101213" })
			@DisplayName("RG_Compta_7, LigneEcritureComptable @MontantComptable constrain Test_Fail ")
			public void ligneEcritureComptableMontantComptableKoTest(String vBigDecimal) {
				vEcritureComptable.getListLigneEcriture().get(0).setDebit(new BigDecimal(vBigDecimal));
				vEcritureComptable.getListLigneEcriture().get(0).setCredit(new BigDecimal(vBigDecimal));
				assertRGCompta0ThrowConstrainException();
				assertCheckEcritureComptableUnitThrowFunctionalException();
			}

		}

		/**
		 * * Assertion on checkRGCompta0 does not throws any exceptions.
		 */
		private void assertRGCompta0DoNotThrowConstrainException() {
			assertThatCode(() -> manager.checkRGCompta0(vEcritureComptable)).doesNotThrowAnyException();
		}

		/**
		 * Assertion on checkRGCompta0 throws ConstraintViolationException cause on
		 * FunctionalException.
		 */
		private void assertRGCompta0ThrowConstrainException() {
			assertThatThrownBy(() -> manager.checkRGCompta0(vEcritureComptable)).isInstanceOf(FunctionalException.class)
					.hasMessage("L'écriture comptable ne respecte pas les règles de gestion.")
					.hasCauseInstanceOf(ConstraintViolationException.class);
		}

	}

	@Nested
	@Tag("ComptabiliteManagerImplMethods")
	@DisplayName("Comptabilite manager methods test")
	public class ComptabiliteManagerImplTestMethodsTest {
		private List<JournalComptable> vJournalComptables;
		private List<CompteComptable> vCompteComptables;
		private List<EcritureComptable> vEcritureComptables;
		private SequenceEcritureComptable vSequenceEcritureComptable;

		@BeforeEach
		public void initLits() {
			vJournalComptables = new ArrayList<>();
			vJournalComptables.add(new JournalComptable("AA", "alpha"));
			vJournalComptables.add(new JournalComptable("BB", "beta"));
			vJournalComptables.add(new JournalComptable("AC", null));
			vCompteComptables = new ArrayList<>();
			vCompteComptables.add(new CompteComptable(1, "alpha"));
			vCompteComptables.add(new CompteComptable(2, "beta"));
			vEcritureComptables = new ArrayList<>();
			vEcritureComptables.add(new EcritureComptable(1, new JournalComptable("AC", null), "AC-2021/00001",
					new Date(), "TestCheckFound_1"));
			vEcritureComptables.add(new EcritureComptable(2, new JournalComptable("AD", null), "AD-2021/00001",
					new Date(), "TestCheckFound_2"));
			vSequenceEcritureComptable = new SequenceEcritureComptable(2021, 123, new JournalComptable("AC", null));

		}

		@Test
		@DisplayName("Date to String parser Test_Pass")
		public void parseDateToStringTestPass() {
			assertThatCode(() -> manager.parseDateToString(vEcritureComptable.getDate())).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Date to String parser Null Date Test_Fail")
		public void parseDateToStringNullDateTestPass() {
			vEcritureComptable.setDate(null);
			assertThatCode(() -> manager.parseDateToString(vEcritureComptable.getDate()))
					.isInstanceOf(FunctionalException.class).hasMessage(" La date est absente");
		}

		@Test
		@DisplayName("getYear Test_Pass")
		public void getYearTestPass() {
			assertThatCode(() -> manager.getYear(vEcritureComptable)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("getYear Null Date Test_Fail")
		public void getYearNullDateTestPass() {
			vEcritureComptable.setDate(null);
			assertThatCode(() -> manager.getYear(vEcritureComptable))
					.isInstanceOf(FunctionalException.class).hasMessage(" La date est absente");
		}


		@ParameterizedTest
		@ValueSource(strings = { "AC", "BB" })
		@DisplayName("is EcritureComptable JournalComptable code is Found in a list<JournalComptable> Test_Pass")
		public void journalComptableFoundTest(String strings) {
			vEcritureComptable.setJournal(new JournalComptable(strings, null));
			assertThat(manager.isJournalComptableExist(vEcritureComptable, vJournalComptables)).isNull();
			assertThatCode(() -> manager.checkJournalComptable(vEcritureComptable, vJournalComptables))
					.doesNotThrowAnyException();
		}

		@ParameterizedTest
		@ValueSource(strings = { "", "CC" })
		@DisplayName("is EcritureComptable JournalComptable code is Found in a list<JournalComptable> Test_Fail")
		public void journalComptableNotFoundTest(String strings) {
			vEcritureComptable.setJournal(new JournalComptable(strings, null));
			assertThat(manager.isJournalComptableExist(vEcritureComptable, vJournalComptables)).isEqualTo(strings);
			assertThatCode(() -> manager.checkJournalComptable(vEcritureComptable, vJournalComptables))
					.isInstanceOf(NotFoundException.class)
					.hasMessage(" Le journal comptable code : [" + strings + "] n'existe pas");
		}

		@Test
		@DisplayName("is EcritureComptable CompteComptable numero is Found in a list<CompteComptable> Test_Pass")
		public void compteComptableFoundTest() {
			assertThat(manager.isCompteComptableExist(vEcritureComptable, vCompteComptables)).isNull();
			assertThatCode(() -> manager.checkCompteComptable(vEcritureComptable, vCompteComptables))
					.doesNotThrowAnyException();
		}

		@ParameterizedTest
		@ValueSource(ints = { 3, 4 })
		@DisplayName("is EcritureComptable CompteComptable code is Found in a list<JournalComptable> Test_Fail")
		public void compteComptableNotFoundTest(int ints) {
			vEcritureComptable.getListLigneEcriture().get(0).getCompteComptable().setNumero(ints);
			assertThat(manager.isCompteComptableExist(vEcritureComptable, vCompteComptables)).isEqualTo(ints);
			assertThatCode(() -> manager.checkCompteComptable(vEcritureComptable, vCompteComptables))
					.isInstanceOf(NotFoundException.class)
					.hasMessage(" Le compte comptable numero : [" + ints + "] n'existe pas");
		}

		@ParameterizedTest
		@ValueSource(ints = { 1, 23, 456, 78910 })
		@DisplayName("Reference number formating Test_Pass")
		public void referenceNumberFormatTestPass(int ints) throws FunctionalException {
			assertThatCode(() -> manager.referenceNumberFormat("5", ints)).doesNotThrowAnyException();
			assertThat(manager.referenceNumberFormat("5", ints).length()).isEqualTo(5);
		}

		@Test
		@DisplayName("Reference number formating Test_Fail")
		public void referenceNumberFormatTestFail() throws FunctionalException {
			assertThatThrownBy(() -> manager.referenceNumberFormat("5", 123456)).isInstanceOf(FunctionalException.class)
					.hasMessage(" Le numéro de référence: [123456] et hors limites");
		}

		@Test
		@DisplayName("EcritureComptable Reference respect regex pattern Test_Pass")
		public void createEcritureComptableReferenceTestPass() throws FunctionalException {
			assertThatCode(() -> manager.createEcritureComptableReference(vEcritureComptable, 123))
					.doesNotThrowAnyException();
			assertThat(manager.createEcritureComptableReference(vEcritureComptable, 123)).isEqualTo("AC-2021/00123");
		}

		@Test
		@DisplayName("EcritureComptable Reference respect regex pattern Test_Fail")
		public void createEcritureComptableReferenceTestFail() throws FunctionalException {
			assertThatThrownBy(() -> manager.createEcritureComptableReference(vEcritureComptable, 123456))
					.isInstanceOf(FunctionalException.class)
					.hasMessage(" Le numéro de référence: [123456] et hors limites");

		}

		@Test
		@DisplayName("EcritureComptable Reference date null Test_Fail")
		public void createEcritureComptableReferenceDateNullTestFail() throws FunctionalException {
			vEcritureComptable.setDate(null);
			assertThatThrownBy(() -> manager.createEcritureComptableReference(vEcritureComptable, 123456))
					.isInstanceOf(FunctionalException.class).hasMessage(" La date est absente");

		}

		@Test
		@DisplayName("Set EcritureComptable Reference From SequenceEcritureComptable Test_Pass")
		public void setEcritureComptableReferenceTestPass() {
			assertThatCode(() -> manager.setEcritureComptableReference(vEcritureComptable, vSequenceEcritureComptable))
					.doesNotThrowAnyException();
			assertThat(vEcritureComptable.getReference()).isEqualTo("AC-2021/00123");
		}

		@Test
		@DisplayName("Set EcritureComptable Reference From SequenceEcritureComptable Test_Fail")
		public void setEcritureComptableReferenceTestFail() {
			vSequenceEcritureComptable.setDerniereValeur(123456);
			assertThatCode(() -> manager.setEcritureComptableReference(vEcritureComptable, vSequenceEcritureComptable))
					.isInstanceOf(FunctionalException.class)
					.hasMessage(" Le numéro de référence: [123456] et hors limites");
		}

		@Test
		@DisplayName("Set EcritureComptable Reference From SequenceEcritureComptable date null Test_Fail")
		public void setEcritureComptableReferenceDateNullTestFail() {
			vEcritureComptable.setDate(null);
			assertThatCode(() -> manager.setEcritureComptableReference(vEcritureComptable, vSequenceEcritureComptable))
					.isInstanceOf(FunctionalException.class).hasMessage(" La date est absente");
		}

		@Test
		@DisplayName("Assert that created SequenceEcritureComptable return expected value")
		public void createSequenceEcritureComptableTest() {
			assertThat(manager.createSequenceEcritureComptable(vEcritureComptable, 1))
					.isEqualTo(new SequenceEcritureComptable(2021, 1, vEcritureComptable.getJournal()));
		}

		@Test
		@DisplayName("Assert that created SequenceEcritureComptable date null Test_Fail")
		public void createSequenceEcritureComptableDateNullTest() {
			vEcritureComptable.setDate(null);
			assertThat(manager.createSequenceEcritureComptable(vEcritureComptable, 1))
					.isEqualTo(new SequenceEcritureComptable(2021, 1, vEcritureComptable.getJournal()));
		}

		@Test
		@DisplayName("Assert that add new dernierValue to SequenceEcritureComptable return expected value")
		public void addTheNewLastValueToSequenceEcritureComptableTest() {
			assertThat(manager.addTheNewLastValueToSequenceEcritureComptable(vSequenceEcritureComptable))
					.isEqualTo(new SequenceEcritureComptable(vSequenceEcritureComptable.getAnnee(), 123 + 1,
							vSequenceEcritureComptable.getJournalComptable()));
		}

		@ParameterizedTest
		@ValueSource(ints = { 1, 2 })
		@DisplayName("is EcritureComptable is Found in a list<EcritureComptable> Test_Pass")
		public void ecritureComptableFoundTest(int ints) {
			vEcritureComptable.setId(ints);
			assertThat(manager.isEcritureComptableExist(vEcritureComptable, vEcritureComptables)).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = { 3, 4 })
		@DisplayName("is EcritureComptable is Found in a list<EcritureComptable> Test_Fail")
		public void ecritureComptableNotFoundTest(int ints) {
			vEcritureComptable.setId(ints);
			String vReference = vEcritureComptable.getReference();
			assertThat(manager.isEcritureComptableExist(vEcritureComptable, vEcritureComptables)).isEqualTo(vReference);
		}

	}

	@Nested
	@Tag("RG_Compta_Check")
	@DisplayName("test RG_Compta")
	public class ComptabiliteManagerImplTestRGComptaTest {
		@Test
		@Tag("RG_Compta_3")
		public void isListLigneEcritureSizePassTestPass() {
			assertThat(manager.isListLigneEcritureSizePass(vEcritureComptable)).isTrue();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("isListLigneEcritureSizePass Fail on size = 0")
		public void isListLigneEcritureSizePassTestFailZero() {
			vEcritureComptable.getListLigneEcriture().clear();
			assertThat(manager.isListLigneEcritureSizePass(vEcritureComptable)).isFalse();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("isListLigneEcritureSizePass Fail on size = 1")
		public void isListLigneEcritureSizePassTestFailOne() {
			vEcritureComptable.getListLigneEcriture().remove(1);
			assertThat(manager.isListLigneEcritureSizePass(vEcritureComptable)).isFalse();
		}

		@Tag("RG_Compta_3")
		public void atLeastOneDebitAndOneCreditTestPass() {
			assertThat(manager.atLeastOneDebitAndOneCredit(vEcritureComptable)).isTrue();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("atLeastOneDebitAndOneCredit Fail No credit No debit")
		public void atLeastOneDebitAndOneCreditTestFailZero() {
			vEcritureComptable.getListLigneEcriture().clear();
			assertThat(manager.atLeastOneDebitAndOneCredit(vEcritureComptable)).isFalse();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("atLeastOneDebitAndOneCredit Fail No credit")
		public void isListLigneEcritureSizePassFailNoCredit() {
			vEcritureComptable.getListLigneEcriture().remove(1);
			assertThat(manager.atLeastOneDebitAndOneCredit(vEcritureComptable)).isFalse();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("atLeastOneDebitAndOneCredit Fail No debit")
		public void isListLigneEcritureSizePassFailNoDebit() {
			vEcritureComptable.getListLigneEcriture().remove(0);
			assertThat(manager.atLeastOneDebitAndOneCredit(vEcritureComptable)).isFalse();
		}

		@Test
		@Tag("RG_Compta_3")
		@DisplayName("checkRGCompta 3 Pass")
		public void checkRGCompta3TestPass() {
			assertThatCode(() -> manager.checkRGCompta3(vEcritureComptable)).doesNotThrowAnyException();
		}

		@ParameterizedTest
		@ValueSource(ints = { 0, 1 })
		@Tag("RG_Compta_3")
		@DisplayName("checkRGCompta 3 Fail")
		public void checkRGCompta3TestFail(int ints) {
			vEcritureComptable.getListLigneEcriture().remove(ints);
			assertThatThrownBy(() -> manager.checkRGCompta3(vEcritureComptable)).isInstanceOf(FunctionalException.class)
					.hasMessage(
							"L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

		@Test
		@Tag("RG_Compta_5")
		@DisplayName("checkReferenceDate Pass")
		public void checkReferenceDateTestPass() throws FunctionalException {
			assertThat(manager.checkReferenceDate(vEcritureComptable, "2021")).isTrue();
		}

		@Test
		@Tag("RG_Compta_5")
		@DisplayName("checkReferenceDate Fail")
		public void checkReferenceDateTestFail() throws FunctionalException {
			assertThat(manager.checkReferenceDate(vEcritureComptable, "2020")).isFalse();
		}

		@Test
		@Tag("RG_Compta_5")
		@DisplayName("checkReferenceJournalCode Pass")
		public void checkReferenceJournalCodeTestPass() {
			assertThat(manager.checkReferenceJournalCode(vEcritureComptable, "AC")).isTrue();
		}

		@Test
		@Tag("RG_Compta_5")
		@DisplayName("checkReferenceJournalCode Fail")
		public void checkReferenceJournalCodeTestFail() {
			assertThat(manager.checkReferenceJournalCode(vEcritureComptable, "BB")).isFalse();

		}

		// TODO be be mocked not here
		@Test
		@Tag("RG_Compta_5")
		@DisplayName("checkRGCompta 5 Pass")
		public void checkRGCompta5TestPass() {
			assertThatCode(() -> manager.checkRGCompta3(vEcritureComptable)).doesNotThrowAnyException();
		}

		@ParameterizedTest
		@ValueSource(strings = { "ABC-2025/00001", "ABC-2021/00001", "AC-2025/00001" })
		@Tag("RG_Compta_3")
		@DisplayName("checkRGCompta 3 Fail")
		public void checkRGCompta5TestFail(String strings) {
			vEcritureComptable.setReference(strings);
			assertThatThrownBy(() -> manager.checkRGCompta5(vEcritureComptable)).isInstanceOf(FunctionalException.class)
					.hasMessage(
							"La Référence de l'écriture comptable contien une erreur sur le code journal et/ou l'année.");
			assertCheckEcritureComptableUnitThrowFunctionalException();
		}

	}

	/*
	 * @Nested
	 *
	 * @Tag("CheckMethodsTest")
	 *
	 * @DisplayName("Business comptabiliteManager check methods test") public class
	 * ComptabiliteManagerCheckMethods {
	 *
	 * private List<JournalComptable> vJournalComptables; private
	 * List<CompteComptable> vCompteComptables;
	 *
	 * @BeforeEach public void initLits() { vJournalComptables = new ArrayList<>();
	 * vJournalComptables.add(new JournalComptable("AA", "alpha"));
	 * vJournalComptables.add(new JournalComptable("BB", "beta")); vCompteComptables
	 * = new ArrayList<>(); vCompteComptables.add(new CompteComptable(1, "alpha"));
	 * vCompteComptables.add(new CompteComptable(2, "beta")); }
	 *
	 * @ParameterizedTest
	 *
	 * @ValueSource(strings = { "AA", "BB" })
	 *
	 * @DisplayName("Check if JournalComptable is found in a list Pass") public void
	 * checkJournalComptableTestPass(String strings) {
	 * vEcritureComptable.setJournal(new JournalComptable(strings, null));
	 * assertThatCode(() -> manager.checkJournalComptable(vEcritureComptable,
	 * vJournalComptables)) .doesNotThrowAnyException(); }
	 *
	 * @ParameterizedTest
	 *
	 * @ValueSource(strings = { "", "CC" })
	 *
	 * @DisplayName("Check if JournalComptable is found in a list Fail") public void
	 * checkJournalComptableTestFail(String strings) {
	 * vEcritureComptable.setJournal(new JournalComptable(strings, null));
	 * assertThatThrownBy(() -> manager.checkJournalComptable(vEcritureComptable,
	 * vJournalComptables)) .isInstanceOf(NotFoundException.class).
	 * hasMessage("JournalComptable introuvable");
	 *
	 * }
	 *
	 * @Test
	 *
	 * @DisplayName("Check if CompteComptable is found in a list Pass") public void
	 * checkCompteComptableTestPass() { assertThatCode(() ->
	 * manager.checkCompteComptable(vEcritureComptable, vCompteComptables))
	 * .doesNotThrowAnyException();
	 *
	 * }
	 *
	 * @Test
	 *
	 * @DisplayName("Check if CompteComptable is found in a list Fail") public void
	 * checkCompteComptableTestFail() {
	 * vEcritureComptable.getListLigneEcriture().clear();
	 * vEcritureComptable.getListLigneEcriture() .add(new LigneEcritureComptable(new
	 * CompteComptable(3), "LigneDebit", new BigDecimal(123), null));
	 * vEcritureComptable.getListLigneEcriture() .add(new LigneEcritureComptable(new
	 * CompteComptable(4), "LigneCredit", null, new BigDecimal(123)));
	 * assertThatThrownBy(() -> manager.checkCompteComptable(vEcritureComptable,
	 * vCompteComptables)) .isInstanceOf(NotFoundException.class).
	 * hasMessage("CompteComptable introuvable");
	 *
	 * }
	 *
	 * }
	 */

	private String libelleSized(int size) {
		StringBuilder str = new StringBuilder();
		str.setLength(size);
		return str.toString();
	}

	private void assertCheckEcritureComptableUnitDoNotThrowAnyException() {
		assertThatCode(() -> manager.checkEcritureComptableUnit(vEcritureComptable)).doesNotThrowAnyException();

	}

	private void assertCheckEcritureComptableUnitThrowFunctionalException() {
		assertThatThrownBy(() -> manager.checkEcritureComptableUnit(vEcritureComptable))
				.isInstanceOf(FunctionalException.class);
	}

	// ======================= FLE @Test end =======================================

}

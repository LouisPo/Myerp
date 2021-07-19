package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;

/**
 *
 * Unit tests on methods which call the DB<br>
 * By mocking:<br>
 * - BusinessProxy <br>
 * - DaoProxy <br>
 * - TransactionManager <br>
 * And configure all of these object in ComptabiliteManager
 *
 */
@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplMockTest {

    private ComptabiliteManagerImpl vComptabiliteManagerImpl;

    @Mock
    BusinessProxyImpl Bproxy;
    @Mock
    DaoProxyImpl Dproxy;
    @Mock
    ComptabiliteDaoImpl Cdao;
    @Mock
    TransactionManager tx;

    @Mock
    private EcritureComptable vmEcritureComptable;

    @Mock
    private JournalComptable vmJournalComptable;

    @Mock
    private CompteComptable vmCompteComptable;

    @BeforeEach
    public void initCompatibiliteManagerImpl() {
	vComptabiliteManagerImpl = new ComptabiliteManagerImpl();

	vComptabiliteManagerImpl.configure(Bproxy, Dproxy, tx);

    }

    @Test
    @Tag("RG_Compta_Check")
    @DisplayName("RG_Compta_2 Pass")
    public void ecritureComptableIsEquilibreeTrue() {
	when(vmEcritureComptable.isEquilibree()).thenReturn(true);
	assertThatCode(() -> vComptabiliteManagerImpl.checkRGCompta2(vmEcritureComptable)).doesNotThrowAnyException();
    }

    @Test
    @Tag("RG_Compta_Check")
    @DisplayName("RG_Compta_2 Fail")
    public void ecritureComptableIsEquilibreeFalse() {
	when(vmEcritureComptable.isEquilibree()).thenReturn(false);
	assertThatThrownBy(() -> vComptabiliteManagerImpl.checkRGCompta2(vmEcritureComptable))
		.isInstanceOf(FunctionalException.class).hasMessage("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    public void ecitureComptableIdValueNotDefaultCheckFail() {
	when(vmEcritureComptable.getId()).thenReturn(-1);
	assertThatThrownBy(() -> vComptabiliteManagerImpl.checkEcritureComptableIdNotDefault(vmEcritureComptable))
		.isInstanceOf(TechnicalException.class).hasMessage("Valeur par defaut appliquée, erreur séquence");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 100 })
    public void ecitureComptableIdValueNotDefaultCheckPass(int ints) {
	when(vmEcritureComptable.getId()).thenReturn(ints);
	assertThatCode(() -> vComptabiliteManagerImpl.checkEcritureComptableIdNotDefault(vmEcritureComptable))
		.doesNotThrowAnyException();
    }

    @Nested
    @Tag("CheckMethods")
    @DisplayName("CheckEcriturComptable methods tests")
    public class ComptabiliteManagerImplMockTestCheckEcritureComptablesTests {

	private ComptabiliteManagerImpl vComptabiliteManagerImplSpy;
	private List<JournalComptable> vJournalComptables;
	private List<CompteComptable> vCompteComptables;
	private List<LigneEcritureComptable> vLignesEcritureComptables;
	private List<EcritureComptable> vEcritureComptables;

	@BeforeEach
	public void initList() {
	    vComptabiliteManagerImplSpy = spy(vComptabiliteManagerImpl);
	    vJournalComptables = new ArrayList<>();
	    vJournalComptables.add(new JournalComptable("AA", "alpha"));
	    vJournalComptables.add(new JournalComptable("BB", "beta"));
	    vCompteComptables = new ArrayList<>();
	    vCompteComptables.add(new CompteComptable(1, "alpha"));
	    vCompteComptables.add(new CompteComptable(2, "beta"));
	    vEcritureComptables = new ArrayList<>();
	    vEcritureComptables.add(new EcritureComptable(1, new JournalComptable("AC", null), "AC-2021/00001",
		    new Date(), "TestCheckFound_1"));
	    vEcritureComptables.add(new EcritureComptable(2, new JournalComptable("AD", null), "AD-2021/00001",
		    new Date(), "TestCheckFound_2"));
	    vLignesEcritureComptables = new ArrayList<>();
	    vLignesEcritureComptables
		    .add(new LigneEcritureComptable(new CompteComptable(1), "LigneDebit", new BigDecimal(123), null));
	    vLignesEcritureComptables
		    .add(new LigneEcritureComptable(new CompteComptable(2), "LigneCredit", null, new BigDecimal(123)));
	 

	}

	@Test
	@DisplayName("check if EcritureComptable journalComptable code and compteComptable numero all exist in DB Test_Pass")
	public void checkIsJournalAndCompteComptableExistTest() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getListJournalComptable()).thenReturn(vJournalComptables);
	    when(Cdao.getListCompteComptable()).thenReturn(vCompteComptables);
	    when(vmEcritureComptable.getJournal()).thenReturn(vmJournalComptable);
	    when(vmJournalComptable.getCode()).thenReturn("AA");
	    assertThatCode(() -> vComptabiliteManagerImpl.checkIsJournalAndCompteComptableExist(vmEcritureComptable))
		    .doesNotThrowAnyException();
	}

	@Test
	@DisplayName("check if EcritureComptable journalComptable code exist in DB Test_Fail")
	public void checkIsJournalAndCompteComptableExistJournalTest() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getListJournalComptable()).thenReturn(vJournalComptables);
	    when(Cdao.getListCompteComptable()).thenReturn(vCompteComptables);
	    when(vmEcritureComptable.getJournal()).thenReturn(vmJournalComptable);
	    when(vmJournalComptable.getCode()).thenReturn("CC");
	    assertThatThrownBy(
		    () -> vComptabiliteManagerImpl.checkIsJournalAndCompteComptableExist(vmEcritureComptable))
			    .isInstanceOf(NotFoundException.class)
			    .hasMessage(" Le journal comptable code : [CC] n'existe pas");
	}

	@Test
	@DisplayName("check if EcritureComptable compteComptable numero all exist in DB Test_Fail")
	public void checkIsJournalAndCompteComptableExistCompteTest() {
	    vCompteComptables.remove(0);
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getListJournalComptable()).thenReturn(vJournalComptables);
	    when(Cdao.getListCompteComptable()).thenReturn(vCompteComptables);
	    when(vmEcritureComptable.getJournal()).thenReturn(vmJournalComptable);
	    when(vmJournalComptable.getCode()).thenReturn("AA");
	    when(vmEcritureComptable.getListLigneEcriture()).thenReturn(vLignesEcritureComptables);
	    assertThatThrownBy(
		    () -> vComptabiliteManagerImpl.checkIsJournalAndCompteComptableExist(vmEcritureComptable))
			    .isInstanceOf(NotFoundException.class)
			    .hasMessage(" Le compte comptable numero : [1] n'existe pas");
	}

	@Test
	@DisplayName("check if EcritureComptable exist in DB Test_Pass")
	public void checkIsEcritureComptableExistTest() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getListEcritureComptable()).thenReturn(vEcritureComptables);
	    when(vmEcritureComptable.getId()).thenReturn(1);
	    assertThatCode(() -> vComptabiliteManagerImpl.checkIsEcritureComptableExist(vmEcritureComptable))
		    .doesNotThrowAnyException();
	}

	@Test
	@DisplayName("check if EcritureComptable exist in DB Test_Pass")
	public void checkIsEcritureComptableExistNotTest() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getListEcritureComptable()).thenReturn(vEcritureComptables);
	    when(vmEcritureComptable.getId()).thenReturn(3);
	    when(vmEcritureComptable.getReference()).thenReturn("Trois cannard et un pingouin");
	    assertThatCode(() -> vComptabiliteManagerImpl.checkIsEcritureComptableExist(vmEcritureComptable))
		    .isInstanceOf(NotFoundException.class).hasMessage(" l'écriture comptable reference : ["
			    + vmEcritureComptable.getReference() + "] n'existe pas");
	}

	@Test
	@Tag("RG_Compta_Check")
	@DisplayName("RG_Compta_6 checkEcritureComptableContext EcritureComptable Null reference Test_Pass")
	/*
	 * test : StringUtils.isNoneEmpty(pEcritureComptable.getReference())
	 *
	 */
	public void checkEcritureComptableContextNullRefTest() throws NotFoundException {
	    assertThatCode(() -> vComptabiliteManagerImpl.checkEcritureComptableContext(vmEcritureComptable))
		    .doesNotThrowAnyException();
	}

	@Test
	@Tag("RG_Compta_Check")
	@DisplayName("RG_Compta_6 checkEcritureComptableContext EcritureComptable Not found reference Test_Pass")
	/*
	 * test : EcritureComptable vECRef = getDaoProxy().getComptabiliteDao()
	 * .getEcritureComptableByRef(pEcritureComptable.getReference()) throw NotFound
	 * Exception
	 */
	public void checkEcritureComptableContextNotFoundRefTest() throws NotFoundException {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(vmEcritureComptable.getReference()).thenReturn("AA-2021/00001");
	    when(Cdao.getEcritureComptableByRef(vmEcritureComptable.getReference())).thenThrow(NotFoundException.class);
	    assertThatCode(() -> vComptabiliteManagerImpl.checkEcritureComptableContext(vmEcritureComptable))
		    .doesNotThrowAnyException();
	}

	@Test
	@Tag("RG_Compta_Check")
	@DisplayName("RG_Compta_6 checkEcritureComptableContext EcritureComptable id != default and equals Test_Pass")
	/*
	 * !pEcritureComptable.getId().equals(vECRef.getId() false &&
	 * pEcritureComptable.getId() == -1 false Not on error cause it is more an
	 * update than a new insert
	 *
	 */
	public void checkEcritureComptableContextNoDefaultIdAndEqualsTest() throws NotFoundException {
	    EcritureComptable vmEcritureComptableToCompare = mock(EcritureComptable.class);
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(vmEcritureComptable.getId()).thenReturn(2);
	    when(vmEcritureComptableToCompare.getId()).thenReturn(2);
	    when(vmEcritureComptable.getReference()).thenReturn("AA-2021/00001");
	    when(Cdao.getEcritureComptableByRef("AA-2021/00001")).thenReturn(vmEcritureComptableToCompare);
	    assertThatCode(() -> vComptabiliteManagerImpl.checkEcritureComptableContext(vmEcritureComptable))
		    .doesNotThrowAnyException();
	}

	@Test
	@Tag("RG_Compta_Check")
	@DisplayName("RG_Compta_6 checkEcritureComptableContext EcritureComptable id == default Test_Fail")
	/*
	 * /* test : pEcritureComptable.getId() == -1 true
	 *
	 */
	public void checkEcritureComptableContextDefaultIdTest() throws NotFoundException {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(vmEcritureComptable.getId()).thenReturn(-1);
	    when(vmEcritureComptable.getReference()).thenReturn("AA-2021/00001");
	    when(Cdao.getEcritureComptableByRef("AA-2021/00001")).thenReturn(vmEcritureComptable);
	    assertThatThrownBy(() -> vComptabiliteManagerImpl.checkEcritureComptableContext(vmEcritureComptable))
		    .isInstanceOf(FunctionalException.class);
	}

	@Test
	@Tag("RG_Compta_Check")
	@DisplayName("RG_Compta_6 checkEcritureComptableContext EcritureComptable id != default and Not equals Test_Fail")
	/*
	 * !pEcritureComptable.getId().equals(vECRef.getId() true &&
	 * pEcritureComptable.getId() == -1 false
	 *
	 */
	public void checkEcritureComptableContextNoDefaultIdAndNotEqualsTest() throws NotFoundException {
	    EcritureComptable vmEcritureComptableToCompare = mock(EcritureComptable.class);
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(vmEcritureComptable.getId()).thenReturn(2);
	    when(vmEcritureComptable.getReference()).thenReturn("AA-2021/00001");
	    when(vmEcritureComptableToCompare.getId()).thenReturn(3);
	    when(Cdao.getEcritureComptableByRef("AA-2021/00001")).thenReturn(vmEcritureComptableToCompare);
	    assertThatThrownBy(() -> vComptabiliteManagerImpl.checkEcritureComptableContext(vmEcritureComptable))
		    .isInstanceOf(FunctionalException.class);
	}
    //TEST NO 1
	@Test
	@DisplayName("checkEcritureComptable Test_Pass")
	public void checkEcritureComptableTest() throws FunctionalException, NotFoundException {
		
		/*checkEcritureComptableUnit  Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
		 * RG_Compta_2 3 et 5*/
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableContext(vmEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).checkIsJournalAndCompteComptableExist(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.checkEcritureComptable(vmEcritureComptable))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("checkEcritureComptable checkEcritureComptableUnit Test_Fail")
	public void checkEcritureComptableCheckEcritureComptableUnitTest()
		throws FunctionalException, NotFoundException {
	    doThrow(new FunctionalException(new ConstraintViolationException(null))).when(vComptabiliteManagerImplSpy)
		    .checkEcritureComptableUnit(vmEcritureComptable);
	    assertThatThrownBy(() -> vComptabiliteManagerImplSpy.checkEcritureComptable(vmEcritureComptable))
		    .isInstanceOf(FunctionalException.class).hasCauseInstanceOf(ConstraintViolationException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("checkEcritureComptable checkEcritureComptableContext Test_Fail")
	public void checkEcritureComptableCheckEcritureComptableContextTest()
		throws FunctionalException, NotFoundException {
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doThrow(FunctionalException.class).when(vComptabiliteManagerImplSpy)
		    .checkEcritureComptableContext(vmEcritureComptable);
	    assertThatThrownBy(() -> vComptabiliteManagerImplSpy.checkEcritureComptable(vmEcritureComptable))
		    .isInstanceOf(FunctionalException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("checkEcritureComptable checkIsJournalAndCompteComptableExist Test_Fail")
	public void checkEcritureComptableCheckIsJournalAndCompteComptableExistTest()
		throws FunctionalException, NotFoundException {
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableContext(vmEcritureComptable);
	    doThrow(NotFoundException.class).when(vComptabiliteManagerImplSpy)
		    .checkIsJournalAndCompteComptableExist(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.checkEcritureComptable(vmEcritureComptable))
		    .isInstanceOf(NotFoundException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("Check Ecriture comptable before upadate Test_Pass")
	public void checkEcritureComptableBeforeUpdateTest() throws FunctionalException, NotFoundException {
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).checkIsJournalAndCompteComptableExist(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.checkEcritureComptableBeforeUpdate(vmEcritureComptable))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptableBeforeUpdate(vmEcritureComptable);
	}

	@Test
	@DisplayName("Check Ecriture comptable before upadate NotFoundException  Test_Fail")
	public void checkEcritureComptableBeforeUpdateNotFoundExceptionTest()
		throws FunctionalException, NotFoundException {
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doThrow(NotFoundException.class).when(vComptabiliteManagerImplSpy)
		    .checkIsJournalAndCompteComptableExist(vmEcritureComptable);
	    assertThatThrownBy(
		    () -> vComptabiliteManagerImplSpy.checkEcritureComptableBeforeUpdate(vmEcritureComptable))
			    .isInstanceOf(NotFoundException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).checkEcritureComptableBeforeUpdate(vmEcritureComptable);
	}

    }

    @Nested
    @Tag("addReferenceTest")
    @DisplayName("check addReference functionality")
    public class ComptabiliteManagerImplMockTestAddRefrenceTests {

	private Integer vYear = 2021;
	private String vCodeJournal = "AC";
	private ComptabiliteManagerImpl vComptabiliteManagerImplSpy;
	private List<JournalComptable> vJournalComptables;
	private SequenceEcritureComptable vSequenceEcritureComptable;

	@BeforeEach
	public void initList() throws NotFoundException {
	    vComptabiliteManagerImplSpy = spy(vComptabiliteManagerImpl);
	    vJournalComptables = new ArrayList<>();
	    vJournalComptables.add(new JournalComptable("AA", "alpha"));
	    vJournalComptables.add(new JournalComptable("BB", "beta"));
	    vSequenceEcritureComptable = new SequenceEcritureComptable(vYear, 123,
		    new JournalComptable(vCodeJournal, null));
	    doReturn(vJournalComptables).when(vComptabiliteManagerImplSpy).getListJournalComptable();
	    doNothing().when(vComptabiliteManagerImplSpy).checkJournalComptable(vmEcritureComptable,
		    vJournalComptables);
	    when(vmEcritureComptable.getJournal()).thenReturn(vmJournalComptable);
	    when(vmJournalComptable.getCode()).thenReturn(vCodeJournal);
	    when(vmEcritureComptable.getDate()).thenReturn(new Date());
	    when(vComptabiliteManagerImplSpy.getvSequenceEcritureComptable()).thenReturn(vSequenceEcritureComptable);

	}

	@Test
	@DisplayName("Add EcritureComptable reference on SequenceEcritureComptable Found Test_Pass")
	public void addreferenceFoundSequenceTest() throws NotFoundException, FunctionalException {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    when(Cdao.getSequenceEcritureComptable(vCodeJournal, vYear)).thenReturn(vSequenceEcritureComptable);
	    when(vComptabiliteManagerImplSpy.getYear(vmEcritureComptable)).thenReturn(vYear);
	    doNothing().when(vComptabiliteManagerImplSpy).updateSequenceEcritureComptable(vSequenceEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.addReference(vmEcritureComptable))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).addReference(vmEcritureComptable);
	}

	@Test
	@DisplayName("Add EcritureComptable reference on SequenceEcritureComptable not Found Test_Pass")
	public void addreferenceNotFoundSequenceTest() throws NotFoundException, FunctionalException {
	    doThrow(NotFoundException.class).when(vComptabiliteManagerImplSpy)
		    .setvSequenceEcritureComptable(vCodeJournal, vYear);
	    doReturn(vSequenceEcritureComptable).when(vComptabiliteManagerImplSpy)
		    .createSequenceEcritureComptable(vmEcritureComptable, 1);
	    when(vComptabiliteManagerImplSpy.getvSequenceEcritureComptable()).thenReturn(vSequenceEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).insertSequenceEcritureComptable(vSequenceEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.addReference(vmEcritureComptable))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).addReference(vmEcritureComptable);

	}

    }

    @Nested
    @Tag("CRUD")
    @DisplayName("ComptabiliteManager CRUD Tests")
    public class ComptabiliteManagerImplMockTestCRUDTest {
	private ComptabiliteManagerImpl vComptabiliteManagerImplSpy;

	@BeforeEach
	public void spyComptabiliteManagerImpl() {
	    vComptabiliteManagerImplSpy = spy(vComptabiliteManagerImpl);
	}

	@Test
	@DisplayName("CRUD insert EcritureComptable Test_Pass")
	public void insertTestPass() throws NotFoundException, FunctionalException {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    doNothing().when(vComptabiliteManagerImplSpy).addReference(vmEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptable(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.insertEcritureComptable((vmEcritureComptable)))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).insertEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD insert EcritureComptable checkEcritureComptable Test_Fail FunctionalException")
	public void InsertTestFailFunctionalException() throws NotFoundException, FunctionalException {
	    doNothing().when(vComptabiliteManagerImplSpy).addReference(vmEcritureComptable);
	    doThrow(new FunctionalException(new ConstraintViolationException(null))).when(vComptabiliteManagerImplSpy)
		    .checkEcritureComptable(vmEcritureComptable);
	    assertThatThrownBy(() -> vComptabiliteManagerImplSpy.insertEcritureComptable((vmEcritureComptable)))
		    .isInstanceOf(FunctionalException.class).hasCauseInstanceOf(ConstraintViolationException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).insertEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD insert EcritureComptable checkEcritureComptable Test_Fail NotFoundException")
	public void InsertTestFailNotFoundException() throws NotFoundException, FunctionalException {
	    doNothing().when(vComptabiliteManagerImplSpy).addReference(vmEcritureComptable);
	    doThrow(new NotFoundException()).when(vComptabiliteManagerImplSpy)
		    .checkEcritureComptable(vmEcritureComptable);
	    assertThatThrownBy(() -> vComptabiliteManagerImplSpy.insertEcritureComptable((vmEcritureComptable)))
		    .isInstanceOf(NotFoundException.class);
	    verify(vComptabiliteManagerImplSpy).insertEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD Update EcritureComptable Test_Pass")
	public void updateTestPass() throws FunctionalException, NotFoundException {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableBeforeUpdate(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.updateEcritureComptable((vmEcritureComptable)))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).updateEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD Update EcritureComptable Test_Fail FunctionalException")
	public void updateTestFailFunctionalException() throws FunctionalException, NotFoundException {
	    // doThrow(new FunctionalException(new
	    // ConstraintViolationException(null))).when(vComptabiliteManagerImplSpy)
	    // .checkEcritureComptableBeforeUpdate(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.updateEcritureComptable((vmEcritureComptable)))
		    .isInstanceOf(FunctionalException.class).hasCauseInstanceOf(ConstraintViolationException.class);
	    verify(vComptabiliteManagerImplSpy).updateEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD Update EcritureComptable Test_Fail NotFoundException")
	public void updateTestFailNotFoundException() throws FunctionalException, NotFoundException {
	    doNothing().when(vComptabiliteManagerImplSpy).checkEcritureComptableUnit(vmEcritureComptable);
	    doThrow(new NotFoundException()).when(vComptabiliteManagerImplSpy)
		    .checkEcritureComptableBeforeUpdate(vmEcritureComptable);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.updateEcritureComptable((vmEcritureComptable)))
		    .isInstanceOf(NotFoundException.class);
	    verify(vComptabiliteManagerImplSpy, times(1)).updateEcritureComptable(vmEcritureComptable);
	}

	@Test
	@DisplayName("CRUD Delete EcritureComptable Test_Pass")
	public void deleteTestPass() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.deleteEcritureComptable((null)))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).deleteEcritureComptable(null);
	}

	@Test
	@DisplayName("CRUD insert SequenceEcritureComptable Test_Pass")
	public void insertSequenceEcritureComptableTestPass() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.insertSequenceEcritureComptable((null)))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).insertSequenceEcritureComptable(null);
	}

	@Test
	@DisplayName("CRUD update SequenceEcritureComptable Test_Pass")
	public void updateSequenceEcritureComptableTestPass() {
	    when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	    assertThatCode(() -> vComptabiliteManagerImplSpy.updateSequenceEcritureComptable((null)))
		    .doesNotThrowAnyException();
	    verify(vComptabiliteManagerImplSpy, times(1)).updateSequenceEcritureComptable(null);
	}

    }

    // TODO to supress
    // @Test
    public void testMockDao() throws FunctionalException {
	List<CompteComptable> list = new ArrayList<>();
	list.add(new CompteComptable(1, "aa"));
	when(Dproxy.getComptabiliteDao()).thenReturn(Cdao);
	when(Cdao.getListCompteComptable()).thenReturn(list);
	assertThat(vComptabiliteManagerImpl.getListCompteComptable()).isNotNull();
	assertThat(vComptabiliteManagerImpl.getListCompteComptable().size()).isEqualTo(1);

    }

}

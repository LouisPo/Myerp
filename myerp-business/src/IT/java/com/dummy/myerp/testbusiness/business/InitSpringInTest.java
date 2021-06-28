package com.dummy.myerp.testbusiness.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class InitSpringInTest extends BusinessTestCase {

	public InitSpringInTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	// @Mock
	// ComptabiliteDao dao;

	ComptabiliteManager manager = new ComptabiliteManagerImpl();
	private EcritureComptable vEcritureComptable = new EcritureComptable();

	@BeforeEach
	public void setUp() {
		SpringRegistry.init();
		manager = getBusinessProxy().getComptabiliteManager();
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setId(1);
		//vEcritureComptable.setReference("AC-2021/00001");;
		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("CrudTest");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(401), "LigneDebit", new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(411), "LigneCredit", null, new BigDecimal(123)));
	}

	@Test
	public void initTest() throws FunctionalException {
		SpringRegistry.init();
		assertThat(getBusinessProxy()).isNotNull();
		assertNotNull(getBusinessProxy());
		assertNotNull(getTransactionManager());
		assertNotNull(getBusinessProxy().getComptabiliteManager());

	}



	@Test
	public void crudInsert() {
		vEcritureComptable.setLibelle("Insert_Test-"+LocalDate.now().toString());
		assertThatCode(()-> manager.insertEcritureComptable(vEcritureComptable)).doesNotThrowAnyException();
	}

	@Test
	public void crudUpdate() throws FunctionalException, NotFoundException {
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable = manager.getListEcritureComptable().get(0);
		vEcritureComptable.setLibelle("Update_Test-"+LocalDate.now().toString());;		;
		assertThatCode(()->manager.updateEcritureComptable(vEcritureComptable)).doesNotThrowAnyException();
	}

	@Test
	public void crudDelete() throws FunctionalException, NotFoundException {
		vEcritureComptable.setLibelle("toDelete");
		manager.insertEcritureComptable(vEcritureComptable);
		List<EcritureComptable> toDelete = manager.getListEcritureComptable().stream()
				.filter(o->o.getLibelle().compareTo("toDelete") ==0).collect(Collectors.toList());
		assertThatCode(()->manager.deleteEcritureComptable(toDelete.get(0).getId())).doesNotThrowAnyException();

	}



}

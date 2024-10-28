package com.example.transaction.authorizer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.transaction.authorizer.enums.TransactionCode;
import com.example.transaction.authorizer.model.Account;
import com.example.transaction.authorizer.model.Transaction;
import com.example.transaction.authorizer.repository.AccountRepository;
import com.example.transaction.authorizer.repository.TransactionRepository;
import com.example.transaction.authorizer.request.AuthorizerRequest;
import com.example.transaction.authorizer.response.AuthorizerResponse;

@SpringJUnitConfig
class AuthorizerServiceTest {

	private static final String ACCOUNT_ID = "d61033b7-75fe-4e19-8a96-2eaea05fffd7";
	private static final String MERCHANT = "MERCHANT";
	private static final String MCC_FOOD = "5411";
	private static final String MCC_MEAL = "5811";
	private static final String MCC_CASH = "1234";

	@InjectMocks
	private AuthorizerService authorizerService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Test
	void createSimpleAuthorizer_whenMccFoodAndAccountExistsAndThereIsEnoughMoney_shouldReturnSuccessCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_FOOD, Double.valueOf(10))));
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_FOOD, Double.valueOf(10)));

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.SUCCESS.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createSimpleAuthorizer_whenMccMealAndAccountExistsAndThereIsEnoughMoney_shouldReturnSuccessCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_MEAL));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_MEAL, Double.valueOf(10))));
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_MEAL, Double.valueOf(10)));

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_MEAL);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.SUCCESS.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createSimpleAuthorizer_whenMccFoodAccountDoesNotExist_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID))).thenReturn(Optional.empty());

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createSimpleAuthorizer_whenThereIsNotEnoughMoney_shouldReturnNoMoneyCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_MEAL));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_MEAL, Double.valueOf(10))));
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_MEAL, Double.valueOf(10)));

		AuthorizerRequest request = getRequest(Double.valueOf(100), MERCHANT, MCC_MEAL);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.NO_MONEY.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createSimpleAuthorizer_whenUpdateFails_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_CASH));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_MEAL, Double.valueOf(10))));
		doThrow(new OptimisticLockingFailureException("Error")).when(accountRepository).save(any());

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_CASH);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createSimpleAuthorizer_whenUpdateReturnsNull_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_FOOD, Double.valueOf(10))));
		when(accountRepository.save(any())).thenReturn(null);

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createFallbackAuthorizer_whenThereIsNotEnoughMoneyWithCashFallback_shouldReturnSuccessCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_FOOD, Double.valueOf(1000))));
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_FOOD, Double.valueOf(1000)));

		AuthorizerRequest request = getRequest(Double.valueOf(100), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createFallbackAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.SUCCESS.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createFallbackAuthorizer_whenAccountDoesNotExist_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID))).thenReturn(Optional.empty());

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createFallbackAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createFallbackAuthorizer_whenUpdateFails_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_CASH));
		when(accountRepository.findById(UUID.fromString(ACCOUNT_ID)))
				.thenReturn(Optional.of(getAccount(MCC_MEAL, Double.valueOf(10))));
		doThrow(new OptimisticLockingFailureException("Error")).when(accountRepository).save(any());

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_CASH);
		AuthorizerResponse response = authorizerService.createFallbackAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createMerchantAuthorizer_whenMerchantIsFoundByName_shouldReturnSuccessCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findByMerchantLike(MERCHANT))
				.thenReturn(Optional.of(getAccount(MCC_FOOD, Double.valueOf(10))));
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_FOOD, Double.valueOf(1000)));

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createMerchantAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.SUCCESS.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createMerchantAuthorizer_whenMerchantIsNotFoundByName_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findByMerchantLike(MERCHANT)).thenReturn(Optional.empty());
		when(accountRepository.save(any())).thenReturn(getAccount(MCC_FOOD, Double.valueOf(1000)));

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createMerchantAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	@Test
	void createMerchantAuthorizer_whenUpdateFails_shouldReturnFailCode() {

		when(transactionRepository.save(any())).thenReturn(getTransaction(MCC_FOOD));
		when(accountRepository.findByMerchantLike(MERCHANT))
				.thenReturn(Optional.of(getAccount(MCC_FOOD, Double.valueOf(10))));
		doThrow(new OptimisticLockingFailureException("Error")).when(accountRepository).save(any());

		AuthorizerRequest request = getRequest(Double.valueOf(1), MERCHANT, MCC_FOOD);
		AuthorizerResponse response = authorizerService.createMerchantAuthorizer(request);

		AuthorizerResponse expectedResponse = getResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
		assertEquals(expectedResponse.getCode(), response.getCode());
	}

	private Account getAccount(String mcc, Double cashValue) {
		return Account.builder().cashAmount(cashValue).foodAmount(Double.valueOf(10)).mealAmount(Double.valueOf(10))
				.merchant(MERCHANT).mcc(mcc).build();
	}

	private Transaction getTransaction(String mcc) {
		return Transaction.builder().accountId(UUID.fromString(ACCOUNT_ID)).amount(Double.valueOf(100))
				.merchant(MERCHANT).mcc(mcc).build();
	}

	private AuthorizerRequest getRequest(Double totalAmount, String merchant, String mcc) {
		return new AuthorizerRequest(UUID.fromString(ACCOUNT_ID), totalAmount, merchant, mcc);
	}

	private AuthorizerResponse getResponse(String code) {
		return new AuthorizerResponse(code);
	}
}

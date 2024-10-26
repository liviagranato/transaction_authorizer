package com.example.transaction.authorizer.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.authorizer.enums.TransactionCode;
import com.example.transaction.authorizer.model.Account;
import com.example.transaction.authorizer.model.Transaction;
import com.example.transaction.authorizer.repository.AccountRepository;
import com.example.transaction.authorizer.repository.TransactionRepository;
import com.example.transaction.authorizer.request.AuthorizerRequest;
import com.example.transaction.authorizer.response.AuthorizerResponse;
import com.example.transaction.authorizer.util.AuthorizerFunctions;

@Service
public class AuthorizerService extends AuthorizerFunctions {

	public final TransactionRepository transactionRepository;

	public AuthorizerService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		super(accountRepository);
		this.transactionRepository = transactionRepository;
	}

	@Transactional
	public AuthorizerResponse createSimpleAuthorizer(AuthorizerRequest authorizerRequest) {

		Transaction transaction = new Transaction().toTransaction(authorizerRequest);
		transactionRepository.save(transaction);

		Optional<Account> accountOptional = accountRepository.findById(authorizerRequest.getAccountId());
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			return finishTransactionAndGetCode(account, authorizerRequest, false, false);
		}

		return new AuthorizerResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
	}

	@Transactional
	public AuthorizerResponse createFallbackAuthorizer(AuthorizerRequest authorizerRequest) {

		Transaction transaction = new Transaction().toTransaction(authorizerRequest);
		transactionRepository.save(transaction);

		Optional<Account> accountOptional = accountRepository.findById(authorizerRequest.getAccountId());
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			return finishTransactionAndGetCode(account, authorizerRequest, true, false);
		}

		return new AuthorizerResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
	}

	@Transactional
	public AuthorizerResponse createMerchantAuthorizer(AuthorizerRequest authorizerRequest) {

		Transaction transaction = new Transaction().toTransaction(authorizerRequest);
		transactionRepository.save(transaction);

		String merchant = authorizerRequest.getMerchant();

		Optional<Account> accountOptional = accountRepository.findByMerchantNameLike(merchant);
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();

			transaction.setMcc(account.getMcc());
			transactionRepository.save(transaction);

			return finishTransactionAndGetCode(account, authorizerRequest, false, true);
		}

		return new AuthorizerResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
	}

	private AuthorizerResponse finishTransactionAndGetCode(Account account, AuthorizerRequest authorizerRequest,
			boolean hasFallback, boolean isMerchantTransaction) {

		String mcc = (isMerchantTransaction) ? account.getMcc() : authorizerRequest.getMcc();
		Double totalAmount = authorizerRequest.getTotalAmount();
		Map<Boolean, String> existsEnoughMoney = isEnoughMoneyByMcc(mcc, totalAmount, account, hasFallback);

		return getResponseCode(account, existsEnoughMoney, totalAmount, isMerchantTransaction);
	}
}

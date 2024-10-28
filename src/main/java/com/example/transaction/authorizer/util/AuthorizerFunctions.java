package com.example.transaction.authorizer.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.authorizer.enums.TransactionCode;
import com.example.transaction.authorizer.enums.TransactionType;
import com.example.transaction.authorizer.model.Account;
import com.example.transaction.authorizer.repository.AccountRepository;
import com.example.transaction.authorizer.response.AuthorizerResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorizerFunctions {

	public final AccountRepository accountRepository;

	public Map<Boolean, String> isEnoughMoneyByMcc(String mcc, Double totalAmount, Account account,
			boolean hasFallback) {
		Map<Boolean, String> isEnoughMoneyAndFoodType = new HashMap<>();
		boolean existsEnoughMoney = false;

		switch (mcc) {
		case "5411":
		case "5412":
			existsEnoughMoney = existsEnoughMoney(totalAmount, account.getFoodAmount());
			isEnoughMoneyAndFoodType.put(existsEnoughMoney, TransactionType.FOOD.name());
			break;

		case "5811":
		case "5812":
			existsEnoughMoney = existsEnoughMoney(totalAmount, account.getMealAmount());
			isEnoughMoneyAndFoodType.put(existsEnoughMoney, TransactionType.MEAL.name());
			break;

		default:
			existsEnoughMoney = existsEnoughMoney(totalAmount, account.getCashAmount());
			isEnoughMoneyAndFoodType.put(existsEnoughMoney, TransactionType.CASH.name());
			break;
		}

		if (hasFallback && isEnoughMoneyAndFoodType.containsKey(false)
				&& !isEnoughMoneyAndFoodType.get(false).equals(TransactionType.CASH.name())) {
			isEnoughMoneyAndFoodType = isEnoughMoneyByMcc("", totalAmount, account, false); // mcc will be cash and it won't be 'fallback' anymore
		}
		return isEnoughMoneyAndFoodType;
	}

	public AuthorizerResponse getResponseCode(Account account, Map<Boolean, String> isEnoughMoneyAndFoodType,
			Double totalAmount) throws Exception {

		if (!isEnoughMoneyAndFoodType.containsKey(true)) {
			return new AuthorizerResponse(TransactionCode.NO_MONEY.getCode());
		}

		String type = isEnoughMoneyAndFoodType.get(true);
		Account updatedAccount = updateAndSaveAccount(account, type, totalAmount);

		if (updatedAccount != null) {
			return new AuthorizerResponse(TransactionCode.SUCCESS.getCode());
		}
		return new AuthorizerResponse(TransactionCode.TRANSACTION_NOT_PROCESSED.getCode());
	}

	private Boolean existsEnoughMoney(Double totalAmount, Double amount) {
		Double finalAmount = getResult(totalAmount, amount);
		return finalAmount >= Double.valueOf(0);
	}

	private Double getResult(Double totalAmount, Double amount) {
		return amount - totalAmount;
	}

	@Transactional
	private Account updateAndSaveAccount(Account account, String type, Double totalAmount) throws Exception {

		switch (type) {
		case "MEAL":
			account.setMealAmount(getResult(totalAmount, account.getMealAmount()));
			break;
		case "FOOD":
			account.setFoodAmount(getResult(totalAmount, account.getFoodAmount()));
			break;
		case "CASH":
			account.setCashAmount(getResult(totalAmount, account.getCashAmount()));
			break;
		default:
			break;
		}

		return accountRepository.save(account);
	}
}

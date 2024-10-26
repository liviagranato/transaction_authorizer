package com.example.transaction.authorizer.enums;

public enum TransactionCode {

	SUCCESS("00"), NO_MONEY("51"), TRANSACTION_NOT_PROCESSED("07");

	private String code;

	TransactionCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

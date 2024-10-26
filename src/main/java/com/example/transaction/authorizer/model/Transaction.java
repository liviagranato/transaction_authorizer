package com.example.transaction.authorizer.model;

import java.util.UUID;

import com.example.transaction.authorizer.request.AuthorizerRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private UUID accountId;

	private Double amount;

	private String merchant;

	private String mcc;
	
	public Transaction toTransaction(AuthorizerRequest authorizerRequest) {
		return Transaction.builder()
				.accountId(authorizerRequest.getAccountId())
				.amount(authorizerRequest.getTotalAmount())
				.merchant(authorizerRequest.getMerchant())
				.mcc(authorizerRequest.getMcc())
				.build();
	}
}

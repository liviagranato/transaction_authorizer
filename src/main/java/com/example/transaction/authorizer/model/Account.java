package com.example.transaction.authorizer.model;

import java.util.UUID;

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
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private Double cashAmount;

	private Double foodAmount;

	private Double mealAmount;

	private String merchant;

	private String mcc;
}

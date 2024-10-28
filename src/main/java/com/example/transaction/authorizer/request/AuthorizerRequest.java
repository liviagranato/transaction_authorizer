package com.example.transaction.authorizer.request;

import java.util.UUID;

import com.example.transaction.authorizer.annotation.MccValidation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizerRequest {

	@NotNull
	@Schema(name = "accountId", description = "Unique account identifier", example = "d61033b7-75fe-4e19-8a96-2eaea05fffd7")
	private UUID accountId;

	@NotNull
	@Schema(name = "totalAmount", description = "Total amount available", example = "100.00")
	private Double totalAmount;

	@NotBlank
	@Schema(name = "merchant", description = "Emporium name", example = "PADARIA DO ZE")
	private String merchant;

	@NotBlank
	@Schema(name = "mcc", description = "4-digit identifier", example = "1234")
	@MccValidation
	private String mcc;

}

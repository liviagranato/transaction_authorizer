package com.example.transaction.authorizer.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorizerResponse {

	@Schema(name = "code", description = "", allowableValues = { "00", "51", "07" })
	private String code;

}

package com.example.transaction.authorizer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction.authorizer.request.AuthorizerRequest;
import com.example.transaction.authorizer.response.AuthorizerResponse;
import com.example.transaction.authorizer.service.AuthorizerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/authorizer")
@RequiredArgsConstructor
public class AuthorizerController {

	private final AuthorizerService authorizerService;

	@PostMapping("/")
	@Operation(summary = "Simple Authorizer", description = "The simple authorizer only uses mcc to map transaction.")
	@ApiResponse(responseCode = "200", description = "Success")
	public ResponseEntity<AuthorizerResponse> createSimpleAuthorizer(
			@RequestBody @Valid AuthorizerRequest authorizerRequest) {

		AuthorizerResponse response = authorizerService.createSimpleAuthorizer(authorizerRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/fallback")
	@Operation(summary = "Simple Authorizer with fallback", description = "The simple authorizer uses mcc to map transaction. If mcc cannot be mapped or card has insufficient credit the fallback is activated")
	@ApiResponse(responseCode = "200", description = "Success")
	public ResponseEntity<AuthorizerResponse> createFallbackAuthorizer(
			@RequestBody @Valid AuthorizerRequest authorizerRequest) {

		AuthorizerResponse response = authorizerService.createFallbackAuthorizer(authorizerRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/merchant")
	@Operation(summary = "Authorizer with merchant verification", description = "The merchant authorizer uses the merchant / store name to process balances")
	@ApiResponse(responseCode = "200", description = "Success")
	public ResponseEntity<AuthorizerResponse> createMerchantAuthorizer(
			@RequestBody @Valid AuthorizerRequest authorizerRequest) {

		AuthorizerResponse response = authorizerService.createMerchantAuthorizer(authorizerRequest);
		return ResponseEntity.ok(response);
	}
}

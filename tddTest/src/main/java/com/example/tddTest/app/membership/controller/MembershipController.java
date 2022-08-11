package com.example.tddTest.app.membership.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.tddTest.app.membership.service.MembershipConstants;
import com.example.tddTest.app.membership.service.MembershipRequest;
import com.example.tddTest.app.membership.service.MembershipResponse;
import com.example.tddTest.app.membership.service.MembershipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;
	
	@PostMapping("/api/v1/memberships")
	public ResponseEntity<MembershipResponse> addMembership(
			@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId
			, @RequestBody @Valid final MembershipRequest membershipRequest) {
		
		final MembershipResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(membershipResponse);
	}
}

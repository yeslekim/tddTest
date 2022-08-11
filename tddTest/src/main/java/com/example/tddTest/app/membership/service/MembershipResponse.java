package com.example.tddTest.app.membership.service;

import com.example.tddTest.app.enums.MembershipType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipResponse {
	
	private final Long id;
	private final MembershipType membershipType;
	
}

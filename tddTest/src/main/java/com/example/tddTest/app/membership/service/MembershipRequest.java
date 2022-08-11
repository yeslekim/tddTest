package com.example.tddTest.app.membership.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.example.tddTest.app.enums.MembershipType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

	@NotNull
	@Min(0)
	private final Integer point;
	
	@NotNull
	private final MembershipType membershipType;
}

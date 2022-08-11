package com.example.tddTest.exception;

import com.example.tddTest.app.enums.MembershipErrorResult;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipException extends RuntimeException{

	private final MembershipErrorResult errorResult;
}

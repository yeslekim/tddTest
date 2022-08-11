package com.example.tddTest.app.membership.service;

import org.springframework.stereotype.Service;

import com.example.tddTest.app.enums.MembershipErrorResult;
import com.example.tddTest.app.enums.MembershipType;
import com.example.tddTest.app.membership.entity.Membership;
import com.example.tddTest.app.membership.repository.MembershipRepository;
import com.example.tddTest.exception.MembershipException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

	private final MembershipRepository membershipRepository;
	
	public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
		
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }
	
        final Membership membership = Membership.builder()
        		.userId(userId)
        		.point(point)
        		.membershipType(membershipType)
        		.build();
        
        final Membership savedMembership = membershipRepository.save(membership);
        
		return MembershipResponse.builder()
				.id(savedMembership.getId())
				.membershipType(savedMembership.getMembershipType())
				.build();
	}
}

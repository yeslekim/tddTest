package com.example.tddTest.app.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tddTest.app.enums.MembershipType;
import com.example.tddTest.app.membership.entity.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long>{

    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

//    List<Membership> findAllByUserId(final String userId);
}

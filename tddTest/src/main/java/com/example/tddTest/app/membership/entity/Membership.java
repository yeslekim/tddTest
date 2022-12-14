package com.example.tddTest.app.membership.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.tddTest.app.enums.MembershipType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;
	
	@Column(nullable = false)
	private String userId;
	
	@Setter
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer point;
	
	@CreationTimestamp
	@Column(nullable = false, length = 20, updatable = false)
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	@Column(length = 20)
	private LocalDateTime updateAt;
}

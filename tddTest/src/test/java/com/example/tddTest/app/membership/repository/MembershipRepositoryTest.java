package com.example.tddTest.app.membership.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.tddTest.app.enums.MembershipType;
import com.example.tddTest.app.membership.entity.Membership;

/*
 * @DataJpaTest
 * - JPA repository들에 대한 빈을 등록하여 단위 테스트의 작성을 용이하게 함.
 * ---------------------------------------------------------------------------------------------
 * Repository 타입의 빈을 등록하기 위해서는 @Repository 어노테이션을 붙여주어야 한다. 
 * 하지만 JpaRepository 하위에 @Repository가 이미 붙어있으므로 @Repository를 붙여주지 않아도 된다.
 * 또한 테스트를 위해서는 테스트 컨텍스트를 잡아주어야 할텐데,
 * @DataJpaTest는 내부적으로 @ExtendWith( SpringExtension.class) 어노테이션을 가지고 있어서, 이 어노테이션만 붙여주면 된다.
 * 마찬가지로 @DataJpaTest에는 @Transactional 어노테이션이 있어서, 
 * 테스트의 롤백 등을 위해 별도로 트랜잭션 어노테이션을 추가하지 않아도 된다.
 * ---------------------------------------------------------------------------------------------
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)	// mysql에서 @DataJpaTest를 사용하기위한 어노테이션 (https://kangwoojin.github.io/programing/auto-configure-test-database/)
public class MembershipRepositoryTest {

	@Autowired
	private MembershipRepository membershipRepository;
	
	@Test
	public void 멤버십등록() {
		// given
		final Membership membership = Membership.builder()
				.userId("userId")
				.membershipType(MembershipType.NAVER)
				.point(10000)
				.build();
		
		// when
		final Membership result = membershipRepository.save(membership);
		
		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getUserId()).isEqualTo("userId");
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(result.getPoint()).isEqualTo(10000);
	}
	
	@Test
	public void 멤버십이존재하는지테스트() {
		// given
		final Membership membership = Membership.builder()
				.userId("userId")
				.membershipType(MembershipType.NAVER)
				.point(10000)
				.build();
		
		// when
		membershipRepository.save(membership);
		final Membership result = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);
		
		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getUserId()).isEqualTo("userId");
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(result.getPoint()).isEqualTo(10000);
	}
}

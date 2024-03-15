package kr.co.dglee.studyjpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.co.dglee.studyjpa.entity.Member;
import kr.co.dglee.studyjpa.entity.Team;
import kr.co.dglee.studyjpa.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestConfiguration("classpath:application-test.yml")
@SpringBootTest
class ProxyTest {

	@Autowired
	private EntityManager entityManager;

	@Test
	@DisplayName("지연로딩을 이용해 회원과 팀을 조회")
	@Rollback(false)	// 테스트 종료 후 롤백하지 않음 (테스트 DB이므로 실제 DB에 영향을 주지 않음)
	@Transactional
	void notUseProxy() {

		// 개발팀 저장
		Team devTeam = Team.builder().name("개발팀").build();
		entityManager.persist(devTeam);


		// 회원 저장 (개발팀에 속함)
		Member member = Member.builder()
							  .name("이동근")
							  .email("practice@gmail.com")
							  .age(32)
							  .role(Role.ADMIN)
							  .team(devTeam)
							  .build();
		entityManager.persist(member);

		// 영속성 컨텍스트 초기화
		entityManager.flush();
		entityManager.clear();

		// 즉시 로딩을 사용한다면 회원을 조회할 때 조인한 팀을 함께 조회
		// 지연 로딩을 사용한다면 회원을 조회할 때 팀을 조회하지 않고 팀을 조회하는 시점에 조회
		member = entityManager.find(Member.class, member.getId());
		Team team = member.getTeam();

		Assertions.assertNotNull(member);

		Assertions.assertNotNull(team);
		Assertions.assertEquals("개발팀", team.getName());
	}
}

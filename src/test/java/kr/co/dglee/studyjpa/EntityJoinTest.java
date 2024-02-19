package kr.co.dglee.studyjpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.co.dglee.studyjpa.entity.Member;
import kr.co.dglee.studyjpa.entity.Team;
import kr.co.dglee.studyjpa.enums.Role;
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
class EntityJoinTest {

	@Autowired
	private EntityManager entityManager;

	@Test
	@DisplayName("회원에게 팀을 추가한다.")
	@Rollback(false)	// 테스트 종료 후 롤백하지 않음 (테스트 DB이므로 실제 DB에 영향을 주지 않음)
	@Transactional
	void userTeamAddTest() {
		// 개발팀 저장
		Team devTeam = Team.builder().name("개발팀").build();
		entityManager.persist(devTeam);

		// 디자인팀 저장
		Team designTeam = Team.builder().name("디자인팀").build();
		entityManager.persist(designTeam);

		// 회원 저장 ( 개발팀에 속함 )
		Member member = Member.builder()
							  .name("이동근")
							  .email("practice@gmail.com")
							  .age(32)
							  .role(Role.ADMIN)
							  .team(devTeam)
							  .build();
		entityManager.persist(member);

		// 회원2 저장 ( 개발팀에 속함 )
		Member member2 = Member.builder()
							   .name("삼동근")
							   .email("practice2@gmail.com")
							   .age(29)
							   .role(Role.USER)
							   .team(devTeam)
							   .build();
		entityManager.persist(member2);

		// 모든 회원은 개발팀에 속해있어야 한다.
		assertEquals(member.getTeam().getId(), member2.getTeam().getId());
		assertEquals(member.getTeam().getName(), member2.getTeam().getName());
	}

	@Test
	@DisplayName("회원의 팀을 변경한다.")
	@Rollback(false)	// 테스트 종료 후 롤백하지 않음 (테스트 DB이므로 실제 DB에 영향을 주지 않음)
	@Transactional
	void changeUserTeamTest() {
		// 개발팀 저장
		Team devTeam = Team.builder().name("개발팀").build();
		entityManager.persist(devTeam);
		
		// 디자인팀 저장
		Team designTeam = Team.builder().name("디자인팀").build();
		entityManager.persist(designTeam);

		// 회원 저장 ( 개발팀에 속함 )
		Member member = Member.builder()
							  .name("이동근")
							  .email("practice@gmail.com")
							  .age(32)
							  .role(Role.ADMIN)
							  .team(devTeam)
							  .build();
		entityManager.persist(member);

		// 회원은 개발팀에 속해있다.
		assertEquals(member.getTeam().getId(), devTeam.getId());
		assertEquals(member.getTeam().getName(), devTeam.getName());

		// 회원의 팀을 디자인팀으로 변경한다.
		member.setTeam(designTeam);
		entityManager.persist(member);

		// 회원은 디자인팀에 속해있다.
		assertEquals(member.getTeam().getId(), designTeam.getId());
		assertEquals(member.getTeam().getName(), designTeam.getName());
	}

	@Test
	@DisplayName("회원이 속한 팀을 삭제한다.")
	@Rollback(false)	// 테스트 종료 후 롤백하지 않음 (테스트 DB이므로 실제 DB에 영향을 주지 않음)
	@Transactional
	void userTeamDeleteTest() {
		// 개발팀 저장
		Team devTeam = Team.builder().name("개발팀").build();
		entityManager.persist(devTeam);

		// 회원 저장 ( 개발팀에 속함 )
		Member member = Member.builder()
							  .name("이동근")
							  .email("practice@gmail.com")
							  .age(32)
							  .role(Role.ADMIN)
							  .team(devTeam)
							  .build();
		entityManager.persist(member);

		// 회원은 개발팀에 속해있다.
		assertEquals(member.getTeam().getId(), devTeam.getId());
		assertEquals(member.getTeam().getName(), devTeam.getName());

		// 회원이 속해있는 팀을 지운다.
		member.setTeam(null);
		entityManager.persist(member);

		// 회원은 팀에 속해있지 않다.
		assertNull(member.getTeam());
	}
}

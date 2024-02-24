package kr.co.dglee.studyjpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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
class TeamTest {

	@Autowired
	private EntityManager entityManager;

	@Test
	@DisplayName("팀에 속한 회원 조회 테스트")
	@Transactional
	@Rollback(false)
	void teamMemberSelectTest() {

		Team devTeam = Team.builder()
						   .name("개발팀")
						   .build();
		entityManager.persist(devTeam);

		Team designTeam = Team.builder()
							  .name("디자인팀")
							  .build();
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

		// 회원 저장 ( 개발팀에 속함 )
		Member member2 = Member.builder()
							   .name("삼동근")
							   .email("practice2@gmail.com")
							   .age(32)
							   .role(Role.USER)
							   .team(devTeam)
							   .build();
		entityManager.persist(member2);

		// 영속성 컨텍스트 초기화 (devTeam을 준영속 상태로 만들어야 DB에서 재조회가 가능)
		entityManager.flush();
		entityManager.clear();

		// 개발팀에 속한 회원이 2명인지 확인(이동근, 삼동근)
		devTeam = entityManager.find(Team.class, devTeam.getId());
		Assertions.assertEquals(2, devTeam.getMemberList().size());
		Assertions.assertEquals("이동근", devTeam.getMemberList().get(0).getName());
		Assertions.assertEquals("삼동근", devTeam.getMemberList().get(1).getName());

		// 디자인 팀은 회원이 없으므로 0명인지 확인
		designTeam = entityManager.find(Team.class, designTeam.getId());
		Assertions.assertEquals(0, designTeam.getMemberList().size());
	}

	@Test
	@DisplayName("주인 객체가 아닌 엔티티에 대한 연관관계 저장 테스트")
	@Transactional
	@Rollback(false)
	void teamMemberAddTest() {

		Team devTeam = Team.builder()
						   .name("개발팀")
						   .memberList(new ArrayList<>())
						   .build();
		entityManager.persist(devTeam);

		// 회원 저장 (개발팀에 속함)
		Member member = Member.builder()
							  .name("이동근")
							  .email("practice@gmail.com")
							  .age(32)
							  .role(Role.ADMIN)
							  .build();
		entityManager.persist(member);

		// 개발팀의 목록에 member를 추가한다. (주인 객체가 아닌 엔티티에 대한 연관관계 저장)
		devTeam.getMemberList().add(member);
		entityManager.persist(devTeam);

		// 영속성 컨텍스트 초기화 (준영속 상태로 만들어야 재조회가 가능)
		entityManager.flush();
		entityManager.clear();

		// 주인 객체가 아니므로 memberList에 추가한 회원이 저장되지 않아야한다.
		devTeam = entityManager.find(Team.class, devTeam.getId());
		Assertions.assertEquals(0, devTeam.getMemberList().size());
	}
}

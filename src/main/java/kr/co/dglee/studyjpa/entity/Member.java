package kr.co.dglee.studyjpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import kr.co.dglee.studyjpa.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	/**
	 * Primary Key로 사용되는 필드에는 @Id 애노테이션을 붙인다.
	 * 기본키는 자동으로 생성되게끔 하기 위해 @GeneratedValue 애노테이션을 사용한다.
	 */
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 10)
	private String name;

	/**
	 * 이메일은 중복될 수 없으며, null이 될 수 없다.
	 */
	@Column(unique = true, nullable = false, length = 100)
	private String email;

	/**
	 * 컬럼명이 같을 경우 이처럼 Column 애노테이션이 생략 가능하다.
	 */
	private int age;

	/**
	 * ENUM을 활용하여 Role을 매핑한다.
	 */
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * 사용자의 생성 일시를 담는다. 날짜 타입이므로 Temproal 어노테이션을 사용하여 매핑한다.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdDate;

	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	/**
	 * 이 필드는 Entity에 존재하지 않는 임시 필드이다. 즉, DB에 매핑되지 않는다.
	 */
	@Transient
	private String temp;
}

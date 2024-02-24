package kr.co.dglee.studyjpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(mappedBy = "team")
	private List<Member> memberList = new ArrayList<>();
}

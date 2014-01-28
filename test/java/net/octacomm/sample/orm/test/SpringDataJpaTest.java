package net.octacomm.sample.orm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.octacomm.sample.launcher.SpringDataJpaConfig;
import net.octacomm.sample.orm.data.TeamRepository;
import net.octacomm.sample.orm.model.QTeam;
import net.octacomm.sample.orm.model.Team;
import net.octacomm.sample.orm.service.TeamService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysema.query.jpa.impl.JPAQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataJpaConfig.class})
@ActiveProfiles({ "springDataJpa" })
public class SpringDataJpaTest {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void crudTest() {
		Team team = new Team();
		team.setId(1);
		team.setName("태영");
		team.setRating(100);
		teamService.addTeam(team);
		
		team.setId(1);
		team.setName("태영2");
		team.setRating(100);
		teamService.updateTeam(team);
		
		Team newTeam = teamService.getTeam(1);
		assertEquals(team.getId(), newTeam.getId());
		assertEquals(team.getName(), newTeam.getName());
		assertEquals(team.getRating(), newTeam.getRating());
		
		teamService.deleteTeam(1);
		
		newTeam = teamService.getTeam(1);
		assertNull(newTeam);
		
		team.setId(1);
		teamService.addTeam(team);		
		team.setId(2);
		teamService.addTeam(team);		
		team.setId(3);
		teamService.addTeam(team);	
		
		List<Team> teams = teamService.getTeams();
		assertEquals(teams.size(), 3);
	}

	/**
	 * Spring Data JPA 에서는 이미 존재하는 데이터는 수정 처리해버리므로,
	 * 키 중복 예외가 발생하지 않는다.
	 * 
	 */
	@Test
	public void duplcatekey() {
		Team team = new Team();
		
		team.setId(100);
		team.setName("test1");
		team.setRating(100);
		teamService.addTeam(team);		
		team.setId(100);
		team.setName("test2");
		team.setRating(100);
		teamService.addTeam(team);
		team.setId(300);
		team.setName("test2");
		team.setRating(100);
		teamService.updateTeam(team);
	}
	
	@Test
	public void customMethod() {
		teamRepository.deleteAll();
		Team team = new Team();

		team.setId(100);
		team.setName("test1");
		team.setRating(100);
		teamService.addTeam(team);
		
		team.setId(101);
		team.setName("test1");
		team.setRating(100);
		teamService.addTeam(team);
		
		team.setId(102);
		team.setName("test3");
		team.setRating(100);
		teamService.addTeam(team);

		System.err.println("findByName => " + teamRepository.findByName("test1"));
		System.err.println("findByNameOrRatingOrderByNameDesc => " + teamRepository.findByNameOrRatingOrderByNameDesc("test1", 100));
		System.err.println("findByNameOrderByNameDesc => " + teamRepository.findByNameOrderByNameDesc("test1"));
		System.err.println("findAll (Sort.DESC) => " + teamRepository.findAll(new Sort(Direction.DESC, "name")));

		Page<?> page = teamRepository.findAll(new PageRequest(0, 2));
		System.err.println("page => " + page + ", " + page.getContent());

		System.err.println("testNativeQuery => " + teamRepository.testNativeQuery());
		System.err.println("testQuery => " + teamRepository.testQuery("test"));

		System.err.println("querydsl => " + teamRepository.findAll(QTeam.team.rating.loe(100)));
	}
	
	@Test
	public void testQueryDSL() {
		Team dummyTeam = new Team();

		dummyTeam.setId(100);
		dummyTeam.setName("test1");
		dummyTeam.setRating(100);
		teamService.addTeam(dummyTeam);
		
		JPAQuery query = new JPAQuery(em);
		QTeam team = QTeam.team;
		System.err.println("querydsl => "
				+ query.from(team)
						.where(team.name.eq("test1").or(team.name.like("태영%")))
						.list(team));
		
	}
}

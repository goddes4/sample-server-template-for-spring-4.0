package net.octacomm.sample.orm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import net.octacomm.sample.launcher.HibernateConfig;
import net.octacomm.sample.orm.model.Team;
import net.octacomm.sample.orm.service.TeamService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class})
@ActiveProfiles({"hibernate"})
public class HibernateDaoTest {

	@Autowired
	private TeamService teamService;
	
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

	@Test(expected = DataIntegrityViolationException.class)
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
	}
}

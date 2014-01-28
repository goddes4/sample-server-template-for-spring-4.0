package net.octacomm.sample.orm.data;

import java.util.List;

import net.octacomm.sample.orm.model.Team;
import net.octacomm.sample.orm.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaTeamService implements TeamService {

	@Autowired
	private TeamRepository teamRepository;
	
	@Override
	public void addTeam(Team team) {
		teamRepository.save(team);
	}

	@Override
	public void updateTeam(Team team) {
		if (teamRepository.findOne(team.getId()) != null) {
			teamRepository.save(team);
		} else {
			System.out.println("Not found team : " + team);
		}
	}

	@Override
	public Team getTeam(int id) {
		return teamRepository.findOne(id);
	}

	@Override
	public void deleteTeam(int id) {
		teamRepository.delete(id);
	}

	@Override
	public List<Team> getTeams() {
		return teamRepository.findAll();
	}

}

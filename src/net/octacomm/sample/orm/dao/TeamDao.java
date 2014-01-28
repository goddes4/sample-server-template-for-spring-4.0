package net.octacomm.sample.orm.dao;

import java.util.List;

import net.octacomm.sample.orm.model.Team;

public interface TeamDao {
	
	void addTeam(Team team);
	
	void updateTeam(Team team);
	
	Team getTeam(int id);
	
	void deleteTeam(int id);
	
	List<Team> getTeams();
}

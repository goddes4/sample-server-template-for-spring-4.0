package net.octacomm.sample.orm.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.octacomm.sample.orm.dao.TeamDao;
import net.octacomm.sample.orm.model.Team;

import org.springframework.stereotype.Repository;

@Repository
public class JpaTeamDao implements TeamDao {

	@PersistenceContext(unitName = "testPersistenceUnit")
	private EntityManager entityManager;
	
	@Override
	public void addTeam(Team team) {
		entityManager.persist(team);
	}

	@Override
	public void updateTeam(Team team) {
		entityManager.merge(team);
	}

	@Override
	public Team getTeam(int id) {
		return entityManager.find(Team.class, id);
	}

	@Override
	public void deleteTeam(int id) {
		entityManager.remove(getTeam(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeams() {
		return entityManager.createQuery("from Team").getResultList();
	}

}

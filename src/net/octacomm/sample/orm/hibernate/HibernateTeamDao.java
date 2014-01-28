package net.octacomm.sample.orm.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.octacomm.sample.orm.dao.TeamDao;
import net.octacomm.sample.orm.model.Team;

@Repository
public class HibernateTeamDao implements TeamDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void addTeam(Team team) {
		getSession().save(team);
	}

	@Override
	public void updateTeam(Team team) {
		getSession().update(team);
	}

	@Override
	public Team getTeam(int id) {
		return (Team) getSession().get(Team.class, id);
	}

	@Override
	public void deleteTeam(int id) {
		getSession().delete(getTeam(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeams() {
		return getSession().createQuery("from Team").list();
	}

}

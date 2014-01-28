package net.octacomm.sample.orm.service;

import org.springframework.transaction.annotation.Transactional;

import net.octacomm.sample.orm.dao.TeamDao;

@Transactional
public interface TeamService extends TeamDao {

}

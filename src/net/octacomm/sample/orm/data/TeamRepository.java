package net.octacomm.sample.orm.data;

import java.util.List;

import net.octacomm.sample.orm.model.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Integer>, QueryDslPredicateExecutor<Team> {
	
	List<Team> findByName(String name);
	
	List<Team> findByNameOrRatingOrderByNameDesc(String name, int rating);

	List<Team> findByNameOrderByNameDesc(String name);

	@Query(value = "select * from teams t where t.name = 'test1'", nativeQuery = true)
	List<Team> testNativeQuery();
	
	@Query(value = "select t.id from #{#entityName} t where t.name like :name%")
	List<String> testQuery(@Param("name") String name);
	
}

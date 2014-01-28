package net.octacomm.sample.launcher;

import java.util.Properties;

import javax.sql.DataSource;

import net.octacomm.sample.orm.hibernate.HibernateTeamDao;
import net.octacomm.sample.orm.model.Team;
import net.octacomm.sample.orm.service.TeamServiceImpl;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("hibernate")
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { TeamServiceImpl.class, HibernateTeamDao.class })
@ImportResource("file:resources/hsqlJdbcScriptContext.xml")
public class HibernateConfig {
	
	@Bean
    public DataSource hsqlDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:file:resources/hsql/db");
		dataSource.setUsername("sa");
		dataSource.setPassword("1234");
		
		return dataSource;
    }
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(hsqlDataSource());
		factoryBean.setAnnotatedClasses(new Class<?>[] { Team.class }); 
//		factoryBean.setPackagesToScan(Team.class.getPackage().toString().substring(8));
		factoryBean.setHibernateProperties(hibernateProperties());
		
		return factoryBean;
	}

	private Properties hibernateProperties() {
		Properties props = new Properties();
		props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		props.put("hibernate.show_sql", "true");

		return null;
	}
	
	@Bean
	public PlatformTransactionManager hibTxManager() {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory().getObject());
		
		return txManager;
	}
	
}

package net.octacomm.sample.launcher;

import javax.sql.DataSource;

import net.octacomm.sample.db.mapper.UserMapper;
import net.octacomm.sample.domain.User;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@Profile("dev")
@MapperScan(basePackageClasses=UserMapper.class, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
@EnableCaching
@ImportResource("file:resources/jdbcScriptContext.xml")
public class MyBatisConfig {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}
		
	@Bean
	@Qualifier("miles")
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource(
			@Value("#{environment['jdbc.driverClass']}") String driverClassName,
			@Value("#{environment['jdbc.url']}") String url,
			@Value("#{environment['jdbc.username']}") String username,
			@Value("#{environment['jdbc.password']}") String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourcePatternResolver resolver) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(resourceLoader.getResource("file:resources/mybatis-config.xml"));
		factoryBean.setTypeAliasesPackage(User.class.getPackage().toString());
		factoryBean.setMapperLocations(resolver.getResources("file:resources/mapper/*.xml"));
		
		return factoryBean.getObject();
	}
}

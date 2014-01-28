<---------------------------------------- (1)MySQL 초기화 ---------------------------------------->
etc/sql/create.sql 파일을 이용해 DB 생성

ex) 
# cd etc/sql 
# mysql -u root -poctacomm -h 192.168.0.250 < create.sql

<---------------------------------------- (2) MyBatis 설정 ---------------------------------------->

1. Mapper Interface 생성
net.octacomm.sample.db.mapper.UserMapper.java

2. Mapper Xml 생성 (namespace 유의)
<mapper namespace="net.octacomm.sample.db.mapper.UserMapper">

3. applicationContext.xml 설정 유의

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="file:test/resources/mybatis-config.xml"/>
    <!-- VO 객체 패키지 변경  -->
    <property name="typeAliasesPackage" value="net.octacomm.sample.comm.vo"/>
    <property name="mapperLocations" value="file:resources/mapper/*.xml"/>
</bean>

<!-- VO 객체 패키지 변경  -->
<bean class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="net.octacomm.sample.db.mapper.UserMapper"/>
</bean>

<---------------------------------------- (3) JUnit 테스트 ---------------------------------------->
test/resources/bootstrap.properties 에서 DB 환경 설정
test/java 폴더에 있는 net.octacomm.sample.db.mapper.UserMapperTest를 실행한다.

<---------------------------------------- (4) logging 설정 ---------------------------------------->
resources/logback.xml 에서 프로젝트에서 사용할 logger 및 appender 추가

<---------------------------------------- (5) 서버 환경 설정 ---------------------------------------->
resources/bootstrap.properties 에서 서버 환경 설정

<---------------------------------------- (6) USN Handler 테스트 ---------------------------------------->
DTools 를 이용하여  테스트
01 01 01 05

<---------------------------------------- (7) GUI Handler 테스트 ---------------------------------------->
test/java 폴더에 있는 net.octacomm.sample.netty.gui.LoginTestClient 를 실행한다.
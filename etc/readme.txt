<---------------------------------------- (1)MySQL �ʱ�ȭ ---------------------------------------->
etc/sql/create.sql ������ �̿��� DB ����

ex) 
# cd etc/sql 
# mysql -u root -poctacomm -h 192.168.0.250 < create.sql

<---------------------------------------- (2) MyBatis ���� ---------------------------------------->

1. Mapper Interface ����
net.octacomm.sample.db.mapper.UserMapper.java

2. Mapper Xml ���� (namespace ����)
<mapper namespace="net.octacomm.sample.db.mapper.UserMapper">

3. applicationContext.xml ���� ����

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="file:test/resources/mybatis-config.xml"/>
    <!-- VO ��ü ��Ű�� ����  -->
    <property name="typeAliasesPackage" value="net.octacomm.sample.comm.vo"/>
    <property name="mapperLocations" value="file:resources/mapper/*.xml"/>
</bean>

<!-- VO ��ü ��Ű�� ����  -->
<bean class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="net.octacomm.sample.db.mapper.UserMapper"/>
</bean>

<---------------------------------------- (3) JUnit �׽�Ʈ ---------------------------------------->
test/resources/bootstrap.properties ���� DB ȯ�� ����
test/java ������ �ִ� net.octacomm.sample.db.mapper.UserMapperTest�� �����Ѵ�.

<---------------------------------------- (4) logging ���� ---------------------------------------->
resources/logback.xml ���� ������Ʈ���� ����� logger �� appender �߰�

<---------------------------------------- (5) ���� ȯ�� ���� ---------------------------------------->
resources/bootstrap.properties ���� ���� ȯ�� ����

<---------------------------------------- (6) USN Handler �׽�Ʈ ---------------------------------------->
DTools �� �̿��Ͽ�  �׽�Ʈ
01 01 01 05

<---------------------------------------- (7) GUI Handler �׽�Ʈ ---------------------------------------->
test/java ������ �ִ� net.octacomm.sample.netty.gui.LoginTestClient �� �����Ѵ�.
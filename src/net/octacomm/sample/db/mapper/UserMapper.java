package net.octacomm.sample.db.mapper;

import net.octacomm.sample.domain.User;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * TB_USER MyBatis Mapper
 * 
 * @author jhlee
 * @since 2012-7-4 
 */
@CacheNamespace
public interface UserMapper {
	
	@Select("SELECT * FROM tb_user WHERE id=#{id}")
	User getUser(String id);
	
	@Select("SELECT * FROM tb_user WHERE id=#{id} and password=#{password}")
	User getUserForAuth(@Param("id") String id, @Param("password") String password);
}

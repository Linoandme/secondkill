package com.lino.secondkill.dao;

import com.lino.secondkill.domain.SecondkillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface LoginDao {
    @Select("select * from secondkill_user where id =#{id}")
   public SecondkillUser getUserById(@Param("id") long id);



}

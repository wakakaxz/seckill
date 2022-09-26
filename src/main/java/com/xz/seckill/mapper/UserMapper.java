package com.xz.seckill.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.xz.seckill.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xz
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-09-26 22:57:59
* @Entity com.xz.seckill.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {
    User selectByUserId(@Param("userId") String userId);
}





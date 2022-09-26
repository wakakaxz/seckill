package com.xz.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id, 电话号代替
     */
    private String userId;

    /**
     * 密码 MD5(MD5(pass))
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 上次登陆时间
     */
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
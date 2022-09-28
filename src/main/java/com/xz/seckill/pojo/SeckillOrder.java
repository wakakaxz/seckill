package com.xz.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName seckill_order
 */
@TableName(value ="seckill_order")
@Data
public class SeckillOrder implements Serializable {
    /**
     * 秒杀订单 id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 订单 id
     */
    private Long orderId;

    /**
     * 商品 id
     */
    private Long goodsId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
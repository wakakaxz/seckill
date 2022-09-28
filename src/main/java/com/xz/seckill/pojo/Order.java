package com.xz.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName order
 */
@TableName(value ="`order`")
@Data
public class Order implements Serializable {
    /**
     * 订单 id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 商品 id
     */
    private Long goodsId;

    /**
     * 收货地址 id
     */
    private Long deliveryAddrId;

    /**
     * 冗余商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 下单渠道, 状态码表示 1 PC, 2 Android, 3 IOS
     */
    private Integer orderChannel;

    /**
     * 订单状态, 0 新建支付, 1 已支付, 2, 已发货, 3, 已收货, 4 已退款, 5 已完成
     */
    private Integer status;

    /**
     * 创建订单时间
     */
    private Date createDate;

    /**
     * 支付时间
     */
    private Date payDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
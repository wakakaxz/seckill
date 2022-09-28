package com.xz.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品返回对象
 * @author xz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo {
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品详情
     */
    private String goodsDetail;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品库存
     */
    private Integer goodsStock;

    /**
     * 上架日期
     */
    private Date createDate;

    /**
     * 修改日期
     */
    private Date modifiedDate;

    /**
     * 额外加的属性
     * 秒杀价
     * 秒杀数量
     * 开始日期
     * 结束日期
     * */
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}

package com.xz.seckill.service;

import com.xz.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author xz
* @description 针对表【goods】的数据库操作Service
* @createDate 2022-09-28 14:19:25
*/
public interface GoodsService extends IService<Goods> {

    /**
     *
     * @return 获取商品列表
     */
    List<GoodsVo> findGoodVo();

    /**
     * @param goodsId
     * @return 根据商品 Id 获取商品详情
     */
    GoodsVo findGoodVoByGoodsId(Long goodsId);
}

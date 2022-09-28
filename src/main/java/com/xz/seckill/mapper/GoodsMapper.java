package com.xz.seckill.mapper;

import com.xz.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xz.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author xz
* @description 针对表【goods】的数据库操作Mapper
* @createDate 2022-09-28 14:19:25
* @Entity com.xz.seckill.pojo.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodVo();

    GoodsVo findGoodVoByGoodsId(Long goodsId);
}





package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.pojo.Goods;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author xz
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2022-09-28 14:19:25
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}





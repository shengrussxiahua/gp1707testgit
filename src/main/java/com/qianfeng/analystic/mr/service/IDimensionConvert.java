package com.qianfeng.analystic.mr.service;

import com.qianfeng.analystic.model.dim.base.BaseDimension;

/**
 * 根据各个基础维度对象获取在数据库中对应的维度id
 */
public interface IDimensionConvert {
    int getDimensionByValue(BaseDimension dimension);
}

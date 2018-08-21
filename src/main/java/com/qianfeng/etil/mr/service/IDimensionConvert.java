package com.qianfeng.etil.mr.service;

import com.qianfeng.analystic.model.dim.base.BaseDimension;

/**
 * 根据各个基础维度对象获取在数据库中对应的维度id
 */
public class IDimensionConvert {
    /**
     * 根据维度获取id
     *
     * @param dimension
     * @return
     */
    int getDimensionByValue(BaseDimension dimension);


}

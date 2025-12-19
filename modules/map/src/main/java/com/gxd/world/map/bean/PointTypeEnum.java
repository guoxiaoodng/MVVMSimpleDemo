package com.gxd.world.map.bean;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gxd
 * @description:
 * @date :2021/6/25 15:31
 */
@IntDef({PointTypeEnum.LOCATING_POINT, PointTypeEnum.CLICK_POINT, PointTypeEnum.INPUT_POINT})
@Retention(RetentionPolicy.SOURCE)
public @interface PointTypeEnum {
    /**
     * 定位点
     */
    int LOCATING_POINT = 1;
    /**
     * 点击点
     */
    int CLICK_POINT = 2;
    /**
     * 外部传入点
     */
    int INPUT_POINT = 3;
}

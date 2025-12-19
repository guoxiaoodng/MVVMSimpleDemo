package com.gxd.world.map.bean;

import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;

/**
 * @author gxd
 * @description:
 * @date :2021/6/25 15:02
 */
public class MapShowPointsViewPointBean {


    public MapShowPointsViewPointBean(@PointTypeEnum int pointType, LatLng latLng) {
        this.pointType = pointType;
        this.latLng = latLng;
    }

    public MapShowPointsViewPointBean(@PointTypeEnum int pointType, LatLng latLng, String latLngAddress) {
        this.pointType = pointType;
        this.latLng = latLng;
        this.latLngAddress = latLngAddress;
    }

    @PointTypeEnum
    private int pointType;

    /**
     * 点位坐标
     */
    private LatLng latLng;
    /**
     * 点位坐标地址信息
     */
    private String latLngAddress;

    /**
     * 定点覆盖物 对象
     */
    private Overlay overlay;

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getLatLngAddress() {
        return latLngAddress;
    }

    public void setLatLngAddress(String latLngAddress) {
        this.latLngAddress = latLngAddress;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public void setOverlay(Overlay overlay) {
        this.overlay = overlay;
    }

}

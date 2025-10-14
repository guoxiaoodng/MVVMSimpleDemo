package com.dds.skywebrtc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 版权 ： gxd
 *
 * @author: gxd
 * @date: 2022/3/25
 * @time: 14:59
 * @description：
 */
public class WebRtcUserBean implements Parcelable {
    private String id;
    private String userName;
    private String token;
    private String headerUrl;
    private String appendData;

    public WebRtcUserBean(String token, String userId) {
        this.token = token;
        this.id = userId;
    }

    public WebRtcUserBean(String id, String userName, String headerUrl) {
        this.id = id;
        this.userName = userName;
        this.headerUrl = headerUrl;
    }

    public WebRtcUserBean(String id, String userName, String headerUrl, String appendData) {
        this.id = id;
        this.userName = userName;
        this.headerUrl = headerUrl;
        this.appendData = appendData;
    }

    protected WebRtcUserBean(Parcel in) {
        id = in.readString();
        userName = in.readString();
        headerUrl = in.readString();
        appendData = in.readString();
        token = in.readString();
    }

    public static final Creator<WebRtcUserBean> CREATOR = new Creator<WebRtcUserBean>() {
        @Override
        public WebRtcUserBean createFromParcel(Parcel in) {
            return new WebRtcUserBean(in);
        }

        @Override
        public WebRtcUserBean[] newArray(int size) {
            return new WebRtcUserBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getAppendData() {
        return appendData;
    }

    public void setAppendData(String appendData) {
        this.appendData = appendData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "WebRtcUserBean{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                ", appendData='" + appendData + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(headerUrl);
        dest.writeString(appendData);
        dest.writeString(token);
    }
}

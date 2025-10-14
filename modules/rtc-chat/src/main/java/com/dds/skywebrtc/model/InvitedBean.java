package com.dds.skywebrtc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InvitedBean implements Parcelable {
    private String id;
    private String inviterId;
    private String inviterName;
    private String roomId;
    private boolean needReply;

    public InvitedBean(Parcel in) {
        id = in.readString();
        inviterId = in.readString();
        inviterName = in.readString();
        roomId = in.readString();
        needReply = in.readByte() != 0;
    }

    public static final Creator<InvitedBean> CREATOR = new Creator<InvitedBean>() {
        @Override
        public InvitedBean createFromParcel(Parcel in) {
            return new InvitedBean(in);
        }

        @Override
        public InvitedBean[] newArray(int size) {
            return new InvitedBean[size];
        }
    };

    public InvitedBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isNeedReply() {
        return needReply;
    }

    public void setNeedReply(boolean needReply) {
        this.needReply = needReply;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(inviterId);
        dest.writeString(inviterName);
        dest.writeString(roomId);
        dest.writeByte((byte) (needReply ? 1 : 0));
    }
}

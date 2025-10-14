package com.dds.skywebrtc.rtccore.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import timber.log.Timber;

/**
 * Created by dds on 2019/7/26.
 * android_shuai@163.com
 */
public class MyWebSocket extends WebSocketClient {

    private final static String TAG = "dds_WebSocket";
    private boolean connectFlag = false;

    public MyWebSocket(URI serverUri) {
        super(serverUri);
    }
    public void setConnectFlag(boolean flag) {
        connectFlag = flag;
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        Timber.e("dds_error" + "onClose:" + reason + "remote:" + remote);
    }

    @Override
    public void onError(Exception ex) {
        Timber.e("dds_error" + "onError:" + ex.toString());
        connectFlag = false;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Timber.e("dds_info" + "onOpen");
        connectFlag = true;
    }

    @Override
    public void onMessage(String message) {
        Timber.d("%s%s", TAG, message);
    }
}

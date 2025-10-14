package com.dds.skywebrtc.rtccore.socket;

import android.os.Handler;
import android.os.Looper;

import com.dds.skywebrtc.model.WebRtcUserBean;

import java.net.URI;
import java.net.URISyntaxException;

import timber.log.Timber;

/**
 * Created by dds on 2019/7/26.
 * ddssignsong@163.com
 */
public class SocketManager {
    private final static String TAG = "dds_SocketManager";
    private MyWebSocket webSocket;
    private int userState;
    private String myId;
    private String userName;
    private WebRtcUserBean mUser;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private SocketManager() {

    }

    private static class Holder {
        private static final SocketManager socketManager = new SocketManager();
    }

    public static SocketManager getInstance() {
        return Holder.socketManager;
    }

    public void connect(String url, int device, WebRtcUserBean user) {
        try {
            if (webSocket == null || !webSocket.isOpen()) {
                URI uri;
                this.userName = user.getUserName();
                this.mUser = user;
                try {
                    String urls = url + "/" + user.getId() + "?token=" + user.getToken() + "&id=" + device;
                    Timber.i(TAG + "webRtc connect url " + urls);
                    uri = new URI(urls);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                webSocket = new MyWebSocket(uri);
                // 设置wss
                /*if (url.startsWith("wss")) {
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        if (sslContext != null) {
                            sslContext.init(null, new TrustManager[]{new MyWebSocket.TrustManagerTest()}, new SecureRandom());
                        }

                        SSLSocketFactory factory = null;
                        if (sslContext != null) {
                            factory = sslContext.getSocketFactory();
                        }

                        if (factory != null) {
                            webSocket.setSocketFactory(factory);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                // 开始connect
                webSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unConnect() {
        if (webSocket != null) {
            webSocket.setConnectFlag(false);
            webSocket.close();
            webSocket = null;
        }
    }
}

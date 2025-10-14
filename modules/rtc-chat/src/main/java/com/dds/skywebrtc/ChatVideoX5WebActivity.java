package com.dds.skywebrtc;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.dds.skywebrtc.model.InvitedBean;
import com.lxj.xpopup.XPopup;
import com.tecent.tecentx5.WebViewJavaScriptFunction;
import com.tecent.tecentx5.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;
import java.util.Random;

import timber.log.Timber;


public class ChatVideoX5WebActivity extends AppCompatActivity {

    private X5WebView webView;
    private static String mToken;
    private InvitedBean mBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());

        setContentView(R.layout.activity_chat_video_x5_web);

        String[] per = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        boolean hasPermission = PermissionUtils.isGranted(per);
        if (hasPermission) {
            initWeb();
        } else {
            new XPopup.Builder(ActivityUtils.getTopActivity())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .autoDismiss(true)
                    .hasNavigationBar(false)
                    .isDestroyOnDismiss(true)
                    .asConfirm("来电通知", "请允许录音和相机权限来通话",
                            "取消", "确定",
                            () -> {
                                PermissionUtils.permission(per).callback(new PermissionUtils.FullCallback() {
                                    @Override
                                    public void onGranted(@NonNull List<String> granted) {
                                        Timber.d("Permissions.request integer = " + granted);
                                    }

                                    @Override
                                    public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                                        Timber.d("Permissions.request integer = " + denied);
                                    }
                                }).request();
                            },
                            () -> {
                            }, false)
                    .show();
        }
    }

    public static void setToken(String token) {
        mToken = token;
    }

    private void initWeb() {
        webView = findViewById(R.id.web_view);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        webView.addJavascriptInterface(new WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {
            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
            }
        }, "AndroidFun");

        InvitedBean bean = getIntent().getParcelableExtra("data");
        mBean = bean;

        if (bean != null) {
            int clientId = new Random().nextInt();
            String userId = "qqq";
            String url = "https://ys.jsweique.com/videoTalk3/room.html?clientId=" + clientId + "&roomId=" + bean.getRoomId() + "&userId=" + userId + "&token=" + mToken + "&type=app";
            Timber.e(url);
            webView.loadUrl(url);
            webView.setWebViewClient(new X5WebViewClient());
        }

        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
    }

    static class X5WebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
            return super.shouldOverrideKeyEvent(webView, keyEvent);
        }

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SocketManager.getInstance().unConnect();
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroy();
        }
    }

    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        return flags;
    }
}

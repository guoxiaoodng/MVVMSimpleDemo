package com.tecent.tecentx5;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tecent.tecentx5.bean.MediaBean;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 媒体文件预览页面
 *
 * @author Administrator
 */
@Route(path = "/X5/MediaPreviewActivity")
public class MediaPreviewActivity extends AppCompatActivity {


    @Autowired(name = "GIFT_PARCELABLE")
    MediaBean bean;

    private X5WebView mX5WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            setContentView(R.layout.activity_media_preview);
            ARouter.getInstance().inject(this);
            findViewById(R.id.toolbar_back).setOnClickListener(v -> finish());
            TextView title = findViewById(R.id.title);
            title.setText(bean.getTitle());
            mX5WebView = findViewById(R.id.web);
            initX5Web();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mX5WebView != null && mX5WebView.canGoBack()) {
            mX5WebView.goBack();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mX5WebView != null) {
                mX5WebView.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        if (mX5WebView != null) {
            mX5WebView.onPause();
        }
        super.onPause();
    }

    private void initX5Web() {
        try {
            mX5WebView.clearCache(true);
            mX5WebView.clearHistory();
            mX5WebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            getWindow().setFormat(PixelFormat.TRANSPARENT);
            mX5WebView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            if (bean == null) {
                Toast.makeText(this, "访问地址为空", Toast.LENGTH_SHORT).show();
            }
            Log.e("MediaPreviewActivity", bean.getUrl());
            mX5WebView.loadUrl(bean.getUrl());
            mX5WebView.setWebViewClient(new X5WebViewClient());
            //js调用
            mX5WebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

                @Override
                public void onJsFunctionCalled(String tag) {
                    // TODO Auto-generated method stub

                }

                @JavascriptInterface
                public void onX5ButtonClicked() {
                    enableX5FullscreenFunc();
                }

                @JavascriptInterface
                public void onCustomButtonClicked() {
                    disableX5FullscreenFunc();
                }

                @JavascriptInterface
                public void onLiteWndButtonClicked() {
                    enableLiteWndFunc();
                }

                @JavascriptInterface
                public void onPageVideoClicked() {
                    enablePageVideoFunc();
                }
            }, "Android");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class X5WebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return super.shouldOverrideUrlLoading(webView, url);
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
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslErro) {
            sslErrorHandler.proceed();
        }
    }

    /**
     * 向webview发出信息
     */
    private void enableX5FullscreenFunc() {

        try {
            if (mX5WebView.getX5WebViewExtension() != null) {
                Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                // true表示标准全屏，false表示X5全屏；不设置默认false，
                data.putBoolean("standardFullScreen", false);
                // false：关闭小窗；true：开启小窗；不设置默认true，
                data.putBoolean("supportLiteWnd", false);
                // 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                data.putInt("DefaultVideoScreen", 2);

                mX5WebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                        data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableX5FullscreenFunc() {
        try {
            if (mX5WebView.getX5WebViewExtension() != null) {
                Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                // true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
                data.putBoolean("standardFullScreen", true);
                // false：关闭小窗；true：开启小窗；不设置默认true，
                data.putBoolean("supportLiteWnd", false);
                // 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                data.putInt("DefaultVideoScreen", 2);

                mX5WebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                        data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableLiteWndFunc() {
        try {
            if (mX5WebView.getX5WebViewExtension() != null) {
                Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                // true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
                data.putBoolean("standardFullScreen", false);
                // false：关闭小窗；true：开启小窗；不设置默认true，
                data.putBoolean("supportLiteWnd", true);
                // 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                data.putInt("DefaultVideoScreen", 2);

                mX5WebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                        data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enablePageVideoFunc() {
        try {
            if (mX5WebView.getX5WebViewExtension() != null) {
                Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                // true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
                data.putBoolean("standardFullScreen", false);
                // false：关闭小窗；true：开启小窗；不设置默认true，
                data.putBoolean("supportLiteWnd", false);
                // 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
                data.putInt("DefaultVideoScreen", 1);

                mX5WebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                        data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
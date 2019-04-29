package com.ceedlive.ceeday.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ceedlive.ceeday.BaseActivity;

public abstract class AbstractWebViewActivity extends BaseActivity {
    /**
     * WebView 조회
     * @return WebView
     */
    public abstract WebView getWebView();

    /**
     * should url loading
     */
    public abstract boolean shouldOverrideUrlLoading(WebView view, String url);

    /**
     * 페이지 탐색 시작
     */
    public abstract void onPageStarted();

    /**
     * 페이지 탐색 종료
     */
    public abstract void onPageFinished();

    /**
     * alert
     */
    public abstract boolean onJsAlert(WebView view, String url, String message, JsResult result);

    /**
     * showToast
     * @param message 메세지
     * @param length 노출시간 (long, short)
     */
    public abstract void showToast(String message, String length);

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);

        final WebView webView = getWebView();
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 롤리팝부터 Mixed Content 에러 막기 위함.
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.addJavascriptInterface(new WebInterface(), "WebView");

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    AbstractWebViewActivity.this.onPageStarted();
                    return AbstractWebViewActivity.this.shouldOverrideUrlLoading(view, url);
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
//                    WebViewActivity.this.onPageStarted();
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    AbstractWebViewActivity.this.onPageFinished();
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    if (AbstractWebViewActivity.this.onJsAlert(view, url, message, result)) {
                        return true;
                    }
                    return super.onJsAlert(view, url, message, result);
                }
            });

            webView.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    @Override
    public void onBackPressed() {
        if (getWebView().canGoBack()) {
            getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        getWebView().stopLoading();
        super.finish();
    }

    public class WebInterface {
        @JavascriptInterface
        public void back() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }

        @JavascriptInterface
        public void close() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void openExternalBrowser(final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void showToast(final String message, final String length) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AbstractWebViewActivity.this.showToast(message, length);
                }
            });
        }
    }
}

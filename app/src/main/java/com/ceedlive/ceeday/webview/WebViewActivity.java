package com.ceedlive.ceeday.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ceedlive.ceeday.R;
import com.ceedlive.ceeday.dialog.DialogBuilder;


public class WebViewActivity extends AbstractWebViewActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initialize();
    }

    @Override
    protected void initialize() {
        mWebView = findViewById(R.id.webview_content);
        mProgressBar = findViewById(R.id.progressBar);

        if (mWebView != null) {

            // setJavaScriptEnabled
            // Using setJavaScriptEnabled can introduce XSS vulnerabilities into your application, review carefully. less... (Ctrl+F1)
            // Inspection info:Your code should not invoke setJavaScriptEnabled if you are not sure that your app really requires JavaScript support.  Issue id: SetJavaScriptEnabled
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 롤리팝부터 Mixed Content 에러 막기 위함.
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            /**
             * reference: https://cofs.tistory.com/186
             */
            mWebView.setWebViewClient(new WebViewClient() {

                // 새로운 URL이 현재 WebView에 로드되려고 할 때 호스트 응용 프로그램에게 컨트롤을
                // 대신할 기회를 줍니다. WebViewClient가 제공되지 않으면, 기본적으로 WebView는 URL에
                // 대한 적절한 핸들러를 선택하려고 Activity Manager에게 물어봅니다. WebViewClient가
                // 제공되면, 호스트 응용 프로그램이 URL을 처리한다는 의미인 true를 반환거나 현재
                // WebView가 URL을 처리한다는 의미인 false를 반환합니다.
                @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebViewActivity.this.onPageStarted();
                    return WebViewActivity.this.shouldOverrideUrlLoading(view, url);
                }

                // 로딩이 시작될 때 WebView 에서 처음 한 번만 호출되는 메서드
                @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
//                    WebViewActivity.this.onPageStarted();
                }

                // 로딩이 완료됐을 때 한번 호출
                @Override public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    WebViewActivity.this.onPageFinished();
                }

                // 오류가 났을 경우, 호스트 응용 프로그램에게 오류를 보고합니다. 이러한 오류는 복구할 수 없습니다.
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);

                    switch (errorCode) {
                        case ERROR_AUTHENTICATION:
                            break;  // 서버에서 사용자 인증 실패
                        case ERROR_BAD_URL:
                            break;  // 잘못된 URL
                        case ERROR_CONNECT:
                            break;  // 서버로 연결 실패
                        case ERROR_FAILED_SSL_HANDSHAKE:
                            break;  // SSL handshake 수행 실패
                        case ERROR_FILE:
                            break;  // 일반 파일 오류
                        case ERROR_FILE_NOT_FOUND:
                            break;  // 파일을 찾을 수 없습니다
                        case ERROR_HOST_LOOKUP:
                            break;  // 서버 또는 프록시 호스트 이름 조회 실패
                        case ERROR_IO:
                            break;  // 서버에서 읽거나 서버로 쓰기 실패
                        case ERROR_PROXY_AUTHENTICATION:
                            break;  // 프록시에서 사용자 인증 실패
                        case ERROR_REDIRECT_LOOP:
                            break;  // 너무 많은 리디렉션
                        case ERROR_TIMEOUT:
                            break;  // 연결 시간 초과
                        case ERROR_TOO_MANY_REQUESTS:
                            break;  // 페이지 로드중 너무 많은 요청 발생
                        case ERROR_UNKNOWN:
                            break;  // 일반 오류
                        case ERROR_UNSUPPORTED_AUTH_SCHEME:
                            break;  // 지원되지 않는 인증 체계
                        case ERROR_UNSUPPORTED_SCHEME:
                            break;  // URI가 지원되지 않는 방식
                    }

                }

                // http 인증 요청이 있는 경우, 기본 동작은 요청 취소
                @Override
                public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                    super.onReceivedHttpAuthRequest(view, handler, host, realm);
                }

                // 확대나 크기 등의 변화가 있는 경우: WebView가 변화하기위해 scale이 적용된다고 알립니다.
                @Override
                public void onScaleChanged(WebView view, float oldScale, float newScale) {
                    super.onScaleChanged(view, oldScale, newScale);
                }

                // 잘못된 키 입력이 있는 경우
                // 호스트 응용 프로그램에게 동기적으로 키 이벤트를 처리할 기회를 줍니다.
                // 예: 메뉴 바로가기 키 이벤트를 이런식으로 필터링해야합니다.
                // true를 반환할 경우, WebView는 키 이벤트를 처리하지 않습니다.
                // false를 반환할 경우, WebView 항상 키 이벤트를 처리합니다.
                // 기본 동작은 false를 반환합니다.
                @Override
                public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                    return super.shouldOverrideKeyEvent(view, event);
                }

            });

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    if (WebViewActivity.this.onJsAlert(view, url, message, result)) {
                        return true;
                    }
                    return super.onJsAlert(view, url, message, result);
                }
            });

            mWebView.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    @Override
    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return onAlert(url, message, result);
    }

    @Override
    public void showToast(String message, String length) {

    }

    public boolean onAlert(String url, String message, final JsResult result) {
        new DialogBuilder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                .show();
        return true;
    }

    public static void loadPage(Context context, String url) {
        final Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}

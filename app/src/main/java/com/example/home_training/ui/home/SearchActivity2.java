package com.example.home_training.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.home_training.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        webView = findViewById(R.id.webview2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false); // 필요에 따라 설정
        webSettings.setAllowFileAccess(true);
        CheckPermission();
        // 웹페이지를 로드할 URL을 설정

        String url = getIntent().getStringExtra("URL_KEY");
        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        } else {
            // 기본 URL 로드 또는 오류 처리
        }

        // 필요한 경우 WebViewClient를 설정
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });
    }

    private void CheckPermission() {

        List<String> permissions = new ArrayList<>();

        permissions.add(android.Manifest.permission.CAMERA);
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> notGrantedList = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            int ret = ContextCompat.checkSelfPermission(this, permission);
            if (ret != PackageManager.PERMISSION_GRANTED) {
                notGrantedList.add(permission);
            }
        }
        if (!notGrantedList.isEmpty()) {
            Log.e("checkPermission", "checkPermission1");
            String[] permissionArr = notGrantedList.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissionArr, 1000);
        }

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.equals("https://homepushup.netlify.app/")) {
                    webView.loadUrl("javascript:stopWebcam();"); // 첫 번째 URL에 대한 함수 호출
                } else if (url.equals("https://homesquat.netlify.app/")) {
                    webView.loadUrl("javascript:stopWebcam();"); // 두 번째 URL에 대한 함수 호출
                } else if (url.equals("https://hometraise.netlify.app/")) {
                    webView.loadUrl("javascript:stopWebcam();"); // 세 번째 URL에 대한 함수 호출
                    // 추가적인 URL 및 함수 호출 조건을 여기에 추가할 수 있습니다.
                }

            }
        });
    }


    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data) {
            // 결과 값을 처리하는 로직
            Intent intent = new Intent();
            intent.putExtra("data", data);
            intent.putExtra("requestFrom", "address");
            setResult(RESULT_OK, intent);
            finish();
        }

        @JavascriptInterface
        public void processDATA2(String data) {
            // 다른 결과 값을 처리하는 로직
            Intent intent = new Intent();
            intent.putExtra("data", data); // 'data' 값을 추가
            intent.putExtra("requestFrom", "address1");
            setResult(RESULT_OK, intent);
            finish();
        }

        @JavascriptInterface
        public void processDATA3(String data) {
            // 다른 결과 값을 처리하는 로직
            Intent intent = new Intent();
            intent.putExtra("data", data); // 'data' 값을 추가
            intent.putExtra("requestFrom", "address2");
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}

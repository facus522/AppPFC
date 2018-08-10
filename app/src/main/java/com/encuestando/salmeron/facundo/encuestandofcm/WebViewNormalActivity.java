package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;

public class WebViewNormalActivity extends AppCompatActivity implements Serializable {

    private WebView webView;
    private String url;
    private ProgressDialog progDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_normal_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar_web_info_normal);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewNormalActivity.this.finish();
            }
        });

        url = (String) getIntent().getSerializableExtra("urlInfo");

        progDialog = ProgressDialog.show(WebViewNormalActivity.this, "Cargando Información","Espere por favor...", true);
        progDialog.setCancelable(true);

        webView = (WebView) findViewById(R.id.web_view_normal);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDialog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDialog.dismiss();
            }
        });


        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        } else{
            WebViewNormalActivity.this.finish();
        }
    }
}

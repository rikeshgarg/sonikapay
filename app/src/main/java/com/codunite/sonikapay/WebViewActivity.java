package com.codunite.sonikapay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asksira.webviewsuite.WebViewSuite;
import com.codunite.commonutility.PreferenceConnector;
import com.codunite.commonutility.ShowCustomToast;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{
    private WebViewSuite webViewSuite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);

        webViewSuite    = findViewById(R.id.webViewSuite);

        ProgressBar pDialog = new ProgressBar(this);

        webViewSuite.startLoading(PreferenceConnector.readString(this, PreferenceConnector.WEBURL, ""));
        webViewSuite.setCustomProgressBar(pDialog);

        webViewSuite.setOpenPDFCallback(() -> finish());

        webViewSuite.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Do your own stuffs. These will be executed after default onPageStarted().
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Do your own stuffs. These will be executed after default onPageFinished().
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("wa.me")) {
                    openWhatsApp();
                    onBackPressed();
                }else if(url.contains("actioncomplete.com")) {
                    ActivityTopupFund.isTranscationComplete = true;
                    onBackPressed();
                }else if (url.contains("upi://pay?")) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        finish();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "No Upi App", Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                }else {
                    view.loadUrl(url);
                }
                return false;
            }
        });

        loadToolBar();
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = findViewById(R.id.heading);
        txtHeading.setText(PreferenceConnector.readString(this, PreferenceConnector.WEBHEADING, ""));

        LinearLayout toolbar_txt_walletbal = findViewById(R.id.img_wallet);
        toolbar_txt_walletbal.setVisibility(View.INVISIBLE);

        LinearLayout toolbar_txt_ewalletbal = findViewById(R.id.img_ewallet);
        toolbar_txt_ewalletbal.setVisibility(View.INVISIBLE);
    }


    private void openWhatsApp() {
        String smsNumber = ""; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I want to know about your services.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(getPackageManager()) == null) {
            new ShowCustomToast(WebViewActivity.this).showToast("Whatsapp have not been installed.", WebViewActivity.this);
            return;
        }
        startActivity(sendIntent);
    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void onRefreshWebView() {
        webViewSuite.refresh();
    }

    private void LoadStaticData() {
//        webViewSuite.startLoadData(data, mimeType, encoding);
    }

    @Override
    public void onBackPressed() {
        if (!webViewSuite.goBackIfPossible()) super.onBackPressed();
    }
}

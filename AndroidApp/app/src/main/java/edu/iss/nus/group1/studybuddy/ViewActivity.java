package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {

    protected WebView webview_fullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        webview_fullscreen = findViewById(R.id.webview_fullscreen);

        // Receive intent
        Intent intent = getIntent();
        String htmlData = intent.getStringExtra("htmlData");
        if (htmlData != null && htmlData.length() > 10) {
            webview_fullscreen.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webview_fullscreen.getSettings().setBuiltInZoomControls(true);
            webview_fullscreen.loadData(htmlData, "text/html", null);

        }

    }
}
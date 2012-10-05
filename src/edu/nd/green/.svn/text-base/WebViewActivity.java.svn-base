package edu.nd.green;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class WebViewActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.webview);

		WebView webview = (WebView)this.findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		
		Intent i = this.getIntent();
		String url = i.getStringExtra("url");
		
		webview.loadUrl(url);
		
		
		
	}

}

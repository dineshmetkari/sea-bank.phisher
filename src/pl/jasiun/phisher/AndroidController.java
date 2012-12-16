package pl.jasiun.phisher;

import android.content.Intent;

public interface AndroidController {
	
	public void sendIntent(Intent intent);
	
	public void loadUrl(String url);
}

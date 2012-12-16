package pl.jasiun.phisher;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptInterface {
    Context context;

    /** Instantiate the interface and set the context */
    JavaScriptInterface(Context c) {
        context = c;
    }

    @JavascriptInterface
    public void save(String key, String value) {
    	SharedPreferences.Editor editor = getSharedPreferences().edit();
    	editor.putString(key, value);
    	editor.commit();
    }
    @JavascriptInterface
    public String load(String key) {
    	return getSharedPreferences().getString(key,"");
    }
    
    private SharedPreferences getSharedPreferences() {
    	SharedPreferences sharedPreferences = context.getSharedPreferences(
    			context.getString(R.string.preference_file), Context.MODE_PRIVATE);
    	return sharedPreferences;
    }
}

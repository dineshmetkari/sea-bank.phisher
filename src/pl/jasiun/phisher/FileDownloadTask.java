package pl.jasiun.phisher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.AsyncTask;

public abstract class FileDownloadTask extends AsyncTask<String, Void, byte[]> {

	@Override
	protected byte[] doInBackground(String... urls) {
		try {
			URL url = new URL(urls[0]);
			
	        URLConnection connection = url.openConnection();
	        connection.connect();
	        
	        int fileLength = connection.getContentLength();
	
	        InputStream input = new BufferedInputStream(url.openStream());
	        byte data[] = new byte[fileLength];
	        input.read(data);
	        
	        input.close();
        
	        return data;
	        
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	abstract protected void onPostExecute(byte[] result);

}


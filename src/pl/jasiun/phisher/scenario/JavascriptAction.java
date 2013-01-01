package pl.jasiun.phisher.scenario;

public class JavascriptAction extends Action {

	private final String javascript;
	private final Scenario scenario;

	public JavascriptAction(Scenario scenario, String javascript) {
		this.scenario = scenario;
		this.javascript = javascript;
	}

	@Override
	public void play() {
		scenario.getStageManager().loadUrl("javascript:"+javascript.replace("[[sms_code]]", getSmsCode()));
	}
}

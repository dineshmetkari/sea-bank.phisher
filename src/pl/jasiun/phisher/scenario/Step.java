package pl.jasiun.phisher.scenario;

import java.util.LinkedList;
import java.util.List;

public class Step {
	
	private Trigger trigger;
	private List<Action> actions;
	
	boolean wasPageLoaded;
	boolean wasSmsReceived;
	
	Scenario scenario;
	private String smsCode = "";
	
	public enum Trigger {
		PAGE_READY,
		SMS_RECIVED_AND_PAGE_READY
	}
	
	public Step(Scenario scenario, Trigger trigger) {
		this.scenario = scenario;
		this.trigger = trigger;
		
		this.actions = new LinkedList<Action>();
		
		this.wasPageLoaded = false;
		this.wasSmsReceived = false;
	}
	
	public void addAction(Action action) {
		action.setStep(this);
		actions.add(action);
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public void pageLoaded() {
		setWasPageLoaded(true);
		playIfReady();		
	}

	synchronized public void setWasPageLoaded(boolean wasPageLoaded) {
		this.wasPageLoaded = wasPageLoaded;
	}

	public void smsReceived(String smsCode) {
		this.smsCode = smsCode;
		setWasSmsReceived(true);
		playIfReady();
	}
	
	synchronized public void setWasSmsReceived(boolean wasSmsReceived) {
		this.wasSmsReceived = wasSmsReceived;
	}
	
	public void playIfReady() {
		if(isReadyToPlay()) {
			for (Action action : actions) {
				action.play();
			}
			scenario.moveToNextStep();
		}
			
	}

	synchronized private boolean isReadyToPlay() {
		if(scenario.isStated == false)
			return false;
		
		if(trigger == Trigger.PAGE_READY) {
			if(wasPageLoaded)
				return true;
			
		} else if(trigger == Trigger.SMS_RECIVED_AND_PAGE_READY) {
			if(wasPageLoaded && wasSmsReceived)
				return true;
		}
		
		return false;
	}

	public String getSmsCode() {
		return smsCode;
	}
}

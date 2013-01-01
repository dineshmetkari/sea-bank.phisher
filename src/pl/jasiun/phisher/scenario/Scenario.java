package pl.jasiun.phisher.scenario;

import java.util.LinkedList;
import java.util.List;


import pl.jasiun.phisher.StageManager;

public class Scenario {

	List<Step> steps;
	private int currentStepPossition;
	
	boolean isStated;
	private StageManager stageManager;
	
	public Scenario() {
		this.currentStepPossition = 0;
				
		this.steps = new LinkedList<Step>();
	}

	public void addStep(Step step) {
		steps.add(step);
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

	public StageManager getStageManager() {
		return stageManager;
	}

	public void setStageManager(StageManager stageManager) {
		this.stageManager = stageManager;
	}

}

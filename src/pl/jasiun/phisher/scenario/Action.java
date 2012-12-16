package pl.jasiun.phisher.scenario;

public abstract class Action {

	private Step step;

	public Step getStep() {
		return step;
	}
	
	public void setStep(Step step) {
		this.step = step;
	}
	
	public String getSmsCode() {
		return getStep().getSmsCode();
	}


	abstract public void play();

}

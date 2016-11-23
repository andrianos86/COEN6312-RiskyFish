package coen.project.riskyfish;

import java.util.Random;

public class RewardType {
	
	private int pointsToAward;
	private int lifesToAward;
	private boolean slowDownAward;
	
	protected RewardType(objType tokenType){
		pointsToAward = 0;
		lifesToAward = 0;
		slowDownAward = false;
		
		switch (tokenType) {
		case FOOD_TKN:
			this.generateLifes();
			break;
		case FEMALE_TKN:
			this.setSlowDownAward(true);
			break;
		case SHELL_TKN:
			this.generatePoints();
			break;
		default:
			pointsToAward = 0;
			lifesToAward = 0;
			slowDownAward = false;
			break;
		}
		
	}
	
	protected RewardType(int points, int lifes, boolean resetSpeed){
		this.setLifesToAward(lifes);
		this.setPointsToAward(points);
		this.setSlowDownAward(resetSpeed);
	}

	private void generatePoints() {
		Random gen = new Random();
		int extraPoints = gen.nextInt(50)+1;
		this.setPointsToAward(extraPoints);		
	}

	private void generateLifes() {
		Random gen = new Random();
		int extraLives = gen.nextInt(3)+1;
		this.setLifesToAward(extraLives);
	}

	public int getPointsToAward() {
		return pointsToAward;
	}

	public void setPointsToAward(int pointsToAward) {
		this.pointsToAward = pointsToAward;
	}

	public int getLifesToAward() {
		return lifesToAward;
	}

	public void setLifesToAward(int lifesToAward) {
		this.lifesToAward = lifesToAward;
	}

	public boolean slowDownAward() {
		return slowDownAward;
	}

	public void setSlowDownAward(boolean slowDownAward) {
		this.slowDownAward = slowDownAward;
	}
	
	public String toString(){
		return "Points: "+this.pointsToAward+", Lives: "+this.lifesToAward+", SlowDown: "+this.slowDownAward;
	}

}

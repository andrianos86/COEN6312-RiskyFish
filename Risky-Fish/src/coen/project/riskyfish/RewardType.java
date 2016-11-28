package coen.project.riskyfish;

import java.util.Random;

public class RewardType {
	
	private int pointsToAward;
	private int lifesToAward;
	private double decelerationFactor;
	
	protected RewardType(objType tokenType){
		pointsToAward = 0;
		lifesToAward = 0;
		decelerationFactor = 0.0;
		
		switch (tokenType) {
		case FOOD_TKN:
			this.generateLifes();
			break;
		case FEMALE_TKN:
			this.generateDecelerationFactor();
			break;
		case SHELL_TKN:
			this.generatePoints();
			break;
		default:
			pointsToAward = 0;
			lifesToAward = 0;
			decelerationFactor = 0.0;
			break;
		}
		
	}
	
	protected RewardType(int points, int lifes, double decelerateFactor){
		this.setLifesToAward(lifes);
		this.setPointsToAward(points);
		this.setDecelarationFactor(decelerateFactor);
	}

	private void generatePoints() {
		Random gen = new Random();
		int extraPoints = gen.nextInt(1000)+500;
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

	public double getDecelarationFactor() {
		return this.decelerationFactor;
	}

	public void setDecelarationFactor(double factor) {
		this.decelerationFactor = factor;
	}
	
	private void generateDecelerationFactor(){
		Random gen = new Random();
		double factor = gen.nextDouble();
		this.setDecelarationFactor(factor);
	}
	
	public String toString(){
		return "Points: "+this.pointsToAward+", Lives: "+this.lifesToAward+", SlowDown: "+this.decelerationFactor;
	}

}

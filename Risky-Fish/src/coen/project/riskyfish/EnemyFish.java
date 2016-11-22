package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class EnemyFish extends OnScreenObject {

	// identifier
	private objType enemyType;

	// sprite path
	protected String spritePath;

	// frames and properties of each player fish animation
	private BufferedImage[] spriteImages;


	// Damage it causes
	protected int damageCaused;

	protected double speed;

	public EnemyFish(World oceanPanel, objType type) {
		super(oceanPanel);
		this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);
		this.enemyType = type;
		this.setVelocityVector(oceanPanel.getSpeed(),oceanPanel.getSpeed());
		this.setSpriteImages(type);
		
		int animationDelay = 5;
		this.setAnimation(spriteImages, animationDelay);
		
		this.setMovingBounds();
		//this.maxY = this.getParent().getYmax()-55-100;
		//this.minY = this.getParent().getYmin()+100;

		this.setImmuneToOthers(true);
	}
	
	public void spawn(double minimumY, double maximumY, double minimumX, double maximumX){
		switch(enemyType){
		case PREDATOR:
			int offset = 150;
			this.minY = this.getParent().getYmin()+150;
			this.maxY = this.getParent().getYmax()-this.height-offset;
			super.spawn(minY,maxY ,maximumX, 3*maximumX);
			this.setPointsToAward(100);
			break;
		default:
			super.spawn(minimumY, maximumY, minimumX, 10*maximumX);
			this.setPointsToAward(50);
		}
	}

	/**
	 * Loads the token sprite images to be used for the animation, and sets the
	 * token's dimensions as per it's image.
	 */
	private void setSpriteImages(objType tokenType) {

		switch (tokenType) {
		case PREDATOR:
			this.spriteImages = SpriteContent.predator[0];
			this.setHeight(79);
			this.setWidth(200);
			this.setCHeight(79);
			this.setCWidth(200);
			break;
		case JELLYFISH:
			this.spriteImages = SpriteContent.jellyfish[0];
			this.setHeight(125);
			this.setWidth(89);
			this.setCHeight(125);
			this.setCWidth(89);
			
			break;
		default:
			System.out.println("Wrong object type enum for enemyFish");
			break;
		}
	}

	public void setVelocityVector(double dx, double dy) {
		if (this.enemyType == objType.PREDATOR) {
			super.setVelocityVector(ocean.getSpeed()-2.0, 2.0*ocean.getSpeed());
		} else if (this.enemyType == objType.JELLYFISH) {
			super.setVelocityVector(ocean.getSpeed(), 0.0);
		}
	}


	public void update() {
		System.out.println(ocean.getXmax() +","+ocean.getYmax() +","+ ocean.getXmin() +"," + ocean.getYmin() +",");
		if(this.enemyType == objType.PREDATOR){
			//System.out.println("(" + this.getX() + ", " + this.getY() + "), (" + this.getDx() + ", " + this.getDy() + ")"+" active: "+this.isActive()+" disc: "+this.isDiscarded()+" ,min/max: "+this.minY+", "+this.maxY);
			System.out.println("minY,maxY : ("+this.minY+", "+this.maxY+")");

		}
		if (this.isActive()){
		super.update();
		}
	}

	public void draw(Graphics g, boolean mbr) {
		if (this.isVisible() && this.isActive()) {
			super.draw(g, mbr);
		}
	}



}

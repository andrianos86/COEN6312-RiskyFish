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

	// points earned for avoiding enemy
	protected int pointsToAward;

	// Damage it causes
	protected int damageCaused;

	protected double speed;

	public EnemyFish(World oceanPanel, objType type) {
		super(oceanPanel);
		
		this.enemyType = type;
		this.setVelocityVector(oceanPanel.getSpeed() + 5, 5*oceanPanel.getSpeed());
		this.setSpriteImages(type);
		
		int animationDelay = 5;
		this.setAnimation(spriteImages, animationDelay);
		
		this.setMovingBounds();
		this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);

		this.setImmuneToOthers(true);
	}

	/**
	 * Loads the token sprite images to be used for the animation, and sets the
	 * token's dimensions as per it's image.
	 */
	private void setSpriteImages(objType tokenType) {

		switch (tokenType) {
		case PREDATOR:
			this.spriteImages = SpriteContent.token[0];
			this.setHeight(55);
			this.setWidth(55);
			this.setCHeight(55);
			this.setCWidth(55);
			this.minY = this.getParent().getYmin() + 100;
			this.maxY = this.getParent().getYmax() - 100;

			break;
		case JELLYFISH:
			this.spriteImages = SpriteContent.token[1];
			this.setHeight(55);
			this.setWidth(55);
			this.setCHeight(45);
			this.setCWidth(45);
			break;
		default:
			System.out.println("Wrong object type enum for enemyFish");
			break;
		}
	}

	public void setVelocityVector(double dx, double dy) {
		if (this.enemyType == objType.PREDATOR) {
			super.setVelocityVector(ocean.getSpeed()-1, 10*ocean.getSpeed());
		} else if (this.enemyType == objType.JELLYFISH) {
			super.setVelocityVector(ocean.getSpeed(), 0.0);
		}
	}


	public void update() {
		System.out.println(ocean.getXmax() +","+ocean.getYmax() +","+ ocean.getXmin() +"," + ocean.getYmin() +",");
		if(this.enemyType == objType.PREDATOR){
			System.out.println("(" + this.getX() + ", " + this.getY() + "), (" + this.getDx() + ", " + this.getDy() + ")"+" active: "+this.isActive()+" disc: "+this.isDiscarded()+" ,min/max: "+this.minY+", "+this.maxY);

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

	public int getPointsToAward() {
		return pointsToAward;
	}

	public void setPointsToAward(int pointsToAward) {
		this.pointsToAward = pointsToAward;
	}


}

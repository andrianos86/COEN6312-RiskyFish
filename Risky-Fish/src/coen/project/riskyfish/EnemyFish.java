package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class EnemyFish extends OnScreenObject {

	// Types of obstacles
	public static final int JELLYFISH = 0;
	public static final int PREDATOR = 1;

	// identifier
	private int enemyType;


	// sprite path
	protected String spritePath;

	// frames and properties of each player fish animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = { 5, 2 };
	private final int[] FRAMEWIDTHS = { 55, 55, };
	private final int[] FRAMEHEIGHTS = { 55, 55 };
	private final int[] SPRITEDELAYS = { 5, 5 };

	// points earned for avoiding enemy
	protected int pointsToAward;

	// Determine if obstacle will be displayed and interact with player
	public boolean active = true;
	boolean visible = false;

	// Damage it causes
	protected int damageCaused;

	protected double speed;

	public EnemyFish(World ocean, int type) {
		super(ocean);

		this.enemyType = type;
		this.setSpeed(getParent().getSpeed());

		init(enemyType);
	}

	private void setSpeed(double scrollingSpeed) {
		this.speed = scrollingSpeed;
		if(this.enemyType == EnemyFish.PREDATOR){
			Random speed_generator = new Random();
			this.speed+=speed_generator.nextInt(5)+1;
		}
	}

	private void init(int enemyType){
		if (enemyType == EnemyFish.JELLYFISH) {

		}
	}

	public void spawnRandomly() {
	}
		
	public void update(){

	}


	public void draw(Graphics g){
		if (this.isVisible()) {
			super.draw(g);

		}
	}

	public int getPointsToAward() {
		return pointsToAward;
	}

	public void setPointsToAward(int pointsToAward) {
		this.pointsToAward = pointsToAward;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getSpeed() {
		return speed;
	}


}

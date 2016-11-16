package coen.project.riskyfish.gamestate;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import coen.project.riskyfish.PlayerFish;
import coen.project.riskyfish.Token;
import coen.project.riskyfish.EnemyFish;
import coen.project.riskyfish.GamePanel;
import coen.project.riskyfish.Obstacle;
import coen.project.riskyfish.World;

public class GamePlayState extends GameState {

	private PlayerFish playerFish;
	private World ocean;

	private ArrayList<Obstacle> obstacles;
	private ArrayList<EnemyFish> enemies;
	private ArrayList<Token> tokens;

	// Holds the current score of the level
	private int levelScore;
	private long timeAlive;
	private long levelHighScore;


	// events
	private boolean blockUserInput = false;
	private int count = 0; // number of events or updates
	private boolean eventGameOver;
	private boolean eventStart;


	public GamePlayState(GameStateManager gsm) {
		super(gsm);
		ocean = new World(GamePanel.WIDTH, GamePanel.HEIGHT);
		ocean.setSpeed(-2.0);
		init();

	}

	@Override
	public void init() {
		levelScore = 0;

		// playerFish
		playerFish = new PlayerFish(ocean);
		playerFish.setPosition(60.0, 50.0);
		// playerFish.setTimer(.getTime);

		// obstacles
		obstacles = new ArrayList<>();
		populateObstacles();

		// Tokens
		tokens = new ArrayList<>();
		populateTokens();

		// enemies
		enemies = new ArrayList<>();
		populateEnemies();

		// hud
		// hud = new HeadsUpDisplay(playerFish);

		// start event
		eventStart = true;
		// startGame();

	}
	
	// handles starting/restart the game
	private void startGame(){
		
	}
	
	// handles a game over event
	private void endGame(){
		
	}

	private void populateTokens() {
		Token randomToken = new Token(ocean);
		tokens.add(randomToken);
	}

	private void populateObstacles() {
		Obstacle seaWeed = new Obstacle(ocean, Obstacle.SEEWEED);
		Obstacle nets = new Obstacle(ocean, Obstacle.NETS);
		obstacles.add(seaWeed);
		obstacles.add(nets);
	}

	private void populateEnemies() {
		// enemies.clear();
	}

	@Override
	public void update() {

		// check if playerFish dead event should start
		if (playerFish.getLives() <= 0 || playerFish.gety() > GamePanel.HEIGHT || (playerFish.gety() -playerFish.getHeight()) < 0) {
			eventGameOver = true;
			blockUserInput = true;
		}

		// playerFish events
		// if(eventStart) startGame();
		// if(eventGameOver) endGame();

		// Update ocean world
		if (ocean.getBackground() != null) {
			ocean.update();
		}

		// move playerFish
		playerFish.update();


		// move obstacles
		for (int i = 0; i < obstacles.size(); i++) {
			obstacles.get(i).update();

			if (obstacles.get(i).isPastWindowEdge()) {
				obstacles.get(i).spawnRandomly();
				int points = obstacles.get(i).getPoints();
				this.addToScore(points);
			}
		}

		// move tokens
		for(Token t:tokens){
			t.update();
			if(t.isPastWindowEdge()){
				t.getNextToken();
				t.spawnRandomly();
			}
			if (t.isVisible()) {
				if (playerFish.intersects(t)) {
					t.collect();
					System.out.println("yey!");
				}

			}
		}
		// check for collisions
		determinColisions();


	}

	private void determinColisions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {

		// draw background
		ocean.draw(g);

		// draw obstacles
		for (Obstacle o : obstacles) {
			o.draw(g);
		}
		// draw tokens
		for(Token t:tokens){
			t.draw(g);
		}

		// draw playerFish
		playerFish.draw(g);

		//draw HUD
		this.drawHUD(g);
		g.drawString(playerFish.getTimeToString(), 750, 480);

	}


	private void drawHUD(Graphics g) {
		
	    }

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

		if ((k == KeyEvent.VK_ESCAPE) && !gsm.isGamePaused()) {
			gsm.setPaused(true);
		}

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		if (k == KeyEvent.VK_SPACE) {
			playerFish.startSwimming();
		}

	}

	public void addToScore(int points) {
		levelScore += points;
	}

	public int getLevelScore(){
		return levelScore;
	}

	public PlayerFish getFish() {
		return playerFish;
	}



}

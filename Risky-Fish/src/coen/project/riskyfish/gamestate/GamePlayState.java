package coen.project.riskyfish.gamestate;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import coen.project.riskyfish.PlayerFish;
import coen.project.riskyfish.Token;
import coen.project.riskyfish.EnemyFish;
import coen.project.riskyfish.GamePanel;
import coen.project.riskyfish.Obstacle;
import coen.project.riskyfish.OnScreenObject;
import coen.project.riskyfish.World;
import coen.project.riskyfish.objType;

public class GamePlayState extends GameState {

	// Define instance variables for the game objects
	private PlayerFish playerFish;
	private ArrayList<OnScreenObject> opponents;
	// private ArrayList<Obstacle> obstacles;
	// private ArrayList<EnemyFish> enemies;
	// private ArrayList<Token> tokens;
	private World ocean;

	// Holds the current score of the level
	private int levelScore;
	private long timeAlive;
	private long levelHighScore;

	// Constructor to initialize the UI components and game objects
	public GamePlayState(GameStateManager gsm) {
		super(gsm);
		// Initialize the game objects
		init();

		// UI components

		// Other UI components such as button, score board, if any.
		// ......

	}

	// All the game related codes here

	// Initialize all the game objects, run only once in the constructor of the
	// main class.
	public void init() {
		levelScore = 0;
		timeAlive = 0;
		// levelHighScore = loadStatistics();

		ocean = new World(GamePanel.WIDTH, GamePanel.HEIGHT);
		ocean.setSpeed(-2.0);

		// playerFish
		playerFish = new PlayerFish(ocean);
		playerFish.setPosition(60.0, 50.0);

		// Opponents
		opponents = new ArrayList<>();
		initializeOponents();

		// hud
		// hud = new HeadsUpDisplay(playerFish);

		// startGame();

	}

	// handles starting/restart the game
	private void startGame() {

	}

	// handles a game over event
	private void endGame() {

	}

	private void initializeOponents() {
		opponents.add(new Token(ocean, objType.FOOD_TKN));
		opponents.add(new Token(ocean, objType.FEMALE_TKN));
		opponents.add(new Token(ocean, objType.SHELL_TKN));
		opponents.add(new Obstacle(ocean, objType.NETS));
		opponents.add(new Obstacle(ocean, objType.SEAWEED));
		opponents.add(new EnemyFish(ocean, objType.PREDATOR));
		opponents.add(new EnemyFish(ocean, objType.JELLYFISH));

		// Position opponents randomly past the right edge of the screen
		for (OnScreenObject op : opponents) {
			op.spawn(ocean.getYmin(), ocean.getYmax(), ocean.getXmin(),ocean.getXmax());
		}

		// .Opponents are ready to be updated
		for (OnScreenObject op : opponents) {
			op.setActive(true);
		}

	}
	/*
	 * private void populateTokens() { if (tokens.isEmpty()) { Token foodTkn =
	 * new Token(ocean, objType.FOOD_TKN); Token femaleTkn = new Token(ocean,
	 * objType.FEMALE_TKN); Token shellTkn = new Token(ocean,
	 * objType.SHELL_TKN); tokens.add(foodTkn); tokens.add(femaleTkn);
	 * tokens.add(shellTkn); } else { for (Token t : tokens) { t.update(); if
	 * (t.isCollected() || t.isDiscarded()) { t = null; } }
	 * tokens.removeAll(Collections.singleton(null));
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private void populateObstacles() { if (obstacles.isEmpty()) { Obstacle
	 * seaWeed = new Obstacle(ocean, objType.SEAWEED); Obstacle nets = new
	 * Obstacle(ocean, objType.NETS); obstacles.clear(); obstacles.add(seaWeed);
	 * obstacles.add(nets); } else { for (Obstacle o : obstacles) { o.update();
	 * if (o.isDiscarded()) { o = null; } }
	 * obstacles.removeAll(Collections.singleton(null)); } }
	 * 
	 * private void populateEnemies() { if (enemies.isEmpty()) { EnemyFish
	 * predator = new EnemyFish(ocean, objType.PREDATOR); EnemyFish jellyFish =
	 * new EnemyFish(ocean, objType.JELLYFISH); enemies.clear();
	 * enemies.add(predator); enemies.add(jellyFish); } else { for (EnemyFish e
	 * : enemies) { e.update(); if (e.isDiscarded()) { e = null; } }
	 * enemies.removeAll(Collections.singleton(null)); } }
	 */

	@Override
	public void update() {

		// check if playerFish dead event should start
		if (playerFish.isDiscarded()) {
			// eventGameOver = true;
			// blockUserInput = true;
		}

		// Update ocean world
		ocean.update();
		// move playerFish
		playerFish.update();

		// move opponents
		for (OnScreenObject op : opponents) {
			if (op.isActive()) {
				op.update();
				if(op.isDiscarded()){
					playerFish.increaseScore(op.getPointsToAward());
				}
			}
		}
		
		// remove discarded opponents and give points 
		
		// regenerate discarded opponents
		/*
		 * // move obstacles for (int i = 0; i < obstacles.size(); i++) {
		 * obstacles.get(i).update();
		 * 
		 * if (obstacles.get(i).isDiscarded()) { int points =
		 * obstacles.get(i).getPoints(); this.addToScore(points);
		 * obstacles.set(i, null); } }
		 * 
		 * // move tokens for (Token t : tokens) { t.update(); if
		 * (t.isDiscarded()) { t = null; } if (t.isVisible()) { if
		 * (playerFish.intersects(t)) { t.collectToken();
		 * System.out.println("yey!"); }
		 * 
		 * } }
		 * 
		 */
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

		// draw playerFish
		playerFish.draw(g, true);

		// draw obstacles
		for (OnScreenObject op : opponents) {
			//System.out.println("(" + op.getX() + ", " + op.getY() + "), (" + op.getDx() + ", " + op.getDy() + ")");

			op.draw(g, true);
		}

		// draw HUD
		this.drawHUD(g);

	}

	private void drawHUD(Graphics g) {
		g.drawString("Time: " + playerFish.getTimeToString() + "  Score: " + playerFish.getScore(), 650, 580);

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

	public int getLevelScore() {
		return levelScore;
	}

	public PlayerFish getFish() {
		return playerFish;
	}

}

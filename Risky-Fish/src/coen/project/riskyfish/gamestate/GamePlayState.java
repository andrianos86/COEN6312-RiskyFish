package coen.project.riskyfish.gamestate;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

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
	private ArrayList<OnScreenObject> toAdd;
	private ArrayList<OnScreenObject> toRemove;

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

		ocean = new World(0, GamePanel.WIDTH, 50, GamePanel.HEIGHT);
		ocean.setSpeed(-2.0);

		// playerFish
		playerFish = new PlayerFish(ocean);
		playerFish.setPosition(60.0, 50.0);

		// Holds all opponents spawned in the game
		opponents = new ArrayList<>();
		// Holds a temporary copy of opponents to add to the game
		toAdd = new ArrayList<>();
		// Holds a temporary copy of discarded or collected objects to remove
		// from the game
		toRemove = new ArrayList<>();
		initializeOpponents();

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

	private void initializeOpponents() {
		opponents.clear();
		toAdd.clear();

		addObstacle();
		addToken();
		addEnemy();

		opponents.addAll(toAdd);
		toAdd.clear();

	}

	private void regenerateOpponent(OnScreenObject obj) {
		if (obj.isDiscarded()) {
			if (obj instanceof Token) {
				addToken();
			} else if (obj instanceof EnemyFish) {
				addEnemy();
			} else if (obj instanceof Obstacle) {
				addObstacle();
			}
		}
	}

	private void addObstacle() {
		Random random = new Random();
		OnScreenObject opponentToAdd;

		int randomChoice = random.nextInt(2);
		System.out.println(randomChoice);

		if (randomChoice == 0) { // randomChoice=0 : nets

			opponentToAdd = new Obstacle(ocean, objType.NETS);
			placeWithNoOverlap(opponentToAdd);

		} else if (randomChoice == 1) { // randomChoice =1 : seaweed
			opponentToAdd = new Obstacle(ocean, objType.SEAWEED);
			placeWithNoOverlap(opponentToAdd);

		} else { // randomChoice = 2: seaweed + nets in same screen
			opponentToAdd = new Obstacle(ocean, objType.NETS);
			placeWithNoOverlap(opponentToAdd);
			opponentToAdd = new Obstacle(ocean, objType.SEAWEED);
			placeWithNoOverlap(opponentToAdd);
		}

	}

	private void addToken() {
		Random random = new Random();
		OnScreenObject tokenToAdd;

		int randomChoice = random.nextInt(3);

		if (randomChoice == 0) { // randomChoice=0 : food token

			tokenToAdd = new Token(ocean, objType.FOOD_TKN);

		} else if (randomChoice == 1) { // randomChoice =1 : female token
			tokenToAdd = new Token(ocean, objType.FEMALE_TKN);
		} else { // randomChoice = 2 : shell token
			tokenToAdd = new Token(ocean, objType.SHELL_TKN);
		}

		placeWithNoOverlap(tokenToAdd);
	}

	private void addEnemy() {
		Random random = new Random();
		OnScreenObject enemyToAdd;

		int randomChoice = random.nextInt(2);

		if (randomChoice == 0) { // randomChoice=0 : predator enemy
			enemyToAdd = new EnemyFish(ocean, objType.PREDATOR);

		} else { // randomChoice =1 : female token
			enemyToAdd = new EnemyFish(ocean, objType.JELLYFISH);
		}

		placeWithNoOverlap(enemyToAdd);
	}

	private void placeWithNoOverlap(OnScreenObject opponent) {
		boolean intersects = true;
		while (intersects) {
			intersects = false;
			// Position opponents randomly past the right edge of the screen
			opponent.spawn(ocean.getYmin(), ocean.getYmax(), ocean.getXmin(), ocean.getXmax());
			System.out.println("min/max: " + ocean.getXmin() + ", " + ocean.getXmax());
			for (OnScreenObject onsb : opponents) {
				if (opponent.intersects(onsb)) {
					intersects = true;
					// opponent.spawn(ocean.getYmin(), ocean.getYmax(),
					// ocean.getXmin(), ocean.getXmax());
				}
			}
		}
		// Opponent does not intersect and ready to be updated/drawn
		opponent.setActive(true);
		toAdd.add(opponent);
	}

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
		// clear temp Array toAdd and toRemove;
		// toAdd.clear();
		// toRemove.clear();
		// move opponents and give points for each one avoided
		for (OnScreenObject currOpponent : opponents) {
			if (currOpponent.isActive()) {
				currOpponent.update();
				if (currOpponent.isDiscarded()) {
					// give points for each discarded/avoided opponents
					playerFish.increaseScore(currOpponent.getPointsToAward());
					regenerateOpponent(currOpponent);
					toRemove.add(currOpponent);
				}
			}
		}

		// check for collisions
		processCollision();
		// clear discarded opponents and add new ones
		opponents.removeAll(toRemove);
		opponents.addAll(toAdd);
		toAdd.clear();
		toRemove.clear();
	}

	private void processCollision() {
		for (OnScreenObject obj : opponents) {
			// only detect Collisions if object in players vicinity
			if (obj.getX() <= playerFish.getX() + playerFish.getWidth()) {
				// collision occurred
				if (playerFish.intersects(obj)) {
					if (obj instanceof Token) {
						playerFish.collectReward(((Token) obj).collectToken());
						regenerateOpponent(obj);
						toRemove.add(obj);
					}
					if (obj instanceof Obstacle) {
						System.out.println("obstacle");
					}
					if (obj instanceof EnemyFish) {
						System.out.println("enemy");
					}
				}
				// check if player collects token

				// check if player hits obstacle

				// check if player hits enemy
			}

		}

	}

	@Override
	public void draw(Graphics g) {

		// draw ocean world
		ocean.draw(g);

		// draw playerFish
		playerFish.draw(g, true);

		// draw obstacles
		for (OnScreenObject op : opponents) {
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

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

  private World ocean;

  // Define instance variables for the game objects
  private PlayerFish playerFish;
  private ArrayList<OnScreenObject> opponents;
  private ArrayList<OnScreenObject> toAdd;
  private ArrayList<OnScreenObject> toRemove;

  // Holds the current score of the level
  private int levelScore;
  private long levelHighScore;
  private boolean gameOver;


  // Constructor to initialize the UI components and game objects
  public GamePlayState(GameStateManager gsm) {
    super(gsm);

    // initialization codes. Run only once.
    init();

    // UI components such as HUD

  }

  // All the game related codes here

  // Initialize all the game objects, run only once in the constructor of the
  // main class.
  public void init() {
    levelScore = 0;
    gameOver = false;
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
    this.playerFish.setActive(false);
    for(OnScreenObject ob: opponents){
      ob.setActive(false);
    }
    opponents.clear();
    //Disable pause functionality and 
    //switch to GameOver state
    gsm.gameOver();
    this.gsm.setState(GameStateManager.GAMEOVERSTATE);

  }

  private void initializeOpponents() {
    opponents.clear();
    toAdd.clear();

    addRandomObstacle();
    addRandomToken();
    addRandomEnemy();


    opponents.addAll(toAdd);
    toAdd.clear();

  }

  private void regenerateOpponent(OnScreenObject obj) {
    if (obj.isDiscarded()) {
      if (obj instanceof Token) {
        addRandomToken();
      } else if (obj instanceof EnemyFish) {
        addRandomEnemy();
      } else if (obj instanceof Obstacle) {
        addRandomObstacle();
      }
    }
  }

  private void addRandomObstacle() {
    Random random = new Random();
    OnScreenObject opponentToAdd;

    int randomChoice = random.nextInt(2);

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

  private void addRandomToken() {
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

  private void addRandomEnemy() {
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
      for (OnScreenObject onsb : opponents) {
        if (opponent.intersects(onsb)) {
          intersects = true;
        }
      }
    }
    // Opponent does not intersect and ready to be updated/drawn
    opponent.setActive(true);
    toAdd.add(opponent);
  }

  @Override
  public void update() {
    if(playerFish.isDead()){
      this.levelScore = this.playerFish.getScore();
      gameOver = true;
      endGame();
    }
      // Update ocean world
      ocean.update();
      // move playerFish
      playerFish.update();

      // move opponents and give points for each one avoided
      for (OnScreenObject currOpponent : opponents) {
        // update all relevant opponents
        if (currOpponent.isActive()) {
          currOpponent.update();
          playerFish.playerCollision(currOpponent);
        }
          if (currOpponent.isDiscarded()) {
            // Increase level score        
            playerFish.increaseScore(currOpponent.getPointsToAward());
            // Once an opponent is offscreen or has been collected we
            // add a new one of the same type in the game and remove the
            // old one from the game
            regenerateOpponent(currOpponent);
            toRemove.add(currOpponent);
          }
        
      }


      // clear discarded opponents and add new ones
      opponents.removeAll(toRemove);
      opponents.addAll(toAdd);
      toAdd.clear();
      toRemove.clear();

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
   /* g.drawString("Score: " + user.getLastScore() + ", Lives: " + user.getLives()+"\r\n"+
    "High Score: " + user.getHighScore() + " ("user.getName()+")");
 */       
    g.drawString("Time: " + playerFish.getTimeToString() + ",  Score: " + levelScore
        + ", Lives: " + playerFish.getLives(), 20, 20);
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


}

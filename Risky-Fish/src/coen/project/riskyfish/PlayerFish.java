package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class PlayerFish extends OnScreenObject {

  /**
   * Defines the absolute minimum vertical velocity a fish can travel with.
   */
  private final double MIN_DY = -8.0;
  /**
   * Defines the absolute maximum vertical velocity a fish can travel with.
   */
  private double MAX_DY = 10;
  /**
   * Defines the absolute minimum horizontal velocity a fish can travel with.
   */
  private final double MIN_DX;
  /**
   * Defines the absolute maximum horizontal velocity a fish can travel with.
   */
  private final double MAX_DX;

  /**
   * Default grace period (minimum time between successive collisions) is 2 seconds;
   */
  private static final int GRACEPERIOD = 120;
  /**
   * Default time fish remains poisoned once colliding with a poisonous seaweed is 10 seconds.
   */
  private static final int POISONDURATION = 600;

  // Gravity factor depending on fish type
  private float upThrust;

  // fish properties
  private int lives;
  private int score;
  private int distanceTravelled;

  // Player events
  // player is swimmingUp
  private boolean isSwimmingUp;
  // collecting food-token
  private boolean isEating;
  // collecting female-fish-token
  private boolean isSlowingDown;
  // Lost final life
  private boolean isDead;
  // player is poisoned from seaweed
  private boolean isPoisoned;
  // player lost a life.
  private boolean isHurt;
  /**
   * Determines the grace period between successive collisions (where life is lost).
   * 
   */
  private long hurtTimer;

  /**
   * Determines how long the playerFish remains poisoned for. By default 10seconds.
   */
  private long poisonedTimer = 600;
  /**
   * Keeps track of how many seconds elapsed since the beginning of the game.
   */
  private long time;

  // how far up fish moves with each user input
  private double swimForce;


  // Animation frames each player fish action
  // private BufferedImage[] sprites;

  // animated actions
  private static final int IDLE = 0;
  private static final int POISONED = 1;
  private static final int DEAD = 2;

  public PlayerFish(World ocean, objType type) {
    super(ocean, type);
    this.MIN_DX = ocean.getSpeed();
    this.MAX_DX = this.MIN_DX * 10.0;

    isSwimmingUp = false;
    isEating = false;
    isSlowingDown = false;
    isDead = false;
    isHurt = false;
    isPoisoned = false;

    setPhysics(type);

    // Collision box dimensions
    this.setCWidth(57);
    this.setCHeight(57);
    this.setWidth(57);
    this.setHeight(57);

    this.setLives(1);

    setAnimation(SpriteContent.playerFish2[IDLE], 10);
    setMovingBounds();
    this.setOffScreenPolicy(this.EXIT_POLICY_DIE);
    this.setActive(true);

  }

  /**
   * Overloaded constructor using default player fish type if one is not specified.
   * 
   * @param ocean
   */
  public PlayerFish(World ocean) {
    this(ocean, objType.PLAYERFISH_2);
  }


  private void setPhysics(objType type) {
    switch (type) {
      case PLAYERFISH_1:
        this.upThrust = -0.03f;
        this.MAX_DY = 7.0f;
        swimForce = -10.0f;
        break;
      case PLAYERFISH_3:
        this.upThrust = 0.3f;
        this.MAX_DY = 7.0f;
        swimForce = -10.0f;
        break;
      default:
        this.upThrust = 0.0f;
        this.MAX_DY = 7.0f;
        swimForce = -10.0f;

    }
  }

  public void setLives(int i) {
    lives = i;
    if (lives <= 0) {
      lives = 0;
      this.discard();
    }
  }

  public void gainLife() {
    lives++;
  }

  public void loseLife() {
    lives--;
    if (lives <= 0) {
      lives = 0;
      this.discard();
    }
  }

  public int getLives() {
    return lives;
  }

  public void increaseScore(int score) {
    this.score += score;
  }

  public int getScore() {
    return score;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long t) {
    time = t;
  }

  private void updateDistanceTravelled() {
    distanceTravelled += (-this.getParent().getSpeed());
  }

  public void update() {
    if (!isDiscarded()) {
      time++;
      updateDistanceTravelled();
      // updateScore();

      // Increase speed every five seconds
      if (time % 300 == 0) {
        this.getParent().setSpeed(this.getParent().getSpeed() - 0.5);
      }

      // isHurt state: check if player should flicker and collide with opponents
      if (isHurt) {
        hurtTimer++;
        if (hurtTimer > GRACEPERIOD) {
          isHurt = false;
          hurtTimer = 0;
          this.setCollidable(true);
        }
      }

      // check if player is poisoned
      if (isPoisoned) {
        // update poisonedTimer
        poisonedTimer--;
        swimForce = swimForce / 2 - 1;
        if (poisonedTimer <= 0) {
          isPoisoned = false;
          poisonedTimer = POISONDURATION; // reset timer to 10 seconds;

          currentAnimation = IDLE;
          setAnimation(SpriteContent.playerFish2[IDLE], 10);
          this.setPhysics(this.getType());
        }
      }


      updatePosition();
    }

    if (isDiscarded() && !isDead()) {
      // If player is discarded play gameOver animation and lift player to the surface
      // Player isDead once has been discarded and reaches the surface
      this.setY(this.getY() - 5);
      if (this.getY() < this.getParent().getYmin()) {
        this.setY(this.getParent().getYmin());
        this.isDead = true;
      }
    }
  
  }

  private void updatePosition() {
    // gravity increases downward speed velocity
    float netGravity = this.upThrust + this.getParent().GRAVITY;

    dx = 0.0;
    dy += netGravity;

    // Limit maximum sinking velocity to MAX_DY- terminal downwards velocity
    if (dy > MAX_DY) {
      dy = MAX_DY;
    }

    // Handle user's input by setting an initial upwards velocity
    if (isSwimmingUp) {
      dy = swimForce;
      isSwimmingUp = false;
    }

    super.update();
  }

  public boolean isDead() {
    return this.isDead;
  }

  protected void discard() {
    // Player should not die for touching the boundaries if in grace period
    if (!this.isHurt) {
      discarded = true;
      this.onDiscard();
    }
  }

  protected void onDiscard() {
    //stop scrolling the world
    this.getParent().setSpeed(0.0);
    currentAnimation = DEAD;
    setAnimation(SpriteContent.playerFish2[DEAD], 10);
    // Player is "Dead" only after his discarded playerFish has reached
    // the surface.
  }

  public void collectReward(RewardType reward) {
    this.setLives(this.getLives() + reward.getLifesToAward());
    this.score += reward.getPointsToAward();
    double newSpeed = this.getParent().getSpeed() * (1 - reward.getDecelarationFactor());
    if (newSpeed > this.MIN_DX) {
      newSpeed = MIN_DX;
    }
    this.getParent().setSpeed(newSpeed);
  }

  public void startSwimming() {
    isSwimmingUp = true;
  }

  public String timerToString(long timer) {
    int seconds = (int) ((timer % 3600) / 60);

    return seconds < 10 ? "0" + seconds + " sec" : "" + seconds + " sec";
  }

  public String getTimeToString() {
    int minutes = (int) (time / 3600);
    int seconds = (int) ((time % 3600) / 60);
    return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
  }

  public void reset() {
    isHurt = false;
    currentAnimation = -1;
  }



  public void draw(Graphics g, boolean mbr) {
    // player Fish is always on visible part of screen
    if (this.isActive()) {
      // If player fish is hurt it should flicker
      if (isHurt) {
        long elapsed = (System.nanoTime() - hurtTimer) / 1000000;

        if (elapsed / 100 % 2 == 0) {
          return; // blink every 100ms
        }
      }

      // If player fish is poisoned, display on the screen how long he will remain poisoned for.
      if (isPoisoned) {
        g.drawString(this.timerToString(this.poisonedTimer), (int) this.getX(), (int) this.getY());
      }
      
      g.drawImage(animation.getImage(), (int) (x), (int) (y), null);
      if (mbr) {
        Rectangle r = getRectangle();
        g.drawRect(r.x, r.y, r.width, r.height);
      }
      
    }
  }

  public void playerCollision(OnScreenObject opponent) {
    // Only check collision of player with opponents that are active, on the
    // screen, and in the vicinity of the player
    if (opponent.isActive() && opponent.isVisible()
        && opponent.getX() <= this.getX() + this.getWidth() && this.isActive()) {
      boolean isColliding =
          this.intersects(opponent) && opponent.isCollidable() && this.isCollidable();
      if (isColliding) {
        // opponent responds to collision by not rewarding points once discarded
        opponent.setPointsToAward(0);
        // Colliding with a Token
        if (opponent instanceof Token) {
          this.collectReward(((Token) opponent).getReward());
          ((Token) opponent).setCollected(true);
          opponent.discard();
        }
        // Colliding with an EnemyFish
        else if (opponent instanceof EnemyFish) {
          opponent.setCollidable(false);
          this.loseLife();
          this.isHurt = true;
          this.setCollidable(false);
        }
        // Collide with an Obstacle
        else if (opponent instanceof Obstacle) {
          if (opponent.getType() == objType.NETS) {
            this.discard();
            // this.isDead = true;
            this.setCollidable(false);
            // Player dies immediately

          }
          // Collide with a poisonous seaweed
          else if (opponent.getType() == objType.SEAWEED) {
            this.isPoisoned = true;
            currentAnimation = POISONED;
            setAnimation(SpriteContent.playerFish2[POISONED], 10);
            opponent.setCollidable(false);


          }
        }
      }
    }
  }


}

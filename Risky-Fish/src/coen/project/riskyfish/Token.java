package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Token extends OnScreenObject {

  /**
   * Specifies the delay between animation frames. Default value for token is 10 corresponding to 10
   * animation update calls before changing frames.
   */
  private int animationDelay = 10;

  /**
   * The reward awarded if player collects the token.
   */
  private RewardType reward;

  /**
   * Identifies if token has been collected by the player.
   */
  private boolean collected = false;

  /**
   * An array of images containing the sprite images of the token
   */
  private BufferedImage[] spriteImages;

  public Token(World oceanPanel, objType type) {
    // initialize token off screen.
    super(oceanPanel, type);

    this.setVelocityVector(oceanPanel.getSpeed(), 0);

    this.reward = new RewardType(type);

    this.setSpriteImages(type);

    this.setAnimation(spriteImages, animationDelay);

    this.setMovingBounds();
    this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);
  }

  public void spawn(double minimumY, double maximumY, double minimumX, double maximumX) {
    // tokens should be rare i.e each type appears once every 5 screen widths
    super.spawn(minimumY, maximumY, 9.0 * maximumX, 10.0 * maximumX);
    this.setPointsToAward(0);
  }

  /**
   * Loads the token sprite images to be used for the animation, and sets the token's dimensions as
   * per it's image.
   */
  private void setSpriteImages(objType tokenType) {

    switch (tokenType) {
      case FOOD_TKN:
        this.spriteImages = SpriteContent.token[0];
        break;
      case FEMALE_TKN:
        this.spriteImages = SpriteContent.token[1];
        break;
      case SHELL_TKN:
        this.spriteImages = SpriteContent.token[2];
        break;
      default:
        System.out.println("Wrong object type enum for token");
        break;
    }

    this.setHeight(55);
    this.setWidth(55);
    this.setCHeight(55);
    this.setCWidth(55);

  }

  public RewardType getReward() {
    if (this.collected) {
      // if already collected don't give extra reward
      return new RewardType(0, 0, 0.0);
    } else {
      // this.collected = true;
      return reward;
    }
  }

  public boolean isCollected() {
    return collected;
  }

  public void setCollected(boolean collect) {
    this.collected = collect;
  }

  public void update() {
    if (isActive()) {
      this.setVelocityVector(this.getParent().getSpeed(), 0.0);
      super.update();
    }
  }

  public void draw(Graphics g, boolean debug) {
    // Draw only if inside the screen, active and not yet collected
    if (this.isVisible() && this.isActive() && !this.isCollected()) {
      super.draw(g, debug);
    }
  }

}

package coen.project.riskyfish;

import java.awt.Color;
import java.awt.Graphics;

import coen.project.riskyfish.gfx.Background;

public class World {

  /**
   * Gravity is a characteristic of the world and is positive constant. It represents the number of
   * pixels per frame^2 that an object's vertical position is incremented every update(frame).
   */
  protected final float GRAVITY = 0.5f;
  // viewable size of the world
  private int pWidth;
  private int pHeight;

  // total world size
  private int worldWidth;
  private int worldHeight;

  // viewable bounding corner coordinates
  private int xmin;
  private int xmax;
  private int ymin;
  private int ymax;

  // speed the world is moving at
  private double scrollSpeed;

  // background animation
  private Background oceanBg;
  //
  private String bgName = Background.BG_RES;

  public World(int xmin, int xmax, int ymin, int ymax) {
    this.xmin = xmin;
    this.xmax = xmax;
    this.ymin = ymin;
    this.ymax = ymax;
    this.scrollSpeed = 0;
    this.pWidth = xmax - xmin;
    this.pHeight = ymax - ymin;

    this.loadBackground(bgName);


  }

  public int getWorldWidth() {
    return this.worldWidth;
  }

  public int getWorldHeight() {
    return this.worldHeight;
  }

  public void loadBackground(String bgName) {
    try {
      oceanBg = new Background(bgName, 1, this.pWidth, this.pHeight);
      oceanBg.setPosition(xmin, ymin);
      oceanBg.setVector(scrollSpeed, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Background getBackground() {
    return oceanBg;
  }

  public double getSpeed() {
    return this.scrollSpeed;
  }

  public void setSpeed(double speed) {
    // world should always move to the left
    if (speed > 0) {
      this.scrollSpeed = -speed;
    }
    this.scrollSpeed = speed;
  }

  public int getWidth() {
    return pWidth;
  }

  public void setWidth(int width) {
    this.pWidth = width;
  }

  public int getHeight() {
    return pHeight;
  }

  public void setHeight(int height) {
    this.pHeight = height;
  }

  public int getXmin() {
    return xmin;
  }

  private void setXmin(int xmin) {
    this.xmin = xmin;
  }

  public int getXmax() {
    return xmax;
  }

  private void setXmax(int xmax) {
    this.xmax = xmax;
  }

  public int getYmin() {
    return ymin;
  }

  private void setYmin(int ymin) {
    this.ymin = ymin;
  }

  public int getYmax() {
    return ymax;
  }

  private void setYmax(int ymax) {
    this.ymax = ymax;
  }

  public void update() {
    // update background speed
    oceanBg.setVector(scrollSpeed, 0);
    oceanBg.update();
  }

  public void draw(Graphics g) {
    // draw sky
    g.setColor(Color.CYAN);
    g.fillRect(0, 0, GamePanel.WIDTH, 45);
    // draw sea boundary
    g.setColor(Color.WHITE);
    g.fillRect(0, 45, GamePanel.WIDTH, 5);
    g.setColor(Color.BLACK);
    // draw ocean water
    oceanBg.draw(g);
  }
}

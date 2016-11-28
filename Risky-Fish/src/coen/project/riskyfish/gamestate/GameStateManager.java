package coen.project.riskyfish.gamestate;

import java.awt.Graphics;
import java.util.ArrayList;

import coen.project.riskyfish.GamePanel;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;

	private PauseState pauseState;
	private boolean isPaused;
	private boolean gameOver;

	public static final int NUMGAMESTATES = 3;
	public static final int MENUSTATE = 0;
	public static final int GAMEPLAYSTATE = 1;
	public static final int GAMEOVERSTATE = 2;

	public GameStateManager() {

		gameStates = new ArrayList<GameState>();
		pauseState = new PauseState(this);
		isPaused = false;
		gameOver = false;

		currentState = MENUSTATE;
		loadState(currentState);

	}

	private void loadState(int state) {
		switch (state) {
		case MENUSTATE:
			gameStates.add(MENUSTATE, new MenuState(this));
			break;
		case GAMEPLAYSTATE:
			gameStates.add(GAMEPLAYSTATE, new GamePlayState(this));
			break;
		case GAMEOVERSTATE:
			gameStates.add(GAMEOVERSTATE, new GameOverState(this));
		}
	}

	private void unloadState(int state) {
		gameStates.set(state, null);
	}

	public void setState(int state) {
		unloadState(currentState);
		this.currentState = state;
		loadState(currentState);
	}

	public void setPaused(boolean paused) {
		isPaused = paused;
	}

	public boolean isGamePaused() {
		return isPaused;
	}

	public void gameOver(){
	  gameOver = true;
	}
	
	public boolean isGameOver(){
	  return gameOver;
	}
	
	public void update() {
		if (isPaused && !isGameOver()) {
			pauseState.update();
			return;
		}
		if (this.gameStates.get(currentState) != null) {
			this.gameStates.get(currentState).update();
		}
	}

	public void draw(Graphics g) {
		if (isPaused && !isGameOver()) {
			pauseState.draw(g);
			return;
		}
		if (this.gameStates.get(currentState) != null) {
			this.gameStates.get(currentState).draw(g);
		} else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH,GamePanel.HEIGHT);
		}
	}

	public void keyPressed(int k) {
		if (isGamePaused() && !isGameOver()) {
			pauseState.keyPressed(k);
		} else {
			if (this.gameStates.get(currentState) != null) {
				this.gameStates.get(currentState).keyPressed(k);
			}
		}

	}

	public void keyReleased(int k) {
		if (this.gameStates.get(currentState) != null)
			this.gameStates.get(currentState).keyReleased(k);

	}
}

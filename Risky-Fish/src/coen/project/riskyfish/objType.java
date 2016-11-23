package coen.project.riskyfish;

public enum objType {
	FOOD_TKN(0), SHELL_TKN(1), FEMALE_TKN(2), NETS(3), SEAWEED(4), PREDATOR(5), JELLYFISH(6), PLAYERFISH(7);
	
	private final int index;
	
	objType(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return this.index;
	}
	
}

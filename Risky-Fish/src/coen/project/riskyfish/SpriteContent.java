package coen.project.riskyfish;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * This class loads resources on boot. OnScreenObject-sheets are taken from here.
 * 
 * @author Andrianos
 * 
 */
public class SpriteContent {

	private static final String SEAWEED_SPRITE_SHEET = "/textures/seaweed.png";
	private static final String NETS_SPRITE_SHEET = "/textures/nets.png";
	private static final String TOKEN_SHEET = "/textures/token_sprites.png";
	private static final String PLAYER_SHEET = "/textures/fish_sprite1.png";
	private static final String PLAYERFISH_2_SHEET = "/textures/fish2_sprites.gif";
	private static final String JELLYFISH_SHEET = "/textures/jellyFish.png";
	private static final String PREDATOR_SHEET = "/textures/predator.png";




	public static BufferedImage[][] playerFish2 = load(PLAYERFISH_2_SHEET, 57, 57);
	//public static BufferedImage[][] playerFish2_poisoned = load(PLAYERFISH_2_POISONED,57,57);

	public static BufferedImage[][] nets = load(NETS_SPRITE_SHEET, 150, 215);
	public static BufferedImage[][] seaweed = load(SEAWEED_SPRITE_SHEET, 150, 150);
	public static BufferedImage[][] jellyfish = load(JELLYFISH_SHEET,89,125);
	public static BufferedImage[][] predator = load(PREDATOR_SHEET,200,79);

	public static BufferedImage[][] token = load(TOKEN_SHEET, 55, 55);

	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(SpriteContent.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
}

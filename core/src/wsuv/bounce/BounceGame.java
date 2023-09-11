package wsuv.bounce;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import javax.crypto.spec.PSource;
import java.util.Random;

public class BounceGame extends Game {
    public static final int RSC_EXPLOSION_FRAMES_ROWS = 8;
    public static final int RSC_EXPLOSION_FRAMES_COLS = 8;
    public static final String RSC_EXPLOSION_FRAMES = "explosion8x8.png";
    public static final String RSC_GAMEOVER_IMG = "gameOverText.png";
    public static final String RSC_DEATH_IMG = "deathMessage.png";
    public static final String RSC_LEVEL_WIN_IMG = "levelVictoryMessage.png";
    public static final String RSC_GAME_VICTORY_IMG = "gameWinMessage.png";
    public static final String RSC_PRESSAKEY_IMG = "keyPromptText.png";
    public static final String RSC_BALL_IMG = "customBall.png";
    public static final String RSC_PADDLE_IMG = "paddle.png";
    public static final String RSC_DEFAULT_BRICK_IMG = "defaultBrick.png";
    public static final String RSC_ONE_HEALTH_BRICK_IMG = "oneHealth.png";
    public static final String RSC_TWO_HEALTH_BRICK_IMG = "twoHealth.png";
    public static final String RSC_THREE_HEALTH_BRICK_IMG = "threeHealth.png";
    public static final String RSC_FOUR_HEALTH_BRICK_IMG = "fourHealth.png";
    public static final String RSC_FIVE_HEALTH_BRICK_IMG = "fiveHealth.png";
    public static final String RSC_SIX_HEALTH_BRICK_IMG = "sixHealth.png";
    public static final String RSC_SPLASH_TEXT_IMG = "splashText.png";
    public static final String RSC_MONO_FONT_FILE = "JetBrainsMono-Regular.ttf";
    public static final String RSC_MONO_FONT = "JBM.ttf";
    public static final String RSC_EXPLOSION_SFX = "explosionSoundCustom.wav";
    public static final String RSC_HIT_SOUND = "ballHitSound.wav";
    public static final String RSC_DEATH_SOUND = "deathAnnounce.wav";
    public static final String RSC_GAME_OVER_SOUND = "gameOverAnnounce.wav";
    public static final String RSC_LEVEL_VICTORY_SOUND = "levelWinAnnounce.wav";
    public static final String RSC_GAME_VICTORY_SOUND = "gameVictoryAnnounce.wav";

    AssetManager am;  // AssetManager provides a single source for loaded resources
    SpriteBatch batch;

    Random random = new Random();

    Music music;
    @Override
    public void create() {
        am = new AssetManager();

		/* True Type Fonts are a bit of a pain. We need to tell the AssetManager
           a bit more than simply the file name in order to get them into an
           easily usable (BitMap) form...
		 */
        FileHandleResolver resolver = new InternalFileHandleResolver();
        am.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        am.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter myFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        myFont.fontFileName = RSC_MONO_FONT_FILE;
        myFont.fontParameters.size = 14;
        am.load(RSC_MONO_FONT, BitmapFont.class, myFont);

        // Load Textures after the font...
        am.load(RSC_BALL_IMG, Texture.class);
        am.load(RSC_PADDLE_IMG, Texture.class);
        am.load(RSC_DEFAULT_BRICK_IMG, Texture.class);
        am.load(RSC_ONE_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_TWO_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_THREE_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_FOUR_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_FIVE_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_SIX_HEALTH_BRICK_IMG, Texture.class);
        am.load(RSC_GAMEOVER_IMG, Texture.class);
        am.load(RSC_DEATH_IMG, Texture.class);
        am.load(RSC_LEVEL_WIN_IMG, Texture.class);
        am.load(RSC_GAME_VICTORY_IMG, Texture.class);
        am.load(RSC_PRESSAKEY_IMG, Texture.class);
        am.load(RSC_EXPLOSION_FRAMES, Texture.class);
        am.load(RSC_SPLASH_TEXT_IMG, Texture.class);

        // Load Sounds
        am.load(RSC_EXPLOSION_SFX, Sound.class);
        am.load(RSC_HIT_SOUND, Sound.class);
        am.load(RSC_DEATH_SOUND, Sound.class);
        am.load(RSC_GAME_OVER_SOUND, Sound.class);
        am.load(RSC_LEVEL_VICTORY_SOUND, Sound.class);
        am.load(RSC_GAME_VICTORY_SOUND, Sound.class);

        batch = new SpriteBatch();
        setScreen(new SplashScreen(this));

        // start the music right away.
        // this one we'll only reference via the GameInstance, and it's streamed
        // so, no need to add it to the AssetManager...
        music = Gdx.audio.newMusic(Gdx.files.internal("sadshark.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);
        //music.play(); //UNCOMMENT TO PLAY DEFAULT BACKGROUND MUSIC.
    }

    @Override
    public void dispose() {
        batch.dispose();
        am.dispose();
    }
}
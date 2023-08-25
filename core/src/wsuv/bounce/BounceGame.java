package wsuv.bounce;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class BounceGame extends ApplicationAdapter {
	public static final String RSC_BALL_IMG = "ball.png";
	public static final String RSC_MONO_FONT_FILE = "JetBrainsMono-Regular.ttf";
	public static final String RSC_MONO_FONT = "JBM.ttf";

	AssetManager am;  // AssetManager provides a single source for loaded resources
	SpriteBatch batch;

	Random random = new Random();

	Ball ball;
	int bounces;

	HUD hud;
	@Override
	public void create () {
		am = new AssetManager();

		// Load Textures...
		am.load(RSC_BALL_IMG, Texture.class);

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

		am.finishLoading();
		batch = new SpriteBatch();
		hud = new HUD(am.get(RSC_MONO_FONT));
		ball = new Ball(this);
		bounces = 0;
		// the HUD will show FPS always, by default.  Here's how
		// to use the HUD interface to silence it (and other HUD Data)
		hud.setDataVisibility(HUDViewCommand.Visibility.WHEN_OPEN);
		hud.registerView("Bounces:", new HUDViewCommand(HUDViewCommand.Visibility.ALWAYS) {
			@Override
			public String execute(boolean consoleIsOpen) {
				return Integer.toString(bounces);
			}
		});

	}

	public void update() {
		if( ball.update() ) {
			// it bounced!
			bounces++;
		}
		// ignore key presses when console is open...
		if (!hud.isOpen()) {
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				ball.yVelocity += 2;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				ball.yVelocity -= 2;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				ball.xVelocity -= 2;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				ball.xVelocity += 2;
			}
		}

	}

	@Override
	public void render () {

		update();

		// draw
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		ball.draw(batch);
		hud.draw(batch);
		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		am.dispose();
	}
}
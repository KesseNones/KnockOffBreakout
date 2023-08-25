package wsuv.bounce;

import com.badlogic.gdx.ApplicationAdapter;
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
	}

	public void update() {
		ball.update();
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
	}
}
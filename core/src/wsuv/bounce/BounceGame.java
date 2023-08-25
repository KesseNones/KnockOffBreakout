package wsuv.bounce;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class BounceGame extends ApplicationAdapter {
	public static final String RSC_BALL_IMG = "ball.png";
	AssetManager am;  // AssetManager provides a single source for loaded resources
	SpriteBatch batch;

	Random random = new Random();

	Ball ball;
	@Override
	public void create () {
		am = new AssetManager();
		am.load(RSC_BALL_IMG, Texture.class);
		am.finishLoading();
		batch = new SpriteBatch();
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
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
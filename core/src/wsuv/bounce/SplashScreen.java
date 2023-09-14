package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

//Used to display splash screen at the start of the game.
public class SplashScreen extends ScreenAdapter {
    BounceGame bounceGame;
    int timer;

    //Establishes new splash screen.
    public SplashScreen(BounceGame game) {
        bounceGame = game;
        timer = 0;
    }

    @Override
    public void show() {
        Gdx.app.log("SplashScreen", "show");
    }

    //Displays splash screen image.
    public void render(float delta) {
        timer++;
        ScreenUtils.clear(0, 0, 0, 1);
        bounceGame.am.update(10);

        //Moves on to load screen if time is up or player presses ESC.
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || timer > 300){
            bounceGame.setScreen(new LoadScreen(bounceGame));
        }

        //Draws splash screen text.
        bounceGame.batch.begin();
        if (bounceGame.am.isLoaded(bounceGame.RSC_SPLASH_TEXT_IMG, Texture.class)){
            bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_SPLASH_TEXT_IMG, Texture.class), 200, 250);
        }

        //This causes the splash screen text to have a fading in effect.
        bounceGame.batch.setColor(1, 1, 1, (float)timer / 180f);
        bounceGame.batch.end();

    }
}

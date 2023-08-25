package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

public class Ball extends Sprite {

    private static final Random random = new Random();
    private Sound sound;
    private float xVelocity;
    private float yVelocity;

    public Ball(BounceGame game) {
        super(game.am.get("ball.png", Texture.class));

        xVelocity = game.random.nextFloat(80, 150);
        yVelocity = game.random.nextFloat(80, 150);
        if (game.random.nextBoolean()) xVelocity *= -1;
        if (game.random.nextBoolean()) yVelocity *= -1;
        setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

    public boolean update() {
        float x = getX();
        float y = getY();
        boolean bounced = false;

        if (x < 0 || (x + getWidth()) > Gdx.graphics.getWidth()) {
            xVelocity *= -1;
            bounced = true;
        }
        if (y < 0 || (y + getHeight()) > Gdx.graphics.getHeight()) {
            yVelocity *= -1;
            bounced = true;
        }
        float time = Gdx.graphics.getDeltaTime();
        setX(x + time*xVelocity);
        setY(y + time*yVelocity);

        return bounced;
    }
}

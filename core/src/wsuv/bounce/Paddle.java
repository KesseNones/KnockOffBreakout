package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This class represents the paddle in game.
public class Paddle extends Sprite {

    //Makes a new paddle when necessary.
    public Paddle(BounceGame game) {
        //Establishes paddle texture and size.
        super(game.am.get("paddle.png", Texture.class));
        //The size of the paddle is scaled down arbitrary. Likely will change later.
        setSize(104f, 13f);

        //Establishes starting location for paddle.
        setCenter(Gdx.graphics.getWidth() / 2f, (Gdx.graphics.getHeight() / 7.5f));
    }

}

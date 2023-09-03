package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Brick extends Sprite {

    public Brick(BounceGame game) {
        super(game.am.get("defaultBrick.png", Texture.class));
        setCenter(Gdx.graphics.getWidth() / 2f, (Gdx.graphics.getHeight() * 0.75f));
    }
}

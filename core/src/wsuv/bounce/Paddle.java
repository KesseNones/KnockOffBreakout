package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Paddle extends Sprite {

    public Paddle(BounceGame game) {
        super(game.am.get("paddle.png", Texture.class));
        //The size of the paddle is scaled down arbitrary. Likely will change later.
        setSize(104f, 13f);
        setCenter(Gdx.graphics.getWidth() / 2f, (Gdx.graphics.getHeight() / 7.5f));
    }

    /**
     * Update the paddle's position based on input
     * of user and its current coordinates.
     *
     *
     * @return true iff something happens. FIGURE OUT LATER
     */
    public boolean update() {
        return false;
    }
}

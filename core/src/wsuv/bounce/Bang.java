package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * An Explosion!
 */
public class Bang {
    float x, y;
    float time;
    Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)

    public Bang(BounceGame game, boolean fast) {
        this(game, fast, 100, 100);
    }

    public Bang(BounceGame game, boolean fast, float x, float y) {
        this.x = x;
        this.y = y;
        time = 0;

        // Load the sprite sheet as a Texture
        Texture boomSheet = new Texture(Gdx.files.internal(BounceGame.RSC_EXPLOSION_FRAMES));

        // Split the texture into individual frames
        TextureRegion[][] tmp = TextureRegion.split(boomSheet,
                boomSheet.getWidth() / BounceGame.RSC_EXPLOSION_FRAMES_COLS,
                boomSheet.getHeight() / BounceGame.RSC_EXPLOSION_FRAMES_ROWS);

        TextureRegion[] frames = new TextureRegion[BounceGame.RSC_EXPLOSION_FRAMES_COLS * BounceGame.RSC_EXPLOSION_FRAMES_ROWS];
        int index = 0;
        for (int i = 0; i < BounceGame.RSC_EXPLOSION_FRAMES_ROWS; i++) {
            for (int j = 0; j < BounceGame.RSC_EXPLOSION_FRAMES_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(fast ? 0.03f : .07f, frames);
    }
    public void draw(Batch sb) {
        time += Gdx.graphics.getDeltaTime();
        sb.draw(animation.getKeyFrame(time, false), x, y);

    }
    public boolean completed() {
        return animation.isAnimationFinished(time);
    }
}

package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * An Explosion!
 */
public class Bang {
    float x, y;
    private int hh, hw;

    float time;
    Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)

    /**
     * An Explosion
     * @param fast - true iff we should animate quickly
     * @param x  - explosion center x
     * @param y  - explosion center y
     */
    public Bang(boolean fast, float x, float y) {
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
        hw = (boomSheet.getWidth() / BounceGame.RSC_EXPLOSION_FRAMES_COLS) / 2;
        hh = (boomSheet.getHeight() / BounceGame.RSC_EXPLOSION_FRAMES_ROWS) / 2;

        int index = 0;
        for (int i = 0; i < BounceGame.RSC_EXPLOSION_FRAMES_ROWS; i++) {
            for (int j = 0; j < BounceGame.RSC_EXPLOSION_FRAMES_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(fast ? 0.06f : .12f, frames);
        System.out.println("Create Bang: " + (boomSheet.getWidth() / BounceGame.RSC_EXPLOSION_FRAMES_COLS));
    }
    public void draw(Batch sb) {
        time += Gdx.graphics.getDeltaTime();
        sb.draw(animation.getKeyFrame(time, false), x-hw, y-hh);

    }
    public boolean completed() {
        return animation.isAnimationFinished(time);
    }
}

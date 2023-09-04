package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Brick extends Sprite {
    private boolean spriteExists;
    private int health;

    public Brick(BounceGame game, int initialHealth, float row, float col) {
        super(game.am.get("defaultBrick.png", Texture.class));
        spriteExists = true;
        //Sets up health of brick based on input.
        if (initialHealth < 1){
            health = 1;
        }else{
            health = initialHealth;
        }
        setX(Gdx.graphics.getWidth() / (10f) * col);
        setY(Gdx.graphics.getHeight() * 0.75f * (1f + row)); //WILL FIGURE THIS OUT LATER
    }

    public boolean collide(){
        health--;
        spriteExists = health > 1;
        return spriteExists;
    }

    public boolean doesSpriteExist(){
        return spriteExists;
    }
}

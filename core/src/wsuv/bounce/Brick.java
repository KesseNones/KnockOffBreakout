package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Brick extends Sprite {
    private boolean spriteExists;
    private int health;

    public Brick(BounceGame game, int initialHealth) {
        super(game.am.get("defaultBrick.png", Texture.class));
        spriteExists = true;
        //Sets up health of brick based on input.
        if (initialHealth < 1){
            health = 1;
        }else{
            health = initialHealth;
        }
        setCenter(Gdx.graphics.getWidth() / 2f, (Gdx.graphics.getHeight() * 0.75f));
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

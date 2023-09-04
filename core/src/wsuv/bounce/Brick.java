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
        boolean rowGreaterThanThree = row > 2;
        float add;
        if (rowGreaterThanThree){add = 40f;}else{add = 0f;}
        setY((Gdx.graphics.getHeight() * 0.6f) + (40f * row) + add);
    }

    public boolean collide(){
        health--;
        spriteExists = health > 1;
        return spriteExists;
    }

    public int getHealth(){return health;}

    public boolean doesSpriteExist(){
        return spriteExists;
    }
}

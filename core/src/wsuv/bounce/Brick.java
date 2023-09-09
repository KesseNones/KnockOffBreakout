package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Brick extends Sprite {
    private boolean spriteExists;
    private int health;

    public Brick(BounceGame game, int initialHealth, float row, float col) {
        super(game.am.get("defaultBrick.png", Texture.class));
        setTexture(findTextureBasedOnHealth(initialHealth));
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

    //Takes in health and returns a new texture object based on the input health.
    private Texture findTextureBasedOnHealth(int health){
        String textureName;
        switch (health){
            case 1:
                textureName = "oneHealth.png";
                break;
            case 2:
                textureName = "twoHealth.png";
                break;
            case 3:
                textureName = "threeHealth.png";
                break;
            case 4:
                textureName = "fourHealth.png";
                break;
            case 5:
                textureName = "fiveHealth.png";
                break;
            case 6:
                textureName = "sixHealth.png";
                break;
            default:
                textureName = "defaultBrick.png";
                break;
        }
        return new Texture(textureName);
    }

    //Destroys a brick due to the ball.
    public boolean collide(){
        health--;
        spriteExists = health > 1;
        if (spriteExists){setTexture(findTextureBasedOnHealth(health));}
        return spriteExists;
    }

    //Brings brick back from the dead.
    public void resurrect(int newHealth){
        health = newHealth;
        setTexture(findTextureBasedOnHealth(health));
        spriteExists = true;
    }

    public int getHealth(){return health;}

    public boolean doesSpriteExist(){
        return spriteExists;
    }
}

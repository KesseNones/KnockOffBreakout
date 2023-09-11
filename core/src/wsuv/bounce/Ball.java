package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite {

    Vector2 velocityVector;

    public Ball(BounceGame game) {
        super(game.am.get("customBall.png", Texture.class));
        setSize(32f, 32f);

        float xVelocity = (game.random.nextFloat() * 70f) + 200f;
        float yVelocity = (game.random.nextFloat() * 70f) + 200f;
        if (game.random.nextBoolean()) xVelocity *= -1;
        if (game.random.nextBoolean()) yVelocity *= -1;
        velocityVector = new Vector2(xVelocity, yVelocity);
        setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
    }

    /**
     * Update the ball's location based on time since last update and velocity.
     * update() should generally be called every frame...
     *
     * @return true iff the ball bounced in this last update.
     */
    public boolean update() {
        float x = getX();
        float y = getY();
        boolean bounced = false;

        if ( (getY() >= 0) && (x < 0 || (x + getWidth()) > Gdx.graphics.getWidth()) ) {
            velocityVector.x = Math.abs(velocityVector.x);
            if ((x + getWidth()) > Gdx.graphics.getWidth()){velocityVector.x *= -1;}
            bounced = true;
        }
        if ((y + getHeight()) > Gdx.graphics.getHeight()) {
            velocityVector.y = Math.abs(velocityVector.y) * -1;
            bounced = true;
        }
        if (y < 0){
            velocityVector.y = Math.abs(velocityVector.y);
        }
        float time = Gdx.graphics.getDeltaTime();
        setX(x + time * velocityVector.x);
        setY(y + time * velocityVector.y);

        //Gravitational effect.
        if (!bounced){velocityVector.y = velocityVector.y - 0.05f;}

        return bounced;
    }

    //A generalized method that detects collisions with of a ball with a sprite.
    public boolean collidedWithObject(Sprite s){
        Rectangle spriteRectangle = s.getBoundingRectangle();
        Rectangle ballRectangle = getBoundingRectangle();
        double tau = Math.PI * 2;
        if (spriteRectangle.overlaps(ballRectangle)){
            Vector2 spriteCenterVec = new Vector2(0, 0);
            Vector2 ballCenterVec = new Vector2(0, 0);
            spriteRectangle.getCenter(spriteCenterVec);
            ballRectangle.getCenter(ballCenterVec);

            Vector2 collisionVector = ballCenterVec.sub(spriteCenterVec);
            float collisionAngle = collisionVector.angleRad();
            velocityVector.setAngleRad(collisionAngle);
            //Ball velocity increases by 0.3 percent per collision.
            velocityVector.x = velocityVector.x * 1.003f;
            velocityVector.y = velocityVector.y * 1.003f;

            return true;
        }
        return false;
    }
}

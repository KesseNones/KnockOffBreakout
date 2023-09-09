package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Vector;


public class Ball extends Sprite {

    Vector2 velocityVector;

    public Ball(BounceGame game) {
        super(game.am.get("ball.png", Texture.class));
        setSize(32f, 32f); //MAY BE UNDONE LATER

        float xVelocity = (game.random.nextFloat() * 70f) + 150f;
        float yVelocity = (game.random.nextFloat() * 70f) + 150f;
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
        float time = Gdx.graphics.getDeltaTime();
        setX(x + time * velocityVector.x);
        setY(y + time * velocityVector.y);

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
//            double firstQuadrantAngle = Math.atan(s.getHeight() / s.getWidth());
//            double complementAngle = (tau / 2) - firstQuadrantAngle;

//            //Right edge collision case.
//            if (collisionAngle > 0 && collisionAngle < firstQuadrantAngle) {
//                velocityVector.x = Math.abs(velocityVector.x) * -1;
//            }
//
//            //Top edge collision case.
//            if (collisionAngle > firstQuadrantAngle && collisionAngle < complementAngle ){
//                velocityVector.y = Math.abs(velocityVector.y);
//            }
//
//            //Left edge collision case.
//            if (collisionAngle > complementAngle && collisionAngle < (complementAngle + firstQuadrantAngle)){
//                velocityVector.x = Math.abs(velocityVector.x) * -1;
//            }
//
//            //Bottom edge collision case.
//            if (collisionAngle > (complementAngle + firstQuadrantAngle)
//                    && collisionAngle < (2 * complementAngle + firstQuadrantAngle)){
//                velocityVector.y = Math.abs(velocityVector.y);
//            }

            return true;
        }
        return false;
    }

    //Used to detect if the ball collided with the paddle.
    public boolean collidedWithPaddle(Paddle pd){
        boolean collided = false;
        float paddleLeftX = pd.getX();
        float paddleRightX = pd.getX() + pd.getWidth();
        float ballLeftX = getX();
        float ballRightX = getX() + getWidth();

        float paddleBottomY = pd.getY();
        float paddleTopY = pd.getY() + pd.getHeight();
        float ballBottomY = getY();
        float ballTopY = getY() + getHeight();

        boolean leftEdgeInXRange = (ballLeftX > paddleLeftX) && (ballLeftX < paddleRightX);
        boolean rightEdgeInXRange = (ballRightX > paddleLeftX) && (ballRightX < paddleRightX);

        boolean topEdgeInYRange = (ballTopY > paddleBottomY) && (ballTopY < paddleTopY);
        boolean bottomEdgeInYRange = (ballBottomY < paddleTopY) && (ballBottomY > paddleBottomY);

        collided = (leftEdgeInXRange || rightEdgeInXRange) && (topEdgeInYRange || bottomEdgeInYRange);

        if (collided) {velocityVector.y *= -1;}

        return collided;
    }

    //Determines if the ball collided with a brick or not and responds accordingly.
    public boolean collidedWithBrick(Brick b){
        //Stops early if there's no brick to collide with.
        if (!b.doesSpriteExist()){
            return false;
        }

        boolean collided = false;
        float brickLeftX = b.getX();
        float brickRightX = b.getX() + b.getWidth();
        float ballLeftX = getX();
        float ballRightX = getX() + getWidth();

        float brickBottomY = b.getY();
        float brickTopY = b.getY() + b.getHeight();
        float ballBottomY = getY();
        float ballTopY = getY() + getHeight();

        boolean leftEdgeInXRange = (ballLeftX > brickLeftX) && (ballLeftX < brickRightX);
        boolean rightEdgeInXRange = (ballRightX > brickLeftX) && (ballRightX < brickRightX);

        boolean topEdgeInYRange = (ballTopY > brickBottomY) && (ballTopY < brickTopY);
        boolean bottomEdgeInYRange = (ballBottomY < brickTopY) && (ballBottomY > brickBottomY);

        collided = (leftEdgeInXRange || rightEdgeInXRange) && (topEdgeInYRange || bottomEdgeInYRange);

        if (collided) {velocityVector.y *= -1;}

        return collided;
    }
}

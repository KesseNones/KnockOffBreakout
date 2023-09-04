package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Ball extends Sprite {

    float xVelocity;
    float yVelocity;

    public Ball(BounceGame game) {
        super(game.am.get("ball.png", Texture.class));
        setSize(32f, 32f); //MAY BE UNDONE LATER

        xVelocity = (game.random.nextFloat() * 70f) + 150f;
        yVelocity = (game.random.nextFloat() * 70f) + 150f;
        if (game.random.nextBoolean()) xVelocity *= -1;
        if (game.random.nextBoolean()) yVelocity *= -1;
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

        if (x < 0 || (x + getWidth()) > Gdx.graphics.getWidth()) {
            xVelocity *= -1;
            bounced = true;
        }
        if (y < 0 || (y + getHeight()) > Gdx.graphics.getHeight()) {
            yVelocity *= -1;
            bounced = true;
        }
        float time = Gdx.graphics.getDeltaTime();
        setX(x + time * xVelocity);
        setY(y + time * yVelocity);

        return bounced;
    }

    //Used to detect if the ball collided with the paddle.
    public boolean collidedWithPaddle(Paddle pd){
        boolean collided = false;
        float ballBottomY = getY();
        float paddleTopY = pd.getY();
        float ballBottomLeftX = getX();
        float ballBottomRightX = getX() + getWidth();
        float paddleLeftX = pd.getX();
        float paddleRightX = pd.getX() + pd.getWidth();

        if ((ballBottomY > (paddleTopY - 1f)) &&
                (ballBottomY < (paddleTopY + 1f)) &&
                ((ballBottomLeftX > paddleLeftX && ballBottomLeftX < paddleRightX) ||
                        (ballBottomRightX > paddleLeftX && ballBottomRightX < paddleRightX))
            ){
            yVelocity *= -1;
            collided = true;
        }

        return collided;
    }

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

        if (collided) {yVelocity *= -1;}

        return collided;
    }
}

package wsuv.bounce;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayScreen extends ScreenAdapter {
    private enum SubState {READY, DEAD, GAME_OVER, PLAYING, LEVEL_WON, GAME_VICTORY}
    private boolean gameHasEnded;
    private boolean wonLevel;
    private int lives;
    private BounceGame bounceGame;
    private Ball ball;
    private Paddle paddle;
    private int numBricks;
    private int aliveBricks;
    private Brick[] bricks;
    private HUD hud;
    private SubState state;
    private int level;
    private float timer;
    private boolean godModeEnabled;

    private Sound boomSfx;
    private Sound hitSound;
    private Sound deathSound;
    private Sound gameOverSound;
    private Sound levelWinSound;
    private Sound gameWinSound;
    private ArrayList<Bang> explosions;
    BangAnimationFrames baf;

    public PlayScreen(BounceGame game) {
        game.batch.setColor(1, 1, 1, 1);
        godModeEnabled = false;
        timer = 0;
        lives = 3;
        gameHasEnded = false;
        wonLevel = false;
        bounceGame = game;
        hud = new HUD(bounceGame.am.get(BounceGame.RSC_MONO_FONT));
        ball = new Ball(game);
        paddle = new Paddle(game);
        level = 1;

        //Creates a row of ten bricks to be hit with the ball.
        numBricks = 60;
        aliveBricks = numBricks;
        bricks = new Brick[numBricks];
        for (int i = 0; i < numBricks; i++){
            bricks[i] = new Brick(game, brickHealthMethod(i, level), (int)(i / 10), i % 10);
        }

        explosions = new ArrayList<>(10);
        boomSfx = bounceGame.am.get(BounceGame.RSC_EXPLOSION_SFX);
        hitSound = bounceGame.am.get(BounceGame.RSC_HIT_SOUND);
        deathSound = bounceGame.am.get(BounceGame.RSC_DEATH_SOUND);
        gameOverSound = bounceGame.am.get(BounceGame.RSC_GAME_OVER_SOUND);
        levelWinSound = bounceGame.am.get(BounceGame.RSC_LEVEL_VICTORY_SOUND);
        gameWinSound = bounceGame.am.get(BounceGame.RSC_GAME_VICTORY_SOUND);

        // we've loaded textures, but the explosion texture isn't quite ready to go--
        // we need to carve it up into frames.  All that work really
        // only needs to happen once.  Since we only use explosions in the PlayScreen,
        // we'll do it here, storing the work in a special object we'll use each time
        // a new Bang instance is created...
        baf = new BangAnimationFrames(bounceGame.am.get(BounceGame.RSC_EXPLOSION_FRAMES));

        // the HUD will show FPS always, by default.  Here's how
        // to use the HUD interface to silence it (and other HUD Data)
        hud.setDataVisibility(HUDViewCommand.Visibility.WHEN_OPEN);

        // HUD Console Commands
        hud.registerAction("ball", new HUDActionCommand() {
            static final String help = "Usage: ball <x> <y> <vx> <vy> | ball ";

            @Override
            public String execute(String[] cmd) {
                try {
                    float x = Float.parseFloat(cmd[1]);
                    float y = Float.parseFloat(cmd[2]);
                    float vx = Float.parseFloat(cmd[3]);
                    float vy = Float.parseFloat(cmd[4]);
                    ball.velocityVector.x = vx;
                    ball.velocityVector.y = vy;
                    ball.setCenter(x, y);
                    return "ok!";
                } catch (Exception e) {
                    return help;
                }
            }

            public String help(String[] cmd) {
                return help;
            }
        });

        //Cheat command that adds one life to the player's life counter,
        // giving them more room to fail.
        hud.registerAction("addlife", new HUDActionCommand() {
            static final String desc = "Adds life to lives.";

            @Override
            public String execute(String[] cmd) {
                lives++;
                return "Life Added";
            }

            public String help(String[] cmd){
                return desc;
            }
        });

        hud.registerAction("godmode", new HUDActionCommand() {
            static final String desc = "Makes ball hitting the bottom not kill player.";

            @Override
            public String execute(String[] cmd) {
                String godString;
                godModeEnabled = !godModeEnabled;
                if (godModeEnabled){
                    godString = "God Mode Enabled";
                }else{
                    godString = "God Mode Disabled";
                }
                return godString;
            }

            public String help(String[] cmd){
                return desc;
            }
        });

        hud.registerAction("winlevel", new HUDActionCommand() {
            static final String desc = "Explodes all remaining bricks \nand wins the level for the player.";

            @Override
            public String execute(String[] cmd) {
                for (int i = 0; i < numBricks; i++){
                    if (bricks[i].doesSpriteExist()){
                        bricks[i].DESTROY();
                        explosions.add(new Bang(baf, true, bricks[i].getX() + bricks[i].getOriginX(),
                                bricks[i].getY() + bricks[i].getOriginY()));
                        boomSfx.play();
                    }
                }
                return "Conglaturation !!! Your Winner   !";
            }

            public String help(String[] cmd){
                return desc;
            }
        });

        hud.registerAction("lose", new HUDActionCommand() {
            static final String desc = "Causes the player to instantly enter the losing state\n and lose a life.";

            @Override
            public String execute(String[] cmd) {
                godModeEnabled = false;
                ball.setCenter(400, -9999);
                return "You lost!";
            }

            public String help(String[] cmd){
                return desc;
            }
        });

        //This stops the entire game process. As a result,
        // it may need to be removed later if deemed too dangerous.
        hud.registerAction("exit", new HUDActionCommand() {
            static final String desc = "Exits the game entirely.";

            @Override
            public String execute(String[] cmd) {
                Gdx.app.exit();
                return "Goodbye!";
            }

            public String help(String[] cmd){
                return desc;
            }
        });

        // HUD Data
        hud.registerView("Level:", new HUDViewCommand(HUDViewCommand.Visibility.ALWAYS) {
            @Override
            public String execute(boolean consoleIsOpen) {
                return Integer.toString(level);
            }
        });

        hud.registerView("Lives:", new HUDViewCommand(HUDViewCommand.Visibility.ALWAYS) {
            @Override
            public String execute(boolean consoleIsOpen) {
                return Integer.toString(lives);
            }
        });
        hud.registerView("Ball @:", new HUDViewCommand(HUDViewCommand.Visibility.WHEN_OPEN) {
            @Override
            public String execute(boolean consoleIsOpen) {
                return String.format("%.0f %.0f [%.0f %.0f] (%d)",
                        ball.getX(), ball.getY(), ball.velocityVector.x, ball.velocityVector.y, explosions.size());
            }
        });

        // we're adding an input processor AFTER the HUD has been created,
        // so we need to be a bit careful here and make sure not to clobber
        // the HUD's input controls. Do that by using an InputMultiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        // let the HUD's input processor handle things first....
        multiplexer.addProcessor(Gdx.input.getInputProcessor());
        // then pass input to our new handler...
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (character == '!') {
                    Gdx.app.log("Boom!",  "(" + explosions.size() + ")" );
                    explosions.add(new Bang(baf, false, ball.getX() + ball.getOriginX(), ball.getY() + ball.getOriginY()));
                    boomSfx.play();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void show() {
        Gdx.app.log("PlayScreen", "show");
        state = SubState.READY;
        level = 1;
    }

    //Calculates the health of a given brick based on the current index in bricks and the current level.
    public int brickHealthMethod(int index, int lvl){
        int divFactor = 30 - (10 * (lvl - 1));
        return 1 + (index / divFactor);
    }

    public void update(float delta) {
        timer += delta;

        //Detects if ball goes below bottom of screen, indicating death.
        if (!hud.isOpen() && state == SubState.PLAYING && ball.getY() < 0 && !godModeEnabled){
            lives--;
            timer = 0;
            if (lives > 0){
                state = SubState.DEAD;
                deathSound.play();
            }else{
                state = SubState.GAME_OVER;
                gameHasEnded = true;
                gameOverSound.play();
            }
        }

        // always update the ball, but ignore bounces unless we're in PLAY state
        if (!hud.isOpen() && state == SubState.PLAYING) {
            boolean ballCollidedWithWall = ball.update();
            boolean ballHitPaddle = ball.collidedWithObject(paddle);

            //Determines if ball has collided with any bricks.
            for (int i = 0; i < numBricks; i++){
                boolean ballHitBrick;
                //If a brick exists that could be hit, check to see if the ball actually hit it.
                if (bricks[i].doesSpriteExist()){
                    ballHitBrick = ball.collidedWithObject(bricks[i]);
                }else {
                    ballHitBrick = false;
                }

                if (ballHitBrick) {
                    boolean stillAlive = bricks[i].collide();
                    if (stillAlive){
                        hitSound.play();
                    }else{
                        explosions.add(new Bang(baf, true, ball.getX() + ball.getOriginX(), ball.getY() + ball.getOriginY()));
                        boomSfx.play();
                    }

                }
            }

            int brickCount = 0;
            for (int i = 0; i < numBricks ; i++){
                if (bricks[i].doesSpriteExist()){brickCount++;}
            }
            aliveBricks = brickCount;

            if (aliveBricks < 1){
                if (level > 2){
                    state = SubState.GAME_VICTORY;
                    gameHasEnded = true;
                    gameWinSound.play();
                }else{
                    state = SubState.LEVEL_WON;
                    wonLevel = true;
                    levelWinSound.play();
                }
                timer = 0;
            }

            //Plays basic hit sound when ball hits wall or paddle.
            if (ballCollidedWithWall || ballHitPaddle){
                hitSound.play();
            }

        }
        if (!hud.isOpen() && state == SubState.READY && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            state = SubState.PLAYING;
            bounceGame.music.setVolume(bounceGame.music.getVolume() / 2);
            //If the game had ended before, the bricks are reset.

            if (gameHasEnded){
                lives = 3;
                level = 1;
                gameHasEnded = false;
                paddle = new Paddle(bounceGame);
                for (int i = 0; i < numBricks; i++){
                    bricks[i].resurrect(brickHealthMethod(i, level));
                }
                aliveBricks = numBricks;
            }
            if (wonLevel){
                level++;
                wonLevel = false;
                paddle = new Paddle(bounceGame);
                for (int i = 0; i < numBricks; i++){
                    bricks[i].resurrect(brickHealthMethod(i, level));
                }
                aliveBricks = numBricks;

            }

            //Increases ball speed based on level amount.
            ball.velocityVector.x = (ball.velocityVector.x - 200f) *
                    (float) (Math.pow( (1.2f) , ((float) level - 1) )) + 200f;
            ball.velocityVector.y = (ball.velocityVector.y - 200f) *
                    (float) (Math.pow( (1.2f) , ((float) level -  1) )) + 200f;
        }

        if (!hud.isOpen() && state != SubState.PLAYING && timer > 5f){
            ball = new Ball(bounceGame);
            paddle = new Paddle(bounceGame);
            state = SubState.READY;
        }

        // ignore key presses when console is open and when game is over...
        if (!hud.isOpen() && state == SubState.PLAYING) {
            //Paddle movement based on default 10 plus 1.1 to the power
            // of level - 1 plus the log of the speed of the ball.
            // This way, the paddle speed scales with the level and speed of the ball.
            float boostExponent = (float)Math.log((float)Math.sqrt((ball.velocityVector.x * ball.velocityVector.x)
                    + (ball.velocityVector.y * ball.velocityVector.y)));
            float levelExponent = level - 1;
            float movementVector = 10f * (float)(Math.pow(1.1f, levelExponent + boostExponent));
            //Moves paddle to the left until collision.
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (paddle.getX() > 0f){
                    paddle.setX(paddle.getX() - movementVector);
                }
            }
            //Moves paddle to the right until collision.
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if ((paddle.getX() + paddle.getWidth()) < Gdx.graphics.getWidth()) paddle.setX(paddle.getX() + movementVector);
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        bounceGame.batch.begin();
        for(Iterator<Bang> bi = explosions.iterator(); bi.hasNext(); ) {
            Bang b = bi.next();
            if (b.completed()) { bi.remove(); }
            else { b.draw(bounceGame.batch); }
        }
        ball.draw(bounceGame.batch);
        paddle.draw(bounceGame.batch);

        //Draws all existing blocks.
        for (int i = 0 ; i < numBricks; i++){
            if (bricks[i].doesSpriteExist()){
                bricks[i].draw(bounceGame.batch);
            }
        }
        // this logic could also be pushed into a method on SubState enum
        switch (state) {
            case DEAD:
                bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_DEATH_IMG, Texture.class), 200, 200);
                break;
            case GAME_OVER:
                bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_GAMEOVER_IMG, Texture.class), 200, 200);
                break;
            case LEVEL_WON:
                bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_LEVEL_WIN_IMG, Texture.class), 200, 200);
                break;
            case GAME_VICTORY:
                bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_GAME_VICTORY_IMG, Texture.class), 200, 200);
                break;
            case READY:
                bounceGame.batch.draw(bounceGame.am.get(BounceGame.RSC_PRESSAKEY_IMG, Texture.class), 200, 200);
                break;
            case PLAYING:
                break;
        }
        hud.draw(bounceGame.batch);
        bounceGame.batch.end();
    }
}

/**
 * This class we'll only instantiate once; it will hold data shared
 * by all Bang instances
 */
class BangAnimationFrames {
    float halfW, halfH;
    TextureRegion[] frames;
    BangAnimationFrames(Texture spritesheet) {
        // split the single spritesheet into an array of equally sized TextureRegions
        TextureRegion[][] tmp = TextureRegion.split(spritesheet,
                spritesheet.getWidth() / BounceGame.RSC_EXPLOSION_FRAMES_COLS,
                spritesheet.getHeight() / BounceGame.RSC_EXPLOSION_FRAMES_ROWS);

        halfW = (spritesheet.getWidth() / 2f) / BounceGame.RSC_EXPLOSION_FRAMES_COLS;
        halfH = (spritesheet.getHeight() / 2f) / BounceGame.RSC_EXPLOSION_FRAMES_ROWS;

        frames = new TextureRegion[BounceGame.RSC_EXPLOSION_FRAMES_COLS * BounceGame.RSC_EXPLOSION_FRAMES_ROWS];
        int index = 0;
        for (int i = 0; i < BounceGame.RSC_EXPLOSION_FRAMES_ROWS; i++) {
            for (int j = 0; j < BounceGame.RSC_EXPLOSION_FRAMES_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }
    }
}
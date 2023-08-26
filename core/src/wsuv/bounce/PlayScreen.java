package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PlayScreen extends ScreenAdapter {
    private enum SubState {READY, GAME_OVER, PLAYING};
    private Ball ball;
    private BounceGame bounceGame;
    private HUD hud;
    private SubState state;
    private int bounces;
    private float timer;

    public PlayScreen(BounceGame game) {
        timer = 0;
        bounceGame = game;
        hud = new HUD(bounceGame.am.get(bounceGame.RSC_MONO_FONT));
        ball = new Ball(game);
        bounces = 0;

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
                    ball.xVelocity = vx;
                    ball.yVelocity = vy;
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

        // HUD Data
        hud.registerView("Bounces:", new HUDViewCommand(HUDViewCommand.Visibility.ALWAYS) {
            @Override
            public String execute(boolean consoleIsOpen) {
                return Integer.toString(bounces);
            }
        });
    }

    @Override
    public void show() {
        Gdx.app.log("PlayScreen", "show");
        state = SubState.READY;
        bounces = 0;

    }

    public void update(float delta) {
        timer += delta;
        // always update the ball, but ignore bounces unless we're in PLAY state
        if (ball.update() && state == SubState.PLAYING) {
            bounces++;
            if (bounces == 5) {
                state = SubState.GAME_OVER;
                timer = 0; // restart the timer.
            }
        }
        if (state == SubState.READY && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            state = SubState.PLAYING;
            bounces = 0;
        }
        if (state == SubState.GAME_OVER && timer > 3.0f) {
            state = SubState.READY;
        }
        // ignore key presses when console is open...
        if (!hud.isOpen()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                ball.yVelocity += 2;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                ball.yVelocity -= 2;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                ball.xVelocity -= 2;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                ball.xVelocity += 2;
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        bounceGame.batch.begin();
        ball.draw(bounceGame.batch);
        // this logic could also be pushed into a method on SubState enum
        switch (state) {
            case GAME_OVER:
                bounceGame.batch.draw(bounceGame.am.get(bounceGame.RSC_GAMEOVER_IMG, Texture.class), 200, 200);
                break;
            case READY:
                bounceGame.batch.draw(bounceGame.am.get(bounceGame.RSC_PRESSAKEY_IMG, Texture.class), 200, 200);
                break;
            case PLAYING:
                break;
        }
        hud.draw(bounceGame.batch);
        bounceGame.batch.end();
    }
}

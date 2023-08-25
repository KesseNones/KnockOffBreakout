package wsuv.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * HUD - the HUD allows simple text interaction with the game...
 */
public class HUD {
    final static String prompt = "> ";
    private int linesbuffered;
    private int xMargin;
    private int yMargin;
    private int rColumn;
    private float lineHeight;
    private boolean open;
    private BitmapFont font;
    private Deque<String> consoleLines;
    private StringBuilder currentLine;
    private Texture background;

    private InputAdapter inputAdapter = new InputAdapter() {
        public boolean keyTyped(char character) {
            String cmd;
            if (character == '`') {
                open = !open;
                return true;
            }
            if (open) {
                if (character == '\n') {
                    cmd = currentLine.toString();
                    String[] words = cmd.split("[ \t]+");
                    // todo... callback.
                    consoleLines.add(prompt + cmd);
                    while (consoleLines.size() >= linesbuffered) {
                        consoleLines.removeFirst();
                    }
                    currentLine.setLength(0);
                } else if (character == '\b') {
                    currentLine.setLength(currentLine.length() - 1);
                } else {
                    currentLine.append(character);
                }
                return true;
            }
            return false;
        }
    };
            /**
             * Make a HUD with sane defaults.
             */
    public HUD(BitmapFont fnt) {
        this(10, 13, 10, 5, fnt);
    }

    /**
     * Make a HUD
     *
     * @param linesbuffd  - number of lines of data buffered
     * @param xmargin     - xmargin from left of window (in pixels)
     * @param ymargin     - ymargin from top of window (in pixels)
     * @param rcol        - the location of the right column (in pixels) where
     *                      hud data is shown when the console is open.
     * @param fnt        - the font to use for display
     */
    public HUD(int linesbuffd, int xmargin, int ymargin, int rcol, BitmapFont fnt) {
        linesbuffered = linesbuffd;
        xMargin = xmargin;
        yMargin = ymargin;
        rColumn = rcol;
        currentLine = new StringBuilder(60);
        consoleLines = new ArrayDeque<String>();

        font = fnt;
        lineHeight = font.getLineHeight();

        // make a background for the console...bigger than needed!
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, .6f);
        pixmap.fill();
        background = new Texture(pixmap);
        pixmap.dispose();

        System.out.println("Creating HUD..."+ Gdx.input.getInputProcessor());
        Gdx.input.setInputProcessor(inputAdapter);
    }

    public boolean isOpen() { return open; }

    public void draw(Batch batch) {
        String console = "";
        int xlocation;

        // draw based on the open/closed status
        if (open) {
            batch.draw(background, 0, Gdx.graphics.getHeight() - ((font.getLineHeight())*linesbuffered) - yMargin);
            console = String.join("\n", consoleLines);
            if (console.equals("")) {
                console = prompt + currentLine.toString();

            } else {
                console = console + '\n' + prompt + currentLine.toString();
            }
            font.draw(batch, console, xMargin, Gdx.graphics.getHeight() - yMargin);
        }
    }
}

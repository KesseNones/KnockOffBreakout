package wsuv.bounce;

/**
 * A HUDActionCommand represents something the HUD can display
 * and the logic to update the display. (Follows the
 * Command pattern.)
 */
public abstract class HUDViewCommand {
    public enum Visibility { ALWAYS, WHEN_OPEN };
    public Visibility vis;

    public HUDViewCommand() {
        vis = Visibility.WHEN_OPEN;
    }
    public HUDViewCommand(Visibility desiredVisiblity) {
        vis = desiredVisiblity;
    }
    public abstract String execute(Visibility visibilityContext);

    public Visibility nextVisiblityState() {
        if (vis == Visibility.ALWAYS) vis = Visibility.WHEN_OPEN;
        else if (vis == Visibility.WHEN_OPEN) vis = Visibility.ALWAYS;
        return vis;
    }
}

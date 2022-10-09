package A4;

public class PlayableManager {
    private Playable defaultPlayable;

    public void setDefaultPlayable(Playable p) {
        assert p != null;
        defaultPlayable = p.clone();
    }

    public Playable getDefaultPlayable() {
        return defaultPlayable.clone();
    }

}

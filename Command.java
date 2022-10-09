package A4;

import java.util.Optional;

public interface Command {
    Optional<Playable> redo();
    void undo();
    Optional<Playable> execute();
}

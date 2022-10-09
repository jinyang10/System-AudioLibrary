package A4;
import java.util.*;

/**
 * Represents a sequence of playables to play in FIFO order.
 */
public class PlayList implements Playable {

    private Stack<Command> toRedoStack = new Stack<>();
    private Stack<Command> toUndoStack = new Stack<>();
    private boolean methodInRedo = true;

    private List<Playable> aList = new LinkedList<>();
    private String aName;

    /**
     * Creates a new empty playlist.
     *
     * @param pName
     *            the name of the list
     * @pre pName!=null;
     */
    public PlayList(String pName) {
        assert pName != null;
        aName = pName;
    }

    @Override
    public PlayList clone() {
        try {
            PlayList clone = (PlayList) super.clone();
            clone.aList = new LinkedList<>();
            for (Playable p : aList) {
                clone.aList.add(p.clone());
            }
            return clone;

        } catch(CloneNotSupportedException e) {
            assert false;
            return null;
        }
    }

    /**
     * Adds a playable at the end of this playlist.
     *
     * @param pPlayable
     *            the content to add to the list
     * @pre pPlayable!=null;
     */


    public void addPlayable(Playable pPlayable) {
        assert pPlayable != null;
        Command command = AddPlayableCommand(pPlayable);
        toUndoStack.push(command);
        toRedoStack.clear();
        methodInRedo = true;
        toRedoStack.push(command);
        command.execute();
    }
    private Command AddPlayableCommand(Playable playable) {
        assert playable != null;
        return new Command() {

            @Override
            public Optional<Playable> redo() {
                return execute();
            }

            @Override
            public void undo() {
                aList.remove(aList.size()-1);
            }

            @Override
            public Optional<Playable> execute() {
                aList.add(playable);
                return Optional.empty();
            }
        };
    }

    /**
     * remove a playable from the Playlist given its index
     * @param pIndex
     *          the index of playable to be removed
     * @return the removed playable
     */
    public Playable removePlayable(int pIndex) {
        assert pIndex >= 0 && pIndex < aList.size();
        Command command = removePlayableCommand(pIndex);
        toUndoStack.push(command);
        toRedoStack.clear();
        methodInRedo = true;
        toRedoStack.push(command);
        Optional<Playable> opt = command.execute();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public Command removePlayableCommand(int index) {
        assert index >= 0 && index < aList.size();
        return new Command() {
            Playable prev;
            @Override
            public Optional<Playable> redo() {
                return execute();
            }

            @Override
            public void undo() {
                aList.add(index, prev);
            }

            @Override
            public Optional<Playable> execute() {
                prev = aList.remove(index);
                return Optional.of(prev);
            }
        };
    }

    /**
     * @return The name of the playlist.
     */
    public String getName() {
        return aName;
    }

    /**
     * modify the name of the playlist
     * @param pName
     *          the new name of the playlist
     */
    public void setName(String pName) {
        assert pName != null;
        Command command = setNameCommand(pName);
        toUndoStack.push(command);
        toRedoStack.clear();
        methodInRedo = true;
        toRedoStack.push(command);
        command.execute();
    }

    public Command setNameCommand(String name) {
        assert name != null;
        return new Command() {
            final String prevName = aName;

            @Override
            public Optional<Playable> redo() {
                return execute();
            }

            @Override
            public void undo() {
                aName = prevName;
            }

            @Override
            public Optional<Playable> execute() {
                aName = name;
                return Optional.empty();
            }
        };
    }

    /**
     * Iterating through the playlist to play playable content.
     */
    @Override
    public void play() {
        for(Playable playable:aList){
            playable.play();
        }
    }
    /**

     * change the order how playlist play the playables it contains
     */
    public void shuffle() {
        Command command = shuffleCommand();
        toUndoStack.push(command);
        toRedoStack.clear();
        methodInRedo = true;
        toRedoStack.push(command);
        command.execute();
    }

    public Command shuffleCommand() {
        return new Command() {
            final List<Playable> prevList = new ArrayList<>();

            @Override
            public Optional<Playable> redo() {
                return Optional.empty();
            }

            @Override
            public void undo() {
                aList = prevList;
            }

            @Override
            public Optional<Playable> execute() {
                for (Playable p : aList) {
                    prevList.add(p.clone());
                }
                Collections.shuffle(aList, new Random(3));
                return Optional.empty();
            }
        };
    }

    public void undo() {
        if (!toUndoStack.isEmpty()) {
            Command undo = toUndoStack.pop();
            if (toRedoStack.size() == 1 && methodInRedo) {
                toRedoStack.clear();
                methodInRedo = false;
            }
            toRedoStack.push(undo);
            undo.undo();
        }
    }

    public void redo() {
        if (!toRedoStack.isEmpty()) {
            Command redo = toRedoStack.pop();
            toUndoStack.push(redo);
            redo.redo();
        }
    }

    /**
     * Checks is two playlists are equal based on playable objects and their order
     *
     * @param o
     *            The object to compare a playlist to
     * @return    This method returns true if the playlist is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayList playList = (PlayList) o;
        return this.aList.equals(playList.aList);
    }

    /**
     * Equal playlists have the same hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(aList);
    }

}

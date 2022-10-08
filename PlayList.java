package AudioLibSystem;

import java.util.*;

/**
 * Represents a sequence of playables to play in FIFO order.
 */
public class PlayList implements Playable {

	private List<Playable> aList = new LinkedList<>();
	private String aName;

	private final List<Command> aExecutedCommands = new ArrayList<>();
	private final List<Command> aUndoneCommands = new ArrayList<>();

	private boolean justModifiedState = false;

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

	/**
	 * Adds a playable at the end of this playlist.
	 *
	 * @param pPlayable
	 *            the content to add to the list
	 * @pre pPlayable!=null;
	 */
	private void addPlayable(Playable pPlayable) {
		assert pPlayable != null;
		aList.add(pPlayable);
	}

	/**
	 * remove a playable from the Playlist given its index
	 * @param pIndex
	 *          the index of playable to be removed
	 * @return the removed playable
	 */
	private Playable removePlayable(int pIndex) {
		assert pIndex >= 0 && pIndex < aList.size();
		return aList.remove(pIndex);
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
	private void setName(String pName) {
		assert pName != null;
		this.aName = pName;
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
	private void shuffle() {
		Collections.shuffle(aList);
	}

	/**
	 * @param sets thepList
	 */
	public void setSequence(List<Playable> pList) {
		aList = new LinkedList<>(pList);
	}

	/**
	 * Checks if two playlists are equal based on playable objects and their order
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

	public PlayList clone() {
		try 
		{
			PlayList clone = (PlayList) super.clone();
			clone.aList = new LinkedList<>();
			for ( Playable playable: aList)
			{
				clone.aList.add(playable.clone());
			}
			return clone;
		} 
		catch (CloneNotSupportedException e) {
			return null;
		}

	}

	public Command createAddPlayableCommand(Playable pPlayable) {
		assert pPlayable != null;
		return new Command() {
			@Override
			public void execute()
			{
				addPlayable(pPlayable);				
			}

			@Override
			public void undo() 
			{
				removePlayable(aList.indexOf(pPlayable));
			}
		};
	}

	public Command createRemovePlayableCommand(int pIndex) {
		assert pIndex >= 0 ;

		return new Command() {

			Playable aPlayable = aList.get(pIndex);

			@Override
			public void execute() {
				aPlayable = aList.get(pIndex);
				removePlayable(pIndex);
			}

			@Override
			public void undo() {
				addPlayable(aPlayable);

			}
		};
	}

	public Command createShuffleCommand() {
		return new Command() {

			List<Playable> preShuffled = new LinkedList<>(aList);

			@Override
			public void execute() {
				preShuffled = new LinkedList<>(aList);
				shuffle();				
			}

			@Override
			public void undo() {
				setSequence(preShuffled);
			}
		};
	}

	public Command createSetNameCommand(String pName) {
		assert pName != null;
		return new Command() {

			String aName = getName();

			@Override
			public void execute() {
				aName = getName();
				setName(pName);				
			}

			@Override
			public void undo() {
				setName(aName);
			}
		};
	}

	public void execute(Command pCommand) {
		assert pCommand != null;
		pCommand.execute();
		aExecutedCommands.add(pCommand);
		justModifiedState = true;

		// If another state-modifying method is called after undo(), 
		// redo() cannot redo any undone actions.
		aUndoneCommands.clear();
	}

	public void undo() {
		assert !aExecutedCommands.isEmpty();
		Command command = aExecutedCommands.remove(aExecutedCommands.size()-1);
		command.undo();
		aUndoneCommands.add(command);
		justModifiedState = false;
	}

	public void redo() {
		// redo() will execute the last method that was undone
		if( !aUndoneCommands.isEmpty() && !justModifiedState) {
			Command command = aUndoneCommands.remove(aUndoneCommands.size()-1);
			command.execute();
			justModifiedState = false;
		}

		// If redo() is called right after a state-modifying method, 
		// it repeats the execution of that method.
		else if (!aExecutedCommands.isEmpty() && justModifiedState) {
			Command command = aExecutedCommands.get(aExecutedCommands.size()-1);
			execute(command);
		}


	}

}

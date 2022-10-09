# System-AudioLibrary

Client can dynamically specify any Playable object for the library as a "default playable" - prototype pattern  
Implemented undo and redo functionality of modifying Playlists - command pattern  
Only 1 instance of audio Library is allowed to be initialized in the system - singleton pattern  

undo() reverts the state to the earlier one before the last state-modifying method was executed  
undo() multiple times consecutively undoes the methods in the reverse order of how they were executed  
undo() does nothing if there are no methods to be undone  

redo() will execute the last method that was undone  
redo() multiple times consecutively executes the undone methods in the reverse order of how they were undone  
redo() does nothing if there are no undone methods to be redone  
If redo() is called right after a state-modifying method, it repeats the execution of that method  

used JUnit to create unit tests  

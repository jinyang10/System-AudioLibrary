import A4.PlayList;
import A4.PlayableManager;
import A4.Podcast;
import A4.Song;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCommands {
    PlayList playList1 = new PlayList("myplaylist");
    PlayList playList2 = new PlayList("myplaylist2");
    Song s1 = new Song("Song1", "mj");
    Song s2 = new Song("Song2", "artist1");
    Song s3 = new Song("Song3", "artist2");

    @Test
    void testPlaylistRedoAfterMethod() {
        playList1.addPlayable(s1);
        playList2.addPlayable(s1); playList2.addPlayable(s1);
        playList1.redo();
        assertEquals(playList2, playList1);
    }

    @Test
    void testPlaylistRedoAfterUndo() {
        playList1.addPlayable(s1);
        playList1.undo();
        playList1.redo();
        playList2.addPlayable(s1);
        assertEquals(playList2, playList1);
    }
    @Test
    void testPlaylistNoMoreRedoLeft() {
        playList1.addPlayable(s1);
        playList1.undo();
        playList1.redo();
        playList1.redo();
        playList2.addPlayable(s1);
        assertEquals(playList2, playList1);
    }
    @Test
    void testPlaylistUndoAfterMethod() {
        playList1.addPlayable(s1);
        playList1.shuffle();
        playList1.undo();
        playList2.addPlayable(s1);
        assertEquals(playList2, playList1);
    }

    @Test
    void testPlaylistUndoAfterRedo() {
        playList1.addPlayable(s1); playList1.addPlayable(s2); playList1.addPlayable(s3);
        playList2.addPlayable(s1); playList2.addPlayable(s2); playList2.addPlayable(s3);

        playList1.shuffle();
        playList1.redo();
        playList1.undo();

        playList2.shuffle();
        assertEquals(playList2, playList1);
    }

    @Test
    void testUndoAfterAddandShuffle() {
        playList1.addPlayable(s1);
        playList1.addPlayable(s2);
        playList1.addPlayable(s3);

        playList1.shuffle();
        playList1.undo();

        playList2.addPlayable(s1); playList2.addPlayable(s2); playList2.addPlayable(s3);

        assertEquals(playList2, playList1);
    }

}

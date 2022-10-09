import A4.PlayList;
import A4.PlayableManager;
import A4.Podcast;
import A4.Song;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestStateModifyingCommands {
    PlayList playList1 = new PlayList("myplaylist");
    PlayList playList2 = new PlayList("myplaylist2");
    Song s1 = new Song("Song1", "michael jackson");

    @Test
    void testAddPlayable() {
        playList1.addPlayable(s1);
        playList2.addPlayable(s1);
        assertEquals(playList1, playList2);
    }

    @Test
    void testRemovePlayable() {
        Song s2 = new Song("song2", "artist1");
        playList1.addPlayable(s1); playList1.addPlayable(s2);

        playList2.addPlayable(s1);
        playList1.removePlayable(1);
        assertEquals(playList2, playList1);
    }

    @Test
    void testSetName() {
        playList1.setName("myplaylist2");
        assertEquals("myplaylist2", playList1.getName());
    }

    @Test
    void testShuffle() {
        Song s2 = new Song("Song2", "artist1");
        Song s3 = new Song("Song3", "artist2");
        playList1.addPlayable(s1); playList1.addPlayable(s2); playList1.addPlayable(s3);
        playList2.addPlayable(s1); playList2.addPlayable(s2); playList2.addPlayable(s3);
        playList1.shuffle();
        playList2.shuffle();
        assertEquals(playList1, playList2);
    }

}

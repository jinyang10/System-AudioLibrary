import A4.PlayList;
import A4.PlayableManager;
import A4.Podcast;
import A4.Song;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDefaultPlayable {
    PlayableManager pm = new PlayableManager();

    @Test
    void testSetDefaultPlayableSong() {
        Song s1 = new Song("Song1", "michael jackson");
        pm.setDefaultPlayable(s1);
        assertEquals(s1, pm.getDefaultPlayable());
    }

    @Test
    void testSetDefaultPlayableEpisode() {
        Podcast.Episode ep1;
        Podcast podcast1 = new Podcast("Pod1", "host1");
        ep1 = podcast1.createAndAddEpisode("ep1");
        pm.setDefaultPlayable(ep1);
        assertEquals(ep1, pm.getDefaultPlayable());
    }

    @Test
    void testSetDefaultPlayablePlaylist() {
        Song s1 = new Song("Song1", "michael jackson");
        Song s2 = new Song("Song2", "lil baby");
        Song s3 = new Song("Song3", "artist 1");
        Podcast podcast1 = new Podcast("Pod1", "host1");
        Podcast.Episode ep1 = podcast1.createAndAddEpisode("ep1");
        Podcast.Episode ep2 = podcast1.createAndAddEpisode("ep2");

        PlayList playList1 = new PlayList("myPlaylist");
        playList1.addPlayable(s1); playList1.addPlayable(s2); playList1.addPlayable(s3); playList1.addPlayable(ep1);
        playList1.addPlayable(ep2);

        pm.setDefaultPlayable(playList1);
        assertEquals(playList1, pm.getDefaultPlayable());
    }

}

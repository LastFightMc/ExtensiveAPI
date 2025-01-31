package fr.skoupi.extensiveapi.core.collection;

import org.junit.jupiter.api.*;

public class RankingListTest {

    static RankingList<String> leaderboard;

    @BeforeEach
    void beforeEach() {
        if (leaderboard == null)
            leaderboard = new RankingList<>();
        else
            leaderboard.clear();
        leaderboard.insert("vSKAH");
        leaderboard.insert("Player1");
        leaderboard.insert("Player2");
    }

    @Test
    @DisplayName("Checking the first")
    void leaderboardTest() {
        leaderboard.incrementScore("vSKAH", 3);
        leaderboard.incrementScore("Player1", 2);
        leaderboard.incrementScore("Player2", 1);

        String firstPlayer = leaderboard.getFirstPair().getKey();
        Assertions.assertEquals("vSKAH", firstPlayer);
    }

    @Test
    @DisplayName("Checking equality")
    void leaderboardEqualityTest() {
        leaderboard.incrementScore("vSKAH", 1);
        leaderboard.incrementScore("Player1", 1);
        leaderboard.incrementScore("Player2", 1);
        Assertions.assertEquals(leaderboard.getFirstPair().getValue(), leaderboard.getLastPair().getValue());
    }

    @Test
    @DisplayName("Test clearing, insertion and removal")
    void insertionAndRemovalTest() {
        leaderboard.clear();
        Assertions.assertTrue(leaderboard.insert("Player3"));
        Assertions.assertTrue(leaderboard.contains("Player3"));
        Assertions.assertTrue(leaderboard.remove("Player3"));
        Assertions.assertFalse(leaderboard.contains("Player3"));
    }

    @Test
    @DisplayName("Test increment and decrement score")
    void incrementAndDecrementScoreTest() {
        leaderboard.incrementScore("vSKAH", 5);
        Assertions.assertEquals(5.0, leaderboard.getScore("vSKAH"));

        leaderboard.decrementScore("vSKAH");
        Assertions.assertEquals(4.0, leaderboard.getScore("vSKAH"));
    }

    @Test
    @DisplayName("Test get position and score")
    void getPositionAndScoreTest() {
        leaderboard.incrementScore("vSKAH", 3);
        leaderboard.incrementScore("Player1", 2);
        leaderboard.incrementScore("Player2", 1);

        Assertions.assertEquals(0, leaderboard.getPosition("vSKAH"));
        Assertions.assertEquals(3.0, leaderboard.getScore("vSKAH"));
    }


}

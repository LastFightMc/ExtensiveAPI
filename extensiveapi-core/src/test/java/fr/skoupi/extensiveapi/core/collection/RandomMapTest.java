package fr.skoupi.extensiveapi.core.collection;

import org.junit.jupiter.api.*;

public class RandomMapTest {


    @DisplayName("Test Total Equals 100")
    @Test
    void testTotal() {
        RandomMap<String> randomMap = new RandomMap<>();
        randomMap.add(100, "This String has 100 weight");
        Assertions.assertEquals(randomMap.getTotal(), 100);
    }

    @DisplayName("100 Percent Find")
    @Test
    void test100Percent() {
        RandomMap<String> randomMap = new RandomMap<>();
        randomMap.add(100, "This String has 100 weight");
        Assertions.assertEquals(randomMap.next(), "This String has 100 weight");
    }

    @DisplayName("Random Not Null Test")
    @Test
    void randomTest() {
        RandomMap<String> randomMap = new RandomMap<>();
        Assertions.assertNotNull(randomMap.getRandom());
    }


    @DisplayName("Random Test")
    @Test
    void testRandom() {
        RandomMap<String> randomMap = new RandomMap<>();
        randomMap.add(50, "This String has 50 weight");
        randomMap.add(50, "This String has 50 weight");
        Assertions.assertNotNull(randomMap.next());
    }

}

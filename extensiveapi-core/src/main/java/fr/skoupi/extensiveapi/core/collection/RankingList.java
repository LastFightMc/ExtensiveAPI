/*  RankingList
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * 14/01/2025
 */

package fr.skoupi.extensiveapi.core.collection;

import fr.skoupi.extensiveapi.core.models.BasicPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RankingList<K> implements Comparator<K> {

    private final List<K> list;
    private final Map<K, Double> scores;
    private final SortMethod sortType;

    public RankingList(SortMethod sortType) {
        this.list = new ArrayList<>();
        this.scores = new HashMap<>();
        this.sortType = sortType;
    }

    /**
     * Light constructor, by default the sortMethod used is "ON_DEMAND"
     */
    public RankingList() {
        this(SortMethod.ON_DEMAND);
    }

    /**
     * Get the first key and his score
     *
     * @return BasicPar of key and value
     */
    public BasicPair<K, Double> getFirstPair() {
        return getPair(0);
    }

    /**
     * Get the key and his score
     *
     * @return BasicPar of key and value
     */
    public BasicPair<K, Double> getPair(int position) {
        if (list.isEmpty()) return BasicPair.empty();
        if (position < 0 || position >= list.size()) return BasicPair.empty();

        final K key = this.list.get(position);
        final var value = this.scores.get(key);
        return new BasicPair<>(key, value);
    }


    /**
     * Get the last key and his score
     *
     * @return BasicPar of key and value
     */
    public BasicPair<K, Double> getLastPair() {
        return getPair(list.size() - 1);
    }


    /**
     * @param key The object you want to add !
     * @return true if the object has been added or false if object is null or already present.
     */
    public boolean insert(@NotNull K key) {
        return insert(key, 0);
    }

    /**
     * @param key          The object you want to add !
     * @param defaultScore The default value of the key
     * @return true if the object has been added or false if object is null or already present.
     */
    public boolean insert(K key, double defaultScore) {
        if (key == null || list.contains(key)) return false;

        list.add(key);
        scores.put(key, defaultScore);
        if (sortType == SortMethod.IO || sortType == SortMethod.IO_AND_SCORING) sort();
        return true;
    }

    /**
     * @param key The object you want to remove" !
     * @return true if the object has been removed or false if object is null or already present.
     */
    public boolean remove(K key) {
        if (key == null) return false;

        final var rResult = list.remove(key);
        if (rResult) {
            scores.remove(key);

            if (sortType == SortMethod.IO || sortType == SortMethod.IO_AND_SCORING) sort();
        }
        return rResult;
    }

    /**
     * Check if the collection contains a key in list and scores.
     *
     * @param key The key to check
     * @return true if the key in contained inside list and scores. Else return false
     */
    public boolean contains(K key) {
        if (key == null) return false;
        return list.contains(key) && scores.containsKey(key);
    }

    private void clearingKey(K key) {
        list.remove(key);
        scores.remove(key);
    }

    /**
     * Increment by 'value' the score of the "Key"
     *
     * @param key   Object present in the collection
     * @param value The current player score will be incremented by value
     * @return false if the object is not present or exception throws, true if the operation is a success;
     */
    public boolean incrementScore(@NotNull K key, double value) {
        try {
            if (!contains(key)) {
                //Removing from collections to assure a certain "atomicity"
                clearingKey(key);
                return false;
            }
            scores.put(key, scores.get(key) + value);
            if (sortType == SortMethod.SCORING || sortType == SortMethod.IO_AND_SCORING) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Increment by '1' the score of the "Key"
     *
     * @param key Object present in the collection
     * @return false if the object is not present or exception throws, true if the operation is a success;
     */
    public boolean incrementScore(@NotNull K key) {
        return incrementScore(key, 1);
    }


    /**
     * Decrement by '1' the score of the "Key"
     *
     * @param key Object present in the collection
     * @return false if the object is not present or exception throws, true if the operation is a success;
     */
    public boolean decrementScore(@NotNull K key) {
        try {
            if (!contains(key)) {
                //Removing from collections to assure a certain "atomicity"
                clearingKey(key);
                return false;
            }
            double score = scores.get(key) - 1;
            if (score < 0) score = 0;
            scores.put(key, score);
            if (sortType == SortMethod.SCORING || sortType == SortMethod.IO_AND_SCORING) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Change the score of the "Key" by the new value in parameter
     *
     * @param key Object present in the collection
     * @return false if the object is not present or exception throws, true if the operation is a success;
     */
    public boolean setScore(@NotNull K key, double newScore) {
        try {
            if (!list.contains(key) || !scores.containsKey(key)) {
                //Removing from collections to assure a certain "atomicity"
                clearingKey(key);
                return false;
            }
            scores.put(key, newScore);
            if (sortType == SortMethod.SCORING || sortType == SortMethod.IO_AND_SCORING) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the position of an Element in the ranking.
     *
     * @param key Object present in the collection
     * @return the position of an "Key" in the ranking, or -1 if not find.
     */
    public int getPosition(@NotNull K key) {
        return list.indexOf(key);
    }

    /**
     * Get the position of an Element in the ranking.
     *
     * @param key Object present in the collection
     * @return the position of an "Key" in the ranking, or {defaultValue} if not find.
     */
    public int getPositionOrDefault(@NotNull K key, int defaultValue) {
        final var rResult = list.indexOf(key);
        return rResult == -1 ? defaultValue : rResult;
    }

    /**
     * Get the score of an Element in the ranking.
     *
     * @param key Object present in the collection
     * @return the score of a "Key" in the ranking, or -1 if not find.
     */
    public double getScore(@NotNull K key) {
        return scores.getOrDefault(key, -1.0D);
    }

    /**
     * Get the score of an Element in the ranking.
     *
     * @param key Object present in the collection
     * @return the score of a "Key" in the ranking, or {defaultValue} if not find.
     */
    public double getScoreOrDefault(@NotNull K key, double defaultValue) {
        return scores.getOrDefault(key, defaultValue);
    }


    /**
     * Get the key at the given position.
     *
     * @param position
     * @return a key or null.
     */
    public @Nullable K getKeyAt(int position) {
        return list.size() > position ? list.get(position) : null;
    }

    /**
     * Sort the Ranking elements.
     * You need to call this function only if the 'SortMethod' of your RankingList is "ON_DEMAND"
     */
    public void sort() {
        list.sort(this);
    }

    /**
     * Sort the Ranking elements.
     * You need to call this function only if the 'SortMethod' of your RankingList is "ON_DEMAND"
     *
     * @param comparator If you want uses another sort type.
     */
    public void sort(@NotNull Comparator<K> comparator) {
        list.sort(comparator);
    }

    /**
     * Clear the collection
     */
    public void clear() {
        list.clear();
        scores.clear();
    }

    public int getSize() {
        return list.size();
    }

    /**
     * @param key1 the first object to be compared.
     * @param key2 the second object to be compared.
     * @return The value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
     */
    @Override
    public int compare(K key1, K key2) {
        if (key1 == null || key2 == null)
            throw new IllegalArgumentException("The keys to compare must not be null.");

        double score1 = scores.getOrDefault(key1, 0.0D);
        double score2 = scores.getOrDefault(key2, 0.0D);

        return Double.compare(score2, score1);
    }

    /**
     * Printing some shit.
     * Usefull for debugging.
     */
    public void print() {
        System.out.println("Contents of the list: ");
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(stringBuilder::append);
        stringBuilder.append("\n------------------");
        System.out.println(stringBuilder);

        System.out.println("Content of the map");
        System.out.println(scores);
        System.out.println("----------------------");

        System.out.println("Process Atomicity Check: ");
        long time = System.currentTimeMillis();

        boolean listError = false;
        for (K k : list) {
            if (!scores.containsKey(k)) {
                listError = true;
                break;
            }
        }
        boolean mapError = false;
        if (!listError) {
            for (K k : scores.keySet()) {
                if (!list.contains(k)) {
                    mapError = true;
                    break;
                }
            }
        }

        System.out.println("Result of Atomicity Check: ");
        System.out.println("Result of list error : " + (listError ? "Error Finded" : "No Error"));
        System.out.println("Result of map error : " + (mapError ? "Error Finded" : "No Error"));
        System.out.println("Time elapsed : " + (System.currentTimeMillis() - time + " (ms) ! "));
        System.out.println("--------------------------");
    }

    public enum SortMethod {
        IO, //On Each Input / Output operation (insert, remove methods)
        SCORING, //On Each Score changes (increment, decrements methods)
        IO_AND_SCORING, // Same of previous but combined
        ON_DEMAND // Don't enable automatic sort the developer must call the sort() method !
    }

}

/*  RankingList
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * 14/01/2025
 */

package fr.skoupi.extensiveapi.core.collection;

import java.util.*;

import fr.skoupi.extensiveapi.core.models.BasicPair;
import org.jetbrains.annotations.NotNull;

/**
 * A generic ranking list that maintains a list of elements and their associated scores.
 * The list can be sorted automatically based on the scores or manually by calling the sort method.
 *
 * @param <K> The type of elements in the ranking list.
 */
public class RankingList<K> implements Comparator<K> {

    private List<K> list;
    private Map<K, Double> scores;
    private final SortMethod sortType;

    /**
     * Constructs a new RankingList with the specified sort method.
     *
     * @param sortType The sort method to use for this ranking list.
     */
    public RankingList(SortMethod sortType) {
        this.list = new ArrayList<>();
        this.scores = new HashMap<>();
        this.sortType = sortType;
    }

    /**
     * Constructs a new RankingList with the default sort method "ON_DEMAND".
     */
    public RankingList() {
        this(SortMethod.ON_DEMAND);
    }

    /**
     * Inserts a key into the ranking list with a default score of 0.
     *
     * @param key The key to insert.
     * @return true if the key was successfully added, false if the key is null or already present.
     * @deprecated Use {@link #insert(K, double)} instead.
     */
    @Deprecated
    public boolean insert(@NotNull K key) {
        return insert(key, 0);
    }

    /**
     * Inserts a key into the ranking list with the specified default score.
     *
     * @param key          The key to insert.
     * @param defaultScore The default score for the key.
     * @return true if the key was successfully added, false if the key is null or already present.
     * @deprecated Use {@link #insert(K, double)} instead.
     */
    @Deprecated
    public boolean insert(@NotNull K key, double defaultScore) {
        if (list.contains(key)) return false;

        list.add(key);
        scores.put(key, defaultScore);
        if (sortType == SortMethod.IO) sort();
        return true;
    }

    /**
     * Removes a key from the ranking list.
     *
     * @param key The key to remove.
     */
    public void remove(@NotNull K key) {
        list.remove(key);
        scores.remove(key);

        if (sortType == SortMethod.IO) sort();
    }

    /**
     * Checks if the ranking list contains the specified key.
     *
     * @param key The key to check.
     * @return true if the key is present in both the list and scores map, false otherwise.
     */
    public boolean contains(@NotNull K key) {
        return list.contains(key) && scores.containsKey(key);
    }

    /**
     * Increments the score of the specified key by the given value.
     *
     * @param key   The key whose score to increment.
     * @param value The value to increment the score by.
     * @return true if the operation was successful, false if an exception occurred.
     */
    public boolean incrementScore(@NotNull K key, double value) {
        try {
            if (!list.contains(key)) list.add(key);
            scores.put(key, scores.getOrDefault(key, 0.0D) + value);
            if (sortType == SortMethod.IO) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Increments the score of the specified key by 1.
     *
     * @param key The key whose score to increment.
     * @return true if the operation was successful, false if an exception occurred.
     */
    public boolean incrementScore(@NotNull K key) {
        return incrementScore(key, 1);
    }

    /**
     * Decrements the score of the specified key by the given value.
     *
     * @param key   The key whose score to decrement.
     * @param value The value to decrement the score by.
     * @return true if the operation was successful, false if an exception occurred or the key is not present.
     */
    public boolean decrementScore(@NotNull K key, double value) {
        try {
            if (!contains(key)) return false;

            double score = scores.get(key) - value;
            if (score < 0) score = 0;
            scores.put(key, score);
            if (sortType == SortMethod.IO) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Decrements the score of the specified key by 1.
     *
     * @param key The key whose score to decrement.
     * @return true if the operation was successful, false if an exception occurred or the key is not present.
     */
    public boolean decrementScore(@NotNull K key) {
        return decrementScore(key, 1);
    }

    /**
     * Sets the score of the specified key to the given value.
     *
     * @param key      The key whose score to set.
     * @param newScore The new score value.
     * @return true if the operation was successful, false if an exception occurred or the key is not present.
     */
    public boolean setScore(@NotNull K key, double newScore) {
        try {
            if (!list.contains(key)) list.add(key);
            scores.put(key, newScore);
            if (sortType == SortMethod.IO) sort();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the first key and its score in the ranking list.
     *
     * @return A pair containing the first key and its score.
     */
    public @NotNull BasicPair<K, Double> getFirstPair() {
        return getPair(0);
    }

    /**
     * Gets the key and its score at the specified position in the ranking list.
     *
     * @param position The position of the key to retrieve.
     * @return A pair containing the key and its score at the specified position.
     */
    public @NotNull BasicPair<K, Double> getPair(int position) {
        if (list.isEmpty()) return BasicPair.empty();
        if (position < 0 || position >= list.size()) return BasicPair.empty();

        final K key = this.list.get(position);
        final var value = this.scores.get(key);
        return new BasicPair<>(key, value);
    }

    /**
     * Gets the last key and its score in the ranking list.
     *
     * @return A pair containing the last key and its score.
     */
    public @NotNull BasicPair<K, Double> getLastPair() {
        return getPair(list.size() - 1);
    }

    /**
     * Gets the position of the specified key in the ranking list.
     *
     * @param key The key whose position to retrieve.
     * @return The position of the key in the ranking list, or -1 if the key is not found.
     */
    public int getPosition(@NotNull K key) {
        return list.indexOf(key);
    }

    /**
     * Gets the position of the specified key in the ranking list, or a default value if the key is not found.
     *
     * @param key          The key whose position to retrieve.
     * @param defaultValue The default value to return if the key is not found.
     * @return The position of the key in the ranking list, or the default value if the key is not found.
     */
    public int getPositionOrDefault(@NotNull K key, int defaultValue) {
        final var result = getPosition(key);
        return result == -1 ? defaultValue : result;
    }

    /**
     * Gets the score of the specified key in the ranking list.
     *
     * @param key The key whose score to retrieve.
     * @return The score of the key in the ranking list, or 0.0 if the key is not found.
     */
    public double getScore(@NotNull K key) {
        return getScoreOrDefault(key, 0.0D);
    }

    /**
     * Gets the score of the specified key in the ranking list, or a default value if the key is not found.
     *
     * @param key          The key whose score to retrieve.
     * @param defaultValue The default value to return if the key is not found.
     * @return The score of the key in the ranking list, or the default value if the key is not found.
     */
    public double getScoreOrDefault(@NotNull K key, double defaultValue) {
        return scores.getOrDefault(key, defaultValue);
    }

    /**
     * Sorts the ranking list elements.
     * You need to call this method only if the 'SortMethod' of your RankingList is "ON_DEMAND".
     */
    public void sort() {
        list.sort(this);
    }

    /**
     * Sorts the ranking list elements using the specified comparator.
     * You need to call this method only if the 'SortMethod' of your RankingList is "ON_DEMAND".
     *
     * @param comparator The comparator to use for sorting.
     */
    public void sort(@NotNull Comparator<K> comparator) {
        list.sort(comparator);
    }

    /**
     * Trims the list and map to keep only the first 'elementsToKeep' entries.
     *
     * @param elementsToKeep The number of entries to keep in the list and map.
     * @throws IllegalArgumentException if the number of elements to keep is negative.
     */
    public void trim(int elementsToKeep) throws IllegalArgumentException {
        if (elementsToKeep < 0) {
            throw new IllegalArgumentException("The number of elements to keep must be non-negative.");
        }

        if (this.list.size() <= elementsToKeep) {
            elementsToKeep = this.list.size();
        }

        List<K> trimmedList = this.list.subList(0, elementsToKeep);
        Map<K, Double> trimmedScores = new HashMap<>();

        for (K key : trimmedList) {
            trimmedScores.put(key, this.scores.get(key));
        }

        // Update the list and map
        this.list = trimmedList;
        this.scores = trimmedScores;

        if (sortType == SortMethod.IO) sort();
    }

    /**
     * Clears the ranking list.
     */
    public void clear() {
        list.clear();
        scores.clear();
    }

    /**
     * Gets the size of the ranking list.
     *
     * @return The number of elements in the ranking list.
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Compares two keys based on their scores.
     *
     * @param key1 The first key to be compared.
     * @param key2 The second key to be compared.
     * @return A negative integer, zero, or a positive integer as the first key is less than, equal to, or greater than the second key.
     * @throws IllegalArgumentException if either key is null.
     */
    @Override
    public int compare(K key1, K key2) throws IllegalArgumentException {
        if (key1 == null || key2 == null)
            throw new IllegalArgumentException("The keys to compare must not be null.");

        double score1 = scores.getOrDefault(key1, 0.0D);
        double score2 = scores.getOrDefault(key2, 0.0D);

        return Double.compare(score2, score1);
    }

    /**
     * Enum representing the sort method for the ranking list.
     */
    public enum SortMethod {
        IO, // On Each Input/Output operation (insert, remove methods)
        ON_DEMAND // Don't enable automatic sort; the developer must call the sort() method.
    }
}

package fr.skoupi.extensiveapi.core.collection;

/*  RandomMap
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import lombok.Getter;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

@Getter
public class RandomMap<E> {

    private final NavigableMap<Double, Object> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomMap() {
        this(new Random());
    }

    public RandomMap(Random random) {
        this.random = random;
    }

    /**
     * Add an object with a weight to the map
     * @param weight The weight of the object (the higher the weight, the more likely it is to be selected)
     * @param object The object to add
     * @return RandomMap instance
     */
    public RandomMap<E> add(double weight, Object object) {
        if (weight <= 0)
            return this;
        total += weight;
        map.put(total, object);
        return this;
    }

    /**
     * Get a random object from the map
     * @return The random object
     * @throws NullPointerException If the map is empty
     *
     * @exemple YourTemplateType result = (YourTemplateType) YourRandomMapInstance.next();
     */
    public Object next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

}
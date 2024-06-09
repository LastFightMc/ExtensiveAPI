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

    public RandomMap<E> add(double weight, Object object) {
        if (weight <= 0)
            return this;
        total += weight;
        map.put(total, object);
        return this;
    }

    public Object next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

}
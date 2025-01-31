/*  BasicPair
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 28/01/2025
 */

package fr.skoupi.extensiveapi.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class BasicPair<K, V> {

    private @Nullable K key;
    private @Nullable V value;

    public static <K, V> BasicPair<K, V> of(K key, V value) {
        return new BasicPair<>(key, value);
    }

    public static <K, V> BasicPair<K, V> empty() {
        return new BasicPair<>(null, null);
    }

    public boolean isEmpty() {
        return key == null && value == null;
    }
}

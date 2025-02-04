/*  BufferedPayment
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 04/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.economy;

import fr.skoupi.extensiveapi.minecraft.economy.interfaces.IEconomyType;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class BufferedPayment {

    private final OfflinePlayer offlinePlayer;
    private final Map<IEconomyType, Double> payments = new HashMap<>();

    public BufferedPayment(OfflinePlayer offlinePlayer, IEconomyType type, double amount) {
        this.offlinePlayer = offlinePlayer;
        this.payments.put(type, amount);
    }

    public double get(IEconomyType type) {
        return payments.getOrDefault(type, 0.0);
    }

    public void set(IEconomyType type, double amount) {
        payments.put(type, amount);
    }

    public Set<IEconomyType> getTypes() {
        return payments.keySet();
    }
}

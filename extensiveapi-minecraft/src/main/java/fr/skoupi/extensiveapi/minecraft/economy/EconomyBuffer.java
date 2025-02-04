/*  EconomyBuffer
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 04/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.economy;

import fr.skoupi.extensiveapi.minecraft.economy.interfaces.IEconomyType;
import fr.skoupi.extensiveapi.minecraft.utils.ActionBarUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Getter
public class EconomyBuffer {

    private final Map<OfflinePlayer, BufferedPayment> paymentCache = new ConcurrentHashMap<>();
    private final JavaPlugin plugin;
    private final Economy economy;

    /**
     * Add payment to player's payment buffer
     *
     * @param payment - payment to be paid
     */
    public void pay(BufferedPayment payment) {
        paymentCache.merge(payment.getOfflinePlayer(), payment, (existing, newPayment) -> {
            for (Map.Entry<IEconomyType, Double> entry : newPayment.getPayments().entrySet()) {
                existing.set(entry.getKey(), existing.get(entry.getKey()) + entry.getValue());
            }
            return existing;
        });
    }

    /**
     * Payout all players the amount they are going to be paid
     */
    public void payAll() {
        if (paymentCache.isEmpty() || !plugin.isEnabled())
            return;

        // Schedule all payments
        for (BufferedPayment payment : paymentCache.values()) {
            schedulePayment(payment);
        }

        // Empty payment cacheplayerId
        paymentCache.clear();
    }


    private void schedulePayment(BufferedPayment payment) {
        final var player = payment.getOfflinePlayer();
        if (player == null || !player.isOnline()) return;
        for (Map.Entry<IEconomyType, Double> paymentsBuffer : payment.getPayments().entrySet()) {
            if (!paymentsBuffer.getKey().processPayment(player, paymentsBuffer.getValue())) {
                plugin.getLogger().warning("Payment for " + player.getName() + " failed!");
                plugin.getLogger().warning("EconomyType: " + paymentsBuffer.getKey().getEconomyName() + ", amount: " + paymentsBuffer.getValue());
            }
        }
    }

    private String getDecimalPlacesMoney() {
        return "%.2f";
    }

    /**
     * Shows the payment in actionbar or chat for the given player if online.
     *
     * @param payment {@link BufferedPayment}
     */
    public void showPayment(BufferedPayment payment, DisplayType displayType, IEconomyType economyType, String message) {
        final var player = payment.getOfflinePlayer().getPlayer();
        if (player == null || !player.isOnline()) return;

        long startAt = System.currentTimeMillis();
        switch (displayType) {
            case ACTION_BAR:
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (task) -> {
                    if (!player.isOnline() || !paymentCache.containsKey(player) || System.currentTimeMillis() > startAt + 3000L) {
                        task.cancel();
                        return;
                    }

                    ActionBarUtils.sendActionBar(player, message.replace("%amount%", String.format(getDecimalPlacesMoney(), payment.getPayments().get(economyType))));
                }, 10L, 30L);
                break;
            case MESSAGE:
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (task) ->
                {
                    if (!player.isOnline() || !paymentCache.containsKey(player)) {
                        task.cancel();
                        return;
                    }
                    player.sendMessage(message.replace("%amount%", String.format(getDecimalPlacesMoney(), payment.getPayments().get(economyType))));
                }, 40L);
                break;
            default:
                break;
        }
    }


    public enum DisplayType {
        ACTION_BAR,
        MESSAGE
    }
}
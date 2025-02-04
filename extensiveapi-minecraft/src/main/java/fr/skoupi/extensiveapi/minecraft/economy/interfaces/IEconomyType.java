/*  AbstractEconomyType
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 04/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.economy.interfaces;

import org.bukkit.OfflinePlayer;

public interface IEconomyType {

    String getEconomyName();
    boolean processPayment(OfflinePlayer player, double amount);

}

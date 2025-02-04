/*  VaultEconomyType
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 04/02/2025
 */

package fr.skoupi.extensiveapi.minecraft.economy.interfaces.impl;

import fr.skoupi.extensiveapi.minecraft.economy.interfaces.IEconomyType;
import fr.skoupi.extensiveapi.minecraft.hooks.Hooks;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

public class VaultEconomyType implements IEconomyType {

    private final Economy economy;

    public VaultEconomyType() {
        this.economy = (Economy) Hooks.getInstance().getLoaded().get("VAULT").getHook();
    }

    @Override
    public String getEconomyName() {
        return "Vault";
    }

    @Override
    public boolean processPayment(OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }
}

package fr.skoupi.extensiveapi.minecraft.hooks.basics;

/*  VaultHook
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import net.milkbowl.vault.economy.Economy;

public class VaultHook extends AbstractHook<Economy> {

	private Economy vaultEconomy;

	public VaultHook() {
		super("Vault", "net.milkbowl.vault.economy.Economy");
	}

	@Override
	public boolean registerHook () {
		if (!pluginEnabled()) return false;
		this.vaultEconomy = getProvider(Economy.class);
		return this.vaultEconomy != null;
	}

	@Override
	public Economy getHook () {
		return vaultEconomy;
	}

}

package fr.skoupi.extensiveapi.minecraft.hooks.basics;

import dev.unnm3d.rediseconomy.api.RedisEconomyAPI;
import dev.unnm3d.rediseconomy.currency.Currency;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;


public class RedisEconomyHook extends AbstractHook<RedisEconomyAPI> {

    private RedisEconomyAPI redisEconomyAPI;

    public RedisEconomyHook() {
        super("RedisEconomy", "dev.unnm3d.rediseconomy.RedisEconomyPlugin");
    }

    @Override
    public boolean registerHook() {
        if (!pluginEnabled()) return false;
        redisEconomyAPI = RedisEconomyAPI.getAPI();
        return true;
    }

    @Override
    public RedisEconomyAPI getHook() {
        return redisEconomyAPI;
    }

    public Currency getCurrency(String currencyName) {
        return getHook().getCurrencyByName(currencyName);
    }
}

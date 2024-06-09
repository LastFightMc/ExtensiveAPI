package fr.skoupi.extensiveapi.minecraft.hooks.basics;

/*  RosePlayerPoints
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import org.black_ixx.playerpoints.PlayerPoints;

public class RosePlayerPointsHook extends AbstractHook<PlayerPoints> {

	private PlayerPoints playerPoints;

	public RosePlayerPointsHook() {
		super("PlayerPoints", "org.black_ixx.playerpoints.PlayerPoints");
	}

	@Override
	public boolean registerHook ()
	{
		if (!pluginEnabled()) return false;
		playerPoints = PlayerPoints.getInstance();
		return true;
	}

	@Override
	public PlayerPoints getHook()
	{
		return playerPoints;
	}
}

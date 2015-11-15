package ftb;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1710.util.MineTweakerHacks;
import minetweaker.runtime.IScriptProvider;
import minetweaker.runtime.providers.ScriptProviderCascade;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ftb.lib.api.EventFTBModeSet;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.util.ArrayList;

public class ReloadHandler
{
	@SubscribeEvent
	public void onReloaded(EventFTBModeSet e)
	{
		if (!e.getFile("scripts").exists())
		{
			e.getFile("scripts").mkdirs();
		}
		if (!e.getCommonFile("scripts").exists()) {
			e.getCommonFile("scripts").mkdirs();
		}
		MineTweakerAPI.tweaker.rollback();

		ArrayList<IScriptProvider> providers = new ArrayList<IScriptProvider>();

		providers.add(new ScriptProviderDirectory(e.getCommonFile("scripts")));
		providers.add(new ScriptProviderDirectory(e.getFile("scripts")));

		// Don't have to mkdir these because MT does that for us already.
		providers.add(new ScriptProviderDirectory(new File("scripts")));
		if (e.side.isServer()) {
			providers.add(new ScriptProviderDirectory(new File(MineTweakerHacks.getWorldDirectory(MinecraftServer.getServer()), "scripts")));
		}

		MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderCascade(providers.toArray(new IScriptProvider[providers.size()])));
		MineTweakerImplementationAPI.reload();
	}
}
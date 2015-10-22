package ftb;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.runtime.providers.ScriptProviderCascade;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ftb.lib.api.EventFTBModeSet;

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
		MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderCascade(new ScriptProviderDirectory(e.getCommonFile("scripts")), new ScriptProviderDirectory(e.getFile("scripts"))));
		MineTweakerImplementationAPI.reload();
	}
}
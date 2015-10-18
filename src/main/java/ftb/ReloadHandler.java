package ftb;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import latmod.ftbu.api.EventFTBUReload;
import latmod.ftbu.world.LMWorldServer;

public class ReloadHandler
{
	@Optional.Method(modid = "FTBU")
	@SubscribeEvent
	public void onReloaded(EventFTBUReload e)
	{
		FTBT.INSTANCE.setServerMode(LMWorldServer.inst.gamemode);
	}
}
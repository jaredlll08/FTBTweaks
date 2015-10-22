package ftb;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "FTBT", name = "FTB Tweaks", version = "1.0.1", dependencies = "required-after:MineTweaker3;required-after:FTBL")
public class FTBT {

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new ReloadHandler());
		FMLCommonHandler.instance().bus().register(new ReloadHandler());
	}

}

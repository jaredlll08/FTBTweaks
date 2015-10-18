package ftb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import ftb.commands.CommandMode;
import ftb.network.MessageMode;
import ftb.network.PacketHandler;

@Mod(modid = "ftbt", name = "FTB Tweaks", version = "1.0.0", dependencies = "required-after:MineTweaker3;after:FTBU")
public class FTBT {

	public static Map<String, PackType> packMap;
	public static File ftbDir;
	public static File packDir;
	public static File packsJson;

	public static PackType current = null;

	public static File serverMode;
	@Instance("ftbt")
	public static FTBT INSTANCE;
	public static boolean isFTBUInstalled = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		packMap = new HashMap<String, PackType>();
		isFTBUInstalled = Loader.isModLoaded("FTBU");
		if (isFTBUInstalled)
			MinecraftForge.EVENT_BUS.register(new ReloadHandler());
		ftbDir = new File(e.getModConfigurationDirectory().getParentFile(), "ftb");
		if (!ftbDir.exists()) {
			ftbDir.mkdirs();
		}
		packDir = new File(ftbDir, "modpacks");
		if (!packDir.exists()) {
			packDir.mkdirs();
		}

		packsJson = new File(ftbDir, "packs.json");
		try {
			if (!packsJson.exists()) {
				packsJson.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(packsJson));
				writer.write("{\n"
						+ "\"packs\":[\n"
						+ "{\n\"id\":\"common\",\n"
						+ "\"folderName\":\"common\"\n"
						+ "}\n]\n}\n");
				writer.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JSONParser<PackType> packTypes = new JSONParser<PackType>(packsJson, PackType.class);

		for (PackType type : packTypes.getElements("packs")) {
			File packFolder = new File(packDir, type.folderName);
			if (!packFolder.exists()) {
				packFolder.mkdirs();
			}
			packMap.put(type.getId(), type);
		}
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		PacketHandler.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {

	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandMode());
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent e) {
		serverMode = new File(MinecraftServer.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "ftb_pack.txt");

		current = null;

		try
		{
			if (!serverMode.exists())
				serverMode.createNewFile();
			Scanner scanner = new Scanner(serverMode);
			if (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				current = new PackType(line.split(":")[0], line.split(":")[1]);
			}
			scanner.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		if (current == null)
			current = (PackType) packMap.values().toArray()[0];

		saveServerMode();
		loadPack(current.getId());
	}

	public void saveServerMode() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(serverMode));
			writer.write(current.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public boolean setServerMode(String id) {
		if (!current.equals(packMap.get(id))) {
			current = packMap.get(id);
			saveServerMode();
			return true;
		}
		return false;
	}

	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (e.player instanceof EntityPlayerMP && current != null) {
			PacketHandler.INSTANCE.sendTo(new MessageMode(current.getId()), (EntityPlayerMP) e.player);
		}
	}

	@SuppressWarnings("deprecation")
	public void loadPack(String id) {
		for (String s : packMap.keySet()) {
			System.out.println(s);
		}
		if (packMap.keySet().contains(id)) {
			File packFolder = new File(packDir, packMap.get(id).getFolderName());
			if (!packFolder.exists()) {
				packFolder = new File(packDir, packMap.get(0).getFolderName());
			}
			MineTweakerAPI.tweaker.rollback();
			MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderDirectory(packFolder));
			MineTweakerImplementationAPI.reload();

		}
	}
}

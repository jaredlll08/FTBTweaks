package ftb.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

	private static int id = 0;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("ftbt");

	public static void preInit() {
		INSTANCE.registerMessage(MessageMode.class, MessageMode.class, id++, Side.CLIENT);
	}

}

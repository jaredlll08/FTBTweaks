package com.feed_the_beast.ftbt;

import com.feed_the_beast.ftbl.api.*;
import minetweaker.*;
import minetweaker.mc1102.MineTweakerMod;
import minetweaker.mc1102.network.MineTweakerLoadScriptsPacket;
import minetweaker.mc1102.util.MineTweakerHacks;
import minetweaker.runtime.IScriptProvider;
import minetweaker.runtime.providers.*;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;

public enum FTBTweaks implements IFTBLibPlugin {
	
	@FTBLibPlugin
	INSTANCE;
	
	
	public static FTBLibAPI API;
	
	@Override
	public void init(FTBLibAPI api)
	{
		API = api;
	}
	
	@Override
	public void onReload(Side side, ICommandSender sender, EnumReloadType type) {
		File scripts = new File((side.isServer() ? API.getServerData() : API.getClientData()).getPackMode().getFolder(), "scripts");
//		File scripts = new File(API.getServerData().getPackMode().getFolder(), "scripts");
//		File scriptsCommon = new File(API.getPackModes().getDefault().getFolder(), "scripts");
		
		if(!scripts.exists()){
			scripts.mkdirs();
		}
		
		
		MineTweakerAPI.tweaker.rollback();
		ArrayList<IScriptProvider> providers = new ArrayList<IScriptProvider>();
		
		providers.add(new ScriptProviderDirectory(scripts));
		
		// Don't have to mkdir these because MT does that for us already.
		providers.add(new ScriptProviderDirectory(new File("scripts")));
		if (side==Side.SERVER) {
			providers.add(new ScriptProviderDirectory(new File(MineTweakerHacks.getWorldDirectory(sender.getServer()), "scripts")));
		}
		
		MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderCascade(providers.toArray(new IScriptProvider[providers.size()])));
		MineTweakerImplementationAPI.reload();
		MineTweakerMod.NETWORK.sendToAll(new MineTweakerLoadScriptsPacket(MineTweakerAPI.tweaker.getScriptData()));
		
	}
}

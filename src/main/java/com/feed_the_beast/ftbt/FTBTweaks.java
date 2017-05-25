package com.feed_the_beast.ftbt;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibPlugin;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1112.MineTweakerMod;
import minetweaker.mc1112.network.MineTweakerLoadScriptsPacket;
import minetweaker.mc1112.util.MineTweakerHacks;
import minetweaker.runtime.IScriptProvider;
import minetweaker.runtime.providers.ScriptProviderCascade;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.feed_the_beast.ftbt.FTBT.proxy;

public enum FTBTweaks implements IFTBLibPlugin
{
    @FTBLibPlugin
    INSTANCE;
    
    public static FTBLibAPI API;
    
    @Override
    public void init(FTBLibAPI api)
    {
        API = api;
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onReload(ReloadEvent event)
    {
        Side side = event.getSide();
        
        if(side.isServer() || side.isClient() && proxy.isSinglePlayer())
        {
            File scripts = new File(API.getSidedData(side).getPackMode().getFolder(), "scripts");
            
            if(!scripts.exists())
            {
                scripts.mkdirs();
            }
            
            MineTweakerAPI.tweaker.rollback();
            List<IScriptProvider> providers = new ArrayList<>();
            
            providers.add(new ScriptProviderDirectory(scripts));
            
            // Don't have to mkdir these because MT does that for us already.
            providers.add(new ScriptProviderDirectory(new File("scripts")));
            
            ICommandSender sender = event.getSender();
            if(side.isServer() && sender.getServer() != null)
            {
                providers.add(new ScriptProviderDirectory(new File(MineTweakerHacks.getWorldDirectory(sender.getServer()), "scripts")));
            }
            
            MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderCascade(providers.toArray(new IScriptProvider[providers.size()])));
            MineTweakerImplementationAPI.reload();
            
            if(side.isServer())
            {
                MineTweakerMod.NETWORK.sendToAll(new MineTweakerLoadScriptsPacket(MineTweakerAPI.tweaker.getScriptData()));
            }
        }
    }
}

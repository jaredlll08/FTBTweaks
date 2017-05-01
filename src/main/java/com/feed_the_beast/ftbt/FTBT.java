package com.feed_the_beast.ftbt;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = "ftbtweaks", name = "FTB Tweaks", version = "2.0.1", dependencies = "required-after:ftbl", useMetadata = false, acceptedMinecraftVersions = "[1.10,1.12)")
public class FTBT
{
    @SidedProxy(clientSide = "com.feed_the_beast.ftbt.ClientProxy", serverSide = "com.feed_the_beast.ftbt.ServerProxy")
    public static CommonProxy proxy;
}
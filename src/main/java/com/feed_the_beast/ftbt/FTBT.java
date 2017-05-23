package com.feed_the_beast.ftbt;

import net.minecraftforge.fml.common.*;

@Mod(modid = "ftbtweaks", name = "FTB Tweaks", version = "3.0.1", dependencies = "required-after:crafttweaker;required-after:ftbl", useMetadata = false)
public class FTBT {
    @SidedProxy(clientSide="com.feed_the_beast.ftbt.ClientProxy", serverSide="com.feed_the_beast.ftbt.ServerProxy")
    public static CommonProxy proxy;
}

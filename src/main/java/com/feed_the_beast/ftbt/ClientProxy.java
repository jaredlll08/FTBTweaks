package com.feed_the_beast.ftbt;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }
}

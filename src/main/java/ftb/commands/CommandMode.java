package ftb.commands;

import java.util.ArrayList;
import java.util.List;

import ftb.FTBT;
import ftb.PackType;
import ftb.network.MessageMode;
import ftb.network.PacketHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandMode extends CommandBase {

	@Override
	public String getCommandName() {
		return "ftb_mode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/ftb_mode [Script ID]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args == null || args.length == 0) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid arguments"));
			String packs = "Available packs are:";
			for (PackType type : FTBT.packMap.values()) {
				packs += type.getId() + "  ";
			}
			sender.addChatMessage(new ChatComponentText(packs));
			return;
		} else {
			if (FTBT.INSTANCE.setServerMode(args[0])) {
				PacketHandler.INSTANCE.sendToAll(new MessageMode(args[0]));
				sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "ScriptPack: " + args[0] + " loaded!"));
			}else{
				sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Problem loading ScriptPack: " + args[0]));
			}
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		List<String> returnList = new ArrayList<String>();
		for (PackType type : FTBT.packMap.values()) {
			if (FTBT.packMap.get(args[0]) == null) {
				if (type.getId().startsWith(args[0])) {
					returnList.add(type.getId());
				}
			}
		}
		return returnList;
	}
}

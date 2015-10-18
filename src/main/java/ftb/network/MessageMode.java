package ftb.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.FTBT;

public class MessageMode implements IMessage, IMessageHandler<MessageMode, IMessage> {

	String id;

	public MessageMode() {
	}

	public MessageMode(String id) {
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id);
	}

	@Override
	public IMessage onMessage(MessageMode message, MessageContext ctx) {
		
		FTBT.INSTANCE.loadPack(message.id);
		return null;
	}

}

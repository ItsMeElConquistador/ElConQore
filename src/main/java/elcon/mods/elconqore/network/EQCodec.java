package elcon.mods.elconqore.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class EQCodec extends FMLIndexedMessageToMessageCodec<EQMessage> {

	public EQCodec() {
		addDiscriminator(0, EQMessage.class);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, EQMessage msg, ByteBuf target) throws Exception {
		msg.encode(target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, EQMessage msg) {
		msg.decode(source);
	}
}

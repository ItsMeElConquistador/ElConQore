package elcon.mods.elconqore.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class EQCodec extends FMLIndexedMessageToMessageCodec<EQMessage> {

	public EQCodec() {
		addDiscriminator(0, EQMessage.class);
	}
	
	public EQCodec(Class<? extends EQMessage>... messages) {
		for(int i = 0; i < messages.length; i++) {
			addDiscriminator(i, messages[i]);
		}		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, EQMessage msg, ByteBuf target) throws Exception {
		msg.encodeTo(target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, EQMessage msg) {
		msg.decodeFrom(source);
	}
}

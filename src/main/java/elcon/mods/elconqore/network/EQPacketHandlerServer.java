package elcon.mods.elconqore.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EQPacketHandlerServer extends SimpleChannelInboundHandler<EQMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, EQMessage msg) throws Exception {
		msg.handle();
	}
}

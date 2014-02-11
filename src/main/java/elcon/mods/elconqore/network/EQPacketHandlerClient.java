package elcon.mods.elconqore.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EQPacketHandlerClient extends SimpleChannelInboundHandler<EQMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, EQMessage msg) throws Exception {
		
	}
}

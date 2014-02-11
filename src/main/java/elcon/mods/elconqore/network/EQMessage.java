package elcon.mods.elconqore.network;

import io.netty.buffer.ByteBuf;

public abstract class EQMessage {

	public abstract void encode(ByteBuf target);
	
	public abstract void decode(ByteBuf source);
}

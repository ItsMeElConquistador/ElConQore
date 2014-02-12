package elcon.mods.elconqore.network;

import io.netty.buffer.ByteBuf;

public abstract class EQMessage {

	public abstract void encodeTo(ByteBuf target);
	
	public abstract void decodeFrom(ByteBuf source);
	
	public abstract void handle();
}

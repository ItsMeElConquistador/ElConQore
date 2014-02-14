package elcon.mods.elconqore.network;

import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import net.minecraft.network.Packet;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EQPacketHandler<T> {
	
	public String channel;
	private FMLIndexedMessageToMessageCodec<T> codec;
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private EnumMap<Side, SimpleChannelInboundHandler<T>> handlers;
	
	public EQPacketHandler(String channel, FMLIndexedMessageToMessageCodec<T> codec) {
		this.channel = channel;
		this.codec = codec;
		channels = NetworkRegistry.INSTANCE.newChannel(channel, codec);
		handlers = Maps.newEnumMap(Side.class);
	}
	
	@SideOnly(Side.CLIENT)
	public SimpleChannelInboundHandler<T> getClientHandler() {
		return handlers.get(Side.CLIENT);
	}
	
	public SimpleChannelInboundHandler<T> getServerHandler() {
		return handlers.get(Side.SERVER);
	}
	
	@SideOnly(Side.CLIENT)
	public void setClientHandler(SimpleChannelInboundHandler<T> handler) {
		FMLEmbeddedChannel channel = channels.get(Side.CLIENT);
		String codecName = channel.findChannelHandlerNameForType(codec.getClass());
		if(handlers.get(Side.CLIENT) != null) {
	        channel.pipeline().remove("ClientHandler");
		}
        channel.pipeline().addAfter(codecName, "ClientHandler", handler);
        handlers.put(Side.CLIENT, handler);
	}
	
	public void setServerHandler(SimpleChannelInboundHandler<T> handler) {		
		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		String codecName = channel.findChannelHandlerNameForType(codec.getClass());
		if(handlers.get(Side.SERVER) != null) {
	        channel.pipeline().remove("ServerHandler");
		}
        channel.pipeline().addAfter(codecName, "ServerHandler", handler);
        handlers.put(Side.SERVER, handler);
	}
	
	public Packet getPacketToClient(T message) {
		return channels.get(Side.SERVER).generatePacketFrom(message);
	}
	
	@SideOnly(Side.CLIENT)
	public Packet getPacketToServer(T message) {
		return channels.get(Side.CLIENT).generatePacketFrom(message);
	}
}

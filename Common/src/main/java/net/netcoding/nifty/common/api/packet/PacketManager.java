package net.netcoding.nifty.common.api.packet;

// TODO: https://www.spigotmc.org/resources/api-packetlistenerapi-1-7-1-8-1-9-1-10.2930/
// TODO: https://github.com/InventivetalentDev/PacketListenerAPI
public abstract class PacketManager {

/*
ChannelPipeline pl = ((CraftPlayer)p).getHandle().playerConnection.networkManager.channel.pipeline();
ChannelDuplexHandler pf = (ChannelDuplexHandler)pl.get("uPlayer_handle");
pf = new ChannelDuplexHandler() {
    @Override public void channelRead(ChannelHandlerContext context, Object pack) throws Exception { super.channelRead(context, pack); }
    @Override public void write(ChannelHandlerContext context, Object pack, ChannelPromise promise) throws Exception { super.write(context, pack, promise); }
};
pl.addBefore("packet_handler","uPlayer_handler", pf);
 */

}
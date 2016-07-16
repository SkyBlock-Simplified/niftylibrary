package net.netcoding.nifty.common.api.packet;

// https://www.spigotmc.org/resources/api-packetlistenerapi-1-7-1-8-1-9-1-10.2930/
// https://github.com/InventivetalentDev/PacketListenerAPI
// https://www.spigotmc.org/threads/tutorial-getting-the-players-client-version.134397/page-2#post-1460322
public abstract class PacketManager { // TODO

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
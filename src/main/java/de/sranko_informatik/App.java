package de.sranko_informatik;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.resolver.DefaultAddressResolverGroup;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws  UnknownHostException, ExecutionException, InterruptedException {
        String hostname = args[0];
        // JDK
        InetAddress[] addresses = InetAddress.getAllByName(hostname);
        System.out.println(Arrays.asList(addresses));

        // JDK
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println(localhost);

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            List<InetSocketAddress> result =
                    DefaultAddressResolverGroup.INSTANCE
                            .getResolver(group.next())
                            .resolveAll(InetSocketAddress.createUnresolved(hostname, 80))
                            .get();
            System.out.println("Netty DefaultAddressResolverGroup " + result);

            DnsNameResolverBuilder builder = new DnsNameResolverBuilder(group.next())
                    .channelType(NioDatagramChannel.class);
            DnsNameResolver resolver = builder.build();
            resolver.resolveAll(hostname)
                    .get();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}

package tcp.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class TCPServerVerticle extends AbstractVerticle {

    final int TCP_PORT = 12345;
    final String TCP_HOST = "localhost";
    
    @Override
    public void start() {

        NetServerOptions options = new NetServerOptions().setPort(TCP_PORT).setHost(TCP_HOST);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler(socket -> {
            System.out.println("[SERVER] handler request from client... ");
            socket.handler(buffer -> {
               System.out.println("[SERVER] I received some bytes: " + buffer.length());
               System.out.println(buffer.toString());
            });

            System.out.println("[SERVER] write some data to client...");
            String str = "[SERVER] Greeting from server!";
            Buffer buffer = Buffer.buffer().appendString(str);
            socket.write(buffer);
        });
//        server.listen();
        server.listen(res -> {
            if (res.succeeded()) {
                System.out.println("[SERVER] Server is now listening");
            } else {
                System.out.println("[SERVER] Failed to bind!");
            }
        });
    }
}

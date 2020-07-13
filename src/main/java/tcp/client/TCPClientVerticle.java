package tcp.client;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

public class TCPClientVerticle extends AbstractVerticle {

    final int TCP_PORT = 12345;
    final String TCP_HOST = "localhost";

    @Override
    public void start() {
        NetClient client = vertx.createNetClient();
        client.connect(TCP_PORT, TCP_HOST, res -> {
           if (res.succeeded()) {
               NetSocket socket = res.result();
               socket.handler(buffer -> {
                   System.out.println("[CLIENT] Net client received from server: " + buffer.length());
                   System.out.println("[CLIENT] data: " + buffer.toString("UTF-8"));
               });

               // now send some data
               for (int i = 0; i < 100; i++) {
                   String str = "hello " + i + "\n";
//                   System.out.print("[CLIENT] Sending to server: " + str);
                   socket.write(str);
               }
           } else {
               System.out.println("Failed to connect to tcp server - cause: " + res.cause());
           }
        });
    }

}

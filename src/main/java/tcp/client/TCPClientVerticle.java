package tcp.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.PfxOptions;
import tcp.server.TCPServerVerticle;

public class TCPClientVerticle extends AbstractVerticle {

    public static final String CLIENT_KEY_PATH = "./keys/client.keystore";

    @Override
    public void start() {

        PfxOptions pfxOptions = new PfxOptions()
//                .setPath(TCPServerVerticle.KEY_PATH)
                .setPath(CLIENT_KEY_PATH)
                .setPassword(TCPServerVerticle.KEY_PASS);


//        NetClientOptions options = new NetClientOptions()
//                .setSsl(true)
//                .setTrustAll(true)
//                .setPfxTrustOptions(pfxOptions);
        NetClientOptions options = new NetClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setPfxKeyCertOptions(pfxOptions);

        NetClient client = vertx.createNetClient(options);

//        NetClient client = vertx.createNetClient();

        client.connect(TCPServerVerticle.TCP_PORT, TCPServerVerticle.TCP_HOST, res -> {
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

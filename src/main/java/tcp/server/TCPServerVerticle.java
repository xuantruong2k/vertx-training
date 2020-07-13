package tcp.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PfxOptions;

public class TCPServerVerticle extends AbstractVerticle {

    public static final int TCP_PORT = 12345;
    public static final String TCP_HOST = "localhost";
    public static final String KEY_PATH = "./keys/server.keystore";
    public static final String KEY_PASS = "abcd@1234";
    
    @Override
    public void start() {

        PfxOptions pfxOptions = new PfxOptions()
                        .setPath(TCPServerVerticle.KEY_PATH)
                        .setPassword(TCPServerVerticle.KEY_PASS);

        NetServerOptions options = new NetServerOptions()
                .setPort(TCP_PORT)
                .setHost(TCP_HOST)
                .setSsl(true)
//                .setClientAuth(ClientAuth.REQUIRED)
                .setPfxKeyCertOptions(pfxOptions);
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
        server.listen();
//        server.listen(res -> {
//            if (res.succeeded()) {
//                System.out.println("[SERVER] Server is now listening");
//            } else {
//                System.out.println("[SERVER] Failed to bind!");
//            }
//        });
    }
}

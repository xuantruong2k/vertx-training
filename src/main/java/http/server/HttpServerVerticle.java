package http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import tcp.server.TCPServerVerticle;

public class HttpServerVerticle extends AbstractVerticle {

    public static final String HTTP_HOST = "localhost";
    public static final int HTTP_PORT = 8080;

    private HttpServer server;

    @Override
    public void start() {

        System.out.println("start HttpServer");

        Router router = createHttpServer();

        server.requestHandler(router).listen(HTTP_PORT, HTTP_HOST, res -> {
            if (res.succeeded()) {
                System.out.println("HttpServer - listen on " + HTTP_PORT);
            } else {
                System.out.println("HttpServer - failed on binding");
            }
        });
    }

    private Router createHttpServer() {

//        PfxOptions pfxOptions = new PfxOptions()
//                .setPath(TCPServerVerticle.KEY_PATH)
//                .setPassword(TCPServerVerticle.KEY_PASS);
//        HttpServerOptions options = new HttpServerOptions();
//        options.setSsl(true);
//        options.setPfxKeyCertOptions(pfxOptions);
//        options.setPfxTrustOptions(pfxOptions);

        SelfSignedCertificate certificate = SelfSignedCertificate.create();
        HttpServerOptions options = new HttpServerOptions()
                .setSsl(true)
                .setKeyCertOptions(certificate.keyCertOptions())
                .setTrustOptions(certificate.trustOptions());

        server = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        // response with text
        Route route = router.route("/");
        route.handler(routingContext -> {
            System.out.println("handler on root /");
            String resStr = "Hello from root!";
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain")
                    .putHeader("content-length", ""+resStr.length())
                    .write(resStr)
                    .end();
        });

        // response json
        route = router.route("/some/path/");
        route.handler(routingContext -> {
            System.out.println("handler on /some/path/");
            HttpServerResponse response = routingContext.response();
            JsonObject jsonObj = new JsonObject();
            jsonObj.put("path", "/some/path/");
            jsonObj.put("destination", "tiki");
            response.putHeader("content-type", "application/json")
                    .putHeader("content-length", ""+jsonObj.toString().length())
                    .write(jsonObj.toString())
                    .end();
        });

        // response with file
        route = router.route("/some/getfile/");
        route.handler(routingContext -> {
            System.out.println("handler on /some/getfile/");
            HttpServerResponse response = routingContext.response();
            String filename = "./keys/test.txt";
            response.sendFile(filename);
        });

        return router;
    }
}

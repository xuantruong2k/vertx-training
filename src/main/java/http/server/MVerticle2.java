package http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class MVerticle2 extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("start MVerticle 2");

        Context context = vertx.getOrCreateContext();
        context.put("data", "hello");

        server = vertx.createHttpServer().requestHandler(req -> this.requestHandler(req));
        server.listen(8081, res -> {
            System.out.println("--- MVerticle 2 - listen on port 8081");
            if (res.succeeded()) {
                System.out.println("MVerticle 2 - succeeded " + res.result());
                startPromise.complete();
            } else {
                System.out.println("MVerticle 2 - failed");
                startPromise.fail(res.cause());
            }
        });
    }

    private void requestHandler(HttpServerRequest req) {
        System.out.println(("Verticle 2 - request handler"));
        Context context = vertx.getOrCreateContext();
        System.out.println("context: " + context.get("data"));
        context.put("sharedData", "from verticle 2222");
        req.response()
                .putHeader("content-type", "text/plain")
                .end("hello from vert.x");
    }
}

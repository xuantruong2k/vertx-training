package http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class MVerticle extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {

        System.out.println("start MVerticle");

        // create http server
        Router router = createHttpServer();

        // bind the server
//        server.requestHandler(router).listen(8080);
//        server.listen(8080, res -> {
        server.requestHandler(router).listen(8080, res -> {
            System.out.println("--- MVerticle - listen on port 8080");
            if (res.succeeded()) {
                System.out.println("MVerticle - succeeded " + res.result());
                startPromise.complete();
            } else {
                System.out.println("MVerticle - failed");
                startPromise.fail(res.cause());
            }
        });
    }

    private Router createHttpServer() {
//        HttpServer _server = vertx.createHttpServer().requestHandler(req -> this.handlerRequest(req));
//        HttpServer _server = vertx.createHttpServer().requestHandler(req -> {
//            System.out.println("--- request handler 1");
//            req.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("Hello from Vert.x");
//        });

        server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Context context = vertx.getOrCreateContext();

        // routing
        Route route = router.route("/");
        route.handler(routingContext -> {
            context.put("sharedData", "from verticle 1");
            System.out.println("verticle - request handler - context - deployment id: " + context.deploymentID());
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain").end("Hello from vertx.");
        });

        route = router.route("/some/path/");
        route.handler(routingContext -> {
            routingContext.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from /some/path/ data: " + context.get("sharedData"));
        });

        route = router.route("/some/anotherpath/");
        route.handler(routingContext -> {
            routingContext.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from /some/antoherpath");
        });

        return router;
    }
}

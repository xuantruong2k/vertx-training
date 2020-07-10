package http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class MVerticle extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {

        System.out.println("start MVerticle");

        // create http server
        Router router = createHttpServer();

        vertx.eventBus().consumer("tv.news", message->this.messageHander(message));

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

        route = router.route("/delivermessage/");
        route.handler(routingContext -> {
            this.deliverMessage();
            routingContext.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from /delivermessage/");
        });

        return router;
    }

    private void messageHander(Message message) {
        System.out.println("Verticle has received a message: " + message.body());
    }

    private int messageId = 0;
    private int messageId2 = 0;

    private void deliverMessage() {
        EventBus eventBus = vertx.eventBus();

        System.out.println("\n");

        JsonObject obj = new JsonObject();
        obj.put("key", "value").put("foo", 123).put("bool", true);

        messageId++;
        eventBus.publish("tv.news", "PUBLISH from tv.news - message id: " + messageId);
        eventBus.send("tv.news", "SEND from tv.news - message id: " + messageId);

        messageId2++;
//        eventBus.publish("mobile.news", "PUBLISH from mobile.news - message id: " + messageId2);
//        eventBus.send("mobile.news","SEND from mobile.news - message id: " + messageId2);

//        eventBus.request("mobile.news", "REQUEST from mobile.news - message id: " + messageId2, ar -> {
        eventBus.request("mobile.news", obj.encode(), ar -> {
            if (ar.succeeded()) {
                System.out.println("Received reply: " + ar.result().body());
            }
        });
    }
}

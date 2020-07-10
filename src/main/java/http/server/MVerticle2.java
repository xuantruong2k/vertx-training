package http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("tv.news", message -> this.messageHandler(message))
                .completionHandler(res -> {
                    if (res.succeeded())
                        System.out.println("Verticle2 - tv.news has reached all nodes");
                    else
                        System.out.println("Verticle2 - tv.news error at completion");
                });

        eventBus.consumer("mobile.news", message -> this.messageHandler2(message));

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

    private void messageHandler(Message message) {
        System.out.println("Verticle2 has received a message: " + message.body());
    }

    private void messageHandler2(Message message) {
//        JsonObject jsonObj = JsonObject.mapFrom(message.body()); // not work
//        JsonObject jsonObj = new JsonObject(message.body().toString()); // working
        Object jsonObj = Json.decodeValue(message.body().toString());
        if (jsonObj instanceof JsonObject)
            System.out.println("JsonObject");
        else if (jsonObj instanceof JsonArray)
            System.out.println("JsonArray");
        else if (jsonObj instanceof  String)
            System.out.println("String");
        else
            System.out.println("unknown");

        System.out.println("json object: " + jsonObj.toString());
        System.out.println("Verticle2 has received another message: " + message.body());
        message.reply("Ping ping");
    }
}

package http.server;

import java.sql.Timestamp;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class MHttpServer {

  private HttpServer server;

  public void startServer() {
    Vertx vertx = Vertx.vertx();

    server = vertx.createHttpServer();

    // simple http web server
    // server.requestHandler(request -> {
    // request.getParam("userid");
    // HttpServerResponse response = request.response();
    // response.putHeader("content-type", "application/json");

    // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // response.end("Hello World!\nCurrent time: " + timestamp);

    // });
    // server.listen(8080);

    // using route
    Router router = Router.router(vertx);

    var delay = 3000;
    // router.route().handler(routingContext -> {
    // HttpServerResponse response = routingContext.response();
    // response.putHeader("content-type", "text/plain");
    // response.end("Hello world from Vert.x-Web!");
    // });

    // Route route = router.route("/some/path/");
    // route.handler(routingContext -> {
    // HttpServerResponse response = routingContext.response();
    // response.setChunked(true);
    // response.write("route1\n");
    // routingContext.vertx().setTimer(delay, tid -> routingContext.next());
    // });

    // route.handler(routingContext -> {
    // HttpServerResponse response = routingContext.response();
    // response.write("route2\n");
    // routingContext.vertx().setTimer(delay, tid -> routingContext.next());
    // });

    // route.handler(routingContext -> {
    // HttpServerResponse response = routingContext.response();
    // response.write("route3");
    // routingContext.response().end();
    // });

    Route route = router.route("/some/path/");
    route.handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.setChunked(true);
      response.write("route 1");
      // routingContext.response().end();
      response.end();
    });

    route = router.route("/some/path2/");
    route.handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.end("route 3");
    });

    // get param from request and response json object
    route = router.route(HttpMethod.GET, "/some/path3/:userid/:transactionid");
    route.handler(routingContext -> {
      String userId = routingContext.request().getParam("userid");
      String transactionId = routingContext.request().getParam("transactionid");

      JsonObject jsonObj = new JsonObject();
      jsonObj.put("userId", userId);
      jsonObj.put("transactionId", transactionId);

      HttpServerResponse response = routingContext.response();
      response.end(jsonObj.encodePrettily());
    });

    server.requestHandler(router).listen(8080);

  }

}
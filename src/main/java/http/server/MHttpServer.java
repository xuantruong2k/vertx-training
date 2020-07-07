package http.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class MHttpServer {

  private HttpServer server;

  public void startServer() {
    Vertx vertx = Vertx.vertx();
    server = vertx.createHttpServer();

    server.requestHandler(request -> {
      HttpServerResponse response = request.response();
      response.putHeader("content-type", "application/json");

      response.end("Hello World!");
    });

    server.listen(8080);


  }

}
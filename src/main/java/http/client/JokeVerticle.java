package http.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

public class JokeVerticle extends AbstractVerticle {

  private HttpRequest<JsonObject> request;

  @Override
  public void start() {
    request = WebClient.create(vertx)
      .get(443, "icanhazdadjoke.com", "/")
      .ssl(true)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK);

    vertx.setPeriodic(5000, id -> fetchJoke());
  }

  public void fetchJoke() {
    request.send(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println(asyncResult.result().body().getString("joke"));
        System.out.println("🤣");
        System.out.println();
      }
    });
  }
}
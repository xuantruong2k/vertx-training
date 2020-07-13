/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package main;

import http.server.HttpServerVerticle;
import io.vertx.core.Vertx;
import tcp.client.TCPClientVerticle;
import tcp.server.TCPServerVerticle;

public class App {

    public String getGreeting() {
      return "Hello World!";
    }

    public static void main(String[] args) {
         Vertx vertx = Vertx.vertx();

         // start http server

////         vertx.deployVerticle(new JokeVerticle());
//
////        new MHttpServer().startServer();
//
//        System.out.println("create new MVerticle instance");
//        MVerticle verticle = new MVerticle();
////        vertx.deployVerticle(verticle);
//        // waiting for deployment complete
//        vertx.deployVerticle(verticle, res -> {
//            if (res.succeeded()) {
//                System.out.println("Verticle - Deployment id is: " + res.result());
//            } else {
//                System.out.println("Verticle - Deployment failed!");
//            }
//        });
//
//        System.out.println("create new MVerticle 2 instance");
//        MVerticle2 verticle2 = new MVerticle2();
//        vertx.deployVerticle(verticle2, res -> {
//            if (res.succeeded()) {
//                System.out.println("Verticle 2 - Deployment id is: " + res.result());
//            } else {
//                System.out.println("Verticle 2 - Deployement failed");
//            }
//        });
//
//        System.out.print("after deployVerticle method");

        // start tcp server
//        TCPServerVerticle tcpServerVerticle = new TCPServerVerticle();
//        vertx.deployVerticle(tcpServerVerticle);
//
//        TCPClientVerticle tcpClientVerticle = new TCPClientVerticle();
//        vertx.deployVerticle(tcpClientVerticle);

        // start http server
        HttpServerVerticle httpServerVerticle = new HttpServerVerticle();
        vertx.deployVerticle(httpServerVerticle);

    }
}

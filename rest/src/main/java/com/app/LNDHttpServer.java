package com.app;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * Main server class, reference is taken from HttpServer documentation
 */
class LNDHttpServer {
    //root context path
    public static final String ROOT = "wallets";

    //Starting point
    public static void main(String[] args) throws IOException {
        //TODO: defining default server port, later we can make configurable with environment variable
        int serverPort = 8080;
        //create the server and pass handler, refer handler for all routing and controller specifications.
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/"+ ROOT, new LNDHttpHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
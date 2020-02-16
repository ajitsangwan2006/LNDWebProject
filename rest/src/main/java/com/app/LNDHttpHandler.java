package com.app;

import com.app.controller.WalletController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * LNDHttpHandler: Responsibilities
 * It is the main request handler class which distributes the responsibilities for each requests.
 * Returns 405 if requested method does not present in allowed list
 * Call validation for request objects
 * See individual method specification for more details
 */
public class LNDHttpHandler implements HttpHandler {
    public static final String GET = "GET";
    public static final String POST = "POST";

    //Possible allowed methods, change here to support more/less
    public static final List<String> allowedMethods = Arrays.asList(GET, POST);

    /*
    method: handle
    params: HttpExchange
    Description: Validate the request method and route the request to specific handler methods.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(allowedMethods.contains(exchange.getRequestMethod())){
            List<String> params = findParams(exchange.getRequestURI());
            WalletController controller = new WalletController();
            if(GET.equals(exchange.getRequestMethod()) && params != null && params.size() == 1){
                controller.getWallet(exchange, params.get(0));
            }else if(POST.equals(exchange.getRequestMethod()) && params != null && params.size() == 2){
                if(params.get(1).equalsIgnoreCase("credit")){
                    controller.credit(exchange, params.get(0));
                }
                if(params.get(1).equalsIgnoreCase("debit")){
                    controller.debit(exchange, params.get(0));
                }
            }else{
                exchange.sendResponseHeaders(404, -1);
            }
        }else {
            exchange.sendResponseHeaders(404, -1);
        }
    }


    /*
     * Extract and returns path params
     */
    private List<String> findParams(URI uri){
        int start = uri.getRawPath().lastIndexOf(LNDHttpServer.ROOT);
        if(start <= 0){
            return null;
        }
        return Arrays.asList(uri.getRawPath().substring(start+LNDHttpServer.ROOT.length()+1).split("/"));

    }
}

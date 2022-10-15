package com.http.server;

import java.io.IOException;
//This is the HTTP Server 
//Example Argument - 9004 ie. Port Number
public class HttpServer {
	//Starting point of multithreaded HTTP Server
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length == 0) {
			System.out.println("SERVER :: SimpleSocketServer <port>");
			System.exit(0);
		}
		int port = 0; 
		port = Integer.parseInt(args[0]);
		

		//Calls the Multithreaded Server Class 
		ConcurrentServer client = new ConcurrentServer(port);
		client.start();
		
	}

}

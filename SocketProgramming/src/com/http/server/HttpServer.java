package com.http.server;

import java.io.IOException;

public class HttpServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("SERVER :: SimpleSocketServer <port>");
			System.exit(0);
		}
		int port = 0; 
		port = Integer.parseInt(args[0]);
		

		
		ConcurrentServer client = new ConcurrentServer(port);
		client.start();
		
	}

}

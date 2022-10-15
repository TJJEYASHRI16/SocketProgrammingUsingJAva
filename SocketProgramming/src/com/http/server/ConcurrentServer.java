package com.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//Class for Concurrent Server
public class ConcurrentServer {
	
	private ServerSocket serverSocket;
	public static int port;
	
	
	public ConcurrentServer(int port) {
		this.port = port;
	}

	public void start() throws IOException, InterruptedException {
		// Socket is created and ready to accept
		serverSocket = new ServerSocket(port);
		System.out.println("Starting the socket server at port:" + port);

		Socket client = null;

		while (true) {
			System.out.println("Waiting for clients...");
			//Connection is accepted with the client
			client = serverSocket.accept();
			System.out.println("The following client has connected:"
					+ client.getInetAddress().getCanonicalHostName());
			// A client has connected to this server. The below is functionality for creating new threads for each client
			Thread thread = new Thread(new ClientHandler(client));
			thread.start();
		}
	}

}

package com.http.client;

import java.io.IOException;

public class SocketMain {

	public static void main(String[] args) throws IOException {
		// The Starting of the Client side 
		// Input :: Arguments by user
		//Example - localhost 9000 PUT test.txt
		if (args.length < 4) {
			System.out.println("Usage: SocketMain <server> <path>");
			System.exit(0);
		}
		String server = args[0];
		String port = args[1];
		String action = args[2];
		String filename = args[3];

		System.out.println("Loading contents of URL: " + server);
		//Logic for Get and Put in SocketClient Class
		SocketClient client = new SocketClient(server, Integer.parseInt(port), action, filename);
		if (action != null && action.equalsIgnoreCase("GET")) {

			client.getCommand();
		} else if (action != null && action.equalsIgnoreCase("PUT")) {
			client.putCommand();
		} else {
			System.out.println("INVALID HTTP COMMAND . Please check request");
		}

	}

}

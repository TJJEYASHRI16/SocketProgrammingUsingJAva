package com.http.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	public  String host;
	public  int port;
	public  String filename;
	public  String action;
	public  String SourceFilePath = "C:\\Users\\jkvas\\eclipse-workspace\\SocketProgramming\\src\\TestFilesClient\\";

	public SocketClient(String host, int port, String action, String filename) {
		this.host = host;
		this.port = port;
		this.filename = filename;
		this.action = action;
	}

	public void getCommand() throws IOException {
		System.out.println("CLIENT : About to get connection with server for file" + filename);
		System.out
		.println("CLIENT  ::  Host " + host + "  port " + port + " filename " + filename + " action " + action);
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket = null;
		try {

			InetAddress addr = InetAddress.getByName(host);
			socket = new Socket(addr, port);

			System.out.println("CLIENT : Connected with server on port " + port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Sending request to HTTP Server

			if (host != null && host.equalsIgnoreCase("localhost")) {
				out.print("GET /" + filename + "/ HTTP/1.0\r\n"); // "+path+"
				out.print("Host: " + host + "\r\n");
				out.print("\r\n");
				out.flush();
			} else {
				out.println("GET " + filename + " HTTP/1.0 ");
				out.println();
			}

			System.out.println("CLIENT :: Request Sent");
			// read the response

			// Read data from the server until we finish reading the document
			String line = in.readLine();
			while (line != null) {
				System.out.println(line);
				line = in.readLine();
			}

			System.out.println("CLIENT :: Response received for GET OPERATION ");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
			socket.close();
		}

	}

	public void putCommand() throws IOException {

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			socket = new Socket(host, port);
			System.out.println("CLIENT :: CONNECTED WITH SERVER FOR PUT  ");

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Sending out to the server
			// Building HTTP Put header
			out.print("PUT /" + filename + "/ HTTP/1.0\r\n"); // "+path+"
			out.print("Host: " + host + "\r\n");
			out.print("Accept-Language: en-us\r\n");
			out.print("Connection: Keep-Alive\r\n");
			out.print("Content-type: text/html\r\n");
			out.print("Content-Length: 0\r\n");
			out.print("\r\n");

			System.out.println("CLIENT :: PUT Request Header is Sent");

			// Send the Data to be PUT
			String fileFromClient = readFile(SourceFilePath + filename);
			out.println(fileFromClient);
			out.flush();

			System.out.println("CLIENT :: PUT Data Sent to the server!");

			// read the response

			String response;

			while ((response = in.readLine()) != null) {
				System.out.println(response);
			}

			System.out.println("CLIENT :: Response received for PUT OPERATION ");

		} catch (Exception e) {

		} finally {
			socket.close();
			in.close();
			out.close();
		}

	}

	private static String readFile(String file) {

		BufferedReader reader;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);

			}
			stringBuilder.append("\u0004");
			reader.close();

		} catch (FileNotFoundException e) {

			System.err.println(e.getMessage());
			System.out.println("Client side issue with file name");
			System.exit(1);

		} catch (IOException e) {

			System.err.println(e.getMessage());
			System.exit(1);
		}
		return stringBuilder.toString();
	}
}

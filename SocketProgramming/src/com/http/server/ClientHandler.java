package com.http.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
//Class for Server Operations
public class ClientHandler implements Runnable {
	private Socket socket;
	public static String DestFilePath = "C:\\Users\\jkvas\\eclipse-workspace\\SocketProgramming\\src\\TestFilesServer\\";
	public static String SourceFilePath = "C:\\Users\\jkvas\\eclipse-workspace\\SocketProgramming\\src\\TestFilesClient\\";

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Thread started with name:" + Thread.currentThread().getName());
			readClient();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readClient() throws IOException {
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			System.out.println("SERVER :: Received a connection");
			//InputStream to read requests from client
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Outputstream to send response to client
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String request = "";
			String temp = ".";
			while (!temp.equals("")) {
				temp = in.readLine();
				System.out.println(temp);
				request += temp + "\n";
			}

			// HTTP METHOD
			StringBuilder requestBuilder = new StringBuilder();
			//To distinguish GET and PUT
			String file = request.split("\n")[0].split(" ")[1].split("/")[1];
			if (request.split("\n")[0].contains("GET") && checkURL(file)) {

				// Get the correct page

				constructResponseHeader(200, requestBuilder);
				System.out.println("SERVER :: RESPONSE HEADER :: " + requestBuilder.toString());
				out.write(requestBuilder.toString());
				// Get the corresponding file data and sent to Client
				out.write(getData(file));

				out.flush();
			} else if (request.split("\n")[0].contains("PUT") && checkURL(file)) {

				System.out.println("SERVER :: GET DATA FROM INPUT ");
				// Get the data from the inputStream

				String contentLine = "";
				String ls = ls = System.getProperty("line.separator");

				StringBuffer buf = new StringBuffer();
				//Read the input file from client
				while (!(contentLine = in.readLine()).equals("\u0004")) {

					buf.append(contentLine);
					buf.append(ls);
				}

				System.out.println("SERVER :: PUT  DATA TO A FILE  ");
				int responseCode = putData(buf.toString(), file);
				System.out.println("SERVER :: Construct Response  " + responseCode);
				constructResponseHeader(responseCode, requestBuilder);
				//Save to disk and send response
				out.write(requestBuilder.toString());
				requestBuilder.setLength(0);
				out.flush();

			} else {
				// Enter the error code
				// 404 page not found
				constructResponseHeader(404, requestBuilder);
				out.write(requestBuilder.toString());
				requestBuilder.setLength(0);
				out.flush();
			}

		} catch (IOException e) {
			System.out.println("SERVER :: Exception" + e.getMessage());

		} finally {
			out.close();
			in.close();
			socket.close();
		}
	}

	// Check the URL from the Request header to the server's database
	private static boolean checkURL(String file) {
		System.out.println("SERVER :: checking file path for file " + SourceFilePath + file);
		File myFile = new File(SourceFilePath + file);
		return myFile.exists() && !myFile.isDirectory();

	}

	// Construct Response Header
	private static void constructResponseHeader(int responseCode, StringBuilder sb) {
		System.out.println("SERVER :: Constructing Response  " + responseCode);
		if (responseCode == 200) {

			sb.append("HTTP/1.0 200 OK  \r\n");
			sb.append("Date:" + String.valueOf(new java.util.Date()) + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("Content-Type: text/html \r\n\r\n");
//			sb.append("Connection: Closed " +"\r\n");
			sb.append("\r\n\r\n\r\n");

		} else if (responseCode == 404) {

			sb.append("HTTP/1.0 404 Not Found\r\n");
			sb.append("Date:" + String.valueOf(new java.util.Date()) + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		} else if (responseCode == 304) {
			sb.append("HTTP/1.0 304 Not Modified\r\n");
			sb.append("Date:" + String.valueOf(new java.util.Date()) + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		}
	}
	//Get the file data
	private static String getData(String file) throws IOException {

		File myFile = new File(file);
		StringBuffer responseToClient = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(SourceFilePath + myFile));
			String line = reader.readLine();
			while (line != null) {

				System.out.println("Printing file" + line);
				responseToClient.append(line);
				responseToClient.append(System.lineSeparator());
				line = reader.readLine();

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			reader.close();
		}
		return responseToClient.toString();
	}

	// PUT data to file by Server
	private static int putData(String putDataFromClient, String file) throws IOException {

		return writeFile(putDataFromClient, file);
	}

	// Write the data to server 
	private static int writeFile(String putDataFromClient, String file) {

		File myFile = new File(DestFilePath + file);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(myFile));
			writer.write(putDataFromClient);
			writer.close();
			return 200;
		} catch (IOException e) {
			return 304;
		}
	}
}

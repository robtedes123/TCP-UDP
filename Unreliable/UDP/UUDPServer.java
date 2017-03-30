//  Unreliable UDP server

import java.util.*;
import java.io.*;
import java.net.*;

public class UUDPServer
{

	public static void main(String[] args) throws IOException
	{	
		int[] operand = new int[2];
		byte[] dataIn = new byte[1024];
		byte[] dataOut;
		String messageIn, messageOut, OC, temp;
		int status, response;
		boolean drop;
		InetAddress IPAddress = null;
		int port = 0;

		// Declare server socket
		DatagramSocket socket = new DatagramSocket(57772);
		System.out.println("Server: Server started.");

		while(true)
		{
			try
			{
				// Read message from client
				DatagramPacket packetIn =
					new DatagramPacket(dataIn, dataIn.length);
				socket.receive(packetIn);
				IPAddress = packetIn.getAddress();
				port = packetIn.getPort();

				messageIn = new String(packetIn.getData(),
									   0, packetIn.getLength());
				

				// Generate random boolean
				drop = Math.random() < 0.5;

				if(!drop)
				{
					System.out.println("Server: Received message: "
									+ messageIn.substring(
										0, messageIn.length() - 1));

					// Parse op code
					StringTokenizer st = new StringTokenizer(messageIn, ",\n");
					System.out.println("Server: Parsing message...");
					OC = st.nextToken();
					System.out.println("Server: Operation code is: " + OC);

					// Quit if op code is s
					if(OC.equals("s"))
					{
						// Build message
						messageOut = "s, 200\n";

						// Send message
						System.out.println("Server: Sending client message: "
											+ messageOut.substring(
												0, messageOut.length() - 1)
											+ "...");
						dataOut = messageOut.getBytes();
						DatagramPacket packetOut =
							new DatagramPacket(dataOut, dataOut.length, 
											   IPAddress, port);
						socket.send(packetOut);

						System.out.println("Server: Closing connection,"
											+ " exiting...");
						socket.close();
						break;
					}

					// Parse operands
					temp = st.nextToken();
					operand[0] = Integer.parseInt(temp);
					System.out.println("Server: First operand is: " + operand[0]);
					temp = st.nextToken();
					operand[1] = Integer.parseInt(temp);
					System.out.println("Server: Second operand is: " + operand[1]);

					// Perform appropriate arithmetic operation	
					switch(OC)
					{
						case "+":
							response = operand[0] + operand[1];
							status = 200;
							break;
						case "-":
							response = operand[0] - operand[1];
							status = 200;
							break;
						case "*":
							response = operand[0] * operand[1];
							status = 200;
							break;
						case "/":
							response = operand[0] / operand[1];
							status = 200;
							break;
						default:
							System.out.println("Server: Invalid Operation code: "
												+ OC);
							response = -1;
							status = 300;
							break;
					}

					// Build message
					messageOut = String.valueOf(response) + "," + status + "\n";

					// Send message
					System.out.println("Server: Sending client message: "
										+ messageOut.substring(
											0, messageOut.length() - 1)
										+ "...");
					dataOut = messageOut.getBytes();
					DatagramPacket packetOut =
						new DatagramPacket(dataOut, dataOut.length, 
										   IPAddress, port);
					socket.send(packetOut);
				}
				else
				{
					System.out.println("Server: Server dropped request.");
				}
			}
			// Print errors and send error message
			catch(Exception e)
			{
				System.out.println("Server: " 
									+ e.toString() 
									+ " caught when processing message.");

				// Build error response
				response = -1;
				status = 300;
				messageOut = String.valueOf(response) + "," + status + "\n";

				// Send Message
				System.out.println("Server: Sending client message: "
									+ messageOut.substring(
										0, messageOut.length() - 1)
									+ "...");
				dataOut = messageOut.getBytes();
				DatagramPacket packetOut =
					new DatagramPacket(dataOut, dataOut.length, 
									   IPAddress, port);
				socket.send(packetOut);

				// Continue execution
				continue;
			}
		}
	}

}

//  TCP server

import java.util.*;
import java.io.*;
import java.net.*;

public class TCPServer
{

	public static void main(String[] args) throws IOException
	{	
		int[] operand = new int[2];
		String messageIn, messageOut, OC, temp;
		int status, response;

		// Declare server socket
		ServerSocket socket = new ServerSocket(57772);
		System.out.println("Server: Server started.");

		// Establish client connection
		Socket client = socket.accept();

		// Open Streams for IO
		BufferedReader input = 
			new BufferedReader(
			new InputStreamReader(
			client.getInputStream()));
		BufferedWriter output = 
			new BufferedWriter(
			new OutputStreamWriter(
			client.getOutputStream()));

		while(true)
		{
			try
			{
				// Read message from client
				messageIn = input.readLine();
				System.out.println("Server: Received message: "
									+ messageIn.substring(
									0, messageIn.length() - 1));

				// Parse op code
				StringTokenizer st = new StringTokenizer(messageIn, ",");
				System.out.println("Server: Parsing message...");
				OC = st.nextToken();
				System.out.println("Server: Operation code is: " + OC);

				// Quit if op code is s
				if(OC.equals("s"))
				{
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
				output.write(messageOut);
				output.flush();
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
				output.write(messageOut);
				output.flush();

				// Continue execution
				continue;
			}
		}
	}

}

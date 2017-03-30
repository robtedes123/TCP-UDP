// UDP Client

import java.util.*;
import java.io.*;
import java.net.*;

public class UDPClient
{

	public static void main(String[] args) throws IOException
	{
		String OC, op1, op2, messageIn, messageOut;
		byte[] dataIn = new byte[1024];
		byte[] dataOut;

		// Server connection
		InetAddress IPAddress = InetAddress.getByName("localhost");
		DatagramSocket socket = new DatagramSocket();
		System.out.println("Client: Client started.");

		// Open Streams for IO
		BufferedReader stdin =
			new BufferedReader(
			new InputStreamReader(
			System.in));

		while(true) 
		{
			try
			{
				// Reset operands to empty strings and read in op code
				System.out.println("Client: Enter an arithmetic operation"
									+ " or s to stop: ");
				OC = stdin.readLine();
				op1 = "";
				op2 = "";

				// If op code isn't s read in operands
				if(!OC.equals("s"))
				{
					System.out.println("Client: Enter an Integer: ");
					op1 = stdin.readLine();
					System.out.println("Client: Enter another Integer: ");
					op2 = stdin.readLine();
				}

				// Build message
				messageOut = OC + "," + op1 + "," + op2 + "\n";

				// Write message to client
				System.out.println("Client: Sending server message: "
									+ messageOut.substring(
										0, messageOut.length() - 1)
									+ "...");
				dataOut = messageOut.getBytes();
				DatagramPacket packetOut =
					new DatagramPacket(dataOut, dataOut.length,
									   IPAddress, 57772);
				socket.send(packetOut);
				
				// If op code was s close connection and quit
				if(OC.equals("s"))
				{
					System.out.println("Client: Closing connection,"
										+ " exiting...");
					socket.close();
					break;
				}

				// Receive response from server
				DatagramPacket packetIn =
					new DatagramPacket(dataIn, dataIn.length);
				socket.receive(packetIn);
				messageIn = new String(packetIn.getData(),
									   0, packetIn.getLength());
				System.out.println("Client: Received message: "
									+ messageIn.substring(
										0, messageIn.length() - 1));

				if(messageIn.charAt(3) == '3')
				{
					System.out.println("Client: Status code 300, error.");
				}
				else if (messageIn.charAt(0) != 's')
				{
					System.out.println("Client: Result = "
										+ messageIn.substring(0, messageIn.length() - 5));
				}
			}
			// Log error, continue
			catch(Exception e)
			{
				System.out.println("Client: "
									+ e.toString()
									+ "occured at client.");
				continue;
			}
		}
	}

}

// Unreliable UDP Client

import java.util.*;
import java.io.*;
import java.net.*;

public class UUDPClient
{

	public static void main(String[] args) throws IOException
	{
		String OC, op1, op2, messageIn, messageOut;
		byte[] dataIn = new byte[1024];
		byte[] dataOut;
		int timeout;

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
				System.out.println("Client: Sending server message: "
									+ messageOut.substring(
										0, messageOut.length() - 1)
									+ "...");
				dataOut = messageOut.getBytes();
				DatagramPacket packetOut =
					new DatagramPacket(dataOut, dataOut.length,
									   IPAddress, 57772);
				DatagramPacket packetIn =
					new DatagramPacket(dataIn, dataIn.length);
				
				// Send message wait on response with timeout
				timeout = 100;
				while(true)
				{
					socket.send(packetOut);
					socket.setSoTimeout(timeout);
					try
					{
						socket.receive(packetIn);
						break;
					}
					catch(SocketTimeoutException e)
					{
						if(timeout > 2000)
						{
							System.out.println("Client: System timeout exceeds 2 seconds,"
											+ " exiting...");
							socket.close();
							System.exit(0);
						}
						System.out.println("Client: Timed Out after "
											+ timeout
											+ " milliseconds. Resending...");
						timeout *= 2;
						continue;
					}
				}

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

				// If op code was s close connection and quit
				if(OC.equals("s"))
				{
					System.out.println("Client: Closing connection,"
										+ " exiting...");
					socket.close();
					break;
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

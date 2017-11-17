package view;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.*;

public class ClientView {
	public void play() {
		//Declare client variables
		String message = "";
		String userInput = "";
		try {
			//Create a socket and connect to the server @localhost:8989
			Socket s = new Socket("localhost", 8989);
			
			//Declarations of data in/out streams, and buffer reader
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			//Play the game: by sending and re
			while(!userInput.equals("0")) {
				message = din.readUTF();
				System.out.print(message);
				
				userInput = br.readLine();
				dout.writeUTF(userInput);
				dout.flush();
			}
			System.out.println("Goodbye!");
			dout.close();
			s.close();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

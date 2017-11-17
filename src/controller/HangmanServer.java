package controller;
import controller.HangmanController;
import view.ServerView;

import java.net.*;
import java.io.*;
public class HangmanServer  implements Runnable{
	private String message;
	private String userInput;
	private ServerSocket ss;
	private Socket s;
	private static int port = 8989;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private HangmanController game;
	private ServerView view;
	HangmanServer(Socket s){this.s = s;}
	public HangmanServer() {}
	
	private void gameCaller() throws Exception{
		userInput = dataIn.readUTF();
		if(userInput.equals("0"))
			view.display("Player opted-out!");
		else if(userInput.equals("1")) {
			message = "";
			String msg = game.instantiateTheGame();
			for (int i=0;i<msg.length();i++)
				message +="_";
			message += "___\n" + msg;
			
			while(!userInput.equals("0")) {
				dataOut.writeUTF(message);
				dataOut.flush();
				if(message.contains("Success!") || message.contains("Game over!")) {
					gameCaller();
					break;
				}
				userInput = dataIn.readUTF();
				message = game.onGame(userInput);
			}
			view.display("Player opted-out!");
		}
		else{
			message = "Please enter a valid key, try again. $ ";
			dataOut.writeUTF(message);
			dataOut.flush();
			gameCaller();
		}
	}
	public void run() {
		try {
			//Declarations of data in/out streams
			dataIn = new DataInputStream(s.getInputStream());
			dataOut = new DataOutputStream(s.getOutputStream());
			game = new HangmanController();
			view = new ServerView();
			
			//Preparations
			message = game.headerMessage();
			dataOut.writeUTF(message);
			dataOut.flush();
					
			gameCaller();
			
			dataOut.close();
			ss.close();
			view.display("Server shutdown!");
		}
		catch(Exception ex) {
			view.display(ex.getMessage());
		}
	}
	public void gameHandler() {
		view = new ServerView();
		view.display("Hangman server is running...");
		
		try {
			//Create a server socket, listen for incoming connections @localhost:8989
			ss = new ServerSocket(port);
			while(true) {
				s = ss.accept();
				//Show who is playing and create its own thread!
				String ipPort = s.getRemoteSocketAddress().toString().substring(1);
				String[] addr = ipPort.split(":");
				view.display("A player from "+addr[0]+" on port "+addr[1]+" is playing");
				
				new Thread(new HangmanServer(s)).start();
			}
		}
		catch(Exception ex) {
			view.display(ex.getMessage());
		}
	}
}
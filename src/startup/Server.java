package startup;

import controller.HangmanServer;

public class Server {
	public static void main(String[] args) {
		HangmanServer handler = new HangmanServer();
		handler.gameHandler();
	}
}
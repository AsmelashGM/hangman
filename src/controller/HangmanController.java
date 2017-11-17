package controller;

import model.HangmanModel;
import view.ServerView;

public class HangmanController {
	
	private HangmanModel model;
	private ServerView view;
	public int score = 0;
	
	public HangmanController() {}
	
	public String headerMessage() {
		String message = "Welcome to hangman game!\n\n"+
					"Game rules:\n"+
					". Only guess the whole word or one letter at a time,\n"+
					". Part of the word is considered as a word,\n"+
					". Any time you submit \'0\', game will quit,\n"+
					". No more attemts implies game over,\n"+
					"-----------------------------------------\n"+
					"Use '1' - to Start the game. '0' to quit.\n"+
					"$ ";
		return message;
	}
	public String instantiateTheGame() {
		model = new HangmanModel();
		view = new ServerView();
		model.setDictionary(); //build a dictionary
		if(model.getErrorMessage()!=null) 
			view.display(model.getErrorMessage());
		int randomIndex = (int)(Math.random() * (model.getDictionary().length-1) + 0);
		
		model.setSlectedWord(model.getDictionary()[randomIndex].toLowerCase()); //Convert the selected word to lower case.
		//view.display(model.getSelectedWord());
		
		model.setRemainingAttempts(model.getSelectedWord().length());
		String userProgress = "";
		for(int i=1; i<=model.getSelectedWord().length();i++) {
			userProgress += "-";
		}
		model.setUserProgress(userProgress);
		model.setSuccess(false);
		
		//Set to current value
		model.setScore(score);
		
		//Message to return 
		String message = "Word: " + model.getUserProgress() + " | ";
		message += "Remaining Attempts: " + model.getRemainingAttempts() + " | ";
		message += "Score: " + model.getScore() + " | $ ";
		
		return message;
	}
	public String onGame(String userGuess) {
		userGuess = userGuess.toLowerCase(); //Convert user input to lower case.
		
		if (userGuess.length() > 1){
			if(model.getSelectedWord().equals(userGuess)) {
				model.setScore(model.getScore()+1);
				model.setUserProgress(userGuess);
				model.setSuccess(true);
			}
			else {
				model.setRemainingAttempts(model.getRemainingAttempts()-1);
			}
		}
		else if(userGuess.length() == 1) {
			if(!model.getSelectedWord().contains(userGuess)) { 
				model.setRemainingAttempts(model.getRemainingAttempts()-1);
			}
			else{
				for (int i=0; i<model.getSelectedWord().length(); i++) {
					if(model.getSelectedWord().charAt(i) == userGuess.charAt(0)) {
						char[] userProgress = model.getUserProgress().toCharArray();
						userProgress[i] = userGuess.charAt(0);
						model.setUserProgress(String.valueOf(userProgress));
					}
				}
				if(model.getUserProgress().equals(model.getSelectedWord())) {//!model.getUserProgress().contains("-")
					model.setScore(model.getScore()+1);
					model.setSuccess(true);
				}
			}
		}
		
		//Message to return 
		String dash = "~~~~";
		String attempts = String.valueOf(model.getRemainingAttempts());
		if(model.getSuccess()) attempts = " ";
		if(model.getRemainingAttempts()==0) //Decrement score
			model.setScore(model.getScore()-1);
			
		String message = "Word: " + model.getUserProgress() + " | ";
		message += "Remaining Attempts: " + attempts + " | ";
		message += "Score: " + model.getScore();
		if(!model.getSuccess()) {
			if(model.getRemainingAttempts() == 0) {
				score = model.getScore();
				for (int i=0; i<message.length();i++)dash += "~";
				message += " |\033[31m :( Game over!\033[0m\n";
				message += dash + "\n";
				message += "Press 1 - to play again. 0 to quit. $ ";
			}
			else message += " | $ ";
		}
		else {
			score = model.getScore();
			for (int i=0; i<message.length();i++)dash += "~";
			message += " |\033[32m :) Success!\033[0m\n";
			message += dash + "\n";
			message += "Press 1 - to play again. 0 to quit. $ ";
		}
			
		return message;
	}
}

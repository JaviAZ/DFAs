import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.StringBuilder;

public class DFA {
	private ArrayList <String> states = new ArrayList <>();
	private ArrayList <String> alphabet = new ArrayList <>();
	private ArrayList <String> endStates = new ArrayList <>();
	private ArrayList <ArrayList<String>> transitions = new ArrayList <>();
	private String startState;

	DFA () { }
	DFA (ArrayList <String> newEndStates,DFA otherDFA){
		setStates(otherDFA.getStates());
		setAlphabet(otherDFA.getAlphabet());
		setTransitions(otherDFA.getTransitions());
		setStartState(otherDFA.getStartState());
		setEndStates(newEndStates);
	}
	DFA (String filename) {
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file);
			int numStates = sc.nextInt();
			for(int i = 0;i < numStates;i++){
				states.add(sc.next());
			}
			int sizeAlphabet = sc.nextInt();
			for(int i=0;i<sizeAlphabet;i++){
				alphabet.add(sc.next());
			}
			for(int i = 0;i < numStates;i++){
				transitions.add(new ArrayList<>());
				for(int j = 0;j < sizeAlphabet;j++){
					transitions.get(i).add(sc.next());
				}
			}
			startState = sc.next();
			int numEndStates = sc.nextInt();
			for(int i = 0;i < numEndStates;i++){
				endStates.add(sc.next());
			}
		}catch (Exception e) {
			if(e instanceof FileNotFoundException) {
				System.err.println("File was not found, please try again.");
				System.exit(1);
			}else if(e instanceof InputMismatchException || e instanceof NoSuchElementException) {
				System.err.println("File does not contain a valid encoding.");
				System.exit(1);
			}else {
				System.err.println(e.getMessage());
			}
		}
	}

	ArrayList <String> getEndStates(){
		return endStates;
	}
	void setEndStates(ArrayList <String> newEndStates) {
		endStates = newEndStates;
	}

	ArrayList <String> getStates(){
		return states;
	}
	void setStates(ArrayList <String> newStates) {
		states = newStates;
	}

	ArrayList <String> getAlphabet(){
		return alphabet;
	}
	void setAlphabet(ArrayList <String> newAlphabet) {
		alphabet = newAlphabet;
	}

	ArrayList <ArrayList<String>> getTransitions(){
		return transitions;
	}
	void setTransitions(ArrayList <ArrayList<String>> newTransitions) {
		transitions = newTransitions;
	}

	String getStartState(){
		return startState;
	}
	void setStartState(String newStartStates) {
		startState = newStartStates;
	}

	String getEncoding() {
		StringBuilder temp = new StringBuilder();
		String returnString = "";
		returnString+=states.size()+"\n";
		for(int i = 0;i < states.size();i++) {
			temp.append(states.get(i));
			if(i<states.size()-1){
				temp.append(" ");
			}
		}
		returnString+=temp.toString()+"\n";
		returnString+=alphabet.size()+"\n";
		temp.delete(0,temp.length());
		for(int i = 0;i < alphabet.size();i++) {
			temp.append(alphabet.get(i));
			if(i<alphabet.size()-1){
				temp.append(" ");
			}
		}
		temp.append("\n");
		for(int i = 0;i < transitions.size();i++){
			for(int j = 0;j < transitions.get(i).size();j++){
				temp.append(transitions.get(i).get(j));
				if(j<transitions.get(i).size()-1){
					temp.append(" ");
				}
			}
			if(i<transitions.size()-1){
				temp.append("\n");
			}
		}
		returnString+=temp.toString()+"\n";
		returnString+=startState+"\n";
		returnString+=endStates.size()+"\n";
		temp.delete(0,temp.length());
		for(int i = 0;i < endStates.size();i++) {
			temp.append(endStates.get(i));
			if(i<endStates.size()-1){
				temp.append(" ");
			}
		}
		returnString+=temp.toString();
		return returnString;
	}
}
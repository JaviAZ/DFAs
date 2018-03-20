import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.StringBuilder;

public class DFA {
	private String [] states, alphabet, endStates;
	private String [][] transitions;
	private String startState;

	public DFA () { }
	public DFA (String [] newEndStates,DFA otherDFA){
		setStates(otherDFA.getStates());
		setAlphabet(otherDFA.getAlphabet());
		setTransitions(otherDFA.getTransitions());
		setStartState(otherDFA.getStartState());
		setEndStates(newEndStates);
	}
	public DFA (String filename) {
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file);
			int numStates = sc.nextInt();
			states = new String [numStates];
			for(int i = 0;i < numStates;i++){
				states[i] = sc.next();
			}
			int sizeAlphabet = sc.nextInt();
			alphabet = new String [sizeAlphabet];
			for(int i=0;i<sizeAlphabet;i++){
				alphabet[i] = sc.next();
			}
			transitions = new String [numStates][sizeAlphabet];
			for(int i = 0;i < numStates;i++){
				for(int j = 0;j < sizeAlphabet;j++){
					transitions [i][j] = sc.next();
				}
			}
			startState = sc.next();
			int numEndStates = sc.nextInt();
			endStates = new String [numEndStates];
			for(int i = 0;i < numEndStates;i++){
				endStates [i] = sc.next();
			}
		}catch (FileNotFoundException e) {
			System.err.println("File was not found, please try again.");
		}
	}

	public String [] getEndStates(){
		return endStates;
	}
	public void setEndStates(String [] newEndStates) {
		endStates=newEndStates;
	}

	public String [] getStates(){
		return states;
	}
	public void setStates(String [] newStates) {
		states=newStates;
	}

	public String [] getAlphabet(){
		return alphabet;
	}
	public void setAlphabet(String [] newAlphabet) {
		alphabet=newAlphabet;
	}

	public String [][] getTransitions(){
		return transitions;
	}
	public void setTransitions(String [][] newTransitions) {
		transitions=newTransitions;
	}

	public String getStartState(){
		return startState;
	}
	public void setStartState(String newStartStates) {
		startState=newStartStates;
	}

	public String getEncoding() {
		StringBuilder temp = new StringBuilder();
		String returnString = "";
		returnString+=states.length+"\n";
		for(int i = 0;i < states.length;i++) {
			temp.append(states[i]);
			if(i<states.length-1){
				temp.append(" ");
			}
		}
		returnString+=temp.toString()+"\n";
		returnString+=alphabet.length+"\n";
		temp.delete(0,temp.length());
		for(int i = 0;i < alphabet.length;i++) {
			temp.append(alphabet[i]);
			if(i<alphabet.length-1){
				temp.append(" ");
			}
		}
		temp.append("\n");
		for(int i = 0;i < transitions.length;i++){
			for(int j = 0;j < transitions[i].length;j++){
				temp.append(transitions[i][j]);
				if(j<transitions[i].length-1){
					temp.append(" ");
				}
			}
			if(i<transitions.length-1){
				temp.append("\n");
			}
		}
		returnString+=temp.toString()+"\n";
		returnString+=startState+"\n";
		returnString+=endStates.length+"\n";
		temp.delete(0,temp.length());
		for(int i = 0;i < endStates.length;i++) {
			temp.append(endStates[i]);
			if(i<endStates.length-1){
				temp.append(" ");
			}
		}
		returnString+=temp.toString();
		return returnString;
	}

	public int getStateIndex(String s) {
		ArrayList<String> temp1 = new ArrayList <>(Arrays.asList(states));
		return temp1.indexOf(s);
	}
}
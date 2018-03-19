import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.StringBuilder;
public class DFA {
	private static String [] states, alphabet, endStates;
	private static String [][] transitions;
	private static String startState;
	public DFA (String [] newEndStates,DFA otherDFA){
		states=otherDFA.getStates();
		alphabet=otherDFA.getAlphabet();
		transitions=otherDFA.getTransitions();
		startState=otherDFA.getStartState();
		endStates=newEndStates;
	}
	public static void main (String [] args){
		if(args.length==0 || args.length>1) {
			System.err.println("A file name must be input.");
			System.err.println("For example: ");
			System.err.println("    java DFA D1.txt");
		} else {
			String filename=args[0];
			loadFile(filename);
			System.out.println("States: "+printStrArray(states));
			System.out.println("Alphabet: "+printStrArray(alphabet));
			System.out.println("Transitions: "+printStrArray(transitions));
			//DFA m = new DFA(findNewEnds(),);
		}
	}
	private static void findNewEnds() {

	}

	private static void loadFile(String filename){
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

	private static String printStrArray(String [] strArray){
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.length;i++){
			if(i>0 && i<strArray.length){
				temp.append(", ");
			}
			temp.append(strArray[i]);
		}
		return temp.toString();
	}

	private static String [] getEndStates(){
		return endStates;
	}

	private static String [] getStates(){
		return states;
	}

	private static String printStrArray(String [][] strArray){
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.length;i++){
			for(int j = 0;j < strArray[i].length;j++){
				temp.append(strArray[i][j]);
				if(j<strArray[i].length-1){
					temp.append(" ");
				}
			}
			if(i<strArray.length-1){
				temp.append("; ");
			}
		}
		return temp.toString();
	}

	private static String [] getAlphabet(){
		return alphabet;
	}

	private static String [][] getTransitions(){
		return transitions;
	}

	private static String getStartState(){
		return startState;
	}

}

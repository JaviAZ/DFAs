import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
/*  Student number: C1628112
	Student name: Javier Alcazar-Zafra
	Deterministic finite automaton application
	DFAs are input as arguments in the form of "filename.txt", user can choose to input either one or two DFAs
 */
public class AppDFA {
	public static void main (String [] args){
		Scanner in = new Scanner(System.in);
		if(args.length==0 || args.length>2) {
			System.err.println("A file name must be input.");
			System.err.println("For example: ");
			System.err.println("    java DFA D1.txt");
		} if(args.length==2) {
			DFA m = new DFA(args[0]);
			DFA n = new DFA(args[1]);
			System.out.println("1.- Intersection.\n2.- Symmetric difference.\n3.- Equivalence");
			try {
				int answer = in.nextInt();
				switch (answer) {
					case 1:
						DFA mn = getIntersect(m,n);
						System.out.println(mn.getEncoding());
						break;
					case 2:
						DFA SymDif = symmetricDifference(m, n);
						System.out.println(SymDif.getEncoding());
						break;
					case 3:
						if(equivalence(m, n)) {
							System.out.println("Equivalent.");
						} else {
							System.out.println("Not equivalent.");
						}
						break;
					default:
						System.out.println("Please input 1, 2, 3.");
						break;
				}
			} catch(InputMismatchException e){
				System.err.println("Please input 1, 2, 3.");
			}
		} else {
			String filename1=args[0];
			DFA m = new DFA(filename1);
			System.out.println("1.- Print M.\n2.- Complementation.\n3.- Check for language.");
			try {
				int answer = in.nextInt();
				switch (answer) {
					case 1:
						System.out.println("Encoding of M: ");
						System.out.println(m.getEncoding());
						System.out.println("\nM: ");
						printDFA(m);
						break;
					case 2:
						DFA n = new DFA(findComplementEnds(m), m);
						System.out.println("\nEncoding of conjugate: ");
						System.out.println(n.getEncoding());
						System.out.println("\nConjugate: ");
						printDFA(n);
						break;
					case 3:
						ArrayList <ArrayList <String>> visitedAndLanguage = new ArrayList<>(hasLanguage(m));
						if(visitedAndLanguage.get(0).size()==0){
							System.out.println("Language empty.");
						}else {
							System.out.println("Language not empty - "+printArray(visitedAndLanguage.get(1))+" accepted.");
						}
						break;
					default:
						System.out.println("Please input 1, 2 or 3.");
						break;
				}
			} catch(InputMismatchException e){
				System.err.println("Please input 1 or 2.");
			}
		}
	}

	private static String printArray(ArrayList <String> strArray) { //Transforms an ArrayList into a String for print out
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.size();i++){
			if(i>0 && i<strArray.size()){
				temp.append(", ");
			}
			temp.append(strArray.get(i));
		}
		return temp.toString();
	}
	private static String print2DArray(ArrayList <ArrayList<String>> strArray) { //Transforms a 2D ArrayList into a String for print out
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.size();i++){
			for(int j = 0;j < strArray.get(i).size();j++){
				temp.append(strArray.get(i).get(j));
				if(j<strArray.get(i).size()-1){
					temp.append(" ");
				}
			}
			if(i<strArray.size()-1){
				temp.append("; ");
			}
		}
		return temp.toString();
	}
	private static void printDFA(DFA m) { // Prints the DFA nicely
		System.out.println("States: "+printArray(m.getStates()));
		System.out.println("Alphabet: "+printArray(m.getAlphabet()));
		System.out.println("Transitions: "+print2DArray(m.getTransitions()));
		System.out.println("End states: "+printArray(m.getEndStates()));
	}
	private static int [] getSplitState(DFA m, DFA n, String tempState) { // Splits a state (combined of two states from two DFAs) and returns their respective position in the states ArrayLists in their DFAs
		int t2Pos = 1;
		int t1Index = m.getStates().indexOf(("" + tempState.substring(0,t2Pos)));
		while(t1Index==-1) {
			t2Pos++;
			t1Index = m.getStates().indexOf(tempState.substring(0,t2Pos));
		}
		int t2Index = n.getStates().indexOf(("" + tempState.substring(t2Pos)));
		while(t2Index==-1) {
			t2Pos++;
			t2Index = n.getStates().indexOf(tempState.substring(t2Pos));
		}
		return new int[]{t1Index,t2Index};
	}
	//Task 1
	private static ArrayList <String> findComplementEnds(DFA m) { // Removes current end states from list of total states to get the complement
		ArrayList<String> temp1 = new ArrayList <>(m.getStates());
		temp1.removeAll(m.getEndStates());
		return temp1;
	}

	//Task 2
	private static DFA getIntersect(DFA m, DFA n) { // Returns a DFA that consists of the intersection of two other DFAs
		DFA x = new DFA();
		ArrayList <String> tempStates = new ArrayList<>();
		ArrayList <ArrayList<String>> tempTransitions = new ArrayList<>();
		ArrayList <String> tempEndStates = new ArrayList<>();
		ArrayList <String> tempAlphabet = new ArrayList<>();
		for(int i = 0;i < m.getAlphabet().size();i++) {
			if(n.getAlphabet().contains(m.getAlphabet().get(i))) {
				tempAlphabet.add(m.getAlphabet().get(i)); // Create the intersection alphabet
			}
		}
		if(tempAlphabet.size()==n.getAlphabet().size()) { // Exit if alphabets don't match since intersection is based on the assumption that they do
			System.err.println("DFAs alphabets don't match.");
			System.exit(1);
		}
		x.setAlphabet(tempAlphabet);
		x.setStartState(m.getStartState() + n.getStartState()); // Set start state to the combination of both
		tempStates.add(x.getStartState()); // Add start state to list of states
		for(int i = 0;i<m.getStates().size()*n.getStates().size();i++) { // Adds both transitions and states at the same time
			if (tempStates.size() > i) {
				tempTransitions.add(new ArrayList<>());
				for(int j = 0;j<x.getAlphabet().size();j++) {
					int [] splitState = getSplitState(m,n,tempStates.get(i));
					String newState = m.getTransitions().get(splitState[0]).get(j) + n.getTransitions().get(splitState[1]).get(j);
					if (!tempStates.contains(newState)) {
						tempStates.add(newState); // If state hasn't been discovered yet, add it
					}
					tempTransitions.get(i).add(newState); // Add transition
				}
			}
		}
		for(int i = 0;i < m.getEndStates().size();i++) { // Mix up end states from both DFA
			for(int j = 0;j < n.getEndStates().size();j++) {
				ArrayList <String> mEnd = m.getEndStates();
				ArrayList <String> nEnd = n.getEndStates();
				if(tempStates.contains(mEnd.get(i)+nEnd.get(j))) { // Making sure they are in the states ArrayList previously made to avoid unreachable states
					tempEndStates.add(mEnd.get(i)+nEnd.get(j));
				}
			}
		}
		x.setStates(tempStates);
		x.setTransitions(tempTransitions);
		x.setEndStates(tempEndStates);
		return x;
	}

	//Task 3     (S* ∩ T) U (S ∩ T*)    =     (S ∩ T)*    Asterisk symbolises complementation
	private static DFA symmetricDifference(DFA m, DFA n) {
		DFA mn = getIntersect(m, n); // Find intersect of DFAs
		ArrayList <String> conjugateEnds = new ArrayList <>(findComplementEnds(mn)); // Find complement ends
		ArrayList <String> mEnd = m.getEndStates(); // Find ms' end states
		ArrayList <String> nEnd = n.getEndStates(); // Find ns' end states
		for(int i = 0;i < conjugateEnds.size();i++) { // Iterate through conjugate ends to see if they appear in both DFAs
			int [] splitState = getSplitState(m,n,conjugateEnds.get(i));
			if(!mEnd.contains(m.getStates().get(splitState[0])+"") && !nEnd.contains(n.getStates().get(splitState[1])+"")){
				conjugateEnds.remove(i); // If they exist in both delete them
				i--;
			}
		}
		return new DFA(conjugateEnds, mn);
	}

	//Task 4
	private static ArrayList <ArrayList <String>> hasLanguage (DFA m) {
		String currState = m.getStartState();
		ArrayList <ArrayList <String>> visitedAndLanguage = new ArrayList<>(); // Create ArrayList containing visited states and language
		visitedAndLanguage.add(new ArrayList<>());
		visitedAndLanguage.add(new ArrayList<>());
		visitedAndLanguage.get(0).add(currState);
		if(m.getEndStates().contains(currState)) { // If initial state is an end state return it
			visitedAndLanguage.get(1).add("Initial state is an end state.");
			return visitedAndLanguage;
		}else {
			visitedAndLanguage = recursiveDFS(m, currState, visitedAndLanguage); // Call recursive DFS
			if (!m.getEndStates().contains(visitedAndLanguage.get(0).get(visitedAndLanguage.get(0).size()-1))) { // If last visited state isn't an end state, empty the language and visited states
				visitedAndLanguage.get(0).removeAll(visitedAndLanguage.get(0));
				visitedAndLanguage.get(1).removeAll(visitedAndLanguage.get(1));
			}
			return visitedAndLanguage; // return empty ArrayLists
		}
	}
	private static ArrayList <ArrayList <String>> recursiveDFS(DFA m, String currState, ArrayList <ArrayList<String>> visitedAndLanguage) {
		ArrayList <String> endStates = m.getEndStates();
		ArrayList <String> currTrans = m.getTransitions().get(m.getStates().indexOf(currState));
		for(int i = 0;i < m.getAlphabet().size();i++) { // Iterate through alphabet to get transitions
			currState = currTrans.get(i);
			if(!visitedAndLanguage.get(0).contains(currState)) { // Check if not visited
				visitedAndLanguage.get(0).add(currState); // Add currState to list of visited
				visitedAndLanguage.get(1).add(m.getAlphabet().get(i)); // Add alphabet to language
				if (endStates.contains(currState)) {
					return visitedAndLanguage; // Return ArrayList if currState is endState
				} else {
					recursiveDFS(m, currState, visitedAndLanguage); // Call recursive method
					if (endStates.contains(visitedAndLanguage.get(0).get(0))) {
						return visitedAndLanguage; // Return ArrayList if last state in the visited states list is an end state
					}
				}
			}
		}
		return visitedAndLanguage; // Return ArrayList as it is
	}

	//Task 5
	private static boolean equivalence(DFA m, DFA n) { // If symmetric difference doesn't have a language they are equivalent
		return (hasLanguage(symmetricDifference(m,n)).get(0).size()==0);
	}
}
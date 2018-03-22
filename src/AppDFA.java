import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
/*  Student number: C1628112
	Student name: Javier Alcazar-Zafra
	Deterministic finite automaton application
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
						DFA equiv = symmetricDifference(m, n);
						if(equiv.getEndStates().size()==0) {
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
						DFA n = new DFA(findConjugateEnds(m), m);
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

	private static String printArray(ArrayList <String> strArray){
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.size();i++){
			if(i>0 && i<strArray.size()){
				temp.append(", ");
			}
			temp.append(strArray.get(i));
		}
		return temp.toString();
	}
	private static String print2DArray(ArrayList <ArrayList<String>> strArray){
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
	private static void printDFA(DFA m) {
		System.out.println("States: "+printArray(m.getStates()));
		System.out.println("Alphabet: "+printArray(m.getAlphabet()));
		System.out.println("Transitions: "+print2DArray(m.getTransitions()));
		System.out.println("End states: "+printArray(m.getEndStates()));
	}

	//Task 1
	private static ArrayList <String> findConjugateEnds(DFA m) {
		ArrayList<String> temp1 = new ArrayList <>(m.getStates());
		temp1.removeAll(m.getEndStates());
		return temp1;
	}

	//Task 2
	private static DFA getIntersect(DFA m, DFA n) {
		DFA x = new DFA();
		ArrayList <String> tempStates = new ArrayList<>();
		ArrayList <ArrayList<String>> tempTransitions = new ArrayList<>();
		ArrayList <String> tempEndStates = new ArrayList<>();
		ArrayList <String> tempAlphabet = new ArrayList<>();
		for(int i = 0;i < m.getAlphabet().size();i++) {
			if(n.getAlphabet().contains(m.getAlphabet().get(i))) {
				tempAlphabet.add(m.getAlphabet().get(i));
			}
		}
		if(tempAlphabet.size()==0) {
			System.err.println("DFAs don't have any matches in their alphabets.");
			System.exit(1);
		}
		x.setAlphabet(tempAlphabet);
		x.setStartState(m.getStartState() + n.getStartState());
		tempStates.add(x.getStartState());
		ArrayList <ArrayList<String>> t1 = m.getTransitions();
		ArrayList <ArrayList<String>> t2 = n.getTransitions();
		int t1Index, t2Index;
		for(int i = 0;i<m.getStates().size()*n.getStates().size();i++) {
			if (tempStates.size() > i) {
				tempTransitions.add(new ArrayList<>());
				for(int j = 0;j<x.getAlphabet().size();j++) {
					int t1Pos = 0;
					int t2Pos = 1;
					t1Index = m.getStates().indexOf(("" + tempStates.get(i).substring(0,t1Pos)));
					t2Index = n.getStates().indexOf(("" + tempStates.get(i).substring(t2Pos,tempStates.get(i).length())));
					while(t1Index==-1) {
						t1Pos++;
						t1Index = m.getStates().indexOf(tempStates.get(i).substring(0,t1Pos));
					}
					while(t2Index==-1) {
						t2Pos++;
						t2Index = n.getStates().indexOf(tempStates.get(i).substring(t2Pos,tempStates.get(i).length()));
					}
					String newState = t1.get(t1Index).get(j) + t2.get(t2Index).get(j);
					if (!tempStates.contains(newState)) {
						tempStates.add(newState);
					}
					tempTransitions.get(i).add(newState);
				}
			}
		}
		for(int i = 0;i < m.getEndStates().size();i++) {
			for(int j = 0;j < n.getEndStates().size();j++) {
				ArrayList <String> mEnd = m.getEndStates();
				ArrayList <String> nEnd = n.getEndStates();
				if(tempStates.contains(mEnd.get(i)+nEnd.get(j))) {
					tempEndStates.add(mEnd.get(i)+nEnd.get(j));
				}
			}
		}
		x.setStates(tempStates);
		x.setTransitions(tempTransitions);
		x.setEndStates(tempEndStates);
		return x;
	}

	//Task 3
	private static DFA symmetricDifference(DFA m, DFA n) {
		DFA mn = getIntersect(m, n);
		ArrayList <String> conjugateEnds = new ArrayList <>(findConjugateEnds(mn));
		ArrayList <String> mEnd = m.getEndStates();
		ArrayList <String> nEnd = n.getEndStates();
		for(int i = 0;i < conjugateEnds.size();i++) {
			if(!mEnd.contains(conjugateEnds.get(i).charAt(0)+"") && !nEnd.contains(conjugateEnds.get(i).charAt(1)+"")){
				conjugateEnds.remove(i);
				i--;
			}
		}
		return new DFA(conjugateEnds, mn);
	}

	//Task 4
	private static ArrayList <ArrayList <String>> hasLanguage (DFA m) {
		String currState = m.getStartState();
		ArrayList <ArrayList <String>> visitedAndLanguage = new ArrayList<>();

		visitedAndLanguage.add(new ArrayList<>());
		visitedAndLanguage.add(new ArrayList<>());
		visitedAndLanguage.get(0).add(currState);
		if(m.getEndStates().contains(currState)) {
			return visitedAndLanguage;
		}else {
			visitedAndLanguage = recursiveHasLanguage(m, currState, visitedAndLanguage);
			if (!m.getEndStates().contains(visitedAndLanguage.get(0).get(visitedAndLanguage.get(0).size()-1))) {
				visitedAndLanguage.get(0).removeAll(visitedAndLanguage.get(0));
				visitedAndLanguage.get(1).removeAll(visitedAndLanguage.get(1));
			}
			return visitedAndLanguage;
		}
	}
	private static ArrayList <ArrayList <String>> recursiveHasLanguage (DFA m, String currState, ArrayList <ArrayList<String>> visitedAndLanguage) {
		ArrayList <String> endStates = m.getEndStates();
		ArrayList <String> currTrans = m.getTransitions().get(m.getStates().indexOf(currState));
		for(int i = 0;i < m.getAlphabet().size();i++) {
			currState = currTrans.get(i);
			if(!visitedAndLanguage.get(0).contains(currState)) {
				visitedAndLanguage.get(0).add(currState);
				visitedAndLanguage.get(1).add(m.getAlphabet().get(i));
				if (endStates.contains(currState)) {
					return visitedAndLanguage;
				} else {
					recursiveHasLanguage(m, currState, visitedAndLanguage);
					if (endStates.contains(visitedAndLanguage.get(0).get(0))) {
						return visitedAndLanguage;
					}
				}
			}
		}
		return visitedAndLanguage;
	}

	//Task 5

}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.InputMismatchException;

public class AppDFA {
	public static void main (String [] args){
		if(args.length==0 || args.length>2) {
			System.err.println("A file name must be input.");
			System.err.println("For example: ");
			System.err.println("    java DFA D1.txt");
		} if(args.length==2) {
			DFA m = new DFA(args[0]);
			DFA n = new DFA(args[1]);
			DFA mn = getIntersect(m,n);
			System.out.println(mn.getEncoding());
		} else {
			String filename1=args[0];
			DFA m = new DFA(filename1);
			Scanner in = new Scanner(System.in);
			System.out.println("1.- Print M.\n2.- Complementation.");
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
					default:
						System.out.println("Please input 1 or 2.");
						break;
				}
			} catch(InputMismatchException e){
				System.err.println("Please input 1 or 2.");
			}
		}
	}

	private static String printArray(String [] strArray){
		StringBuilder temp=new StringBuilder();
		for(int i = 0;i < strArray.length;i++){
			if(i>0 && i<strArray.length){
				temp.append(", ");
			}
			temp.append(strArray[i]);
		}
		return temp.toString();
	}

	private static String printArray(String [][] strArray){
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

	private static void printDFA(DFA m) {
		System.out.println("States: "+printArray(m.getStates()));
		System.out.println("Alphabet: "+printArray(m.getAlphabet()));
		System.out.println("Transitions: "+printArray(m.getTransitions()));
		System.out.println("End states: "+printArray(m.getEndStates()));
	}

	private static String [] findConjugateEnds(DFA m) {
		ArrayList<String> temp1 = new ArrayList <>(Arrays.asList(m.getStates()));
		temp1.removeAll(Arrays.asList(m.getEndStates()));
		return temp1.toArray(new String [temp1.size()]);
	}

	private static DFA getIntersect(DFA m, DFA n) {
		DFA x = new DFA();
		ArrayList <String> tempStates = new ArrayList<>();
		ArrayList <ArrayList<String>> tempTransitions = new ArrayList<>();
		ArrayList <String> tempEndStates = new ArrayList<>();
		x.setAlphabet(m.getAlphabet());
		x.setStartState(m.getStartState()+n.getStartState());
		tempStates.add(x.getStartState());
		String [] [] t1=m.getTransitions();
		String [] [] t2=n.getTransitions();
		String [] [] tempT;
		int t1Index;
		int t2Index;
		for(int i = 0;i<m.getStates().length*n.getStates().length;i++) {
			if (tempStates.size() > i) {
				tempTransitions.add(new ArrayList<>());
				for(int j = 0;j<x.getAlphabet().length;j++) {
					t1Index = m.getStateIndex(("" + tempStates.get(i).charAt(0)));
					t2Index = n.getStateIndex(("" + tempStates.get(i).charAt(1)));
					String newState = t1[t1Index][j] + t2[t2Index][j];
					if (!tempStates.contains(newState)) {
						tempStates.add(newState);
					}
					tempTransitions.get(i).add(newState);
				}
			}
		}
		for(int i = 0;i < m.getEndStates().length;i++) {
			for(int j = 0;j < n.getEndStates().length;j++) {
				String [] mEnd = m.getEndStates();
				String [] nEnd = n.getEndStates();
				if(tempStates.contains(mEnd[i]+nEnd[j])) {
					tempEndStates.add(mEnd[i]+nEnd[j]);
				}
			}
		}
		x.setStates(tempStates.toArray(new String [tempStates.size()]));
		tempT = new String [tempTransitions.size()][];
		for(int i = 0;i < tempTransitions.size();i++) {
				tempT[i] = tempTransitions.get(i).toArray(new String [tempTransitions.get(i).size()]);
		}
		x.setTransitions(tempT);
		x.setEndStates(tempEndStates.toArray(new String [tempEndStates.size()]));
		return x;
	}
}

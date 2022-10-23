package Challenge2;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.util.*;

//This file is intended as a bare-bones interpreter.
public class BareBones {
  String[][] code;

  public static void main(String[] args) throws IOException {
    BareBones runtime = new BareBones();
    runtime.setupCodebase();
    runtime.runCode();
  }

  private void setupCodebase() throws IOException {
    //Gets the path from the user
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the filename (starts from ./tests/, auto appends .txt)");
    String path = "Challenge2/tests/" + reader.readLine() + ".txt";

    //Reads in file to a 1D "code array"
    //Scanner methods learnt from: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    String[] code1D = new String[0];
    File filePath = new File(path);
    Scanner codeFile = new Scanner(filePath);
    while (codeFile.hasNextLine()) {
      code1D = Arrays.copyOf(code1D, code1D.length + 1);
      code1D[code1D.length - 1] = codeFile.nextLine();
    }

    //Convert codebase into 2D array where 1st index is the line 2nd is the section
    //Such that:
    //"incr name;" = ["incr", "name"]
    //"while X not 0 do;" = ["while", "X", "not", "0", "do"]
    code = new String[code1D.length][2];
    //Loops over each line
    for (int lineNumber = 0; lineNumber < code1D.length; lineNumber++) {
      //Remove all left pad
      while (code1D[lineNumber].charAt(0) == ' ') {
        code1D[lineNumber] = code1D[lineNumber].substring(1);
      }

      //Splits into chunks
      code[lineNumber] = code1D[lineNumber].split("\\s+");

      //Removes semicolon
      String last = code[lineNumber][code[lineNumber].length - 1];
      code[lineNumber][code[lineNumber].length - 1] = last.substring(0, last.length() - 1);
      }
  }

  private void runCode() {
    int pointer = 0;    //Current line
    Memory memory = new Memory(); //Variable store
    Stack<Integer> callStack= new Stack(); //Loop stack - hold where to jump to when an end is reached;
    while (pointer < code.length) {
      switch (code[pointer][0]) {
        case "clear":
          memory.clr(code[pointer][1]);
          break;
        case "incr":
          memory.incr(code[pointer][1]);
          break;
        case "decr":
          memory.decr(code[pointer][1]);
          break;
        case "while":
          //Check if conditions met
          if (memory.get(code[pointer][1]) == Integer.parseInt(code[pointer][3])) {
            //If they are go to line after the next end (skip past the code in the while)
            for (int searchLine = pointer; searchLine < code.length; searchLine++) {
              if (code[searchLine][0].contains("end")) {
                pointer = searchLine;
                break;
              }
            }
          } else {
            //Otherwise add this while to the call stack for when end is reached
            callStack.push(pointer);
          }
          break;
        case "end":
          //Pop last while off stack and goto it
          if (!(callStack.empty())) {
            pointer = callStack.pop() - 1;
          }
          break;
        default:
          System.out.println("Unknown Command found, skipping: " + code[pointer].toString());
          break;
      }
      //Once a line has been dealt with move onto the next line
      pointer++;
    }
  }
}
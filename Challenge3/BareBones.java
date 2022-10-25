package Challenge3;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

//This file is intended as a bare-bones interpreter.
public class BareBones {

  private ArrayList<String[]> code = new ArrayList<String[]>();
  //String[][] code;

  public static void main(String[] args) throws IOException {
    BareBones runtime = new BareBones();
    runtime.setupCodebase();
    runtime.runCode();
  }

  //This function asks the user to input the file name and reads it into a 2D array [line,instruction segment]
  private void setupCodebase() throws IOException {
    //Gets the path from the user
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the filename (starts from ./tests/, auto appends .txt)");
    String path = "Challenge3/tests/" + reader.readLine() + ".txt";

    //Reads in file to a string
    //Scanner methods learnt from: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    String codeString = "";
    File filePath = new File(path);
    Scanner codeFile = new Scanner(filePath);
    while (codeFile.hasNextLine()) {
      String currentLine = codeFile.nextLine();
      currentLine.trim();
      codeString += currentLine;
    }
    //Convert string into 2D array where 1st index is the line 2nd is the section
    //Such that:
    //"incr name;" => ["incr", "name"]
    //"while X not 0 do;" => ["while", "X", "not", "0", "do"]
    for (String currentLine: codeString.split(";")) {
      code.add(currentLine.trim().split(" "));
    }
  }
  private int getMatchingEnd(int currentPos) {
    // A section is started on commands such as whiles and is ended with while statements
    int sectionCount = 0;
    Pattern sectionStarter = Pattern.compile("while",Pattern.CASE_INSENSITIVE);
    Pattern sectionEnder = Pattern.compile("end", Pattern.CASE_INSENSITIVE);

    for (int searchLine = currentPos; searchLine < code.size(); searchLine++) {
      if (sectionStarter.matcher(code.get(searchLine)[0]).find()) {
        sectionCount++;
      } else if (sectionEnder.matcher(code.get(searchLine)[0]).find()) {
        sectionCount--;
        if (sectionCount == 0) {
          return searchLine;
        }
      }
    }
    return -1;
  }

  private int handleWhile(int pointer, Memory memory, Stack<Integer> callStack) {
    //Check if conditions met
    if (memory.get(code.get(pointer)[1]) == Integer.parseInt(code.get(pointer)[3])) {
      //If they are go to line after the next end (skip past the code in the while)
      //Changed to allow for handling of nested while loops
      return getMatchingEnd(pointer);
    } else {
      //Otherwise add this while to the call stack for when end is reached
      callStack.push(pointer);
    }
    return pointer;
  }

  private void runCode() {
    int pointer = 0;    //Current line
    Memory memory = new Memory(); //Variable store
    Stack<Integer> callStack= new Stack(); //Loop stack - hold where to jump to when an end is reached;
    ALU calculator = new ALU();
    while (pointer < code.size()) {
      switch (code.get(pointer)[0]) {
        case "clear" -> {
          memory.clr(code.get(pointer)[1]);
        }
        case "incr" -> {
          memory.incr(code.get(pointer)[1]);
        }
        case "decr" -> {
          memory.decr(code.get(pointer)[1]);
        }
        case "while"  -> {
          pointer = handleWhile(pointer, memory, callStack);
          if (pointer == -1) {
            System.out.println("Matching end to while not found");
          }
        }
        case "end" -> {
          if (!(callStack.empty())) {
            pointer = callStack.pop() - 1;
          }
        }
        default -> {
          switch (code.get(pointer)[1]) {
            case "=" -> {
              calculator.calculateSet(code.get(pointer), memory);
            }default -> {
              System.out.println("Unknown Command found, skipping: " + code.get(pointer).toString());
            }
          }
        }
      }
      //Once a line has been dealt with move onto the next line
      pointer++;
    }
  }
}
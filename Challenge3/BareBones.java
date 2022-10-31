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
    Pattern sectionStarter = Pattern.compile("while|if",Pattern.CASE_INSENSITIVE);
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

  private String[] processLine(String[] line, Memory memory, ALU calculator) {
    for (int ai = 0; ai < line.length; ai++) {
      if (line[ai].charAt(0) == '=' && line[ai].charAt(1) != '=') {
        line[ai] = calculator.processMaths(line[ai], memory);
      }
    }
    return line;
  }

  private void runCode() {
    int pointer = 0;    //Current line
    Memory memory = new Memory(); //Variable store
    Stack<Integer> callStack= new Stack(); //Loop stack - hold where to jump to when an end is reached;
    boolean echo = true;
    ALU calculator = new ALU();
    while (pointer < code.size()) {
      //Calculate maths
      String[] processedLine = processLine(code.get(pointer),memory, calculator);
      //Handle instruction
      switch (processedLine[0]) {
        case "clear" -> {
          memory.clr(processedLine[1], echo);
        }
        case "incr" -> {
          memory.incr(processedLine[1], echo);
        }
        case "decr" -> {
          memory.decr(processedLine[1], echo);
        }
        case "while"  -> {
          pointer = handleWhile(pointer, memory, callStack);
          if (pointer == -1) {
            System.out.println("Matching end to while not found");
          }
        }
        case "end" -> {
          if (!(callStack.empty())) {
            int newPoint = callStack.pop();
            if (newPoint >= 0) {
              pointer = newPoint - 1;
            }
          }
        }
        case "set" -> {
          if (processedLine[2].charAt(0) == '#') {
            memory.set(processedLine[1],Integer.parseInt(processedLine[2].substring(1)), echo);
          } else {
            System.out.println(processedLine[2]);
            memory.set(processedLine[1],memory.get(processedLine[2]), echo);
          }
        }
        case "//" -> {
          //Code is a comment, do nothing
        }
        case "echo" -> {
          if (processedLine[1].equals("0") || processedLine[1].equals("off")) {
            echo = false;
          } else {
            echo = true;
          }
        }
        case "input" -> {
          int input = -1;
          while (input < 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
              input = Integer.parseInt(reader.readLine());
            } catch (Exception ex) {
              input = -1;
            }
            if (input < 0) {
              System.out.println("Invalid number, try again");
            }
          }
          memory.set(processedLine[1], input, echo);
        }
        case "output" -> {
          for (int ai = 1; ai < processedLine.length; ai++) {
            System.out.print(processedLine[ai] + " ");
          }
          System.out.println();
        }
        case "if" -> {
          if (calculator.evaluateIf(processedLine,memory) == false) {
            //If is false
            int matchingEnd = getMatchingEnd(pointer);
            if (matchingEnd != -1) {
              pointer = matchingEnd;
            }
          } else {
            callStack.push(-1);
          }
        }
        default -> {
          System.out.println("Unknown Command found, skipping: " + processedLine.toString());
        }
      }
      //Once a line has been dealt with move onto the next line
      pointer++;
    }
    //Print final state:
    System.out.println("Final state:");
    memory.dump();
  }
}
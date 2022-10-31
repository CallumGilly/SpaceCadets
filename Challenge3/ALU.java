package Challenge3;

import java.util.Stack;
import java.util.regex.Pattern;

public class ALU {

  public String processMaths(String line, Memory memory) {
    line = line.substring(1);
    String[] math = line.split(",");
    Stack<Integer> stack = new Stack<Integer>();
    Pattern operation = Pattern.compile("\\+|-|\\*|\\/|%|\\^");
    for (int ai = 0; ai < math.length; ai++) {
      if (math[ai].charAt(0) == '#') {
        //Here we have a direct number
        stack.push(Integer.parseInt(math[ai].substring(1)));
      } else if (operation.matcher(math[ai]).matches()) {
        //Here we have an operation
        int num2 = stack.pop();
        int num1 = stack.pop();
        switch (math[ai]) {
          case "+" -> {
            stack.push(num1 + num2);
          }
          case "-" -> {
            stack.push( Math.max(num1 - num2,0));
          }
          case "*" -> {
            stack.push(num1 * num2);
          }
          case "/" -> {
            stack.push(num1 / num2);
          }
          case "%" -> {
            stack.push(num1 % num2);
          }
          case "^" -> {
            stack.push((int) Math.pow(num1, num2));
          }
        }
      } else {
        //Here we have a memory reference
        stack.push(memory.get(math[ai]));
      }
    }
    return "#" + stack.pop();
  }

  public boolean evaluateIf(String[] line, Memory memory) {
    //Boolean request should be: val relation val
    int thenPos = line.length;
    for (int lineIndex = 2; lineIndex < line.length; lineIndex++) {
      if (line[lineIndex].equals("then")) {
        thenPos = lineIndex;
      }
    }
    //Now between 1 and thenPos is a binary expression
    int nextIndex = 1;
    String leftSide = null;
    String rightSide = null;

    while (nextIndex < thenPos) {
      if (leftSide == null) {
        leftSide = evalBlock(line[nextIndex], memory);
        nextIndex += 2;
      } else if (rightSide == null) {
        rightSide = evalBlock(line[nextIndex], memory);
        nextIndex -= 1;
      } else {
        switch (line[nextIndex]) {
          case "not", "!=" -> {
            if (Integer.parseInt(leftSide) != Integer.parseInt(rightSide)) {
              leftSide = "1";
            } else {
              leftSide = "0";
            }
          }
          case "<=", "=<" -> {
            if (Integer.parseInt(leftSide) <= Integer.parseInt(rightSide)) {
              leftSide = "1";
            } else {
              leftSide = "0";
            }
          }
          case ">=", "=>" -> {
            if (Integer.parseInt(leftSide) >= Integer.parseInt(rightSide)) {
              leftSide = "1";
            } else {
              leftSide = "0";
            }
          }
          case "==" -> {
            if (Integer.parseInt(leftSide) == Integer.parseInt(rightSide)) {
              leftSide = "1";
            } else {
              leftSide = "0";
            }
          }
        }
        rightSide = null;
        nextIndex += 2;
      }
    }
    if (leftSide == "1") {
      return true;
    } else {
      return false;
    }
  }
  private String evalBlock(String block, Memory memory) {
    String evaluatedBlock;
    if (block.equals("f") || block.equals("0") || block.equals("false")) {
      evaluatedBlock = "0";
    } else if (block.equals("t") || block.equals("1") || block.equals("true")) {
      evaluatedBlock = "1";
    } else if (block.charAt(0) == '#') {
      evaluatedBlock = block.substring(1);
    } else {
      evaluatedBlock = String.valueOf(memory.get(block));
    }
    return evaluatedBlock;
  }
}

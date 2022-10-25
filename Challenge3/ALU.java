package Challenge3;

public class ALU {
  public void calculateSet(String[] instruction, Memory memory) {
    String[] calculation = shiftReduceArray(instruction,2);

    memory.set(instruction[0], calculateReturn(calculation, memory));
  }

  public String[] shiftReduceArray(String[] original, int shiftValue) {
    String[] newArr = new String[3];
    for (int i = 0; i < 3; i++) {
      newArr[i] = original[i+shiftValue];
    }
    return newArr;
  }
  public int calculateReturn(String[] instruction, Memory memory) {
    int val1 = memory.get(instruction[0]);
    int val2;
    if (instruction[2].substring(0,1) == "#") {
      val2 = Integer.parseInt(instruction[2].substring(1));
    } else {
      val2 = memory.get(instruction[2]);
    }
    switch (instruction[1]) {
      case "+" -> {
        return val1 + val2;
      }
      case "-" -> {
       return Math.max(val1 - val2,0);
      }
      case "/" -> {
        return Math.floorDiv(val1, val2);
      }
      case "*" -> {
        return val1 * val2;
      } default -> {
        return -1;
      }
    }
  }
}

package Challenge3;

import java.util.Hashtable;

//Memory class is effectively just an accessor for the hashtable
public class Memory {
  private Hashtable<String, Integer> varTable = new Hashtable();
  public void clr(String varName) {
    clr(varName,true);
  }

  public void clr(String varName, boolean dump) {
    varTable.put(varName, 0);
    if (dump) {
      dump();
    }
  }

  public void incr(String varName) {
    incr(varName, true);
  }

  public void incr(String varName, boolean dump) {
    //If var does not exist it will be assumed to have been zero
    varTable.put(varName,varTable.getOrDefault(varName,0) + 1);
    if (dump) {
      dump();
    }
  }

  public void decr(String varName) {
    decr(varName, true);
  }

  public void decr(String varName, boolean dump) {
    //If var does not exist it will be assumed to have been zero
    varTable.put(varName,varTable.getOrDefault(varName,0) - 1);
    if (dump) {
      dump();
    }
  }

  public void set(String varName, int value) {
    set(varName, value,true);
  }

  public void set(String varName, int value, Boolean dump) {
    varTable.put(varName,value);
    if (dump) {
      dump();
    }
  }

  public int get(String varName) {
    return varTable.get(varName);
  }

  public void dump() {
    System.out.println(varTable.toString());
  }
}

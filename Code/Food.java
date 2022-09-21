import java.util.*;

class Food {
  private String date;
  private String name;
  private int netCarbs;
  private int gI;
  private int units;

  public Food(String name, String date, int units) {
    this.name = name;
    this.units = units;
    this.date = date;
  }

  public Food(String name, int netCarbs, int gI) {
    this.name = name;
    this.netCarbs = netCarbs;
    this.gI = gI;
  }

  public String toString() {
    return name;
  }


  public void print() {
    System.out.print(name);
  }

  public int getCarbs() {
    return (netCarbs * units);
  }

  public String getName() {
    return name;
  }

  public String getDate(){
    return date;
  }

  public int getGI() {
    return gI;
  }

  public static ArrayList<Food> sortByDates(ArrayList<Food> foods) {
    ArrayList<Food> foodHolder = foods;
    boolean exchangeMade = true;
    for (int pass = 1; pass < foodHolder.size() && exchangeMade; pass++) {
      exchangeMade = false;
      for (int index = 0; index < foodHolder.size() - pass; index++) {
        if (Food.compareDates(foodHolder.get(index), foodHolder.get(index + 1))) {
          Food temp = foodHolder.get(index);
          foodHolder.set(index, foodHolder.get(index + 1));
          foodHolder.set(index + 1, temp);
          exchangeMade = true;
        }
      }
    }
    return foodHolder;
  }

  public static boolean compareDates(Food a, Food b) {
    String[] holder = a.getDate().split("/");
    String[] holderTwo = b.getDate().split("/");

    if (Integer.parseInt(holder[2]) > Integer.parseInt(holderTwo[2])) {
      return true;
    } else if (Integer.parseInt(holder[2]) < Integer.parseInt(holderTwo[2])) {
      return false;
    }

    if (Integer.parseInt(holder[0]) > Integer.parseInt(holderTwo[0])) {
      return true;
    } else if (Integer.parseInt(holder[0]) < Integer.parseInt(holderTwo[0])) {
      return false;
    }

    if (Integer.parseInt(holder[1]) > Integer.parseInt(holderTwo[1])) {
      return true;
    } else if (Integer.parseInt(holder[1]) < Integer.parseInt(holderTwo[1])) {
      return false;
    }
    String[] timeHolder = holder[3].split(":");
    String[] timeHolderTwo = holderTwo[3].split(":");
    if (Integer.parseInt(timeHolder[0]) > Integer.parseInt(timeHolderTwo[0])) {
      return true;
    } else if (Integer.parseInt(timeHolder[0]) < Integer.parseInt(timeHolderTwo[0])) {
      return false;
    }

    if (Integer.parseInt(timeHolder[1]) > Integer.parseInt(timeHolderTwo[1])) {
      return true;
    } else {
      return false;
    }
  }
}
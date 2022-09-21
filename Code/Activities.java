import java.util.*;

class Activities {
  private String date;
  private String time;
  private String activity;

  public Activities(String activity, String date, String time) {
    this.date = date;
    this.time = time;
    this.activity = activity;
  }

  public String toString() {
    String[] holder= date.split("/");
    return activity + " for " + time + " minutes on " + holder[3];
  }

  public String getDate(){
    return date;
  }

  public static ArrayList<Activities> sortByDates(ArrayList<Activities> activities) {
    ArrayList<Activities> actHolder = activities;
    boolean exchangeMade = true;
    for (int pass = 1; pass < actHolder.size() && exchangeMade; pass++) {
      exchangeMade = false;
      for (int index = 0; index < actHolder.size() - pass; index++) {
        if (Activities.compareDates(actHolder.get(index), actHolder.get(index + 1))) {
          Activities temp = actHolder.get(index);
          actHolder.set(index, actHolder.get(index + 1));
          actHolder.set(index + 1, temp);
          exchangeMade = true;
        }
      }
    }
    return actHolder;
  }

  public static boolean compareDates(Activities a, Activities b) {
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
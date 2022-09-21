import java.util.*;

class Day {
  private String date;
  private ArrayList<Log> logs;
  private ArrayList<Activities> activities;
  private ArrayList<Food> foods;
  private static int totalDays;

  public Day(String date, ArrayList<Log> logs, ArrayList<Activities> activities, ArrayList<Food> foods) {
    this.date = date;
    this.logs = Log.sortByDates(logs);
    this.activities = Activities.sortByDates(activities);
    this.foods = Food.sortByDates(foods);
    totalDays++;
  }

  public Day(String date) {
    this.date = date;
    totalDays++;
  }

  public int getDays() {
    return totalDays;
  }

  public ArrayList<Activities> getActivities(){
    return activities;
  }

  public String getDate() {
    return date;
  }

  public ArrayList<Log> getLogs() {
    return logs;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setLog(ArrayList<Log> logs) {
    this.logs = logs;
  }

  public void setFood(ArrayList<Food> foods) {
    this.foods = foods;
  }

  public void setActivities(ArrayList<Activities> activities) {
    this.activities = activities;
  }

  public void addLog(Log log) {
    logs.add(log);
    logs = Log.sortByDates(logs);
  }

  public void addActivity(Activities activity) {
    this.activities.add(activity);
  }

  public void addFood(Food food) {
    this.foods.add(food);
  }

  public void addLogs(ArrayList<Log> logs) {
    for (int i = 0; i < logs.size(); i++) {
      this.logs.add(logs.get(i));
    }
  }

  public void addActivities(ArrayList<Activities> activities) {
    for (int i = 0; i < activities.size(); i++) {
      this.activities.add(activities.get(i));
    }
  }

  public void addFoods(ArrayList<Food> foods) {
    for (int i = 0; i < foods.size(); i++) {
      this.foods.add(foods.get(i));
    }
  }

  public double logAvg() {
    double total = 0;
    for (Log log : logs) {
      total += log.getSugar();
    }
    total /= logs.size();
    return total;
  }

  public String toString() {
    logs=Log.sortByDates(logs);
    return "\n"+date + ":\n\nactivities: " + activities + "\n\nfoods: " + foods + "\n\nlogs:" + logs + "\n"
        + "average glucose level:" + String.format("%.2f", logAvg());
  }

  public void print() {
    System.out.print("\nDate: " + date + "\n--Logs--\n");
    for (int i = 0; i < logs.size(); i++) {
      System.out.print(logs.get(i));
    }
    System.out.print("\n--Activities--\n");
    for (int i = 0; i < activities.size(); i++) {
      System.out.print(activities.get(i));
    }
    System.out.print("\n--Foods--\n");
    for (int i = 0; i < foods.size(); i++) {
      System.out.print(foods.get(i));
    }
    System.out.println("\nAverage glucose level: " + String.format("%.2f", logAvg()) + "\n");
  }

  private static boolean compareDates(Day a, Day b) {
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
    } else {
      return false;
    }
  }

  public static ArrayList<Day> sortDays(ArrayList<Day> days) {
    ArrayList<Day> dayHolder = days;
    boolean exchangeMade = true;
    for (int pass = 1; pass < dayHolder.size() && exchangeMade; pass++) {
      exchangeMade = false;
      for (int index = 0; index < dayHolder.size() - pass; index++) {
        if (Day.compareDates((dayHolder).get(index), dayHolder.get(index + 1))) {
          Day temp = dayHolder.get(index);
          dayHolder.set(index, dayHolder.get(index + 1));
          dayHolder.set(index + 1, temp);
          exchangeMade = true;
        }
      }
    }
    return dayHolder;
  }

  public static int searchDay(ArrayList<Day> days, String target) {
    days=sortDays(days);
    String[] holder= target.split("/");
    target = holder[0]+"/"+holder[1]+"/"+holder[2];
    int leftSide = 0;
    int rightSide = days.size() - 1;
    int middleNum = (leftSide + rightSide) / 2;

    while (leftSide <= rightSide) {
      middleNum = (leftSide + rightSide) / 2;
      int decision = compareDate(target, days.get(middleNum));
      switch (decision) {
      case 0:
        return -1;
      case 1:
        leftSide = middleNum + 1;
        break;
      case 2:
        rightSide = middleNum - 1;
        break;
      case 3:
        return middleNum;
      }
    }
    return -1;
  }

  private static int compareDate(String target, Day day) {
    String[] targetDate = target.split("/");
    String[] dayDate = day.getDate().split("/");
    int dayTotal = 0;
    int targetTotal = 0;
    for (int i = 1; i <= 12; i++) {
      if (i == 2) {
        if (i <= Integer.parseInt(targetDate[0])) {
          targetTotal += 28;
        }
        if (i <= Integer.parseInt(dayDate[0])) {
          dayTotal += 28;
        }
      }
      if (i % 2 == 1) {
        if (i <= Integer.parseInt(targetDate[0])) {
          targetTotal += 31;
        }
        if (i <= Integer.parseInt(dayDate[0])) {
          dayTotal += 31;
        }
      } else {
        if (i <= Integer.parseInt(targetDate[0])) {
          targetTotal += 30;
        }
        if (i <= Integer.parseInt(dayDate[0])) {
          dayTotal += 30;
        }
      }
    }

    targetTotal += Integer.parseInt(targetDate[2]) * 365 + Integer.parseInt(targetDate[1]);
    dayTotal += Integer.parseInt(dayDate[2]) * 365 + Integer.parseInt(dayDate[1]);

    if (targetTotal > dayTotal) {
      return 1;
    } else if (dayTotal > targetTotal) {
      return 2;
    } else if (dayTotal==targetTotal){
      return 3;
    }else{
      return 0;
    }

  }
}
import java.util.*;

class Log {
  private double bloodSugar;
  private String date;
  private boolean dangerZone;

  public Log(double bloodSugar, String date) {
    if (bloodSugar < 0 || bloodSugar > 20) {
      this.bloodSugar = 7.0;
    } else {
      this.bloodSugar = bloodSugar;
      dangerZone = (bloodSugar < 4.0 || bloodSugar > 11.0);
    }
    this.date = date;
  }

  public double getSugar() {
    return bloodSugar;
  }

  public String getDate() {
    return date;
  }

  public boolean getDanger() {
    return dangerZone;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public static boolean compareDates(Log a, Log b) {
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

  public String toString() {
    String[] holder=date.split("/");
    return "Time: " + holder[3] + " Sugar level: " + bloodSugar + " In danger zone: " + dangerZone + "\n";
  }

  public static ArrayList<Log> sortByDates(ArrayList<Log> logs) {
    ArrayList<Log> logHolder = logs;
    boolean exchangeMade = true;
    for (int pass = 1; pass < logHolder.size() && exchangeMade; pass++) {
      exchangeMade = false;
      for (int index = 0; index < logHolder.size() - pass; index++) {
        if (Log.compareDates(logHolder.get(index), logHolder.get(index + 1))) {
          Log temp = logHolder.get(index);
          logHolder.set(index, logHolder.get(index + 1));
          logHolder.set(index + 1, temp);
          exchangeMade = true;
        }
      }
    }
    return logHolder;
  }

  public static ArrayList<Log> sortBySugar(ArrayList<Log> logs) {
    ArrayList<Log> logHolder = logs;
    boolean exchangeMade = true;
    for (int pass = 1; pass < logHolder.size() && exchangeMade; pass++) {
      exchangeMade = false;
      for (int index = 0; index < logHolder.size() - pass; index++) {
        if (logHolder.get(index).getSugar() > logHolder.get(index + 1).getSugar()) {
          Log temp = logHolder.get(index);
          logHolder.set(index, logHolder.get(index + 1));
          logHolder.set(index + 1, temp);
          exchangeMade = true;
        }
      }
    }
    return logHolder;
  }

  public static int numDangers(ArrayList<Log> logs) {
    int total = 0;
    for (int i = 0; i < logs.size(); i++) {
      if (logs.get(i).getDanger()) {
        total++;
      }
    }
    return total;
  }
}
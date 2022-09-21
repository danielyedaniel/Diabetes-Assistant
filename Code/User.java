import java.util.*;
import java.io.*;
import java.text.*;

class User {
  private String name;
  private String gender;
  private int age;
  private double bw;
  private double height;
  private ArrayList<Day> days;
  private ArrayList<Food> foods;
  private File file;

  public User(String name, String gender, int age, double height, double bw, ArrayList<Day> days, ArrayList<Food> foods) {
    this.gender = gender;
    this.age = age;
    this.name = name;
    this.days = Day.sortDays(days);
    this.foods = foods;
    this.height=height;
    this.bw=bw;
    file = new File(name + ".csv");
  }

  public User(String name) {
    this.name = name;
    file = new File(name + ".csv");

  }

  public User() {
  }

  public void addDays(ArrayList<Day> days) {
    for (int i = 0; i < days.size(); i++) {
      this.days.add(days.get(i));
    }
  }

  public void addDay(Day day) {
    days.add(day);
  }

  public void addLog(int index, Log log) {
    days.get(index).addLog(log);
  }

  public String getName() {
    return name;
  }
  public String getGender(){
    return gender;
  }
  public double getHeight(){
    return height;
  }
  public int getAge(){
    return age;
  }
  public double getBW(){
    return bw;
  }

  public ArrayList<Day> getDays() {
    return days;
  }

  public ArrayList<Food> getFoods() {
    return foods;
  }

  public String toString() {
    return "Name: " + name + "\nGender: " + gender + "\nAge: " + age + "\nBody Weight: "+bw+" lbs";
  }

  public void sortDays() {
    days = Day.sortDays(days);
  }

  public void newUser() {
    try {
      File temp = new File("newUser.csv");
      FileWriter fw = new FileWriter(file, true);
      Scanner copying = new Scanner(temp);
      while (copying.hasNext()) {
        String holder = "\n" + copying.nextLine();
        fw.write(holder);
      }
      copying.close();
      fw.close();
    } catch (Exception e) {
      System.out.println("ERROR");
    }
  }

  public String previousOccurences(double sugar) {
    days=Day.sortDays(days);
    System.out.println();
    String holder="";
    int prints=0;
    for (int i = days.size()-1; i >=0; i--) {
      for (int j = days.get(i).getLogs().size()-1; j >=0; j--) {
        if (days.get(i).getLogs().get(j).getSugar() > (sugar - 0.5)
            && days.get(i).getLogs().get(j).getSugar() < (sugar + 0.5) && j < days.get(i).getLogs().size() - 1 && prints<4) {
          holder+="Similar log on " + days.get(i).getLogs().get(j).getDate() + " sugar level of "
              + days.get(i).getLogs().get(j).getSugar() + "\nFollowed by a log of "
              + days.get(i).getLogs().get(j + 1).getSugar() + " sugar level"
              + dateDiff(days.get(i).getLogs().get(j).getDate(), days.get(i).getLogs().get(j + 1).getDate())+"\n\n";
          System.out.println("Similar log on " + days.get(i).getLogs().get(j).getDate() + " sugar level of "
              + days.get(i).getLogs().get(j).getSugar() + " | Followed by a log of "
              + days.get(i).getLogs().get(j + 1).getSugar() + " sugar level"
              + dateDiff(days.get(i).getLogs().get(j).getDate(), days.get(i).getLogs().get(j + 1).getDate()));
          prints++;
        }
      }
    }
    return holder;
  }

  private String dateDiff(String dateOne, String dateTwo) {
    String[] holderOne = dateOne.split("/");
    String[] holderTwo = dateTwo.split("/");

    String[] timeOne = holderOne[3].split(":");
    String[] timeTwo = holderTwo[3].split(":");

    int hours = Integer.parseInt(timeTwo[0]) - Integer.parseInt(timeOne[0]);
    int minutes = Integer.parseInt(timeTwo[1]) - Integer.parseInt(timeOne[1]);

    return " which was taken " + hours + " hours, " + minutes + " minutes later.";
  }

  public void createCurrentLogs() {
    int choices = -1;
    do {
      TimeZone.setDefault(TimeZone.getTimeZone("EST"));
      SimpleDateFormat time_format = new SimpleDateFormat("MM/dd/YYYY/HH:mm");
      Date date = new Date();
      String time = time_format.format(date);
      String[] times = time.split("/");
      String[] timess = times[3].split(":");
      int hour = Integer.parseInt(timess[0]) + 1;
      if (hour==25){hour=1;}
      time = times[0] + "/" + times[1] + "/" + times[2] + "/" + hour + ":" + timess[1];
      try {
        FileWriter fw = new FileWriter(file, true);
        Scanner kbReader = new Scanner(System.in);
        System.out
            .println("What type of log would you like to create? \n0:Sugar Log\n1:Food Log\n2:Activity Log\n3:exit");
        choices = Integer.parseInt(kbReader.nextLine());
        switch (choices) {
        case 0:
          System.out.println("Please enter the sugar level: ");
          double sugarLvl = Double.parseDouble(kbReader.nextLine());
          fw.write("\nsugar," + sugarLvl + "," + time);
          Log log = new Log(sugarLvl, time);
          System.out.println("What is the current trend of the sugar?\n0:down\n1:flat\n2:up");
          int trend = Integer.parseInt(kbReader.nextLine());
          if (log.getSugar() < 4.0) {
            System.out.print("\nYou should eat something! How about: ");
            for (Food food : foods) {
              if (food.getGI() > 50) {
                food.print();
                System.out.print(", ");
              }
            }
          }else if (log.getSugar()<5.0 && (trend==0||trend==1)){
            for (Food food : foods) {
              if (food.getGI() > 30 && food.getGI()<70) {
                food.print();
                System.out.print(", ");
              }
            }
          }else if (log.getSugar()>11.0){
            System.out.println("You should exercise! Maybe go for a walk.");
          } else if (log.getSugar()>9.0 && (trend==1 || trend==2)){
            System.out.println("Watch out for foods with sugar. Wait until sugar level gets a little lower before eating.");
          }else{
            System.out.println("Sugar levels are looking good!");
          }
          previousOccurences(sugarLvl);
          break;
          case 1:
            System.out.println("Please enter the name of this food:");
            String food=kbReader.nextLine();
            System.out.println("Please enter the number of units consumed:");
            int units=Integer.parseInt(kbReader.nextLine());
            fw.write("\nfood,"+food+","+time+","+units);
            if (units>1){System.out.println("Be careful about your eating. Some foods can spike sugar levels by a lot.");}
            break;
          case 2:
            System.out.println("Please enter the name of this activity:");
            String activity=kbReader.nextLine();
            System.out.println("Please enter the duration of the activity done in minutes:");
            int duration=Integer.parseInt(kbReader.nextLine());
            fw.write("\nactivity,"+activity+","+time+","+duration);
            System.out.println("Be sure to keep track of your sugar while active. You burn through sugar when exercising.");
            break;
          case 3: break;
          default: System.out.println("Invalid option!");
        }
        fw.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } while (choices != 3);
  }

  public void createDayLogs() {
    try {
      Scanner kbReader = new Scanner(System.in);
      System.out.println("Please enter the date of the entries following this format: MM/DD/YYYY ex: 03/05/2004");
      String date = kbReader.nextLine();
      int answer;
      do {
        FileWriter fw = new FileWriter(this.file, true);
        System.out.println(
            "Enter some logs! Select between\n0:Sugar Log\n1:Food Log\n2:Activity Log\n3:New Food\n4:Change date \n5:menu\n6:exit");
        answer = Integer.parseInt(kbReader.nextLine());
        if (answer == 5) {
          menu();
          answer = Integer.parseInt(kbReader.nextLine());
        }
        String input = "";
        switch (answer) {
        case 0:
          input = "sugar";
          System.out.println("Please enter sugar level:");
          input += "," + kbReader.nextLine() + "," + date;
          System.out.println("Please enter the time of this entry using the 24 hour clock format ex: 21:30");
          input += "/" + kbReader.nextLine();
          fw.write("\n" + input);
          fw.close();
          break;
        case 1:
          input = "food";
          System.out.println("Please enter name of food:");
          input += "," + kbReader.nextLine() + "," + date;
          System.out.println("Please enter the time of this entry using the 24 hour clock format ex: 21:30");
          input += "/" + kbReader.nextLine();
          System.out.println("Please enter the units consumed.");
          input += "," + kbReader.nextLine();
          fw.write("\n" + input);
          fw.close();
          break;
        case 2:
          input = "activity";
          System.out.println("Please enter the name of the activity:");
          input += "," + kbReader.nextLine() + "," + date;
          System.out.println("Please enter the time of this entry using the 24 hour clock format ex: 21:30");
          input += "/" + kbReader.nextLine();
          System.out.println("Please enter the duration of the activity in minutes");
          input += "," + kbReader.nextLine();
          fw.write("\n" + input);
          fw.close();
          break;
        case 3:
          input = "newFood";
          System.out.println("Please enter the name of the new food:");
          input += "," + kbReader.nextLine();
          System.out.println("Please enter the net carbohydrates of a single unit");
          input += "," + kbReader.nextLine();
          System.out.println("Please enter the glycemic index value of the food");
          input += "," + kbReader.nextLine();
          fw.write("\n" + input);
          fw.close();
          break;
        case 4:
          System.out.println("Please enter the date of the entries following this format: MM/DD/YYYY ex: 03/05/2004");
          date = kbReader.nextLine();
          break;
        case 6:
          break;
        default:
          System.out.println("Invalid option!");
        }
      } while (answer != 6);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void findDay() {
    int answer;
    do {
      String date;
      Scanner kbReader = new Scanner(System.in);
      System.out.println("Would you like to \n0:Open specific date\n1:Open all dates\n2:exit");
      answer = Integer.parseInt(kbReader.nextLine());
      days = Day.sortDays(days);
      switch (answer) {
      case 0:
        System.out.println("Please enter the date of the day you would like to open following this format: MM/DD/YYYY");
        date = kbReader.nextLine();
        int index = Day.searchDay(days, date);
        if (index == -1) {
          System.out.println("No logs for this date.");
        } else {
          days.get(index).print();
        }
        break;
      case 1:
        for (Day day : days) {
          day.print();
          System.out.println("0:Continue\n1:Exit");
          int answ = Integer.parseInt(kbReader.nextLine());
          if (answ == 1) {
            break;
          }
        }
        break;
      case 2:
        break;
      }
    } while (answer != 2);
  }

  public void changeInfo(){
    String gender=this.gender;
    int age=this.age;
    double bw=this.bw;
    double height=this.height;
    Scanner kbReader=new Scanner(System.in);
    System.out.println("What would you like to change?\n0:gender\n1:age\n2:height\n3:body weight\n4:exit");
    int choice=Integer.parseInt(kbReader.nextLine());
    switch (choice){
      case 0:System.out.println("Enter gender\n0:female\n1:male");gender=kbReader.nextLine();break;
      case 1:System.out.println("Enter your age:");age=Integer.parseInt(kbReader.nextLine());break;
      case 2:System.out.println("Enter your height (cm):");height=Double.parseDouble(kbReader.nextLine());
      case 3:System.out.println("Enter your body weight (lbs):");bw=Double.parseDouble(kbReader.nextLine());break;
      case 4:break;
      default:System.out.println("Invalid option!");}
    try{
      FileWriter fw=new FileWriter(file,true);
      fw.write("\nuser,"+this.name+","+gender+","+age+","+height+","+bw);
      fw.close();
    }catch(Exception e){e.printStackTrace();}
  }

  public String info(){
    double calories;
    double total=0;
    for(Day day:days){
      total+=day.getActivities().size();
    }
    total/=days.size();
    if (total<1){total=1;}
    else if (total<2){total=1.28;}
    else if (total<3.5){total=1.4;}
    else if (total<5){total=1.6;}
    else{total=1;}
    if (gender.equals("male")){calories=66 + (13.8 * bw/2.2) + (5 * height) - (6.8 * age);}
    else{calories=655 + (9.6 * bw/2.2) + (1.8 * height) - (4.7 * age);}
    double recCalories=calories*total;
    System.out.println(toString()+"\nRecommended daily calories: "+String.format("%.1f", recCalories)+"\nBase metabolism: "+String.format("%.1f", calories)+"\nRecommended daily grams of carbohydrates: "+(int)(recCalories/8)+"\n");
    return toString()+"\nRecommended daily calories: "+String.format("%.1f", recCalories)+"\nBase metabolism: "+String.format("%.1f", calories)+"\nRecommended daily grams of carbohydrates: "+(int)(recCalories/8)+"\n";
  }

  public void menu() {
    System.out.println(
        "\"Sugar Log\": Enter a recording of sugar level\n\"Food Log\":Enter a recording of food eaten\n\"Activity Log\": Enter a record of activity done\n\"New Food\": Enter a new type of food you enjoy!\n\"Change Date\": Change the date of the entries\nexit: exit log recording\nPlease type an option:");
  }
}
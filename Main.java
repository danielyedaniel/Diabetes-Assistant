import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.*;

class Main{
  private static JFrame frame;
  private static JPanel mainPanel;
  private static JPanel topPanel;
  private static JPanel bottomPanel;
  private static JPanel leftPanel;
  private static JPanel rightPanel;
  private static JTextField field1;
  private static JTextField field2;
  private static User user;
  private static File userFile;
  private static String name;

  public static User readUser(File file) {
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Day> days = new ArrayList<>();
    ArrayList<Log> logs = new ArrayList<>();
    ArrayList<Activities> activities = new ArrayList<>();
    ArrayList<Food> foodLogs = new ArrayList<>();
    String name = "";
    String gender = "";
    int age = 0;
    double bw = 0;
    double height=0;
    try {
      Scanner fr = new Scanner(file);
      while (fr.hasNext()) {
        String[] holder = fr.nextLine().split(",");
        if (holder.length>2){
          if (holder[0].equals("user")) {
            name = holder[1];
            gender = holder[2];
            age = Integer.parseInt(holder[3]);
            height = Double.parseDouble(holder[4]);
            bw=Double.parseDouble(holder[5]);
          } else if (holder[0].equals("newFood")) {
            foods.add(new Food(holder[1], Integer.parseInt(holder[2]), Integer.parseInt(holder[3])));
          } else {
            int index = Day.searchDay(days, holder[2]);
            if (index == -1) {
              logs = new ArrayList<>();
              activities = new ArrayList<>();
              foodLogs = new ArrayList<>();
              String[] date = holder[2].split("/");
              String temp = date[0] + "/" + date[1] + "/" + date[2];
              if (holder[0].equals("food")) {
                foodLogs.add(new Food(holder[1], holder[2], Integer.parseInt(holder[3])));
              } else if (holder[0].equals("sugar")) {
                logs.add(new Log(Double.parseDouble(holder[1]), holder[2]));
              } else if (holder[0].equals("activity")) {
                activities.add(new Activities(holder[1], holder[2], holder[3]));
              }
              days.add(new Day(temp, logs, activities, foodLogs));
            } else {
              if (holder[0].equals("food")) {
                days.get(index).addFood(new Food(holder[1], holder[2], Integer.parseInt(holder[3])));
              } else if (holder[0].equals("sugar")) {
                days.get(index).addLog(new Log(Double.parseDouble(holder[1]), holder[2]));
              } else if (holder[0].equals("activity")) {
                days.get(index).addActivity(new Activities(holder[1], holder[2], holder[3]));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      User temp = new User(name, gender, age, height, bw, days, foods);
      ;
      return temp;
    }
    User temp = new User(name, gender, age, height, bw, days, foods);
    return temp;
  }

  public static void clear(){
    mainPanel.removeAll();
    mainPanel.repaint();
  }

  public static void entry(){
    clear();
    mainPanel.setLayout(new GridLayout(5,1,5,5));
    JLabel user=new JLabel("Please enter your username below");
    field1=new JTextField();
    JButton next=new JButton("next");
    JButton newUser=new JButton("New User");

    next.addActionListener(e->checkUser(field1.getText()));
    newUser.addActionListener(e->newUser());
    JPanel panel=new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(field1,BorderLayout.CENTER);
    panel.add(next,BorderLayout.WEST);

    mainPanel.add(user);
    mainPanel.add(new JLabel());
    mainPanel.add(panel);
    mainPanel.add(new JLabel());
    mainPanel.add(newUser);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  public static void newUser(){
    clear();
    mainPanel.setLayout(new GridLayout(13,1,0,2));
    JLabel login=new JLabel("Please enter the new user's name below");
    JTextField newUser=new JTextField();
    JLabel gender=new JLabel("Select your gender");

    String[] genders={"male","female"};
    JComboBox genderSel=new JComboBox(genders);

    JLabel age=new JLabel("Enter your age");
    JTextField userAge=new JTextField();
    JLabel bw=new JLabel("Enter your body weight in lbs");
    JTextField userBW=new JTextField();
    JLabel height=new JLabel("Enter your height in cm");
    JTextField userHeight=new JTextField();
    JButton ok=new JButton("Ok");
    JButton back=new JButton("Back");
    ok.addActionListener(e->checkUser(newUser.getText(),checkGender(genderSel.getSelectedIndex()),Integer.parseInt(userAge.getText()),Double.parseDouble(userBW.getText()),Double.parseDouble(userHeight.getText())));
    back.addActionListener(e->entry());

    mainPanel.add(login);
    mainPanel.add(newUser);
    mainPanel.add(gender);
    mainPanel.add(genderSel);
    mainPanel.add(age);
    mainPanel.add(userAge);
    mainPanel.add(bw);
    mainPanel.add(userBW);
    mainPanel.add(height);
    mainPanel.add(userHeight);
    mainPanel.add(ok);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static String checkGender(int index){
    switch (index){
      case 0:
      return "male";
      default:
      return "female";
    }
  }

  private static void checkUser(String name){
    userFile=new File(name+".csv");
    if (userFile.exists()){
      user=readUser(userFile);
      mainScreen();
      SwingUtilities.updateComponentTreeUI(frame);
    }else{
      JOptionPane.showMessageDialog(null,"User does not exist, please enter different username.","error",JOptionPane.WARNING_MESSAGE);
    }
  }

  private static void checkUser(String name,String gender, int age, double bw, double height ){
    userFile=new File(name+".csv");
    if (userFile.exists()){
      JOptionPane.showMessageDialog(null,"User already exists, please enter different username.","error",JOptionPane.WARNING_MESSAGE);
    }else{
      try{
        userFile.createNewFile();
        FileWriter fw=new FileWriter(userFile,true);
        fw.write("user,"+name+","+gender+","+age+","+bw+","+height);
        fw.close();
        writeUser();
        user=readUser(userFile);
        mainScreen();
        SwingUtilities.updateComponentTreeUI(frame);
      } catch (Exception e){JOptionPane.showMessageDialog(null,"There was an error.","error",JOptionPane.PLAIN_MESSAGE);}
    }
  }

  private static void writeUser(){
    try{
      File wen=new File("newUser.csv");
      Scanner reader=new Scanner(wen);
      FileWriter fw=new FileWriter(userFile,true);
      String holder;
      while (reader.hasNext()){
        holder=reader.nextLine();
        fw.write("\n"+holder);
      }
      reader.close();
      fw.close();
    } catch (Exception e){JOptionPane.showMessageDialog(null,"There was an error.","error",JOptionPane.PLAIN_MESSAGE);}
  }
  

  public static void mainScreen(){
    clear();
    mainPanel.setLayout(new GridLayout(6,1,15,15));

    JButton currentLog=new JButton("Enter current logs");
    JButton pastLog=new JButton("Enter past logs");
    JButton openDay=new JButton("Open a date");
    JButton userInfo=new JButton("Look at your information");
    JButton changeInfo=new JButton("Change your information");
    JButton changeUser=new JButton("Change the user");

    currentLog.addActionListener(e->currentLog());
    pastLog.addActionListener(e->pastLogTime());
    openDay.addActionListener(e->openDay());
    userInfo.addActionListener(e->userInfo());
    changeInfo.addActionListener(e->changeInfo());
    changeUser.addActionListener(e->entry());

    mainPanel.add(currentLog);
    mainPanel.add(pastLog);
    mainPanel.add(openDay);
    mainPanel.add(userInfo);
    mainPanel.add(changeInfo);
    mainPanel.add(changeUser);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  public static void currentLog(){
    clear();
    mainPanel.setLayout(new GridLayout(5,1,10,10));

    JLabel choice=new JLabel("Select which type of log you would like to create.");
    JButton sugarLog=new JButton("Sugar level");
    JButton activity=new JButton("Activity");
    JButton food=new JButton("Food");
    JButton exit=new JButton("Exit");

    TimeZone.setDefault(TimeZone.getTimeZone("EST"));
    SimpleDateFormat time_format = new SimpleDateFormat("MM/dd/YYYY/HH:mm");
    Date date = new Date();
    String time = time_format.format(date);
    String[] times = time.split("/");
    String[] timess = times[3].split(":");
    int hour = Integer.parseInt(timess[0]) + 1;
    if (hour==25){hour=1;}
    final String Time = times[0] + "/" + times[1] + "/" + times[2] + "/" + hour + ":" + timess[1];

    sugarLog.addActionListener(e->cSugarLog(Time));
    activity.addActionListener(e->cActivityLog(Time));
    food.addActionListener(e->cFoodLog(Time));
    exit.addActionListener(e-> mainScreen());

    mainPanel.add(choice);
    mainPanel.add(sugarLog);
    mainPanel.add(activity);
    mainPanel.add(food);
    mainPanel.add(exit);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static void cSugarLog(String time){
    clear();
    mainPanel.setLayout(new GridLayout(7,1,10,10));
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel level=new JLabel("Enter the sugar level below");
    field1=new JTextField();
    JLabel trend=new JLabel("Select the trend of the sugar level");
    String[] trends={"up","flat","down"};
    JComboBox sTrend=new JComboBox(trends);
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");

    back.addActionListener(e-> currentLog());
    submit.addActionListener(e-> submitLog("sugar,"+field1.getText()+","+time,true,trends[sTrend.getSelectedIndex()],1));

    mainPanel.add(cTime);
    mainPanel.add(level);
    mainPanel.add(field1);
    mainPanel.add(trend);
    mainPanel.add(sTrend);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static void cActivityLog(String time){
    clear();
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel name=new JLabel("Enter the name of the acitivity");
    field1=new JTextField();
    JLabel dur=new JLabel("Enter the duration of this activity in minutes");
    field2=new JTextField();
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");

    submit.addActionListener(e-> submitLog("activity,"+field1.getText()+","+time+","+field2.getText(),true,"",2));
    back.addActionListener(e-> currentLog());

    mainPanel.setLayout(new GridLayout(7,1,10,10));
    mainPanel.add(cTime);
    mainPanel.add(name);
    mainPanel.add(field1);
    mainPanel.add(dur);
    mainPanel.add(field2);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static void cFoodLog(String time){
    clear();
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel name=new JLabel("Enter the name of the food");
    field1=new JTextField();
    JLabel uni=new JLabel("Enter the amount of units consumed");
    field2=new JTextField();
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");

    submit.addActionListener(e-> submitLog("food,"+field1.getText()+","+time+","+field2.getText(),true,"",2));
    back.addActionListener(e-> currentLog());

    mainPanel.setLayout(new GridLayout(7,1,10,10));
    mainPanel.add(cTime);
    mainPanel.add(name);
    mainPanel.add(field1);
    mainPanel.add(uni);
    mainPanel.add(field2);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static void submitLog(String submission, boolean current,String trend,int boxes){
    if (checkBoxes(boxes)){
      try{
      FileWriter fw=new FileWriter(userFile,true);
      fw.write("\n"+submission);
      fw.close();
      if (current){
        clear();
        JTextArea result=new JTextArea();
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        JButton back=new JButton("back");
        back.setSize(new Dimension(100,50));
        back.addActionListener(e->currentLog());
        String[] holder=submission.split(",");
        if (holder[0].equals("sugar")){
          user=readUser(userFile);
          double sugar=Double.parseDouble(holder[1]);
          String hold="";
          if (sugar < 4.0) {
              System.out.print("\nYou should eat something! How about: ");
              hold="\nYou should eat something! How about: ";
              for (Food food : user.getFoods()) {
                if (food.getGI() > 50) {
                  food.print();
                  System.out.print(", ");
                  hold+=food.toString()+", ";
                }
              }
            }else if (sugar<5.0 && (trend.equals("down")||trend.equals("flat"))){
              for (Food food : user.getFoods()) {
                if (food.getGI() > 30 && food.getGI()<70) {
                  food.print();
                  System.out.print(", ");
                  hold+=food.toString()+", ";
                }
              }
            }else if (sugar>11.0){
              System.out.println("You should exercise! Maybe go for a walk.");
              hold="You should exercise! Maybe go for a walk.";
            } else if (sugar>9.0 && (trend.equals("flat") || trend.equals("up"))){
              System.out.println("Watch out for foods with sugar. Wait until sugar level gets a little lower before eating.");
              hold="Watch out for foods with sugar. Wait until sugar level gets a little lower before eating.";
            }else{
              System.out.println("Sugar levels are looking good!");
              hold="Sugar levels are looking good!";
            }
          hold+="\n\n"+user.previousOccurences(Double.parseDouble(holder[1]));
          result.setText(hold);
        }else if (holder[0].equals("activity")){
          result.setText("Be sure to keep track of your sugar while active. You burn through sugar when exercising.");
        }else {
          result.setText("Be careful about your eating. Some foods can spike sugar levels by a lot.");
        }
        mainPanel.setLayout(new BorderLayout(10,10));
        mainPanel.add(result,BorderLayout.CENTER);
        mainPanel.add(back,BorderLayout.SOUTH);
        SwingUtilities.updateComponentTreeUI(frame);
      }else{
        pastLog(trend);
      }
    }catch (Exception e){
      JOptionPane.showMessageDialog(null,"There was an error.","error",JOptionPane.PLAIN_MESSAGE);
     }
    }
  }

  private static boolean checkBoxes(int count){
    switch (count){
      case 1:
      if(field1.getText().isEmpty()){
        JOptionPane.showMessageDialog(null,"Required fields are empty","error",JOptionPane.ERROR_MESSAGE);
        return false;}
      return true;
      case 2:
      if(field1.getText().isEmpty()||field2.getText().isEmpty()){
        JOptionPane.showMessageDialog(null,"Required fields are empty","error",JOptionPane.ERROR_MESSAGE);
        return false;}
    }
    return true;
  }

  private static void pastLogTime(){
    clear();
    JLabel time=new JLabel("Please select a date for the following logs");
    String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    JComboBox month=new JComboBox(months);
    String[] days=new String[31];
    for(int i=0;i<31;i++){
      days[i]=""+(i+1);
    }
    JComboBox day=new JComboBox(days);
    TimeZone.setDefault(TimeZone.getTimeZone("EST"));
    SimpleDateFormat time_format = new SimpleDateFormat("YYYY");
    Date date = new Date();
    String yearT = time_format.format(date);
    int year=Integer.parseInt(yearT);
    String[] years=new String[50];
    for (int i=0;i<50;i++){
      years[i]=""+(year-i);
    }
    JComboBox yearOptions=new JComboBox(years);
    JButton ok=new JButton("Ok");
    JButton back=new JButton("back");

    ok.addActionListener(e-> pastLog((month.getSelectedIndex()+1)+"/"+days[day.getSelectedIndex()]+"/"+years[yearOptions.getSelectedIndex()]));
    back.addActionListener(e->mainScreen());

    mainPanel.setLayout(new BorderLayout());
    JPanel temp1=new JPanel();
    JPanel temp2=new JPanel();
    mainPanel.add(time,BorderLayout.NORTH);
    temp1.setLayout(new GridLayout(3,1,15,15));
    temp2.setLayout(new GridLayout(2,3));
    temp2.add(new JLabel("Month"));
    temp2.add(new JLabel("Day"));
    temp2.add(new JLabel("Year"));
    temp2.add(month);
    temp2.add(day);
    temp2.add(yearOptions);
    temp1.add(temp2);
    temp1.add(ok);
    temp1.add(back);
    mainPanel.add(temp1,BorderLayout.CENTER);
    SwingUtilities.updateComponentTreeUI(frame);
  }

  private static void pastLog(String time){
    clear();
    JLabel date=new JLabel("logs for: "+time);
    JLabel choices=new JLabel("Which type of log would you like to add?");
    JButton sugar=new JButton("Sugar");
    JButton activity=new JButton("Activity");
    JButton food=new JButton("Food");
    JPanel temp=new JPanel();
    JButton changeDate=new JButton("Change date");
    JButton newFood=new JButton("Add New Food");

    sugar.addActionListener(e->pSugarLog(time));
    activity.addActionListener(e->pActivityLog(time));
    food.addActionListener(e->pFoodLog(time));
    newFood.addActionListener(e->newFood(time));
    changeDate.addActionListener(e->pastLogTime());

    temp.setLayout(new GridLayout(1,2,10,0));
    temp.add(changeDate);
    temp.add(newFood);
    
    mainPanel.setLayout(new GridLayout(6,1,10,10));
    mainPanel.add(date);
    mainPanel.add(choices);
    mainPanel.add(sugar);
    mainPanel.add(activity);
    mainPanel.add(food);
    mainPanel.add(temp);
    SwingUtilities.updateComponentTreeUI(frame);
  }

private static void pSugarLog(String time){
    clear();
    mainPanel.setLayout(new GridLayout(7,1,10,10));
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel sTime=new JLabel("Please select the time of this entry");
    JPanel temp=new JPanel();

    temp.setLayout(new GridLayout(1,3));
    String[] hours=new String[12];
    String[] minutes=new String[12];
    String[] amPM={"am","pm"};

    for (int i=0;i<12;i++){
      hours[i]=""+(i+1);
      minutes[i]=""+(i*5);
    }

    JComboBox hour=new JComboBox(hours);
    JComboBox minute=new JComboBox(minutes);
    JComboBox am=new JComboBox(amPM);
    temp.add(hour);
    temp.add(minute);
    temp.add(am);

    JLabel level=new JLabel("Enter the sugar level below");
    field1=new JTextField();
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");

    back.addActionListener(e-> pastLog(time));
    submit.addActionListener(e-> submitLog("sugar,"+field1.getText()+","+time+"/"+(Integer.parseInt(hours[hour.getSelectedIndex()])+am.getSelectedIndex()*12)+":"+minutes[minute.getSelectedIndex()],false,time,1));

    mainPanel.add(cTime);
    mainPanel.add(sTime);
    mainPanel.add(temp);
    mainPanel.add(level);
    mainPanel.add(field1);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
}

private static void pActivityLog(String time){
    clear();
    mainPanel.setLayout(new GridLayout(9,1,10,10));
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel sTime=new JLabel("Please select the time of this entry");
    JPanel temp=new JPanel();

    temp.setLayout(new GridLayout(1,3));
    String[] hours=new String[12];
    String[] minutes=new String[12];
    String[] amPM={"am","pm"};

    for (int i=0;i<12;i++){
      hours[i]=""+(i+1);
      minutes[i]=""+(i*5);
    }

    JComboBox hour=new JComboBox(hours);
    JComboBox minute=new JComboBox(minutes);
    JComboBox am=new JComboBox(amPM);
    temp.add(hour);
    temp.add(minute);
    temp.add(am);

    JLabel level=new JLabel("Enter the name of the activity below");
    field1=new JTextField();
    JLabel dur=new JLabel("Enter the duration of this activity in minutes");
    field2=new JTextField();
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");
    back.addActionListener(e-> pastLog(time));
    submit.addActionListener(e-> submitLog("activity,"+field1.getText()+","+time+"/"+(Integer.parseInt(hours[hour.getSelectedIndex()])+am.getSelectedIndex()*12)+":"+minutes[minute.getSelectedIndex()]+","+field2.getText(),false,time,2));

    mainPanel.add(cTime);
    mainPanel.add(sTime);
    mainPanel.add(temp);
    mainPanel.add(level);
    mainPanel.add(field1);
    mainPanel.add(dur);
    mainPanel.add(field2);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
}

private static void pFoodLog(String time){
    clear();
    mainPanel.setLayout(new GridLayout(9,1,10,10));
    JLabel cTime=new JLabel("Log for: "+time);
    JLabel sTime=new JLabel("Please select the time of this entry");
    JPanel temp=new JPanel();

    temp.setLayout(new GridLayout(1,3));
    String[] hours=new String[12];
    String[] minutes=new String[12];
    String[] amPM={"am","pm"};

    for (int i=0;i<12;i++){
      hours[i]=""+(i+1);
      minutes[i]=""+(i*5);
    }

    JComboBox hour=new JComboBox(hours);
    JComboBox minute=new JComboBox(minutes);
    JComboBox am=new JComboBox(amPM);
    temp.add(hour);
    temp.add(minute);
    temp.add(am);

    JLabel level=new JLabel("Enter the name of the food below");
    field1=new JTextField();
    JLabel dur=new JLabel("Enter the amount of units consumed");
    field2=new JTextField();
    JButton submit=new JButton("submit");
    JButton back=new JButton("back");
    back.addActionListener(e-> pastLog(time));
    submit.addActionListener(e-> submitLog("food,"+field1.getText()+","+time+"/"+(Integer.parseInt(hours[hour.getSelectedIndex()])+am.getSelectedIndex()*12)+":"+minutes[minute.getSelectedIndex()]+","+field2.getText(),false,time,2));

    mainPanel.add(cTime);
    mainPanel.add(sTime);
    mainPanel.add(temp);
    mainPanel.add(level);
    mainPanel.add(field1);
    mainPanel.add(dur);
    mainPanel.add(field2);
    mainPanel.add(submit);
    mainPanel.add(back);
    SwingUtilities.updateComponentTreeUI(frame);
}

private static void newFood(String time){
  clear();
  JLabel name=new JLabel("Enter the name of this food");
  field1=new JTextField();
  JLabel carbs=new JLabel("Enter the amount of carbohydrates in a single unit");
  field2=new JTextField();
  JLabel gI=new JLabel("Enter the glycemic index value of this food");
  JTextField glycemic=new JTextField();
  JButton submit =new JButton("Submit");
  JButton back=new JButton("Back");

  back.addActionListener(e-> pastLog(time));
  submit.addActionListener(e-> submitLog("newFood,"+field1.getText()+","+field2.getText()+","+glycemic.getText(),false,time,2));

  mainPanel.setLayout(new GridLayout(8,1,10,10));
  mainPanel.add(name);
  mainPanel.add(field1);
  mainPanel.add(carbs);
  mainPanel.add(field2);
  mainPanel.add(gI);
  mainPanel.add(glycemic);
  mainPanel.add(submit);
  mainPanel.add(back);
  SwingUtilities.updateComponentTreeUI(frame);

}

private static void openDay(){
  user=readUser(userFile);
  clear();
  JLabel time=new JLabel("Please select a date to open");
  String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
  JComboBox month=new JComboBox(months);
  String[] days=new String[31];
  for(int i=0;i<31;i++){
    days[i]=""+(i+1);
  }
  JComboBox day=new JComboBox(days);
  TimeZone.setDefault(TimeZone.getTimeZone("EST"));
  SimpleDateFormat time_format = new SimpleDateFormat("YYYY");
  Date date = new Date();
  String yearT = time_format.format(date);
  int year=Integer.parseInt(yearT);
  String[] years=new String[50];
  for (int i=0;i<50;i++){
     years[i]=""+(year-i);
  }
  JComboBox yearOptions=new JComboBox(years);
  JButton ok=new JButton("Ok");
  JButton back=new JButton("back");

  ok.addActionListener(e->openDay(""+(month.getSelectedIndex()+1)+"/"+day.getSelectedItem()+"/"+yearOptions.getSelectedItem()));
  back.addActionListener(e->mainScreen());

  mainPanel.setLayout(new GridLayout(4,1,10,10));
  JPanel panel=new JPanel();
  panel.setLayout(new GridLayout(2,3));
  panel.add(new JLabel("Month"));
  panel.add(new JLabel("Day"));
  panel.add(new JLabel("Year"));
  panel.add(month);
  panel.add(day);
  panel.add(yearOptions);
  mainPanel.add(time);
  mainPanel.add(panel);
  mainPanel.add(ok);
  mainPanel.add(back);
  SwingUtilities.updateComponentTreeUI(frame);

}

private static void openDay(String date){
  int index=Day.searchDay(user.getDays(),date);
  if (index==-1){
    JOptionPane.showMessageDialog(null,"No logs for this date.","error",JOptionPane.ERROR_MESSAGE);
  }else{
    clear();
    JTextArea result=new JTextArea();
    result.setLineWrap(true);
    result.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(result);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    result.setText(user.getDays().get(index).toString());
    JButton back=new JButton("Back");
    back.addActionListener(e->openDay());
    mainPanel.setLayout(new BorderLayout(10,10));
    mainPanel.add(result,BorderLayout.CENTER);
    mainPanel.add(scroll,BorderLayout.EAST);
    mainPanel.add(back,BorderLayout.SOUTH);
    SwingUtilities.updateComponentTreeUI(frame);

  }
}

private static void userInfo(){
  user=readUser(userFile);
  clear();
  JTextArea result=new JTextArea();
  result.setLineWrap(true);
  result.setWrapStyleWord(true);
  JScrollPane scroll = new JScrollPane(result);
  scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  result.setText(user.info());
  JButton back=new JButton("Back");
  back.addActionListener(e->mainScreen());

  mainPanel.setLayout(new BorderLayout());
  mainPanel.add(result,BorderLayout.CENTER);
  mainPanel.add(scroll,BorderLayout.EAST);
  mainPanel.add(back,BorderLayout.SOUTH);
  SwingUtilities.updateComponentTreeUI(frame);
}

private static JTextField uAge;
private static JTextField uGender;
private static JTextField uHeight;
private static JTextField uBW;

private static void changeInfo(){
  clear();
  int age=user.getAge();
  String gender=user.getGender();
  double height=user.getHeight();
  double bw=user.getBW();

  uAge=new JTextField();
  uGender=new JTextField();
  uHeight=new JTextField();
  uBW=new JTextField();

  JButton ok=new JButton("Submit");
  JButton back=new JButton("Back");

  back.addActionListener(e->mainScreen());
  ok.addActionListener(e->check());

  mainPanel.setLayout(new GridLayout(10,1,10,10));
  mainPanel.add(new JLabel("Current age: "+age));
  mainPanel.add(uAge);
  mainPanel.add(new JLabel("Current gender: "+gender));
  mainPanel.add(uGender);
  mainPanel.add(new JLabel("Current height (cm): "+height));
  mainPanel.add(uHeight);
  mainPanel.add(new JLabel("Current body weight (lbs): "+bw));
  mainPanel.add(uBW);
  mainPanel.add(ok);
  mainPanel.add(back);
  SwingUtilities.updateComponentTreeUI(frame);
}

private static void check(){
  int age=user.getAge();
  String gender=user.getGender();
  double height=user.getHeight();
  double bw=user.getBW();

  if (!uAge.getText().isEmpty()){age=Integer.parseInt(uAge.getText());}
  if (!uGender.getText().isEmpty()){gender=uGender.getText();}
  if (!uHeight.getText().isEmpty()){height=Double.parseDouble(uHeight.getText());}
  if (!uBW.getText().isEmpty()){bw=Double.parseDouble(uBW.getText());}
  try{
  FileWriter fw=new FileWriter(userFile,true);
  fw.write("user,"+user.getName()+","+gender+","+age+","+height+","+bw);
  fw.close();
  JOptionPane.showMessageDialog(null,"Updated!","",JOptionPane.PLAIN_MESSAGE);
  mainScreen();
  }catch(Exception e){JOptionPane.showMessageDialog(null,"There was an error!","Error",JOptionPane.ERROR_MESSAGE);}
}

  public static void main(String[] args) {
    frame=new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500,500);
    frame.setLayout(new BorderLayout(10,10));
    frame.setVisible(true);
    frame.setTitle("Diabetes Assistant");

    mainPanel=new JPanel();
    topPanel=new JPanel();
    bottomPanel=new JPanel();
    rightPanel=new JPanel();
    leftPanel=new JPanel();

    mainPanel.setPreferredSize(new Dimension(200,200));
    topPanel.setPreferredSize(new Dimension(50,50));
    bottomPanel.setPreferredSize(new Dimension(50,50));
    rightPanel.setPreferredSize(new Dimension(50,50));
    leftPanel.setPreferredSize(new Dimension(50,50));

    frame.add(mainPanel, BorderLayout.CENTER);
    frame.add(topPanel,BorderLayout.NORTH);
    frame.add(bottomPanel,BorderLayout.SOUTH);
    frame.add(rightPanel,BorderLayout.EAST);
    frame.add(leftPanel,BorderLayout.WEST);

    topPanel.add(new JLabel("Diabetes Virtual Assistant"));
    entry();
    SwingUtilities.updateComponentTreeUI(frame);
  }
}
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This clas contains the main method which will utilize all the other classes methods and classes
 *
 * @author Michael Fasano
 * michael.fasano@stonybrook.edu
 * 110798138
 */
public class StormStatServer implements Serializable {
    private static HashMap<String, Storm> database = new HashMap<String, Storm>();
    private static String exit = "";
    private static String option = "";
    private static String filename = "StormChasers.ser";

    public static void main(String args[]) {
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            database = (HashMap) in.readObject();
            in.close();
            file.close();
            System.out.println("StormChaser.ser Was Found And Loaded In");
        } catch (Exception e) {
            System.out.println("No File Found");
        }
        System.out.println("Welcome To The Storm Chasers Database!");
        while (!exit.equals("exit")) {
            System.out.println("\nA:Add A Storm\nL:Look Up A Storm\nD:Remove A Storm\nE:Edit A Storm\nR:Print Storms by Rainfall\nW:Print Storms by Windspeed" +
                    "\nX:Save and Quit\nQ:Quit Without Saving");
            System.out.println("Please Select An Option:");
            Scanner input = new Scanner(System.in);
            option = input.nextLine();
            option = option.toLowerCase();
            switch (option) {
                case "a":
                    System.out.println("Please Enter The Name Of The Storm:");
                    String name = input.nextLine();
                    System.out.println("Please Enter The Date Of The Storm(YYYY-MM-DD):");
                    String date = input.nextLine();
                    if (!(date.length() == 10 && Character.isDigit(date.charAt(0)) && Character.isDigit(date.charAt(1)) && Character.isDigit(date.charAt(2)) && Character.isDigit(date.charAt(3))
                            && date.charAt(4) == (char) 45 && Character.isDigit(date.charAt(5)) && Character.isDigit(date.charAt(6)) && date.charAt(7) == (char) 45 &&
                            Character.isDigit(date.charAt(8)) && Character.isDigit(date.charAt(9)))) {
                        System.out.println("Invalid Date");
                        break;
                    }
                    System.out.println("Please Enter The Precipitation Levels(cm):");
                    double precipitation = input.nextDouble();
                    System.out.println("Please Enter The Windspeed(km/h):");
                    double windspeed = input.nextDouble();

                    Storm newStorm = new Storm(name, precipitation, windspeed, date);
                    database.put(name, newStorm);
                    System.out.println("The Storm Has Been Added To The Database.");
                    break;

                case "l":
                    System.out.println("Please Enter The Name Of The Storm You'd Like To Find:");
                    name = input.nextLine();
                    if (database.containsKey(name)) {
                        System.out.println("Storm Name:" + database.get(name).getName() + "\nDate:" + database.get(name).getDate() + "\nWindspeeds:"
                                + database.get(name).getWindSpeed() + "\nPrecipitation:" + database.get(name).getPrecipitation());
                    } else {
                        System.out.println("No Occurence Of A Storm With This Name.");
                    }
                    break;

                case "d":
                    System.out.println("Please Enter The Name Of The Storm You'd Like To Delete:");
                    name = input.nextLine();
                    if (database.containsKey(name)) {
                        database.remove(name);
                        System.out.println("This Storms Entry Has Been Removed.");
                    } else {
                        System.out.println("No Occurence Of A Storm With This Name");
                    }
                    break;

                case "e":
                    System.out.println("Please Enter The Name Of The Storm You'd Like To Edit:");
                    name = input.nextLine();
                    if (!database.containsKey(name)) {
                        System.out.println("No Occurence Of A Storm With This Name");
                        break;
                    }
                    System.out.println("In Edit Mode, press enter without any input to leave data unchanged.");
                    System.out.println("Please Enter Date:");
                    date = input.nextLine();
                    if (!date.isEmpty() && date.length() == 10 && Character.isDigit(date.charAt(0)) && Character.isDigit(date.charAt(1)) && Character.isDigit(date.charAt(2))
                            && Character.isDigit(date.charAt(3)) && date.charAt(4) == (char) 47 && Character.isDigit(date.charAt(5)) && Character.isDigit(date.charAt(6))
                            && date.charAt(7) == (char) 47 && Character.isDigit(date.charAt(8)) && Character.isDigit(date.charAt(9))) {
                        database.get(name).setDate(date);
                    } else if (date.isEmpty()) {

                    } else {
                        System.out.println("Invalid Date Format");
                        break;
                    }
                    System.out.println("Please Enter Precipitation Levels:");
                    String rainfall = input.nextLine();
                    if (!rainfall.isEmpty()) {
                        double rains = Double.parseDouble(rainfall);
                        database.get(name).setPrecipitation(rains);
                    }
                    System.out.println("Please Enter Windspeeds:");
                    String winds = input.nextLine();
                    if (!winds.isEmpty()) {
                        double windy = Double.parseDouble(winds);
                        database.get(name).setWindSpeed(windy);
                    }
                    System.out.println("Storm " + database.get(name).getName() + " Has Been Edited");
                    break;

                case "r":
                    ArrayList<Storm> rainfallSort = new ArrayList<Storm>();
                    PrecipitationComparator comparator = new PrecipitationComparator();
                    for (String key : database.keySet()) {
                        rainfallSort.add(database.get(key));
                    }
                    System.out.printf("Name\tWindspeed\tRainfall\n------------------------------------------\n");
                    Collections.sort(rainfallSort, comparator);
                    for (int i = 0; i < rainfallSort.size(); i++) {
                        System.out.printf(rainfallSort.get(i).getName() + "\t" + rainfallSort.get(i).getWindSpeed() + "\t" + rainfallSort.get(i).getPrecipitation() + "\n");
                    }
                    break;

                case "w":
                    ArrayList<Storm> windspeedSort = new ArrayList<Storm>();
                    WindSpeedComparator comparator1 = new WindSpeedComparator();
                    for (String key : database.keySet()) {
                        windspeedSort.add(database.get(key));
                    }
                    System.out.println("Name\tWindspeed\tRainfall\n------------------------------------------");
                    Collections.sort(windspeedSort, comparator1);
                    for (int i = 0; i < windspeedSort.size(); i++) {
                        System.out.println(windspeedSort.get(i).getName() + "\t" + windspeedSort.get(i).getWindSpeed() + "\t" + windspeedSort.get(i).getPrecipitation());
                    }
                    break;

                case "x":
                    try {
                        FileOutputStream file = new FileOutputStream(filename);
                        ObjectOutputStream out = new ObjectOutputStream(file);
                        out.writeObject(database);
                        out.close();
                        file.close();
                        System.out.println("Database Has Been Saved");
                    } catch (IOException e) {
                        System.out.println("An Error Has Ocurred");
                    }
                    exit = "exit";
                    System.out.println("Goodbye Boss");
                    break;

                case "q":
                    exit = "exit";
                    System.out.println("Goodbye Boss");
                    break;
            }
        }
    }
}

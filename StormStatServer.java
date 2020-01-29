import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

/**
 * This class contains the main method which will utilize all the other classes methods and classes
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
    private static JFrame frame = new JFrame();

    public static void main(String args[]) throws IOException {
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

            frame.setSize(500,400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            frame.add(panel);

            homeComponents(panel);
            frame.setVisible(true);
            frame.setTitle("Storm Chasers Database");

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

    /**
     * This loads all the buttons and images onto the screen. Each button has an event handler that calls its respective method
     * @param panel
     * @throws IOException
     */
    private static void homeComponents(JPanel panel) throws IOException {
        panel.setLayout(null);
        JLabel welcome = new JLabel("Welcome to the Storm Chasers Database");
        welcome.setBounds(125,20,300,25);
        panel.add(welcome);

        BufferedImage picture = ImageIO.read(new File("D:/Meme Folder/StormChasers.jpg"));
        JLabel picLabel = new JLabel(new ImageIcon(picture));
        picLabel.setBounds(40,50,400,150);
        panel.add(picLabel);

        JButton addButton = new JButton("Add A Storm");
        addButton.setBounds(40, 230, 130,25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                addStormComponents(panel);
            }
        });

        JButton editButton = new JButton("Edit A Storm");
        editButton.setBounds(180, 230, 130,25);
        panel.add(editButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                editStormComponents(panel);
            }
        });

        JButton removeButton = new JButton("Remove A Storm");
        removeButton.setBounds(320, 230, 130,25);
        panel.add(removeButton);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                removeStormComponents(panel);
            }
        });

        JButton lookUpButton = new JButton("Look Up A Storm");
        lookUpButton.setBounds(40, 260, 130,25);
        panel.add(lookUpButton);

        lookUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                lookUpStormComponents(panel);
            }
        });

        JButton rainfallButton = new JButton("Print Database");
        rainfallButton.setBounds(180, 260, 130,25);
        panel.add(rainfallButton);

        rainfallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                printDatabase(panel);
            }
        });

        JButton saveAndQuit = new JButton("Save and Quit");
        saveAndQuit.setBounds(320, 260, 130,25);
        panel.add(saveAndQuit);

        /**
         * This button event will serialize the hashmap acting as our database to save its state so it can be reloaded later
         * and then close the panel.
         */
        saveAndQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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
                frame.dispose();
                exit = "exit";
            }
        });

        JButton dontSaveAndQuit = new JButton("Quit w/o Saving");
        dontSaveAndQuit.setBounds(180, 290, 130,25);
        panel.add(dontSaveAndQuit);

        dontSaveAndQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                exit = "exit";
                frame.dispose();
            }
        });
    }

    /**
     * This creates the panel for the page that storms are added from. It uses text fields where the user inputs information and then when
     * the confirm button is pressed it takes the data and stores it into the database
     * @param panel
     */
    private static void addStormComponents(JPanel panel) {
        panel.removeAll();
        panel.setLayout(null);

        JLabel stormName = new JLabel("Storm Name");
        stormName.setBounds(10,20,110,25);
        panel.add(stormName);

        JTextField stormNameText = new JTextField(20);
        stormNameText.setBounds(130,20,165,25);
        panel.add(stormNameText);

        JLabel stormDate = new JLabel("Storm Date");
        stormDate.setBounds(10,50,110,25);
        panel.add(stormDate);

        JTextField stormDateText = new JTextField(20);
        stormDateText.setBounds(130,50,165,25);
        panel.add(stormDateText);

        JLabel precipitationLevel = new JLabel("Precipitation (cm)");
        precipitationLevel.setBounds(10,80,110,25);
        panel.add(precipitationLevel);

        JTextField precipitationLevelText = new JTextField(20);
        precipitationLevelText.setBounds(130,80,165,25);
        panel.add(precipitationLevelText);

        JLabel windSpeeds = new JLabel("Wind Speed (km/h)");
        windSpeeds.setBounds(10,110,110,25);
        panel.add(windSpeeds);

        JTextField windSpeedsText = new JTextField(20);
        windSpeedsText.setBounds(130,110,165,25);
        panel.add(windSpeedsText);

        JButton confirmButton = new JButton("Add Storm");
        confirmButton.setBounds(100,140,130,25);
        panel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stormName = stormNameText.getText();
                String stormDate = stormDateText.getText();
                String precipitation = precipitationLevelText.getText();
                Double precipitationNum = Double.parseDouble(precipitation);
                String windSpeed = windSpeedsText.getText();
                Double windSpeedNum = Double.parseDouble(windSpeed);
                System.out.println(stormDate);
                if (!(stormDate.length() == 10 && Character.isDigit(stormDate.charAt(0)) && Character.isDigit(stormDate.charAt(1)) && Character.isDigit(stormDate.charAt(2)) && Character.isDigit(stormDate.charAt(3))
                        && stormDate.charAt(4) == (char) 45 && Character.isDigit(stormDate.charAt(5)) && Character.isDigit(stormDate.charAt(6)) && stormDate.charAt(7) == (char) 45 &&
                        Character.isDigit(stormDate.charAt(8)) && Character.isDigit(stormDate.charAt(9)))) {
                    JOptionPane.showMessageDialog(null, "Invalid Date Format (YYYY-MM-DD)");

                    panel.removeAll();
                    panel.updateUI();

                    try {
                        homeComponents(panel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Storm newStorm = new Storm(stormName, precipitationNum, windSpeedNum, stormDate);
                    database.put(stormName, newStorm);

                    System.out.println(stormName + "\n" + stormDate + "\n" + precipitationNum + "\n" + windSpeedNum);

                    panel.removeAll();
                    panel.updateUI();

                    try {
                        homeComponents(panel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * This runs when the edit storm button is pressed on the home screen and functions similarly to the add storm method
     * but instead of adding to the database it overwrites an existing storm
     * @param panel
     */
    private static void editStormComponents(JPanel panel) {
        panel.removeAll();
        panel.setLayout(null);

        JLabel stormName = new JLabel("Storm Name");
        stormName.setBounds(10,20,110,25);
        panel.add(stormName);

        JTextField stormNameText = new JTextField(20);
        stormNameText.setBounds(130,20,165,25);
        panel.add(stormNameText);

        JLabel stormDate = new JLabel("Storm Date");
        stormDate.setBounds(10,50,110,25);
        panel.add(stormDate);

        JTextField stormDateText = new JTextField(20);
        stormDateText.setBounds(130,50,165,25);
        panel.add(stormDateText);

        JLabel precipitationLevel = new JLabel("Precipitation (cm)");
        precipitationLevel.setBounds(10,80,110,25);
        panel.add(precipitationLevel);

        JTextField precipitationLevelText = new JTextField(20);
        precipitationLevelText.setBounds(130,80,165,25);
        panel.add(precipitationLevelText);

        JLabel windSpeeds = new JLabel("Wind Speed (km/h)");
        windSpeeds.setBounds(10,110,110,25);
        panel.add(windSpeeds);

        JTextField windSpeedsText = new JTextField(20);
        windSpeedsText.setBounds(130,110,165,25);
        panel.add(windSpeedsText);

        JButton confirmButton = new JButton("Edit Storm");
        confirmButton.setBounds(100,140,130,25);
        panel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stormName = stormNameText.getText();
                String stormDate = stormDateText.getText();
                String precipitation = precipitationLevelText.getText();
                //Double precipitationNum = Double.parseDouble(precipitation);
                String windSpeed = windSpeedsText.getText();
                //Double windSpeedNum = Double.parseDouble(windSpeed);

                if ((stormDate.length() == 10 && Character.isDigit(stormDate.charAt(0)) && Character.isDigit(stormDate.charAt(1)) && Character.isDigit(stormDate.charAt(2)) && Character.isDigit(stormDate.charAt(3))
                        && stormDate.charAt(4) == (char) 45 && Character.isDigit(stormDate.charAt(5)) && Character.isDigit(stormDate.charAt(6)) && stormDate.charAt(7) == (char) 45 &&
                        Character.isDigit(stormDate.charAt(8)) && Character.isDigit(stormDate.charAt(9)))) {
                    database.get(stormName).setDate(stormDate);
                } else if (stormDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No Date Entered");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Date Format (YYYY-MM-DD)");
                }

                if (!precipitation.isEmpty()) {
                    double rains = Double.parseDouble(precipitation);
                    database.get(stormName).setPrecipitation(rains);
                }
                else {
                    JOptionPane.showMessageDialog(null, "No Precipitation Entered");
                }

                if (!windSpeed.isEmpty()) {
                    double windy = Double.parseDouble(windSpeed);
                    database.get(stormName).setWindSpeed(windy);
                }
                else {
                    JOptionPane.showMessageDialog(null, "No Wind Speed Entered");
                }
                //System.out.println("Storm " + database.get(stormName).getName() + " Has Been Edited");

                //System.out.println(stormName + "\n" + stormDate + "\n" + precipitationNum + "\n" + windSpeedNum);

                panel.removeAll();
                panel.updateUI();

                try {
                    homeComponents(panel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This runs when the remove storm button is pressed on the home screen and takes a single string input of the name
     * of the storm you'd like to remove. If the storm exists it will be removed otherwise it sends back to home screen
     * @param panel
     */
    private static void removeStormComponents(JPanel panel) {
        panel.removeAll();
        panel.setLayout(null);

        JLabel stormName = new JLabel("Storm Name");
        stormName.setBounds(10,20,110,25);
        panel.add(stormName);

        JTextField stormNameText = new JTextField(20);
        stormNameText.setBounds(130,20,165,25);
        panel.add(stormNameText);

        JButton confirmButton = new JButton("Remove Storm");
        confirmButton.setBounds(10,50,110,25);
        panel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = stormNameText.getText();

                if (database.containsKey(name)) {
                    database.remove(name);
                    JOptionPane.showMessageDialog(null, "Success! Storm Has Been Deleted");
                } else {
                    JOptionPane.showMessageDialog(null,"Error! Storm Does Not Exist");
                    System.out.println("No Occurence Of A Storm With This Name");
                }

                panel.removeAll();
                panel.updateUI();

                try {
                    homeComponents(panel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method runs when look up button is pressed on the home screen and takes a single string input then passes
     * that name to another method lookUpResults that displays the storm info
     * @param panel
     */
    private static void lookUpStormComponents(JPanel panel) {
        panel.removeAll();
        panel.setLayout(null);

        JLabel stormName = new JLabel("Storm Name");
        stormName.setBounds(10,20,110,25);
        panel.add(stormName);

        JTextField stormNameText = new JTextField(20);
        stormNameText.setBounds(130,20,110,25);
        panel.add(stormNameText);

        JButton confirmButton = new JButton("Look Up Storm");
        confirmButton.setBounds(10,50,110,25);
        panel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = stormNameText.getText();
                if(database.containsKey(name)) {
                    lookUpResults(panel, stormNameText.getText());
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error! Storm Does Not Exist");
                    panel.removeAll();
                    panel.updateUI();
                    try {
                        homeComponents(panel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //panel.removeAll();
                //panel.updateUI();
            }
        });
    }

    /**
     * This is a helper method to the lookUpStorm method which takes the string input from lookUpStorm and displays
     * a new panel with the storm info. A home button will bring you back to the home screen.
     * @param panel
     * @param name
     */
    private static void lookUpResults(JPanel panel, String name) {
        panel.removeAll();
        panel.updateUI();
        panel.setLayout(null);
        //String passedName = database.get(name).getName();

        JLabel storm = new JLabel("Name");
        storm.setBounds(10,20,110,25);
        panel.add(storm);

        JLabel stormName = new JLabel(database.get(name).getName());
        stormName.setBounds(130,20,110,25);
        panel.add(stormName);

        JLabel dates = new JLabel("Date");
        dates.setBounds(10,50,110,25);
        panel.add(dates);

        JLabel stormDate = new JLabel(database.get(name).getDate());
        stormDate.setBounds(130,50,110,25);
        panel.add(stormDate);

        JLabel rain = new JLabel("Precipitation");
        rain.setBounds(10,80,110,25);
        panel.add(rain);

        JLabel precipitation = new JLabel(String.valueOf(database.get(name).getPrecipitation()));
        precipitation.setBounds(130,80,110,25);
        panel.add(precipitation);

        JLabel winds = new JLabel("Wind Speed");
        winds.setBounds(10,110,110,25);
        panel.add(winds);

        JLabel windSpeed = new JLabel(String.valueOf(database.get(name).getWindSpeed()));
        windSpeed.setBounds(130,110,110,25);
        panel.add(windSpeed);

        JButton home = new JButton("Back to Home");
        home.setBounds(10,140,110,25);
        panel.add(home);

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();

                try {
                    homeComponents(panel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * This method prints the whole database using JLabels.
     * TODO maybe use JTables instead if possible for neater display and extra functionality
     * @param panel
     */
    private static void printDatabase (JPanel panel) {
        panel.removeAll();
        panel.updateUI();
        panel.setLayout(null);
        int hashSize = database.size();
        String[] columnNames = {"Name", "Date", "Precipitation", "Wind Speed"};
        String[][] arr = new String[hashSize][4];
        String[] keys = new String[hashSize];
        int j = 0;
        for(String key : database.keySet()) {
            keys[j] = key;
            j++;
        }
        for(int i = 0; i < hashSize; i++) {
            arr[i][0] = database.get(keys[i]).getName();
            arr[i][1] = database.get(keys[i]).getDate();
            arr[i][2] = String.valueOf(database.get(keys[i]).getPrecipitation());
            arr[i][3] = String.valueOf(database.get(keys[i]).getWindSpeed());
        }

        /*JTable table = new JTable(arr, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(400,50));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane);
        panel.add(table);
         */
        JLabel name = new JLabel("Name");
        name.setBounds(10,20,100,25);
        panel.add(name);

        JLabel date = new JLabel("Date");
        date.setBounds(90,20,100,25);
        panel.add(date);

        JLabel rainfall = new JLabel("Precipitation");
        rainfall.setBounds(170,20,100,25);
        panel.add(rainfall);

        JLabel wind = new JLabel("Wind Speed");
        wind.setBounds(250,20,100,25);
        panel.add(wind);

        int yVal = 50;
        for(int i = 0; i < arr.length; i++) {
            JLabel stormName = new JLabel(arr[i][0]);
            stormName.setBounds(10,yVal,100,25);
            panel.add(stormName);

            JLabel stormDate = new JLabel(arr[i][1]);
            stormDate.setBounds(90,yVal,100,25);
            panel.add(stormDate);

            JLabel precipitation = new JLabel(arr[i][2]);
            precipitation.setBounds(170,yVal,100,25);
            panel.add(precipitation);

            JLabel windSpeed = new JLabel(arr[i][3]);
            windSpeed.setBounds(250,yVal,100,25);
            panel.add(windSpeed);

            yVal += 30;
        }

        JButton home = new JButton("Back to Home");
        home.setBounds(10,yVal+30,100,25);
        panel.add(home);

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                panel.updateUI();
                try {
                    homeComponents(panel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

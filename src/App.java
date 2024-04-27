import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Define the Cell class
class Cell {
    // Instance variables
    private String oem;
    private String model;
    private Integer launch_announced;
    private String launch_status;
    private String body_dimensions;
    private Float body_weight;
    private String body_sim;
    private String display_type;
    private Float display_size;
    private String display_resolution;
    private String features_sensors;
    private String platform_os;

    // Getters and Setters Methods 
    public String getOEM(){
        return oem;
    }
    public void setOEM(String OEM){
        this.oem = OEM;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public Integer getlaunchAnnounced(){
        return launch_announced;
    }

    public void setLaunchAnnounced(Integer launchedAnnounced){
        this.launch_announced = launchedAnnounced;
    }

    public String getLaunchStatus(){
        return launch_status;
    }

    public void setLaunchStatus(String launchStatus){
        this.launch_status = launchStatus;
    }

    public String getBodyDimensions(){
        return body_dimensions;
    }

    public void setBodyDimensions(String bodyDimensions){
        this.body_dimensions = bodyDimensions;
    }

    public Float getBodyWeight(){
        return body_weight;
    }

    public void setBodyWeight(Float bodyWeight){
        this.body_weight = bodyWeight;
    }

    public String getBodySim(){
        return body_sim;
    }

    public void setBodySim(String bodySim){
        this.body_sim = bodySim;
    }

    public String getDisplayType(){
        return display_type;
    }

    public void setDisplayType(String displayType){
        this.display_type = displayType;
    }

    public Float getDisplaySize(){
        return display_size;
    }

    public void setDisplaySize(Float displaySize){
        this.display_size = displaySize;
    }

    public String getDisplayResolution(){
        return display_resolution;
    }

    public void setDisplayResolution(String displayResolution){
        this.display_resolution = displayResolution;
    }

    public String getFeaturesSensors(){
        return features_sensors;
    }

    public void setFeaturesSensors(String featureSensor){
        this.features_sensors = featureSensor;
    }

    public String getPlatformOS(){
        return platform_os;
    }

    public void setPlatformOS(String platformOS){
        this.platform_os = platformOS;
    }
}

public class App {
    public static void main(String[] args) {
        String csvFile = "cells.csv";

        HashMap<Integer, Cell> cellMap = new HashMap<>();
        ArrayList<String> launchAnnouncedList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            //Skip the headers
            br.readLine();

            String line;
            int id = 1; // Start ID from 1

            //Go through each row of the csv
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                //Take in input of csv into cell object
                Cell cell = new Cell();
                cell.setOEM(cleanStrings(data[0]));
                cell.setModel(cleanStrings(data[1]));

                launchAnnouncedList.add(data[2]);

                cell.setLaunchAnnounced(cleanLaunchAnnounced(data[2]));
                cell.setLaunchStatus(cleanLaunchStatus(data[3]));
                cell.setBodyDimensions(cleanStrings(data[4]));
                cell.setBodyWeight(cleanBodyWeight(data[5]));
                cell.setBodySim(cleanBodySim(data[6]));
                cell.setDisplayType(cleanStrings(data[7]));
                cell.setDisplaySize(cleanDisplaySize(data[8]));
                cell.setDisplayResolution(cleanStrings(data[9]));
                cell.setFeaturesSensors(cleanFeatureSensors(data[10]));
                cell.setPlatformOS(cleanPlatformOS(data[11]));

                //Store each cell object in hashmap, mapped to its row number.
                cellMap.put(id++, cell);
            }


            displayUniqueData(cellMap);
            boolean userInput = true;
            Scanner scanner = new Scanner(System.in);

            while (userInput) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Add a new cell object");
                System.out.println("2. Delete an existing cell by index");
                System.out.println("3. Calculate the mode of the Launch Announced column");
                System.out.println("4. Display unique values for each column");
                System.out.println("5. Display the contents of each column");
                System.out.println("6. Display the mean of Body Weight");
                System.out.println("7. Display the median of Display Size");
                System.out.println("8. Find the company with the highest average body weight");
                System.out.println("9. Find phones announced in one year and released in another");
                System.out.println("10. Display phones with only one sensor feature");
                System.out.println("11. Display the year (post-1999) with the most phone releases");
                System.out.println("12. Exit");





                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        String temp = addCell(cellMap, scanner);
                        System.out.print("\n" + temp);
                        break;

                    case 2:
                        System.out.print("Enter index of cell to delete");
                        String s = deleteCell(cellMap, scanner);
                        System.out.print("\n" + s);
                        break;

                    case 3:
                        int mode = modeLaunchAnnounced(cellMap);
                        System.out.print("\nThe mode is: " + mode);
                        break;

                    case 4:
                        displayUniqueData(cellMap);
                        break;

                    case 5:
                        printContents(cellMap);
                        break;

                    case 6:
                        bodyWeightMean(cellMap);
                        break;

                    case 7:
                        displaySizeMedian(cellMap);
                        break;

                    case 8:
                        highestCompanyBodyWeight(cellMap);
                        break;
                    case 9:
                        phonesDifferentYears(cellMap, launchAnnouncedList);
                        break;
                    case 10:
                        phoneOneSensor(cellMap);
                        break;
                    case 11:
                        yearWithMostPhones(cellMap);
                        break;
                    case 12:
                        userInput = false;
                        break;

                    default:
                        System.out.println("\nInvalid choice. Please enter a valid option.");
                }
            }
            //Get unique data for each cell attribute

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans an input string removing any quotes.
     *
     * @param str The input string to be cleaned
     * @return The cleaned string with all quotes removed
     */
    public static String removeQuotes(String str) {
        return str.replace("\"", "");
    }

    /**
     * Cleans and trims the input string. If the input string is null or empty, it returns null.
     *
     * @param input The string to be cleaned and trimmed
     * @return The cleaned and trimmed string, or null if the input string is null or empty
     */

    public static String clean(String input) {
        if (input == null || input.trim().isEmpty() || input.equals("-")) {
            return null;
        }
        input = input.trim();
        return input;
    }

    /**
     * Ensuring all missing or "-" data is replaced with a null value.
     *
     * @param input The string to be cleaned
     * @return The cleaned string, or null if the resulting string is empty
     */

    public static String cleanStrings(String input){
        input = removeQuotes(input);
        if( input.equals("-") ||  input.isEmpty()){
            return null;
        }
        return input;
    }

    /**
     * Cleans the Platform OS data extracted from the CSV file by extracting data up to the first comma.
     * If the input string is empty or contains "-", returns null.
     *
     * @param input The string to be cleaned (Platform OS data from CSV)
     * @return The cleaned data string, or null if the input is empty or contains "-"
     */

    public static String cleanPlatformOS(String input) {
        if (input.equals("-") || input.isEmpty()) {
            return null;
        }
        input = removeQuotes(input);
        // Regular expression pattern to match everything up to the first comma or end of string
        Pattern pattern = Pattern.compile("^(.*?)(?:,|$)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return input.trim();
        }
    }


    /**
     * Checks if a string is composed of only numbers by attempting to parse string into a double.
     * @param input Input string to be checked if it is composed of only numbers.
     * @return Returns a boolean value based on if a string is composed of only numbers.
     */

    public static boolean onlyNumbers(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks featureSensors string to make sure it is not empty, or composed of only numbers.
     * @param input String that is passed for data cleaning
     * @return Returns cleaned string of featureSensros
     */
    public static String cleanFeatureSensors(String input){
        input = removeQuotes(input);
        if(input.equals("-") || input.isEmpty()){
            return null;
        }
        if(onlyNumbers(input)){
            return null;
        }
        return input;
    }
    /**
     * Checks if a given string can be parsed as a integer.
     * @param input Input string used to check if an input string can be parsed as an integer.
     * @return Returns a boolean value based on if a string can be parsed as an integer.
     */
    public static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Cleans passed launchAnnounced data string, parses the first year, and makes sure that the given string is not empty.
     * Also removes quotes from givens string.
     * @param input String that is passed for data cleaning
     * @return Returns cleaned launchAnnounced string.
     */
    public static Integer cleanLaunchAnnounced(String input){
        input = removeQuotes(input);

        Integer returnValue;
        if(input.length() < 4){
            return null;
        }
        else if(!isNumeric(input.substring(0,4))){
            return null;
        }
        else{
            String temp = input.substring(0,4);
            returnValue = Integer.parseInt(temp);
        }

        return returnValue;
    }
    public static Float cleanDisplaySize(String input) {
        //Regular expression to match the weight in grams ------
        Pattern pattern = Pattern.compile("(\\d+) inches");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String temp = matcher.group(1);
            return Float.parseFloat(temp);
        } else {
            return null;
        }
    }
    public static String cleanLaunchStatus(String str) {
        //4-digit year
        str = removeQuotes(str);
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(str);

        if("Discontinued".equals(str) || "Cancelled".equals(str )){
            return str;
        }
        else if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }

    }
    public static String cleanBodySim(String input) {
        input = removeQuotes(input);
        if(input.equals("Yes" )|| input.equals("No")){
            return null;
        }
        return input;

    }
    public static Float cleanBodyWeight(String input) {
        //Weight in grams
        Pattern pattern = Pattern.compile("(\\d+) g");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String temp = matcher.group(1);
            return Float.parseFloat(temp);
        } else {
            return null;
        }
    }
    /**
     * Adds a new Cell object to the cellMap based on user input.
     * @param cellMap The HashMap containing Cell objects.
     * @param scanner Scanner object to parse user input.
     * @return A string indicating the success or failure of the operation.
     */
    public static String addCell(HashMap<Integer, Cell> cellMap, Scanner scanner) {
        // Prompt user for cell details
        System.out.print("Enter the OEM (Original Equipment Manufacturer): ");
        String oem = scanner.nextLine();

        System.out.print("Enter the model: ");
        String model = scanner.nextLine();

        System.out.print("Enter the year of launch: ");
        int launchYear = getIntInput(scanner);

        System.out.print("Enter the launch status: ");
        String launchStatus = scanner.nextLine();

        System.out.print("Enter the body dimensions: ");
        String bodyDimensions = scanner.nextLine();

        System.out.print("Enter the body weight: ");
        float bodyWeight = getFloatInput(scanner);

        System.out.print("Enter the SIM type: ");
        String simType = scanner.nextLine();

        System.out.print("Enter the display type: ");
        String displayType = scanner.nextLine();

        System.out.print("Enter the display size (in inches): ");
        float displaySize = getFloatInput(scanner);

        System.out.print("Enter the display resolution: ");
        String displayResolution = scanner.nextLine();

        System.out.print("Enter the supported sensors and features: ");
        String featureSensors = scanner.nextLine();

        System.out.print("Enter the operating system (OS) platform: ");
        String osPlatform = scanner.nextLine();

        // Create a new Cell object and set its attributes
        Cell newCell = new Cell();
        newCell.setOEM(oem);
        newCell.setModel(model);
        newCell.setLaunchAnnounced(launchYear);
        newCell.setLaunchStatus(launchStatus);
        newCell.setBodyDimensions(bodyDimensions);
        newCell.setBodyWeight(bodyWeight);
        newCell.setBodySim(simType);
        newCell.setDisplayType(displayType);
        newCell.setDisplaySize(displaySize);
        newCell.setDisplayResolution(displayResolution);
        newCell.setFeaturesSensors(featureSensors);
        newCell.setPlatformOS(osPlatform);

        // Determine the index for the new cell in the map
        int newIndex = cellMap.size() + 1;

        // Add the new Cell to the cellMap
        cellMap.put(newIndex, newCell);

        // Return a success message with the index of the newly added cell
        return "Successfully added a new cell with index " + newIndex;
    }

    /**
     * Takes in a user input of a float, handles exceptions if user input
     * cannot be parsed as float.
     * @param scanner Used to parse user input
     * @return Returns user input in form of float.
     */
    private static Float getFloatInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextFloat();
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Enter a number: ");
                scanner.nextLine();
            }
        }
    }

    /**
     * Takes in the input of an integer, re-prompts the user if the input cannot be parsed as an integer.
     * @param scanner Scanner object used to parse user input.
     * @return The parsed integer input.
     */
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Enter a number: ");
                scanner.nextLine();
            }
        }
    }

    /**
     * Removes a Cell object from a HashMap based on the provided index.
     * @param cellMap A HashMap containing Cell objects, where keys are integers representing indices.
     * @param scanner Scanner object used for parsing user input.
     * @return A message indicating whether the specified index was not found or
     * the corresponding object was successfully deleted.
     */

    public static String deleteCell(HashMap<Integer, Cell> cellMap, Scanner scanner){
        System.out.print("\nEnter index of the cell to delete: ");
        int indexToDelete = getIntInput(scanner);
        if (cellMap.containsKey(indexToDelete)) {
            cellMap.remove(indexToDelete);
            return "Object deleted.";
        } else {
            return "Index isn't in the Cell Map.";
        }
    }

    /**
     * Calculates the mode (most frequently occurring value) of the launchAnnounced values
     * within the provided HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     * @return The mode (most frequent value) of the launchAnnounced values in the specified cellMap.
     */

    private static Integer modeLaunchAnnounced(HashMap<Integer, Cell> cellMap) {
        Map<Integer, Integer> countMap = new HashMap<>();
        Integer mode = null;
        int max = -1;

        for (Cell cell : cellMap.values()) {
            Integer launchAnnounced = cell.getlaunchAnnounced();
            if (launchAnnounced != null) { // Check for null before using as key
                countMap.put(launchAnnounced, countMap.getOrDefault(launchAnnounced, 0) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > max) {
                mode = entry.getKey();
                max = entry.getValue();
            }
        }

        return mode;
    }


    /**
     * Displays the unique values of each column in the provided HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void displayUniqueData(HashMap<Integer, Cell> cellMap){
        //Initialize unique data function input arrays
        ArrayList<String> oemList = new ArrayList<>();
        ArrayList<String> modelList = new ArrayList<>();
        ArrayList<Integer> launchAnnouncedList = new ArrayList<>();
        ArrayList<String> launchStatusList = new ArrayList<>();
        ArrayList<String> bodyDimensionsList = new ArrayList<>();
        ArrayList<Float> bodyWeightList = new ArrayList<>();
        ArrayList<String> bodySimList = new ArrayList<>();
        ArrayList<String> displayTypeList = new ArrayList<>();
        ArrayList<Float> displaySizeList = new ArrayList<>();
        ArrayList<String> displayResolutionList = new ArrayList<>();
        ArrayList<String> featureSensorsList = new ArrayList<>();
        ArrayList<String> platformOSsList = new ArrayList<>();

        for(Cell cell : cellMap.values()){
            oemList.add(cell.getOEM());
            modelList.add(cell.getModel());
            launchAnnouncedList.add(cell.getlaunchAnnounced());
            launchStatusList.add(cell.getLaunchStatus());
            bodyDimensionsList.add(cell.getBodyDimensions());
            bodyWeightList.add(cell.getBodyWeight());
            bodySimList.add(cell.getBodySim());
            displayTypeList.add(cell.getDisplayType());
            displaySizeList.add(cell.getDisplaySize());
            displayResolutionList.add(cell.getDisplayResolution());
            featureSensorsList.add(cell.getFeaturesSensors());
            platformOSsList.add(cell.getPlatformOS());
        }

        Set<String> uniqueOEM = uniqueDataString(oemList);
        Set<String> uniqueModel = uniqueDataString(modelList);
        Set<Integer> uniqueLaunchAnnounced = uniqueDataInteger(launchAnnouncedList);
        Set<String> uniqueLaunchStatus = uniqueDataString(launchStatusList);
        Set<String> uniqueBodyDimension = uniqueDataString(bodyDimensionsList);
        Set<Float> uniqueBodyWeightList = uniqueDataFloat(bodyWeightList);
        Set<String> uniqueBodySim = uniqueDataString(bodySimList);
        Set<String> uniqueDisplayType = uniqueDataString(displayTypeList);
        Set<Float> uniqueDisplaySize = uniqueDataFloat(displaySizeList);
        Set<String> uniqueDisplayResolution = uniqueDataString(displayResolutionList);
        Set<String> uniqueFeatureSensors = uniqueDataString(featureSensorsList);
        Set<String> uniquePlatformOS = uniqueDataString(platformOSsList);

        System.out.println("Unique items for OEM: " + uniqueOEM);
        System.out.println("\n\nUnique items for Model: " + uniqueModel);
        System.out.println("\n\nUnique items for Launch Announced: " + uniqueLaunchAnnounced);
        System.out.println("\n\nUnique items for Launch Status: " + uniqueLaunchStatus);
        System.out.println("\n\nUnique items for Body Dimension: " + uniqueBodyDimension);
        System.out.println("\n\nUnique items for Body Weight: " + uniqueBodyWeightList);
        System.out.println("\n\nUnique items for Body Sim: " + uniqueBodySim);
        System.out.println("\n\nUnique items for Display Type: " + uniqueDisplayType);
        System.out.println("\n\nUnique items for Display Size: " + uniqueDisplaySize);
        System.out.println("\n\nUnique items for Display Resolution: " + uniqueDisplayResolution);
        System.out.println("\n\nUnique items for Feature Sensors: " + uniqueFeatureSensors);
        System.out.println("\n\nUnique items for Platform OS: " + uniquePlatformOS);
    }

    /**
     * Extracts unique strings from the provided ArrayList of strings.
     *
     * @param input An ArrayList of strings containing data.
     * @return A Set containing unique strings from the input ArrayList.
     */

    public static Set<String> uniqueDataString(ArrayList<String> input){
        Set<String> uniqueValues = new HashSet<>();
        for(String item : input){
            String s = clean(item);
            uniqueValues.add(s);
        }

        return uniqueValues;
    }

    /**
     * Extracts unique strings from the provided ArrayList of strings.
     *
     * @param input An ArrayList of strings containing data.
     * @return A Set containing unique strings from the input ArrayList.
     */

    public static Set<Float> uniqueDataFloat(ArrayList<Float> input){
        Set<Float> uniqueValues = new HashSet<>();
        for(Float item : input){
            uniqueValues.add(item);
        }
        return uniqueValues;
    }

    /**
     * Retrieves a set of unique integers from the specified ArrayList of integers.
     *
     * @param input An ArrayList of integers containing data.
     * @return A Set containing unique integers extracted from the input ArrayList.
     */
    public static Set<Integer> uniqueDataInteger(ArrayList<Integer> input) {
        Set<Integer> uniqueValues = new HashSet<>();
        for (Integer item : input) {
            uniqueValues.add(item);
        }
        return uniqueValues;
    }

    /**
     * Prints the contents of the specified HashMap of Cell objects to the console.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void printContents(HashMap<Integer, Cell> cellMap) {
        for (Cell cell : cellMap.values()) {
            System.out.println("\nCell Details:");
            System.out.println("OEM: " + cell.getOEM());
            System.out.println("Model: " + cell.getModel());
            System.out.println("Launch Announced: " + (cell.getlaunchAnnounced() != null ? cell.getlaunchAnnounced() : "N/A"));
            System.out.println("Launch Status: " + cell.getLaunchStatus());
            System.out.println("Body Dimensions: " + cell.getBodyDimensions());
            System.out.println("Body Weight: " + (cell.getBodyWeight() != null ? cell.getBodyWeight() : "N/A"));
            System.out.println("Body SIM: " + cell.getBodySim());
            System.out.println("Display Type: " + cell.getDisplayType());
            System.out.println("Display Size: " + (cell.getDisplaySize() != null ? cell.getDisplaySize() : "N/A"));
            System.out.println("Display Resolution: " + cell.getDisplayResolution());
            System.out.println("Features & Sensors: " + cell.getFeaturesSensors());
            System.out.println("Platform OS: " + cell.getPlatformOS());
        }
    }

    /**
     * Calculates and prints the average (mean) of body weight values
     * from the specified HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void bodyWeightMean(HashMap<Integer, Cell> cellMap) {
        Float total = (float) 0;
        Float numberOfValues = (float) 0;
        for (Cell cell : cellMap.values()) {
            if (cell.getBodyWeight() != null) {
                total += cell.getBodyWeight();
                numberOfValues++;
            }
        }
        Float mean = total / numberOfValues;

        System.out.println("\n\nThe mean of body weight is: " + mean);
    }

    /**
     * Calculates and prints the median of the displaySize values
     * from the specified HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void displaySizeMedian(HashMap<Integer, Cell> cellMap) {
        ArrayList<Float> displaySizeList = new ArrayList<>();

        // Extract displaySize values from Cell objects
        for (Cell cell : cellMap.values()) {
            if (cell.getDisplaySize() != null) {
                displaySizeList.add(cell.getDisplaySize());
            }
        }

        // Sort the list of displaySize values
        Collections.sort(displaySizeList);

        // Calculate the median
        int size = displaySizeList.size();
        Float median;
        if (size % 2 == 0) {
            int midIndex1 = size / 2 - 1;
            int midIndex2 = size / 2;
            median = (displaySizeList.get(midIndex1) + displaySizeList.get(midIndex2)) / 2;
        } else {
            int midIndex = size / 2;
            median = displaySizeList.get(midIndex);
        }

        // Print the median value
        System.out.println("\n\nThe median of display size is: " + median);
    }



    /**
     * Identifies the company with the highest average body weight among the Cell objects in the provided HashMap.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void highestCompanyBodyWeight(HashMap<Integer, Cell> cellMap) {
        // HashMap to store the total body weight per company
        HashMap<String, Float> bwMap = new HashMap<>();
        // HashMap to store the count of body weights per company
        HashMap<String, Integer> companyCounter = new HashMap<>();

        // Populate bwMap and companyCounter based on Cell objects in cellMap
        for (Cell cell : cellMap.values()) {
            if (cell.getBodyWeight() != null) {
                bwMap.put(cell.getOEM(), bwMap.getOrDefault(cell.getOEM(), (float) 0) + cell.getBodyWeight());
                companyCounter.put(cell.getOEM(), companyCounter.getOrDefault(cell.getOEM(), 0) + 1);
            }
        }

        // Find the company with the highest average body weight
        String highestMeanOEM = null;
        float highestMean = 0f;
        for (String oem : bwMap.keySet()) {
            float totalWeight = bwMap.get(oem);
            int count = companyCounter.get(oem);
            float meanWeight = totalWeight / count;

            if (meanWeight > highestMean) {
                highestMean = meanWeight;
                highestMeanOEM = oem;
            }
        }

        // Print the company with the highest average body weight
        System.out.println("\n\nThe company: " + highestMeanOEM + " has the highest average body weight of: " + highestMean);
    }

    /**
     * Identifies and prints the OEM and model of cell phones where the launch date is later than the announced date,
     * based on the data in the provided HashMap of Cell objects and ArrayList of launch announcement strings.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     * @param launchAnnounced An ArrayList of strings containing original launch announcement information.
     */
    public static void phonesDifferentYears(HashMap<Integer, Cell> cellMap, ArrayList<String> launchAnnounced) {
        // List to store OEM and model strings for phones meeting the criteria
        ArrayList<String> oemAndModels = new ArrayList<>();

        // Pattern to extract release year from launch announcement strings
        Pattern pattern = Pattern.compile("Released *(\\d{4})");

        // Iterate through the Cell objects in cellMap and launchAnnounced strings
        for (int i = 0; i < launchAnnounced.size(); i++) { // Iterate over valid index range of launchAnnounced
            Cell cell = cellMap.get(i + 1); // Adjust index to retrieve Cell object
            if (cell != null && cell.getlaunchAnnounced() != null) {
                Matcher matcher = pattern.matcher(launchAnnounced.get(i));
                if (matcher.find()) {
                    int releaseYear = Integer.parseInt(matcher.group(1));

                    // Check if release year is later than launch announced year
                    if (releaseYear > cell.getlaunchAnnounced()) {
                        String info = "\n\nThe OEM is: " + cell.getOEM() + " and the model is: " + cell.getModel();
                        oemAndModels.add(info);
                    }
                }
            }
        }

        // Print the OEM and model information for phones meeting the criteria
        for (String info : oemAndModels) {
            System.out.println(info);
        }
    }

    /**
     * Calculates and prints the total number of phones that have exactly one sensor,
     * based on the data in the provided HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void phoneOneSensor(HashMap<Integer, Cell> cellMap) {
        int count = 0;

        // Iterate through the Cell objects in cellMap
        for (Cell cell : cellMap.values()) {
            String sensors = cell.getFeaturesSensors();

            // Check if the featuresSensors string contains only one sensor
            if (!sensors.contains(",")) {
                count++;
            }
        }

        // Print the total number of phones with exactly one sensor
        System.out.println("\n\nTotal number of phones with exactly one sensor: " + count);
    }

    /**
     * Identifies and prints the year (greater than 1999) with the most phones released,
     * based on the data in the provided HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects with integer keys representing indices.
     */
    public static void yearWithMostPhones(HashMap<Integer, Cell> cellMap) {
        // HashMap to track the number of phone releases per year (after 1999)
        HashMap<Integer, Integer> yearAndNumReleases = new HashMap<>();

        // Iterate through the Cell objects in cellMap
        for (Cell cell : cellMap.values()) {
            if (cell.getlaunchAnnounced() != null && cell.getlaunchAnnounced() > 1999) {
                yearAndNumReleases.put(cell.getlaunchAnnounced(), yearAndNumReleases.getOrDefault(cell.getlaunchAnnounced(), 0) + 1);
            }
        }

        // Find the year with the most phone releases
        int maxReleases = 0;
        int yearWithMostReleases = 0;
        for (Map.Entry<Integer, Integer> entry : yearAndNumReleases.entrySet()) {
            int year = entry.getKey();
            int numReleases = entry.getValue();

            if (numReleases > maxReleases) {
                maxReleases = numReleases;
                yearWithMostReleases = year;
            }
        }

        // Print the year with the most phone releases and the corresponding number of releases
        System.out.println("\n\nYear: " + yearWithMostReleases + ", Number of Releases: " + maxReleases);
    }








}


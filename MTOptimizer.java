import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
Main driving class for the program. All passenger and vehicle subclass objects are
stored in this class as attributes in array lists.
 */
public class MTOptimizer {

    //attributes
    //vehicle arrayLists
    private static ArrayList<Subway> subways = new ArrayList<>();
    private static ArrayList<GoTrain> gotrains = new ArrayList<>();
    private static ArrayList<Bus> buses = new ArrayList<>();
    private static ArrayList<GoBus> gobuses = new ArrayList<>();
    private static ArrayList<Streetcar> streetcars = new ArrayList<>();

    //passenger arrayLists
    private static ArrayList<Child> children = new ArrayList<>();
    private static ArrayList<Adult> adults = new ArrayList<>();
    private static ArrayList<Senior> seniors = new ArrayList<>();

    //broken lines to put into error log text file
    private static ArrayList<String> brokenLines = new ArrayList<>();

    //array of integers representing ridership by vehicle and time of day
        //column indexes represent: 0 - subway, 1 - gotrains, 2 - buses, 3 - gobuses, 4 - streetcars
        //row indexes represent time of day in range (1,24). For clarity index i means hour i,
        //therefore row i = 0 is unused
    private static float[][] ridershipTable = new float[25][5];

    //arrays of all linear combinations of vehicles for each of the five types;
        //these will be used for the optimization of the fleet
        //supposing for each type of vehicle there are m groups of capacities, then there will be m + 1 columns
        //the first row columns represent the capacities of vehicles (c1, c, ... , cm), where the last spot is empty (i.e. 0)
        //for the rest of the table's rows:
        //the first m columns will represent the number of vehicles (n1, n2, ... , nm) written in lexicographic order
        //the final column will be the linear combinations c1*n1 + c2*n2 + ... + cm*nm
        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
    private static int[][] subwayCombinations;
    private static int[][] goTrainCombinations;
    private static int[][] busCombinations;
    private static int[][] goBusCombinations;
    private static int[][] streetcarCombinations;

    public static void main(String[] args) {
        //build the vehicle information array lists
        buildVehicleLists();

        //build passenger array lists, building broken lines list as we go
        buildPassengerLists();

        //take broken lines list and write to text file errorlog.txt
        writeErrorLog();

        //build ridership table representing at index (i,j) ridership at ith hour for jth type of transit, see above
        buildRidershipTable();

        //calculate tables of all the possible riderships (passengers) that can be formed for each
        //transportation type using the available fleet, to be used to optimize fleet use
        buildLinearCombinations();

        //using the ridership table and linear combination tables, write the results to the file 'InOperationFleets.txt'
        writeResultsFile();
    }

    /**
     This method builds the array lists of vehicles
     */
    private static void buildVehicleLists() {

        //BUILD SUBWAYS
        Scanner fromFileStream = null;

        // Open the file. Check for file I/O exceptions.
        try {
            fromFileStream = new Scanner(new FileInputStream("subways.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'subways.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        String inputLine;
        ArrayList<String> subwayLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            subwayLines.add(inputLine);
        }
        fromFileStream.close();

        String[] currLine;
        Subway currSubway;

        //make the subway objects and put them in the arrayList
        for (int i = 0; i < subwayLines.size(); i++){
            currLine = subwayLines.get(i).split(",");
            currSubway = new Subway(currLine[1], Integer.parseInt(currLine[0]), Integer.parseInt(currLine[2]),
                    Integer.parseInt(currLine[3]), currLine[4], currLine[5]);
            subways.add(currSubway);
        }

        //BUILD GOTRAINS
        try {
            fromFileStream = new Scanner(new FileInputStream("gotrains.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'gotrains.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        ArrayList<String> goTrainLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            goTrainLines.add(inputLine);
        }
        fromFileStream.close();

        GoTrain currGoTrain;

        //make the subway objects and put them in the arrayList
        for (int i = 0; i < goTrainLines.size(); i++){
            currLine = goTrainLines.get(i).split(",");
            currGoTrain = new GoTrain(currLine[1], Integer.parseInt(currLine[0]), Integer.parseInt(currLine[2]));
            gotrains.add(currGoTrain);
        }

        //BUILD BUSES
        try {
            fromFileStream = new Scanner(new FileInputStream("buses.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'buses.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        ArrayList<String> busLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            busLines.add(inputLine);
        }
        fromFileStream.close();

        Bus currBus;

        //make the subway objects and put them in the arrayList
        for (int i = 0; i < busLines.size(); i++){
            currLine = busLines.get(i).split(",");
            currBus = new Bus(currLine[1], Integer.parseInt(currLine[0]), Integer.parseInt(currLine[2]));
            buses.add(currBus);
        }

        //BUILD GOBUSES
        try {
            fromFileStream = new Scanner(new FileInputStream("gobuses.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'gobuses.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        ArrayList<String> goBusLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            goBusLines.add(inputLine);
        }
        fromFileStream.close();

        GoBus currGoBus;

        //make the subway objects and put them in the arrayList
        for (int i = 0; i < goBusLines.size(); i++){
            currLine = goBusLines.get(i).split(",");
            currGoBus = new GoBus(currLine[1], Integer.parseInt(currLine[0]), Integer.parseInt(currLine[2]));
            gobuses.add(currGoBus);
        }

        //BUILD STREETCARS
        try {
            fromFileStream = new Scanner(new FileInputStream("streetcars.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'streetcars.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        ArrayList<String> streetcarLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            streetcarLines.add(inputLine);
        }
        fromFileStream.close();

        Streetcar currStreetcar;

        //make the subway objects and put them in the arrayList
        for (int i = 0; i < streetcarLines.size(); i++){
            currLine = streetcarLines.get(i).split(",");
            currStreetcar = new Streetcar(currLine[1], Integer.parseInt(currLine[0]), currLine[2]);
            streetcars.add(currStreetcar);
        }
    }

    /**
     This methods builds the array lists of passengers
    */
    private static void buildPassengerLists(){

        //Read ridership file
        Scanner fromFileStream = null;

        // Open the file. Check for file I/O exceptions.
        try {
            fromFileStream = new Scanner(new FileInputStream("ridership.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'ridership.txt'. Program aborted!");
            System.exit(0);
        }

        //Put the lines of the file in an array list of strings
        String inputLine;
        ArrayList<String> ridershipLines= new ArrayList<>();

        while (fromFileStream.hasNextLine()) {
            inputLine = fromFileStream.nextLine();
            ridershipLines.add(inputLine);
        }
        fromFileStream.close();

        String[] currLine;

        //process ridershipLines array list into the array lists of children, adults, seniors
        for (int i = 0; i < ridershipLines.size(); i++) {
            currLine = ridershipLines.get(i).split(",");
            buildPassenger(currLine);
        }

    }

    /**
     Takes an array of Strings and builds a passenger of type child, adult, or senior. If it is broken it adds it
     to an array list 'brokenLines' to be put into the error file
     */
    private static void buildPassenger(String[] currLine) {

        try {
            //get ride of faulty data
            if (!(currLine[1].equals("S") || currLine[1].equals("G") || currLine[1].equals("X")
                    || currLine[1].equals("C") || currLine[1].equals("D")))
                //make sure transit type is correct
                throw new Exception("Transit type is incorrect.");
            if (!(Integer.parseInt(currLine[3]) >= 1 && Integer.parseInt(currLine[3]) <= 24))
                //make sure hour of day is in correct range
                throw new Exception("Hour of the day is incorrect.");
            if (!(Integer.parseInt(currLine[4]) == 20190304))
                //make sure date is correct
                throw new Exception("Date is incorrect.");
            if (currLine.length > 5)
                //ensure not too many number of attributes
                throw new Exception("Too many attributes.");
            if (currLine[0].equals("") || currLine[1].equals("") || currLine[2].equals("") || currLine[3].equals("")
                    || currLine[4].equals(""))
                //ensure not too few
                throw new Exception("Too few attributes.");
            if (!(currLine[0].equals("*") ||currLine[0].startsWith("T") || Integer.parseInt(currLine[0]) <= 9999999))
                //check ID validity
                throw new Exception("Invalid ID.");


            if (currLine[2].equals("A")) {
                //passenger is adult
                adults.add(new Adult(currLine[0], currLine[1],
                        Integer.parseInt(currLine[3]), Integer.parseInt(currLine[4])));
            } else if (currLine[2].equals("C")) {
                //passenger is child
                children.add(new Child(currLine[0], currLine[1],
                        Integer.parseInt(currLine[3]), Integer.parseInt(currLine[4])));

            } else if (currLine[2].equals("S")) {
                //passenger is senior
                seniors.add(new Senior(currLine[0], currLine[1],
                        Integer.parseInt(currLine[3]), Integer.parseInt(currLine[4])));

            } else {
                throw new Exception("Age group is incorrect.");
            }
        }
        catch (NumberFormatException e) {
            String concatenate = "";
            for (int i = 0; i < currLine.length; ++i) {
                concatenate += currLine[i] + ",";
            }
            brokenLines.add(concatenate.substring(0, concatenate.length() - 1) + " " + "Invalid ID\n");
        }
        catch (Exception e) {
            //line is broken, add to brokenLines static variable
            String concatenate = "";
            for (int i = 0; i < currLine.length; ++i) {
                concatenate += currLine[i] + ",";
            }
            //add the concatenate string and the error message, cutting of the java.lang part
            brokenLines.add(concatenate.substring(0, concatenate.length() - 1) + " " + e.toString().substring(20) + "\n");
        }
    }

    /**
     Function writes list brokenLines to errorlog.txt file
     */
    private static void writeErrorLog() {

        PrintWriter toFileStream = null;

        // Check for file I/O exceptions.
        try {
            toFileStream = new PrintWriter(new FileOutputStream("errorlog.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error opening the file 'errorlog.txt'. Program aborted!");
            System.exit(0);
        }

        //write to errorlog.txt file
        for (int i = 0; i < brokenLines.size(); i++) {
            toFileStream.println(brokenLines.get(i));
        }

        toFileStream.close();
    }

    /**
     This method builds the ridership table to use later
     */
    private static void buildRidershipTable() {

        String modality ;
        int hourOfDay;

        //go through children
        for (int i = 0; i < children.size(); i++){
            modality = children.get(i).getModality();
            hourOfDay = children.get(i).getHourOfTheDay();
            ridershipTable[hourOfDay][modalityLetterToIndex(modality)] += 0.75;
        }
        //go through adults
        for (int i = 0; i < adults.size(); i++){
            modality = adults.get(i).getModality();
            hourOfDay = adults.get(i).getHourOfTheDay();
            ridershipTable[hourOfDay][modalityLetterToIndex(modality)] += 1;
        }
        //go through seniors
        for (int i = 0; i < seniors.size(); i++){
            modality = seniors.get(i).getModality();
            hourOfDay = seniors.get(i).getHourOfTheDay();
            ridershipTable[hourOfDay][modalityLetterToIndex(modality)] += 1.25;
        }
    }

    /**
     * Converts letter modality to corresponding index
     * @param modality a String representing the type of vehicle by a letter.
     * @return an integer that is its corresponding index.
     */
    private static int modalityLetterToIndex(String modality){

        if (modality.equals("S"))
            return 0;
        else if (modality.equals("G"))
            return 1;
        else if (modality.equals("C"))
            return 2;
        else if (modality.equals("D"))
            return 3;
        else if (modality.equals("X"))
            return 4;
        else
            return -1;
    }

    /**
     Builds 2-d int arrays of all the possible combinations, and passenger accommodation, for each of the vehicles
     */
    private static void buildLinearCombinations() {

        //sort all the vehicle lists by capacity
        sortVehicleLists();

        //make linear combination tables for all vehicles

        /*
        ******
        SUBWAY
        ******
         */

        //group vehicles by capacity
        ArrayList<int[]> capacityGrouping = new ArrayList<>();

        int[] temp;                 //first number in temp is the capacity currCapacity, second number is the number of
                                    //vehicles at that capacity
        int currCapacity;           //the current capacity

        int index = 0;              //index

        while (index < subways.size()) {
            //go through vehicle array list
            temp = new int[2];
            //initialize temp to empty it
            currCapacity = subways.get(index).getCapacity();
            temp[0] = currCapacity;

            while (index < subways.size() && subways.get(index).getCapacity() == currCapacity) {
                if (subways.get(index).functional())
                    temp[1]++;

                index++;
            }

            //add the current group the the capacity grouping array list
            capacityGrouping.add(temp);
        }
        int rowNum = 1;
        int colNum;

        //as described before, there are m + 1 columns, for m capacity groups
        colNum = capacityGrouping.size()+ 1;

        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
        for (int i = 0; i < capacityGrouping.size(); i++) {
            rowNum *= (capacityGrouping.get(i)[1] + 1);
        }
        rowNum += 1;

        //initialize table
        subwayCombinations = new int[rowNum][colNum];

        //build the combination table using the capacityGrouping data structure
        int[][] linCombTemp = buildACombinationTable(capacityGrouping, rowNum, colNum);

        //deep copy linCompTemp into subwayCombinations
        for (int row = 0; row < linCombTemp.length; row++) {
            for (int col = 0; col < linCombTemp[row].length; col++){
                subwayCombinations[row][col] = linCombTemp[row][col];
            }
        }

        /*
        ******
        GOTRAIN
        ******
         */

        //group vehicles by capacity
        capacityGrouping.clear();

        //reset index
        index = 0;

        while (index < gotrains.size()) {
            //go through vehicle array list
            temp = new int[2];
            //initialize temp to empty it
            currCapacity = gotrains.get(index).getCapacity();
            temp[0] = currCapacity;

            while (index < gotrains.size() && gotrains.get(index).getCapacity() == currCapacity) {
                temp[1]++;

                index++;
            }

            //add the current group the the capacity grouping array list
            capacityGrouping.add(temp);
        }

        //reset rowNum
        rowNum = 1;

        //as described before, there are m + 1 columns, for m capacity groups
        colNum = capacityGrouping.size()+ 1;

        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
        for (int i = 0; i < capacityGrouping.size(); i++) {
            rowNum *= (capacityGrouping.get(i)[1] + 1);
        }
        rowNum += 1;

        //initialize table
        goTrainCombinations = new int[rowNum][colNum];

        //build the combination table using the capacityGrouping data structure
        linCombTemp = buildACombinationTable(capacityGrouping, rowNum, colNum);

        //deep copy linCompTemp into goTrainCombinations
        for (int row = 0; row < linCombTemp.length; row++) {
            for (int col = 0; col < linCombTemp[row].length; col++){
                goTrainCombinations[row][col] = linCombTemp[row][col];
            }
        }

         /*
        ******
        BUSES
        ******
         */

        //group vehicles by capacity
        capacityGrouping.clear();

        //reset index
        index = 0;

        while (index < buses.size()) {
            //go through vehicle array list
            temp = new int[2];
            //initialize temp to empty it
            currCapacity = buses.get(index).getCapacity();
            temp[0] = currCapacity;

            while (index < buses.size() && buses.get(index).getCapacity() == currCapacity) {
                temp[1]++;

                index++;
            }

            //add the current group the the capacity grouping array list
            capacityGrouping.add(temp);
        }

        //reset rowNum
        rowNum = 1;

        //as described before, there are m + 1 columns, for m capacity groups
        colNum = capacityGrouping.size()+ 1;

        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
        for (int i = 0; i < capacityGrouping.size(); i++) {
            rowNum *= (capacityGrouping.get(i)[1] + 1);
        }
        rowNum += 1;

        //initialize table
        busCombinations = new int[rowNum][colNum];

        //build the combination table using the capacityGrouping data structure
        linCombTemp = buildACombinationTable(capacityGrouping, rowNum, colNum);

        //deep copy linCompTemp into busCombinations
        for (int row = 0; row < linCombTemp.length; row++) {
            for (int col = 0; col < linCombTemp[row].length; col++){
                busCombinations[row][col] = linCombTemp[row][col];
            }
        }

        /*
        ******
        GOBUSES
        ******
         */

        //group vehicles by capacity
        capacityGrouping.clear();

        //reset index
        index = 0;

        while (index < gobuses.size()) {
            //go through vehicle array list
            temp = new int[2];
            //initialize temp to empty it
            currCapacity = gobuses.get(index).getCapacity();
            temp[0] = currCapacity;

            while (index < gobuses.size() && gobuses.get(index).getCapacity() == currCapacity) {
                temp[1]++;

                index++;
            }

            //add the current group the the capacity grouping array list
            capacityGrouping.add(temp);
        }

        //reset rowNum
        rowNum = 1;

        //as described before, there are m + 1 columns, for m capacity groups
        colNum = capacityGrouping.size()+ 1;

        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
        for (int i = 0; i < capacityGrouping.size(); i++) {
            rowNum *= (capacityGrouping.get(i)[1] + 1);
        }
        rowNum += 1;

        //initialize table
        goBusCombinations = new int[rowNum][colNum];

        //build the combination table using the capacityGrouping data structure
        linCombTemp = buildACombinationTable(capacityGrouping, rowNum, colNum);

        //deep copy linCompTemp into gobuseCombinations
        for (int row = 0; row < linCombTemp.length; row++) {
            for (int col = 0; col < linCombTemp[row].length; col++){
                goBusCombinations[row][col] = linCombTemp[row][col];
            }
        }

        /*
        ******
        STREETCAR
        ******
         */

        //group vehicles by capacity
        capacityGrouping.clear();

        //reset index
        index = 0;

        while (index < streetcars.size()) {
            //go through vehicle array list
            temp = new int[2];
            //initialize temp to empty it
            currCapacity = streetcars.get(index).getCapacity();
            temp[0] = currCapacity;

            while (index < streetcars.size() && streetcars.get(index).getCapacity() == currCapacity) {
                temp[1]++;

                index++;
            }

            //add the current group the the capacity grouping array list
            capacityGrouping.add(temp);
        }

        //reset rowNum
        rowNum = 1;

        //as described before, there are m + 1 columns, for m capacity groups
        colNum = capacityGrouping.size()+ 1;

        //therefore there will be (n1 + 1) * (n2 + 1) * ... * (nm + 1) + 1 rows
        for (int i = 0; i < capacityGrouping.size(); i++) {
            rowNum *= (capacityGrouping.get(i)[1] + 1);
        }
        rowNum += 1;

        //initialize table
        streetcarCombinations = new int[rowNum][colNum];

        //build the combination table using the capacityGrouping data structure
        linCombTemp = buildACombinationTable(capacityGrouping, rowNum, colNum);

        //deep copy linCompTemp into streetcarCombinations
        for (int row = 0; row < linCombTemp.length; row++) {
            for (int col = 0; col < linCombTemp[row].length; col++){
                streetcarCombinations[row][col] = linCombTemp[row][col];
            }
        }

    }

    /**
     * Takes in an array list of int arrays of size 2, where first value is a capacity, and second number is
     * corresponding number of vehicles at that capacity. Rows and cols represent how many rows and columns
     * the array of linear combinations will have.
     * Returns a sorted array of linear combinations as described in the attributes at the top.
     * @param capacityGrouping the array list of capacity/capacity volume grouping
     * @param rows the number of rows of the linear combination table.
     * @param cols the number of columns of the table.
     * @return sorted array of linear combinations.
     */
    private static int[][] buildACombinationTable(ArrayList<int[]> capacityGrouping, int rows, int cols) {
        /*

         */

        //build linear combinations table
        int[][] linearCombinations = new int[rows][cols];

        //fill first row with the capacities c1 through cm (last position is unused)
        for (int i = 0; i < capacityGrouping.size(); i++) {
            linearCombinations[0][i] = capacityGrouping.get(i)[0];
        }


        //write out in lexicographic order all numbers 0 0 0,...,0 to n1 n2 n3, ...,nm
        int row = 2;
        int currCol;
        int currCombination = 0;

        while (row < linearCombinations.length) {
            //go through each row of linear combinations, starting at the 3rd
            //because the first has been filled and the second is always all 0

            //for each row write the next lexicographic number in the first m columns

            //set currCol to index of second last col
            currCol = linearCombinations[0].length - 2;

            while (linearCombinations[row - 1][currCol] == capacityGrouping.get(currCol)[1]) {
                linearCombinations[row][currCol] = 0;
                currCol--;
            }

            linearCombinations[row][currCol] = linearCombinations[row-1][currCol] + 1;

            //copy the remaining numbers to the left of currCol as they are
            for (int i = 0; i < currCol; i++)
                linearCombinations[row][i] = linearCombinations[row-1][i];

            //calculate the linear combination for the last column
            for (int i = 0; i < capacityGrouping.size(); i++) {
                //calculate combination c1n1 + c2n2 + ... + cm * nm
                currCombination += linearCombinations[row][i] * linearCombinations[0][i];
            }

            linearCombinations[row][linearCombinations[0].length - 1] = currCombination;
            currCombination = 0;

            row++;
        }

        //sort the table by the linear combinations (last column)
        Arrays.sort(linearCombinations, new Comparator<int[]>() {
            public int compare(int[] int1, int[] int2) {
                Integer num1 = int1[int1.length - 1];
                Integer num2 = int2[int2.length - 1];
                return num1.compareTo(num2);
            }
        });

        return linearCombinations;

    }

    /**
     Sorts all the vehicle array lists by capacity
     */
    private static void sortVehicleLists() {

        //sort subway
        Collections.sort(subways, Subway.subwayComparator);

        //sort gotrain
        Collections.sort(gotrains, GoTrain.goTrainComparator);

        //sort bus
        Collections.sort(buses, Bus.busComparator);

        //sort gobus
        Collections.sort(gobuses, GoBus.goBusComparator);

        //sort streetcar
        Collections.sort(streetcars, Streetcar.streetcarComparator);
    }

    /**
     This is the final step. Using the calculations of hourly ridership for each vehicle, in ridershipTable,
     and linear combinations in the linearCombinations tables, find the optimal fleet uses and input into
     the file 'InOperationsFleet.txt'.
     */
    private static void writeResultsFile(){

        //make object to write to file
        PrintWriter toFileStream = null;

        // Check for file I/O exceptions.
        try {
            toFileStream = new PrintWriter(new FileOutputStream("InOperationFleets.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error.Program aborted!");
            System.exit(0);
        }

        //0 - subway, 1 - gotrains, 2 - buses, 3 - gobuses, 4 - streetcars
        //corresponds to rideship table attribute
        String[] vehicleNames = {"Subways", "GoTrains", "Buses", "GoBuses", "Streetcars"};
        float currRideship;
        String currFleet;

        //organize file by vehicle sections and hour subsections for each
        for (int i = 0; i < vehicleNames.length; i++){
            //for each vehicle
            toFileStream.println("[" + vehicleNames[i] + "]");

            for(int j = 1; j < 25; j++) {
                //for each hour 1 through 24
                if (j < 10) {
                    toFileStream.println("[Hour = 0" + j + "]");
                }
                else {
                    toFileStream.println("[Hour = " + j + "]");
                }

                currRideship = ridershipTable[j][i];
                toFileStream.println("Required rideship: " + currRideship);

                //get the current fleet and print it to the file
                currFleet = getFleetForVehicleIndexAtHour(i, currRideship);

                toFileStream.println(currFleet);

            }
            toFileStream.println();
        }
        toFileStream.close();
    }

    /**
     Receives vehicle index (as defined above) and ridership. Returns the fleet as a String.
     */
    private static String getFleetForVehicleIndexAtHour(int vehicleIndex, float currRideship){

        String fleet = "[Fleet description:]\n";

        //find the row in the linear combination array that satisfies the ridership using this indexing variable
        int row = 1;
        int indexinVehicleList = 0;
        int total = 0;

        switch (vehicleIndex) {
            //switch between vehicle cases
            case 0:{
                //"subway"
                while (row < subwayCombinations.length && (float)subwayCombinations[row][subwayCombinations[0].length - 1] < currRideship) {
                    row++;
                }
                //row now represents the row index in the linear combinations array that is the desired fleet

                //find what vehicle objects this combination represents in the array list of corresponding vehicles
                //copy each vehicle's 'toString()' method return value to 'fleet';

                for (int i = 0; i < subwayCombinations[0].length - 1; i++){
                    //for each nj, going through all columns but the last which represents combination

                    for (int nj = subwayCombinations[row][i]; nj > 0; nj--){
                        //count down from nj to 0, add one subway each time

                        fleet += subways.get(indexinVehicleList).toString() + "\n";
                        indexinVehicleList++;
                    }

                    //move pointer indexInVehicles to next group of vehicles with same capacity
                    while (indexinVehicleList < subways.size() && subways.get(indexinVehicleList).getCapacity() == subwayCombinations[0][i])
                        indexinVehicleList++;
                }

                //total vehicle count and passenger capacity for the fleet
                int totalcapacity = 0;
                for (int i = 0; i < subwayCombinations[0].length - 1; i++){
                    total += subwayCombinations[row][i];
                    totalcapacity += subwayCombinations[row][i] * subwayCombinations[0][i];
                }

                fleet += "[Count: " + total + "]\n";
                fleet += "[Total rider capacity with this fleet: " + totalcapacity + "]\n";
                break;
            }
            case 1:{
                //"goTrain"
                while (row < goTrainCombinations.length && (float)goTrainCombinations[row][goTrainCombinations[0].length - 1] < currRideship) {
                    row++;
                }

                //row now represents the row index in the linear combinations array that is the desired fleet

                //find what vehicle objects this combination represents in the array list of corresponding vehicles
                //copy each vehicle's 'toString()' method return value to 'fleet';

                for (int i = 0; i < goTrainCombinations[0].length - 1; i++){
                    //for each nj, going through all columns but the last which represents combination

                    for (int nj = goTrainCombinations[row][i]; nj > 0; nj--){
                        //count down from nj to 0, add one goTrain each time

                        fleet += gotrains.get(indexinVehicleList).toString() + "\n";
                        indexinVehicleList++;
                    }

                    //move pointer indexInVehicles to next group of vehicles with same capacity
                    while (indexinVehicleList < gotrains.size() && gotrains.get(indexinVehicleList).getCapacity() == goTrainCombinations[0][i])
                        indexinVehicleList++;
                }

                //total vehicle count and passenger capacity for the fleet
                int totalcapacity = 0;
                for (int i = 0; i < goTrainCombinations[0].length - 1; i++){
                    total += goTrainCombinations[row][i];
                    totalcapacity += goTrainCombinations[row][i] * goTrainCombinations[0][i];
                }

                fleet += "[Count: " + total + "]\n";
                fleet += "[Total rider capacity with this fleet: " + totalcapacity + "]\n";
                break;

            }
            case 2:{
                //"bus"
                while (row < busCombinations.length && (float)busCombinations[row][busCombinations[0].length - 1] < currRideship) {
                    row++;
                }

                //row now represents the row index in the linear combinations array that is the desired fleet

                //find what vehicle objects this combination represents in the array list of corresponding vehicles
                //copy each vehicle's 'toString()' method return value to 'fleet';

                for (int i = 0; i < busCombinations[0].length - 1; i++){
                    //for each nj, going through all columns but the last which represents combination

                    for (int nj = busCombinations[row][i]; nj > 0; nj--){
                        //count down from nj to 0, add one bus each time

                        fleet += buses.get(indexinVehicleList).toString() + "\n";
                        indexinVehicleList++;
                    }

                    //move pointer indexInVehicles to next group of vehicles with same capacity
                    while (indexinVehicleList < buses.size() && buses.get(indexinVehicleList).getCapacity() == busCombinations[0][i])
                        indexinVehicleList++;
                }

                //total vehicle count and passenger capacity for the fleet
                int totalcapacity = 0;
                for (int i = 0; i < busCombinations[0].length - 1; i++){
                    total += busCombinations[row][i];
                    totalcapacity += busCombinations[row][i] * busCombinations[0][i];
                }

                fleet += "[Count: " + total + "]\n";
                fleet += "[Total rider capacity with this fleet: " + totalcapacity + "]\n";
                break;

            }
            case 3:{
                //"goBus"
                while (row < goBusCombinations.length && (float)goBusCombinations[row][goBusCombinations[0].length - 1] < currRideship) {
                    row++;
                }

                //row now represents the row index in the linear combinations array that is the desired fleet

                //find what vehicle objects this combination represents in the array list of corresponding vehicles
                //copy each vehicle's 'toString()' method return value to 'fleet';

                for (int i = 0; i < goBusCombinations[0].length - 1; i++){
                    //for each nj, going through all columns but the last which represents combination

                    for (int nj = goBusCombinations[row][i]; nj > 0; nj--){
                        //count down from nj to 0, add one goBus each time

                        fleet += gobuses.get(indexinVehicleList).toString() + "\n";
                        indexinVehicleList++;
                    }

                    //move pointer indexInVehicles to next group of vehicles with same capacity
                    while (indexinVehicleList < gobuses.size() && gobuses.get(indexinVehicleList).getCapacity() == goBusCombinations[0][i])
                        indexinVehicleList++;
                }

                //total vehicle count and passenger capacity for the fleet
                int totalcapacity = 0;
                for (int i = 0; i < goBusCombinations[0].length - 1; i++){
                    total += goBusCombinations[row][i];
                    totalcapacity += goBusCombinations[row][i] * goBusCombinations[0][i];
                }

                fleet += "[Count: " + total + "]\n";
                fleet += "[Total rider capacity with this fleet: " + totalcapacity + "]\n";
                break;
            }
            case 4:{
                //"streetcar"
                while (row < streetcarCombinations.length && (float)streetcarCombinations[row][streetcarCombinations[0].length - 1] < currRideship) {
                    row++;
                }

                //row now represents the row index in the linear combinations array that is the desired fleet

                //find what vehicle objects this combination represents in the array list of corresponding vehicles
                //copy each vehicle's 'toString()' method return value to 'fleet';

                for (int i = 0; i < streetcarCombinations[0].length - 1; i++){
                    //for each nj, going through all columns but the last which represents combination

                    for (int nj = streetcarCombinations[row][i]; nj > 0; nj--){
                        //count down from nj to 0, add one streetcar each time

                        fleet += streetcars.get(indexinVehicleList).toString() + "\n";
                        indexinVehicleList++;
                    }

                    //move pointer indexInVehicles to next group of vehicles with same capacity
                    while (indexinVehicleList < streetcars.size() && streetcars.get(indexinVehicleList).getCapacity() == streetcarCombinations[0][i])
                        indexinVehicleList++;
                }

                //total vehicle count and passenger capacity for the fleet
                int totalcapacity = 0;
                for (int i = 0; i < streetcarCombinations[0].length - 1; i++){
                    total += streetcarCombinations[row][i];
                    totalcapacity += streetcarCombinations[row][i] * streetcarCombinations[0][i];
                }

                fleet += "[Count: " + total + "]\n";
                fleet += "[Total rider capacity with this fleet: " + totalcapacity + "]\n";
                break;
            }
            default: {
                return fleet;
            }
        }
        return fleet;
    }
}

import java.util.*;

/**
 * Subway class holds information about subway objects. It inherits Vehicle.
 */
public class Subway extends Vehicle{

    //additional attributes

    private int numberOfCars;
    private int passengersPerCar;
    private String operationalStatus;
    private String operationalDate;

    public static Comparator<Subway> subwayComparator = new Comparator<Subway>(){
        public int compare(Subway s1, Subway s2) {
            return Integer.compare(s1.getCapacity(), s2.getCapacity());
        }
    };

    //constructor

    /**
     * Subway objects are instantiated with the following attributes.
     * @param identification holds 3-digit identification number in range 100-999.
     * @param unitNumber a unique 4-digit number that identifies a vehicle in the TTC’s Computerized Information System
     * @param numberOfCars a 1-digit indicating the number of cars per train.
     * @param passengersPerCar 2-digit number indicating a car’s maximum passenger capacity.
     * @param operationalStatus A 1-character field indicating whether train is available or
     *                          not on the date indicated in the Operation Date field: Available (A),
     *                          Unavailable (U), Unknown (*).
     * @param operationalDate Date when the train operational status is valid, in the format yyyymmdd.
     */
    Subway (String identification, int unitNumber, int numberOfCars, int passengersPerCar,
            String operationalStatus, String operationalDate){
        super(identification, unitNumber);

        this.numberOfCars = numberOfCars;
        this.passengersPerCar = passengersPerCar;
        this.operationalStatus = operationalStatus;
        this.operationalDate = operationalDate;
    }

    //accessor methods

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public int getPassengersPerCar() {
        return passengersPerCar;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public String getOperationalDate() {
        return operationalDate;
    }

    /**
     * Implements getCapacity from Vehicle.
     * @return the total capacity of the vehicle.
     */
    public int getCapacity() {
        return numberOfCars * passengersPerCar;
    }

    /**
     * Determines if a subway is functional.
     * @return boolean true if subway object is known to be in service.
     */
    public boolean functional() {
        return operationalStatus.equals("A");
    }

    //behaviour methods

    /**
     * Overrides equals methods in Vehicle class.
     * @param otherObject another object to be tested for equality.
     * @return boolean true or false whether subway objects are equal.
     */
    public boolean equals (Object otherObject){
        if (!(otherObject instanceof Subway))
            return false;
        else {
            Subway otherSubway = (Subway) otherObject;
            return (getIdentification().equals(otherSubway.getIdentification()) &&
                    getUnitNumber() == otherSubway.getUnitNumber() &&
                    this.numberOfCars == otherSubway.getNumberOfCars() &&
                    this.passengersPerCar == otherSubway.getPassengersPerCar() &&
                    this.operationalStatus.equals(otherSubway.getOperationalStatus()) &&
                    this.operationalDate.equals(otherSubway.getOperationalDate()));
        }
    }

    /**
     * Implements method from Vehicle.
     * @return a String with all the information about the vehicle object.
     */
    public String toString(){
        return getUnitNumber() + "," + getIdentification() + "," + numberOfCars + "," + passengersPerCar + "," +
                operationalStatus + "," + operationalDate + "," + getCapacity();
    }
}

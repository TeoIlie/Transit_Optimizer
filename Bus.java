import java.util.Comparator;

/**
Bus class holds information about bus objects.
 */
public class Bus extends Vehicle {

    //additional attributes other than ones in Vehicle
    private int capacity;
    public static Comparator<Bus> busComparator = new Comparator<Bus>(){
        public int compare(Bus s1, Bus s2) {
            return Integer.compare(s1.getCapacity(), s2.getCapacity());
        }
    };

    //constructor

    /**
     * Bus objects all have the following attributes.
     * @param identification A 5-character string with the format MBnnn, where nnn is in the range 100-999.
     * @param unitNumber A unique 4-digit number that identifies a vehicle in the TTC’s Computerized Information System.
     * @param capacity A 2-digit number indicating the maximum number of passengers that the bus can accommodate.
     */
    Bus (String identification, int unitNumber, int capacity) {
        super(identification, unitNumber);
        this.capacity = capacity;
    }

    //accessor methods
    /**
     * Implements getCapacity from Vehicle.
     * @return the total capacity of the vehicle.
     */
    public int getCapacity () {
        return this.capacity;
    }

    //behaviour methods
    /**
     * Overrides equals methods in Vehicle class.
     * @param otherObject another object to be tested for equality.
     * @return boolean true or false whether subway objects are equal.
     */
    public boolean equals(Object otherObject){
        if (!(otherObject instanceof Bus))
            return false;
        else {
            Bus otherBus = (Bus) otherObject;
            return (getIdentification().equals(otherBus.getIdentification()) &&
                    getUnitNumber() == otherBus.getUnitNumber() &&
                    this.capacity == otherBus.getCapacity());
        }
    }

    /**
     * Implements method from Vehicle.
     * @return a String with all the information about the vehicle object.
     */
    public String toString() {
        return getUnitNumber() + "," + getIdentification() + "," + capacity;
    }
}

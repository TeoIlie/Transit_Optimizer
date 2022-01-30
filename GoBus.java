import java.util.Comparator;

/**
GoBus class holds information about goBus objects.
 */
public class GoBus extends Vehicle {

    //additional attributes other than ones in Vehicle
    private int capacity;
    public static Comparator<GoBus> goBusComparator = new Comparator<GoBus>(){
        public int compare(GoBus s1, GoBus s2) {
            return Integer.compare(s1.getCapacity(), s2.getCapacity());
        }
    };
    //constructor

    /**
     * GoBus objects all are instantiated with the following attributes.
     * @param identification  A 5-character string with the format GBnnn, where nnn is in the range 100-999.
     * @param unitNumber A unique 4-digit number that identifies a vehicle in the TTC’s Computerized Information System.
     * @param capacity A 2-digit number indicating the maximum number of passengers that the bus can accommodate.
     */
    GoBus (String identification, int unitNumber, int capacity) {
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
        if (!(otherObject instanceof GoBus))
            return false;
        else {
            GoBus otherGoBus = (GoBus) otherObject;
            return (getIdentification().equals(otherGoBus.getIdentification()) &&
                    getUnitNumber() == otherGoBus.getUnitNumber() &&
                    this.capacity == otherGoBus.getCapacity());
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

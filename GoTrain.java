import java.util.Comparator;

/**
 GoTrain class holds information about go train units. It inherits Vehicle.
 */
public class GoTrain extends Vehicle {

    private int capacity;
    public static Comparator<GoTrain> goTrainComparator = new Comparator<GoTrain>(){
        public int compare(GoTrain s1, GoTrain s2) {
            return Integer.compare(s1.getCapacity(), s2.getCapacity());
        }
    };

    //constructor

    /**
     * GoTrain objects are instantiated with the following parameters.
     * @param identification A 4-character string with the format Gnnn, where nnn is in the range 100-999.
     * @param unitNumber A unique 4-digit number that identifies a vehicle in the TTC’s Computerized Information System.
     * @param capacity A 2- to 3-digit number indicating the train’s maximum passenger capacity.
     */
    GoTrain (String identification, int unitNumber, int capacity) {
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
        if (!(otherObject instanceof GoTrain))
            return false;
        else {
            GoTrain otherGoTrain = (GoTrain) otherObject;
            return (getIdentification().equals(otherGoTrain.getIdentification()) &&
                    getUnitNumber() == otherGoTrain.getUnitNumber() &&
                    this.capacity == otherGoTrain.getCapacity());
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

import java.util.Comparator;

/**
 Streetcar class holds information about streetcar objects,
 */
public class Streetcar extends Vehicle {

    //additional attributes other than Vehicle
    private String type;
    public static Comparator<Streetcar> streetcarComparator = new Comparator<Streetcar>(){
        public int compare(Streetcar s1, Streetcar s2) {
            return Integer.compare(s1.getCapacity(), s2.getCapacity());
        }
    };

    //constructor

    /**
     * Streetcar objects instantiated with the following attributes.
     * @param identificationTemp A 5-character string with the format SCnnn, where nnn is in the range 100-999.
     * @param unitNumberTemp A unique 4-digit number that identifies a vehicle in the TTC’s Computerized Information System.
     * @param type A 1-character field indicating a single (S) or double (D) section streetcar.
     *             (Note: each section of the street car can accommodate a maximum of 40 passengers).
     */
    Streetcar(String identificationTemp, int unitNumberTemp, String type) {
        super(identificationTemp, unitNumberTemp);
        this.type = type;
    }

    //accessor methods
    public String getType() {
        return this.type;
    }

    /**
     * Implements getCapacity from Vehicle.
     * @return the total capacity of the vehicle.
     */
    public int getCapacity() {
        if (type.equals("S"))
            return 40;
        if (type.equals("D"))
            return 80;
        return 0;
    }

    //behaviour methods
    /**
     * Overrides equals methods in Vehicle class.
     * @param otherObject another object to be tested for equality.
     * @return boolean true or false whether subway objects are equal.
     */
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Streetcar))
            return false;
        else {
            Streetcar otherStreetcar = (Streetcar) otherObject;
            return (getIdentification().equals(otherStreetcar.getIdentification()) &&
                    getUnitNumber() == otherStreetcar.getUnitNumber() &&
                    this.type.equals(otherStreetcar.getType()));
        }
    }

    /**
     * Implements method from Vehicle.
     * @return a String with all the information about the vehicle object.
     */
    public String toString() {
        return getUnitNumber() + "," + getIdentification() + "," + type + "," + getCapacity();
    }
}

/**
Vehicle class is abstract, inherited by Subway, GoTrain, Bus, GoBus and Streetcar.
 */
public abstract class Vehicle {

    //Attributes

    private String identification;
    private int unitNumber;

    //Constructor

    /**
     * Vehicle objects are instantiated with the following attributes, as all five types share these.
     * @param identificationTemp an identification number.
     * @param unitNumberTemp a unit number.
     */
    Vehicle (String identificationTemp, int unitNumberTemp) {
        identification = identificationTemp;
        unitNumber = unitNumberTemp;
    }

    //Accessor methods

    public int getUnitNumber(){ return unitNumber; }

    public String getIdentification() { return identification; }

    //Abstract methods

    /**
     * Enforce all classes inheriting Vehicle to write a getCapacity method.
     * @return the capacity of a vehicle.
     */
    public abstract int getCapacity();

    /**
     * Enforce all classes that inherit Vehicle to overwrite the Object class method toString.
     * @return a String with the information about a vehicle.
     */
    public abstract String toString();

    //Behaviour methods

    /**
     * Overrides toString method in Object class.
     * @return a String with all the information about the vehicle object.
     */
    public boolean equals (Object otherObject) {
        if (!(otherObject instanceof Vehicle))
            return false;
        else {
            Vehicle otherVehicle = (Vehicle) otherObject;
            return (identification.equals(otherVehicle.getIdentification()) &&
                    unitNumber == otherVehicle.getUnitNumber());
        }
    }
}

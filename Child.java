/**
Child inherits the passenger class.
 */
public class Child extends Passenger {

    //constructor
    /**
     * Objects have the following attributes.
     * @param identification see Passenger.
     * @param modality see Passenger.
     * @param hourOfTheDay see Passenger.
     * @param date see Passenger.
     */
    Child (String identification, String modality, int hourOfTheDay, int date) {
        super(identification, modality, hourOfTheDay, date);
    }

    //behaviour methods
    /**
     * Implements toString from Passenger.
     * @return returns a String with all the information about a passenger.
     */
    public String toString(){
        return getIdentification() + "," + getModality() + ",C," + getHourOfTheDay() + "," + getDate();
    }

    /**
     * Implement equals.
     * @param otherObject another object for comparison.
     * @return boolean if passengers are the same.
     */
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Child))
            return false;
        else {
            Child otherChild = (Child) otherObject;
            return (getIdentification().equals(otherChild.getIdentification()) &&
                    getModality().equals(otherChild.getModality()) && getHourOfTheDay() == otherChild.getHourOfTheDay()
                    && getDate() == otherChild.getDate());
        }
    }

}

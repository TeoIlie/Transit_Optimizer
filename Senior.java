/**
Senior inherits the passenger class.
 */
public class Senior extends Passenger {

    //constructor
    /**
     * Objects have the following attributes.
     * @param identification see Passenger.
     * @param modality see Passenger.
     * @param hourOfTheDay see Passenger.
     * @param date see Passenger.
     */
    Senior (String identification, String modality, int hourOfTheDay, int date) {
        super(identification, modality, hourOfTheDay, date);
    }

    //behaviour methods
    /**
     * Implements toString from Passenger.
     * @return returns a String with all the information about a passenger.
     */
    public String toString(){
        return getIdentification() + "," + getModality() + ",S," + getHourOfTheDay() + "," + getDate();
    }

    /**
     * Implement equals.
     * @param otherObject another object for comparison.
     * @return boolean if passengers are the same.
     */
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Senior))
            return false;
        else {
            Senior other = (Senior) otherObject;
            return (getIdentification().equals(other.getIdentification()) &&
                    getModality().equals(other.getModality()) && getHourOfTheDay() == other.getHourOfTheDay()
                    && getDate() == other.getDate());
        }
    }

}

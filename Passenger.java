/**
Passenger class is the base abstract class that child, adult, and senior inherits.
 */
public abstract class Passenger {

    //Attributes

    private String identification;
    private String modality;
    private int hourOfTheDay;
    private int date;

    //Constructor

    /**
     * Passenger objects instantiated with following attributes.
     * @param identification A 7-digit card identification number for a rider using a monthly pass,
     *                       or a 14-character ticket code (whose format is: Tyymmddnnnnnnn,
     *                       where yy=year, mm=month, dd=day, nnnnnnn=unique 7-digit number) for a
     *                       rider using a 10- trip discount ticket, or a “*” for a casual rider
     *                       using tokens or paying cash fares.
     * @param modality  A one-character field indicating the means of transportation used by the rider,
     *                 with values as follows: (S) Subway, (G) GO-train, (X) Streetcars, (C) City bus, (D) GO-Bus.
     * @param hourOfTheDay A 1- or 2-digit number (1 – 24) indicating the hour of the day when the rider used
     *                     the specified transportation modality.
     * @param date The date when the rider used the specified transportation modality in the format yyyymmdd,
     *             where yyyy is 2018 or 2019, mm is in the range 01-12, and dd is in the range 00-31.
     */
    Passenger(String identification, String modality, int hourOfTheDay, int date) {
        this.identification = identification;
        this.modality = modality;
        this.hourOfTheDay = hourOfTheDay;
        this.date = date;
    }

    //Accessor methods

    public String getIdentification() {
        return identification;
    }

    public String getModality() {
        return modality;
    }

    public int getHourOfTheDay() {
        return hourOfTheDay;
    }

    public int getDate() {
        return date;
    }

    //Abstract methods

    /**
     * Enforces subclasses to overwrite toString method of class Object.
     * @return a String with passenger info.
     */
    public abstract String toString();

    /**
     * Enforce subclasses to implement equals method.
     * @param otherObject another object for comparison.
     * @return a boolean true if the passengers are equal.
     */
    public abstract boolean equals(Object otherObject);

}

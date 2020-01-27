import java.io.Serializable;

/**
 * Thi class makes both a default storm object and a complete storm object that takes 4 paramaters. Methods needed
 * to edit or check what the storms variables are are also provided here.
 *
 * @author Michael Fasano
 * michael.fasano@stonybrook.edu
 * 110798138
 */
public class Storm implements Serializable {
    private String name;
    private double precipitation;
    private double windSpeed;
    private String Date;

    /**
     * This creates a default storm class with no paramaters being set
     */
    public Storm() {

    }

    /**
     * This creates a storm class and is passed all the parameters that it will use.
     *
     * @param name
     * @param precipitation
     * @param windSpeed
     * @param Date
     */
    public Storm(String name, double precipitation, double windSpeed, String Date) {
        this.name = name;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
        this.Date = Date;
    }

    /**
     * This method returns the name of the storm
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * This method takes a string  and sets the name of the storm to that name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns a double that represents the level of precipitation for the storm
     *
     * @return double
     */
    public double getPrecipitation() {
        return precipitation;
    }

    /**
     * This method sets the precipitation of the storm and takes a double parameter to do so.
     *
     * @param precipitation
     */
    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * This method returns a double representing the windspeeds of the storm
     *
     * @return double
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * This method sets the windspeed levels of the storm and takes a double parameter to do so
     *
     * @param windSpeed
     */
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * This method returns a string that represents the date of the storm
     *
     * @return String
     */
    public String getDate() {
        return Date;
    }

    /**
     * This method takes a string paramter and sets the date of the storm to that
     *
     * @param date
     */
    public void setDate(String date) {
        Date = date;
    }
}


import java.util.Comparator;

/**
 * This method has a default windspeed comparator object built in and has the method necessary for comparison as well
 *
 * @author Michael Fasano
 * michael.fasano@stonybrooke.edu
 * 110798138
 */
public class WindSpeedComparator implements Comparator<Storm> {
    public int compare(Storm left, Storm right) {
        return Double.compare(left.getWindSpeed(), right.getWindSpeed());
    }
}

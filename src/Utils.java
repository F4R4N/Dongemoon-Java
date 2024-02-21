import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {
    public static SimpleDateFormat dateAndTimeParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static boolean isStringEmptyOrNull(String string) {
        return string == null || string.isEmpty();
    }

    public static int minOfTwoIntegers(int firstInteger, int secondInteger) {
        if (firstInteger < secondInteger) {
            return firstInteger;
        }
        return secondInteger;
    }

    public static Date getDateByDateString(String startDateAndTimeInput) {
        try {
            dateAndTimeParser.setLenient(false);
            Date dateAndTime = dateAndTimeParser.parse(startDateAndTimeInput);
            Date now = new Date();
            Date oldestValidDate = dateAndTimeParser.parse("1974-1-1 00:00");
            if (dateAndTime.after(now) || dateAndTime.before(oldestValidDate)) {
                return null;
            }
            return dateAndTime;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Person getPersonWithMinNet(HashMap<Person, Integer> personsNet) {
        Optional<Person> firstPerson = personsNet.keySet().stream().findFirst();
        if (firstPerson.isPresent()) {
            Person minNetPerson = firstPerson.get();
            for (Map.Entry<Person, Integer> entry : personsNet.entrySet()) {
                if (entry.getValue() < personsNet.get(minNetPerson)) {
                    minNetPerson = entry.getKey();
                }
            }
            return minNetPerson;
        } else {
            return null;
        }
    }

    public static Person getPersonWithMaxNet(HashMap<Person, Integer> personsNet) {
        Optional<Person> firstPerson = personsNet.keySet().stream().findFirst();
        if (firstPerson.isPresent()) {
            Person maxNetPerson = firstPerson.get();
            for (Map.Entry<Person, Integer> entry : personsNet.entrySet()) {
                if (entry.getValue() > personsNet.get(maxNetPerson)) {
                    maxNetPerson = entry.getKey();
                }
            }
            return maxNetPerson;
        } else {
            return null;
        }
    }
}

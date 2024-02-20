import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {
    public static boolean isStringEmptyOrNull(String string) {
        return string == null || string.isEmpty();
    }

    public static int minOfTwoIntegers(int firstInteger, int secondInteger) {
        if (firstInteger < secondInteger) {
            return firstInteger;
        }
        return secondInteger;
    }

    public static Person getPersonWithMinNet(HashMap<Person, Integer> personsNet) {
        Optional<Person> firstPerson = personsNet.keySet().stream().findFirst();
        if (firstPerson.isPresent()) {
            Person minNetPerson =firstPerson.get();
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

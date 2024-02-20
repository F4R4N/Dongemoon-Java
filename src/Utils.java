import java.util.HashMap;
import java.util.Map;

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
        Person minNetPerson = (Person) personsNet.keySet().toArray()[0];
        for (Map.Entry<Person, Integer> entry : personsNet.entrySet()) {
            if (entry.getValue() < personsNet.get(minNetPerson)) {
                minNetPerson = entry.getKey();
            }
        }
        return minNetPerson;
    }

    public static Person getPersonWithMaxNet(HashMap<Person, Integer> personsNet) {
        Person maxNetPerson = (Person) personsNet.keySet().toArray()[0];
        for (Map.Entry<Person, Integer> entry : personsNet.entrySet()) {
            if (entry.getValue() > personsNet.get(maxNetPerson)) {
                maxNetPerson = entry.getKey();
            }
        }
        return maxNetPerson;
    }
}

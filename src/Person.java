import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable{
    private String name;

    public Person(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Person> getAllExistingPersons(){
        ArrayList<Person> allPersons = new ArrayList<Person>();
        if (User.getLoggedInUser().getPeriods()==null) {
            return allPersons;
        }
        for (int index = 0; index < User.getLoggedInUser().getPeriods().size(); index++) {
            Period period = User.getLoggedInUser().getPeriods().get(index);
            for (int i = 0; i < period.getPersons().size(); i++) {
                if (!allPersons.contains(period.getPersons().get(i))) {
                    allPersons.add(period.getPersons().get(i));
                }
            }
        }
        return allPersons;
    }

    public static Person getOrCreatePerson(String personInput){ //TODO POSSIBLE DELETE
        Person person;
        for (int i = 0; i < Person.getAllExistingPersons().size(); i++) {
            if (Person.getAllExistingPersons().get(i).getName().equals(personInput)) {
                person = Person.getAllExistingPersons().get(i);
                return person;
            }
        }
        person = new Person(personInput);
        return person;
    }

    public static void printPersons(ArrayList<Person> persons) {
        if (persons.size()==0) {
            System.out.println("No person exist yet");
        } else {
            String format = "|%-10s  |%-30s|%n";
            System.out.printf(format, "NO.", "Name");
            System.out
                    .print(String.format("|%012d|%030d|%n", 0, 0)
                            .replace("0", "="));
            for (int index = 0; index < persons.size(); index++) {
                Person person = persons.get(index);
                System.out.printf(format, (index + 1), person.getName());
            }
        }
    }

    public static boolean isInvalidIndex(ArrayList<Person> persons, int personIndexInput) {
        return personIndexInput > persons.size() - 1 || personIndexInput < 0;
    }

    public static boolean isNameDuplicated(ArrayList<Person> persons, String name){
        for (int index = 0; index < persons.size(); index++) {
            if (persons.get(index).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}

import java.util.ArrayList;
public class Person {
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

    public static Person addOrCreatePerson(String personInput){
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
}

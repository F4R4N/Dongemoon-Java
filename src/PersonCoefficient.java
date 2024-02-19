import java.util.ArrayList;

public class PersonCoefficient {
    private Person person;
    private int coefficient;

    public PersonCoefficient(Person person, int coefficient) {
        this.person = person;
        this.coefficient = coefficient;
    }
    public int getCoefficient() {
        return coefficient;
    }
    public Person getPerson() {
        return person;
    }
    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    public static void printPersonCoefficients(ArrayList<PersonCoefficient> persons) {
        if (persons.size()==0) {
            System.out.println("No person exist yet");
        } else {
            String format = "|%-10s  |%-30s|%-11s|%n";
            System.out.printf(format, "NO.", "Name", "Coefficient");
            System.out
                    .print(String.format("|%012d|%030d|%011d|%n", 0, 0,0)
                            .replace("0", "-"));
            for (int index = 0; index < persons.size(); index++) {
                PersonCoefficient person = persons.get(index);
                System.out.printf(format, (index + 1), person.getPerson().getName(), person.getCoefficient());
            }
        }
    }

    public static boolean doesPersonExistInPurchaseUsers(Purchase purchase, Person person){
        for (int i = 0; i < purchase.getPurchaseUsers().size(); i++) {
            if (purchase.getPurchaseUsers().get(i).getPerson() == person) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInvalidIndex(ArrayList<PersonCoefficient> personCoefficients, int personCoefficientIndexInput) {
        return personCoefficientIndexInput > personCoefficients.size() - 1 || personCoefficientIndexInput < 0;
    }
}

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Period {
    private String name;
    private Date startDate;
    private ArrayList<Person> persons;
    private ArrayList<Purchase> purchases;
    private static SimpleDateFormat dateAndTimeParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Period(String name, Date startDate) {
        this.name = name;
        this.startDate = startDate;
        this.persons = new ArrayList<Person>();
        this.purchases = new ArrayList<Purchase>();
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public static Date getDateByDateString(String startDateAndTimeInput) {
        try {
            dateAndTimeParser.setLenient(false);
            Date dateAndTime = dateAndTimeParser.parse(startDateAndTimeInput);
            return dateAndTime;
        } catch (ParseException e) {
            return null;
        }
    }

    public static void printListOfPeriods(ArrayList<Period> periods) {
        if (periods.size() == 0) {
            System.out.println("No period exist yet.");
        } else {
            String format = "|%-10s  |%-25s|%-35s|%n";
            System.out.printf(format, "NO.", "Name", "Start Date");
            System.out
                    .print(String.format("|%012d|%025d|%035d|%n", 0, 0, 0)
                            .replace("0", "-"));
            for (int index = 0; index < periods.size(); index++) {
                Period period = periods.get(index);
                System.out.printf(format, (index + 1), period.getName(), period.getStartDate());
            }
        }
    }
    public static void printPeriods(ArrayList<Period> periods) {
        if (periods.size() == 0) {
            System.out.println("No period exist yet.");
        } else {
            String format = "|%-10s  |%-25s|%-35s|%n";
            System.out.printf(format, "NO.", "Name", "Start Date");
            System.out
                    .print(String.format("|%012d|%025d|%035d|%n", 0, 0, 0)
                            .replace("0", "-"));
            for (int index = 0; index < periods.size(); index++) {
                Period period = periods.get(index);
                System.out.printf(format, (index + 1), period.getName(), period.getStartDate());
                Person.printPersons(period.getPersons());
                UI.printPurchasesInPeriod(period);
                System.out.println(
                        "---------------------------------------------------------------------------------------------------------");
            }
        }
    }

    public int getTotalExpenses() {
        int expenses = 0;
        for (int index = 0; index < this.purchases.size(); index++) {
            expenses += this.getPurchases().get(index).getExpense();
        }
        return expenses;
    }

    public static void printPeriodDetail(Period period) {
        UI.printTitle("'" + period.getName() + "'s' period detail");
        System.out.println("Start date and time: " + dateAndTimeParser.format(period.getStartDate()));
        System.out.println("Number of persons involved in this period: " + period.persons.size());
        System.out.println("Total Expenses: " + period.getTotalExpenses());
        System.out.println("Number of purchases in period: " + period.getPurchases().size());
        System.out.println("Each persons average expense: "+ period.getOverallPersonAverageExpense());
        HashMap<Person, Integer> personDirectExpenses = period.getPersonsDirectExpenses();
        printPersonsDirectExpenses(period, personDirectExpenses);


    }

    public double getOverallPersonAverageExpense(){
        return this.getTotalExpenses() / this.getPurchases().size();
    }

    public HashMap<Person, Integer> getPersonsDirectExpenses() {
        HashMap<Person, Integer> personsAverageExpenses = new HashMap<Person, Integer>();
        for (int index = 0; index < this.purchases.size(); index++) {
            Purchase purchase = this.purchases.get(index);
            personsAverageExpenses.put(purchase.getBuyer(),
                    personsAverageExpenses.getOrDefault(purchase.getBuyer(), 0) + purchase.getExpense());
        }
        return personsAverageExpenses;
    }

    // TODO: TEST WITH PERSON AN PURCHASES
    public static void printPersonsDirectExpenses(Period period, HashMap<Person, Integer> personAverageExpenses) {
        System.out.println("Persons average expenses in '" + period.getName() + "'s' period.");
        for (Map.Entry<Person, Integer> entry : personAverageExpenses.entrySet()) {
            Person person = entry.getKey();
            Integer directExpense = entry.getValue();
            System.out.println("'" + person.getName() + "' has direct expense of: '" + directExpense + "'");
        }
    }

    public static boolean isInvalidIndex(ArrayList<Period> periods, int periodIndexInput) {
        return periodIndexInput > periods.size() - 1 || periodIndexInput < 0;
    }

    public static boolean isNameDuplicated(String name) {
        ArrayList<Period> periods = User.getLoggedInUser().getPeriods();
        for (int i = 0; i < periods.size(); i++) {
            if (periods.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}

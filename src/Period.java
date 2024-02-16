import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public int getTotalExpenses(){
        int expenses = 0;
        for (int index = 0; index < this.purchases.size(); index++) {
            expenses += this.purchases.get(index).getExpense();
        }
        return expenses;
    }

    public static void printPeriodDetail(Period period){
        UI.printTitle("'"+period.getName()+"'s' period detail");
        System.out.println("Start date and time"+dateAndTimeParser.format(period.getStartDate()));
        System.out.println("Number of persons involved in this period: "+period.persons.size());
        System.out.println("Total Expenses: "+ period.getTotalExpenses());
        System.out.println("Number of purchases in period: "+ period.getPurchases().size());
    }
}

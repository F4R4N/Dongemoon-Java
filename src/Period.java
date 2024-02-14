import java.util.ArrayList;
import java.util.Date;

public class Period {
    private String name;
    private Date startDate;
    private ArrayList<Person> persons;
    private ArrayList<Purchase> purchases;
    private User owner;

    public Period(String name, Date startDate, User owner) {
        this.name = name;
        this.startDate = startDate;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
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
}

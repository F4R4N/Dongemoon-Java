import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Purchase implements Serializable{
    private String title;
    private int expense;
    private Person buyer;
    private Date dateAndTime;
    private ArrayList<PersonCoefficient> consumers;

    public Purchase(String title, int expense, Person buyer, ArrayList<PersonCoefficient> consumers,
            Date dateAndTime) {
        this.title = title;
        this.expense = expense;
        this.buyer = buyer;
        this.dateAndTime = dateAndTime;
        this.consumers = consumers;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public String getTitle() {
        return title;
    }

    public int getExpense() {
        return expense;
    }

    public Person getBuyer() {
        return buyer;
    }

    public ArrayList<PersonCoefficient> getConsumers() {
        return consumers;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public void setBuyer(Person buyer) {
        this.buyer = buyer;
    }

    public void setConsumers(ArrayList<PersonCoefficient> consumers) {
        this.consumers = consumers;
    }

    public void addToConsumers(PersonCoefficient personCoefficient) {
        this.consumers.add(personCoefficient);
    }

    public void removeFromConsumers(PersonCoefficient personCoefficient) {
        this.consumers.remove(personCoefficient);
    }

    public static void printListOfPurchases(ArrayList<Purchase> purchases) {
        if (purchases.size() == 0) {
            System.out.println("No purchases exists yet.");
        } else {
            String format = "|%-10s  |%-30s|%-35s|%-15s|%-20s|%-57s|%n";
            String dataFormat = "|%-10s  |%-30s|%-35s|%-15s|%-20s|%-57s";
            System.out.printf(format, "NO.", "Title", "Date and Time", "Expense", "Buyer", "Consumers");
            System.out
                    .print(String.format("|%012d|%030d|%035d|%015d|%020d|%057d|%n", 0, 0, 0, 0, 0,0)
                            .replace("0", "="));
            for (int index = 0; index < purchases.size(); index++) {
                Purchase purchase = purchases.get(index);
                System.out.printf(dataFormat, (index + 1), purchase.getTitle(), purchase.getDateAndTime(),
                purchase.getExpense(), purchase.getBuyer().getName(), PersonCoefficient.getPersonCoefficientTableForPurchase(purchase.getConsumers()));
                System.out.print(String.format("|%012d|%030d|%035d|%015d|%020d|%057d|%n", 0, 0, 0, 0, 0, 0).replace("0", "-"));
            }
        }
    }
    
    public static boolean isInvalidIndex(ArrayList<Purchase> purchases, int purchaseIndexInput) {
        return purchaseIndexInput > purchases.size() - 1 || purchaseIndexInput < 0;
    }

    public static boolean isTitleDuplicated(Period period, String name) {
        ArrayList<Purchase> purchases = period.getPurchases();
        for (int i = 0; i < purchases.size(); i++) {
            if (purchases.get(i).getTitle().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Purchase> getBuyerFilteredPurchases(Period period, Person person) {
        ArrayList<Purchase> filteredPurchases = new ArrayList<Purchase>();
        for (int i = 0; i < period.getPurchases().size(); i++) {
            Purchase purchase = period.getPurchases().get(i);
            if (purchase.getBuyer() == person) {
                filteredPurchases.add(purchase);
            }
        }
        return filteredPurchases;
    }

    public static ArrayList<Purchase> getConsumersFilteredPurchases(Period period, Person person) {
        ArrayList<Purchase> filteredPurchases = new ArrayList<Purchase>();
        for (int i = 0; i < period.getPurchases().size(); i++) {
            Purchase purchase = period.getPurchases().get(i);
            for (int j = 0; j < purchase.getConsumers().size(); j++) {
                PersonCoefficient consumer = purchase.getConsumers().get(j);
                if (consumer.getPerson() == person) {
                    filteredPurchases.add(purchase);
                }
            }
        }
        return filteredPurchases;
    }

    public static ArrayList<Purchase> clonePurchases(ArrayList<Purchase> purchases){
        ArrayList<Purchase> clonedPurchases = new ArrayList<Purchase>();
        for (int index = 0; index < purchases.size(); index++) {
            clonedPurchases.add(new Purchase(purchases.get(index).getTitle(), purchases.get(index).getExpense(), purchases.get(index).getBuyer(), purchases.get(index).getConsumers(), purchases.get(index).getDateAndTime()));
        }
        return clonedPurchases;
    }

    public int calculateConsumerShare(PersonCoefficient consumer){
        int coefficientSum = 0;
        for (int index = 0; index < this.consumers.size(); index++) {
            coefficientSum += this.consumers.get(index).getCoefficient();
        }
        int eachCoefficientExpense = this.getExpense() / coefficientSum;
        return consumer.getCoefficient() * eachCoefficientExpense;
    }
}

import java.util.ArrayList;
import java.util.Date;

public class Purchase {
    private String title;
    private int expense;
    private Person buyer;
    private Date dateAndTime;
    private ArrayList<PersonCoefficient> purchaseUsers;

    public Purchase(String title, int expense, Person buyer, ArrayList<PersonCoefficient> purchaseUsers,
            Date dateAndTime) {
        this.title = title;
        this.expense = expense;
        this.buyer = buyer;
        this.dateAndTime = dateAndTime;
        this.purchaseUsers = purchaseUsers;
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

    public ArrayList<PersonCoefficient> getPurchaseUsers() {
        return purchaseUsers;
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

    public void setPurchaseUsers(ArrayList<PersonCoefficient> purchaseUsers) {
        this.purchaseUsers = purchaseUsers;
    }

    public void addToPurchaseUsers(PersonCoefficient personCoefficient) {
        this.purchaseUsers.add(personCoefficient);
    }

    public void removeFromPurchaseUsers(PersonCoefficient personCoefficient) {
        this.purchaseUsers.remove(personCoefficient);
    }

    public static void printListOfPurchases(ArrayList<Purchase> purchases) {
        if (purchases.size() == 0) {
            System.out.println("No purchases exists yet.");
        } else {
            String format = "|%-10s  |%-30s|%-35s|%-15s|%-20s|%n";
            System.out.printf(format, "NO.", "Title", "Date and Time", "Expense", "Buyer");
            System.out
                    .print(String.format("|%012d|%030d|%035d|%015d|%020d|%n", 0, 0, 0, 0, 0)
                            .replace("0", "-"));
            for (int index = 0; index < purchases.size(); index++) {
                Purchase purchase = purchases.get(index);
                System.out.printf(format, (index + 1), purchase.getTitle(), purchase.getDateAndTime(),
                purchase.getExpense(), purchase.getBuyer().getName());
                System.out.println("Purchase Users: ");
                PersonCoefficient.printPersonCoefficients(purchase.getPurchaseUsers());
                System.out.print(String.format("|%013d%031d%036d%016d%020d|%n", 0, 0, 0, 0, 0).replace("0", "-"));
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

    public static ArrayList<Purchase> getPurchaseUsersFilteredPurchases(Period period, Person person) {
        ArrayList<Purchase> filteredPurchases = new ArrayList<Purchase>();
        for (int i = 0; i < period.getPurchases().size(); i++) {
            Purchase purchase = period.getPurchases().get(i);
            for (int j = 0; j < purchase.getPurchaseUsers().size(); j++) {
                PersonCoefficient purchaseUser = purchase.getPurchaseUsers().get(j);
                if (purchaseUser.getPerson() == person) {
                    filteredPurchases.add(purchase);
                }
            }
        }
        return filteredPurchases;
    }

    public static ArrayList<Purchase> clonePurchases(ArrayList<Purchase> purchases){
        ArrayList<Purchase> clonedPurchases = new ArrayList<Purchase>();
        for (int index = 0; index < purchases.size(); index++) {
            clonedPurchases.add(new Purchase(purchases.get(index).getTitle(), purchases.get(index).getExpense(), purchases.get(index).getBuyer(), purchases.get(index).getPurchaseUsers(), purchases.get(index).getDateAndTime()));
        }
        return clonedPurchases;
    }
}

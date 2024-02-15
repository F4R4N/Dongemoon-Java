import java.util.ArrayList;

public class Purchase {
    private String title;
    private int expense;
    private Person buyer;
    private ArrayList<PersonCoefficient> purchaseUsers;

    public Purchase(String title, int expense, Person buyer, ArrayList<PersonCoefficient> purchaseUsers) {
        this.title = title;
        this.expense = expense;
        this.buyer = buyer;
        this.purchaseUsers = purchaseUsers;
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
}

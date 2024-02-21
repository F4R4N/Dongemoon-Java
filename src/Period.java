import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Period implements Serializable {
    private String name;
    private Date startDateAndTime;
    private ArrayList<Person> persons;
    private ArrayList<Purchase> purchases;
    private HashMap<Person, HashMap<Person, Integer>> payments = new HashMap<Person, HashMap<Person, Integer>>();

    public Period(String name, Date startDateAndTime) {
        this.name = name;
        this.startDateAndTime = startDateAndTime;
        this.persons = new ArrayList<Person>();
        this.purchases = new ArrayList<Purchase>();
    }

    public HashMap<Person, HashMap<Person, Integer>> getPayments() {
        return payments;
    }
    public void setPayments(HashMap<Person, HashMap<Person, Integer>> payments) {
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public Date getStartDateAndTime() {
        return startDateAndTime;
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

    public void setStartDateAndTime(Date startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public static void printListOfPeriods(ArrayList<Period> periods) {
        if (periods.size() == 0) {
            System.out.println("No period exist yet.");
        } else {
            String format = "|%-10s  |%-25s|%-35s|%n";
            System.out.printf(format, "NO.", "Name", "Start Date");
            System.out
                    .print(String.format("|%012d|%025d|%035d|%n", 0, 0, 0)
                            .replace("0", "="));
            for (int index = 0; index < periods.size(); index++) {
                Period period = periods.get(index);
                System.out.printf(format, (index + 1), period.getName(), period.getStartDateAndTime());
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
                            .replace("0", "="));
            for (int index = 0; index < periods.size(); index++) {
                Period period = periods.get(index);
                System.out.printf(format, (index + 1), period.getName(), period.getStartDateAndTime());
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

    public void printPeriodDetail() {
        UI.printTitle("'" + this.getName() + "'s' period detail");
        System.out.println("Start date and time: " + Utils.dateAndTimeParser.format(this.getStartDateAndTime()));
        System.out.println("Number of persons in this period: " + this.persons.size());
        System.out.println("Total Expenses: " + this.getTotalExpenses());
        System.out.println("Number of purchases in period: " + this.getPurchases().size());
        System.out.println("Each persons average expense: " + this.getOverallPersonAverageExpense());
        HashMap<Person, Integer> personDirectExpenses = this.getPersonsDirectExpenses();
        UI.printPersonsDirectExpenses(this, personDirectExpenses);
        this.printPeriodDebtsAndCredits();
        UI.printTitle("List Of Purchases");
        Purchase.printListOfPurchases(this.getPurchases());
    }

    public ArrayList<Person> getPersonsInvolvedInPurchases() {
        ArrayList<Person> personInvolvedInPurchases = new ArrayList<Person>();
        for (int index = 0; index < this.getPersons().size(); index++) {
            if (isPersonInvolvedInPurchases(this, this.getPersons().get(index))) {
                personInvolvedInPurchases.add(this.getPersons().get(index));
            }
        }
        return personInvolvedInPurchases;
    }

    public double getOverallPersonAverageExpense() {
        if (this.getPurchases().size() == 0) {
            return 0;
        } else {
            return this.getTotalExpenses() / this.getPersonsInvolvedInPurchases().size();
        }
    }

    public HashMap<Person, Integer> getPersonsDirectExpenses() {
        HashMap<Person, Integer> personsDirectExpenses = new HashMap<Person, Integer>();
        for (int index = 0; index < this.purchases.size(); index++) {
            Purchase purchase = this.purchases.get(index);
            Integer personExpenseSum = personsDirectExpenses.getOrDefault(purchase.getBuyer(), 0)
                    + purchase.getExpense();
            personsDirectExpenses.put(purchase.getBuyer(), personExpenseSum);
            for (int i = 0; i < purchase.getConsumers().size(); i++) {
                PersonCoefficient consumer = purchase.getConsumers().get(i);
                Integer consumerExpenseSum = personsDirectExpenses.getOrDefault(consumer.getPerson(), 0);
                personsDirectExpenses.put(consumer.getPerson(), consumerExpenseSum);
            }
        }
        return personsDirectExpenses;
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

    public static boolean isPersonInvolvedInPurchases(Period period, Person person) {
        boolean isInvolved = false;
        for (int i = 0; i < period.getPurchases().size(); i++) {
            if (period.getPurchases().get(i).getBuyer() == person) {
                isInvolved = true;
            }
            for (int j = 0; j < period.getPurchases().get(i).getConsumers().size(); j++) {
                if (period.getPurchases().get(i).getConsumers().get(j).getPerson() == person) {
                    isInvolved = true;
                }
            }
        }
        return isInvolved;
    }

    public ArrayList<Purchase> getExpenseSortedPurchases() {
        ArrayList<Purchase> clonedPurchases = Purchase.clonePurchases(this.getPurchases());
        for (int i = 0; i < clonedPurchases.size(); i++) {
            int purchaseWithMinExpenseIndex = i;
            for (int j = i; j < clonedPurchases.size(); j++) {
                if (clonedPurchases.get(j).getExpense() < clonedPurchases.get(purchaseWithMinExpenseIndex)
                        .getExpense()) {
                    purchaseWithMinExpenseIndex = j;
                }
            }
            Purchase tempPurchase = clonedPurchases.get(i);
            clonedPurchases.set(i, clonedPurchases.get(purchaseWithMinExpenseIndex));
            clonedPurchases.set(purchaseWithMinExpenseIndex, tempPurchase);
        }
        return clonedPurchases;
    }

    public ArrayList<Purchase> getDateAndTimeSortedPurchases() {
        ArrayList<Purchase> clonedPurchases = Purchase.clonePurchases(this.getPurchases());
        for (int i = 0; i < clonedPurchases.size(); i++) {
            int purchaseWithMinDateAndTimeIndex = i;
            for (int j = i; j < clonedPurchases.size(); j++) {
                if (clonedPurchases.get(j).getDateAndTime()
                        .compareTo(clonedPurchases.get(purchaseWithMinDateAndTimeIndex).getDateAndTime()) < 0) {
                    purchaseWithMinDateAndTimeIndex = j;
                }
            }
            Purchase tempPurchase = clonedPurchases.get(i);
            clonedPurchases.set(i, clonedPurchases.get(purchaseWithMinDateAndTimeIndex));
            clonedPurchases.set(purchaseWithMinDateAndTimeIndex, tempPurchase);
        }
        return clonedPurchases;
    }

    public HashMap<Person, HashMap<Person, Integer>> normalizeDebtorsData() {
        HashMap<Person, HashMap<Person, Integer>> debtorsData = new HashMap<Person, HashMap<Person, Integer>>();
        for (int index = 0; index < this.getPurchases().size(); index++) {
            Purchase purchase = this.getPurchases().get(index);
            for (int i = 0; i < purchase.getConsumers().size(); i++) {
                PersonCoefficient consumer = purchase.getConsumers().get(i);
                HashMap<Person, Integer> creditor = new HashMap<Person, Integer>();
                int consumerShare = purchase.calculateConsumerShare(consumer);
                creditor.put(purchase.getBuyer(), consumerShare);
                if (debtorsData.containsKey(consumer.getPerson())) {
                    Integer debtSum = debtorsData.get(consumer.getPerson()).getOrDefault(purchase.getBuyer(), 0)
                            + consumerShare;
                    debtorsData.get(consumer.getPerson()).put(purchase.getBuyer(), debtSum);
                } else {
                    if (consumer.getPerson() != purchase.getBuyer()) {
                        debtorsData.put(consumer.getPerson(), creditor);
                    }
                }
            }
        }
        return debtorsData;
    }

    public static int calculateDebts(HashMap<Person, Integer> debtorsDebts) {
        int debt = 0;
        if (debtorsDebts == null) {
            return 0;
        } else {
            for (Map.Entry<Person, Integer> entry : debtorsDebts.entrySet()) {
                debt += entry.getValue();
            }
            return debt;
        }
    }

    public static int calculateCredits(HashMap<Person, HashMap<Person, Integer>> data, Person creditor) {
        int credit = 0;
        for (Map.Entry<Person, HashMap<Person, Integer>> entry : data.entrySet()) {
            for (Map.Entry<Person, Integer> entry2 : entry.getValue().entrySet()) {
                if (entry2.getKey() == creditor) {
                    credit += entry2.getValue();
                }
            }
        }
        return credit;
    }

    public HashMap<Person, Integer> calculatePersonsNetPayment(HashMap<Person, HashMap<Person, Integer>> data) {
        HashMap<Person, Integer> personsNetPayment = new HashMap<Person, Integer>();
        for (int i = 0; i < this.getPersonsInvolvedInPurchases().size(); i++) {
            Person debtor = this.getPersonsInvolvedInPurchases().get(i);
            int netPayment = calculateCredits(data, debtor) - calculateDebts(data.get(debtor));
            personsNetPayment.put(debtor, netPayment);
        }
        return personsNetPayment;
    }

    public void calculatePersonCreditsAndDebits(HashMap<Person, Integer> personNetPayment) {
        Person maxCredit = Utils.getPersonWithMaxNet(personNetPayment);
        Person maxDebit = Utils.getPersonWithMinNet(personNetPayment);
        if (personNetPayment.get(maxCredit) == 0 && personNetPayment.get(maxDebit) == 0) {
            return;
        }

        int min = Utils.minOfTwoIntegers(-personNetPayment.get(maxDebit), personNetPayment.get(maxCredit));
        personNetPayment.put(maxCredit, personNetPayment.get(maxCredit) - min);
        personNetPayment.put(maxDebit, personNetPayment.get(maxDebit) + min);
        if (this.getPayments().containsKey(maxDebit)) {
            this.getPayments().get(maxDebit).put(maxCredit, min);
        } else {
            HashMap<Person, Integer> creditorMap = new HashMap<Person, Integer>();
            creditorMap.put(maxCredit, min);
            this.getPayments().put(maxDebit, creditorMap);
        }
        calculatePersonCreditsAndDebits(personNetPayment);
    }

    public void setPaymentsData(){
        HashMap<Person, HashMap<Person, Integer>> normalizedData = this.normalizeDebtorsData();
        HashMap<Person, Integer> personsNetPayments = this.calculatePersonsNetPayment(normalizedData);
        this.setPayments(new HashMap<Person, HashMap<Person, Integer>>());
        calculatePersonCreditsAndDebits(personsNetPayments);
    }

    public void printPeriodDebtsAndCredits() {
        UI.printTitle("Payment Details");
        if (this.getPurchases().size() == 0) {
            System.out.println("No period detail exist yet.");
        } else {
            this.setPaymentsData();
            String format = "|%-10s  |%-35s|%-12s|%-10s|%-35s|%n";
            System.out.printf(format, "NO.", "Person", "Action", "Amount", "To");
            System.out
                    .print(String.format("|%012d|%035d|%012d|%010d|%035d|%n", 0, 0, 0, 0, 0)
                            .replace("0", "-"));
            int index = 0;
            for (Map.Entry<Person, HashMap<Person, Integer>> debtorEntry : this.getPayments().entrySet()) {
                for (Map.Entry<Person, Integer> creditorEntry : debtorEntry.getValue().entrySet()) {
                    Person creditor = creditorEntry.getKey();
                    Integer amount = creditorEntry.getValue();
                    System.out.printf(format, (index + 1), debtorEntry.getKey().getName(), "Should Pay", amount,
                            creditor.getName());
                    index++;
                }
            }
        }
    }

    public String getPersonsCommaSeparated() {
        String persons = "";
        for (int index = 0; index < this.getPersons().size(); index++) {
            persons += this.getPersons().get(index).getName() + ",";
        }
        return persons;
    }

    public String getPurchasesCommaSeparated() {
        String purchasesData = "\nPurchases:\nTitle,Expense,Buyer,Date and Time,Consumers\n";
        for (int index = 0; index < this.getPurchases().size(); index++) {
            Purchase purchase = this.getPurchases().get(index);
            purchasesData += purchase.getTitle() + "," + purchase.getExpense() + "," + purchase.getBuyer() + ",\""
                    + purchase.getDateAndTime() + ",";
            for (int j = 0; j < purchase.getConsumers().size(); j++) {
                purchasesData += purchase.getConsumers().get(j).getPerson().getName() + ": "
                        + purchase.getConsumers().get(j).getCoefficient() + "\n";
            }
            purchasesData += "\"";
        }
        return purchasesData;
    }

    public String getExportData() {
        String data = "";
        data += "Name:," + this.getName() + "\nStart Date and Time:," + this.getStartDateAndTime() + "\nPersons:,";
        data += getPersonsCommaSeparated();
        data += getPurchasesCommaSeparated();
        data+="\nDetails:\nNumber of Persons in Period:,"+this.getPersons().size()+"\nTotal Expenses:,"+this.getTotalExpenses()+"\nNumber of Purchases:,"+this.getPurchases().size()+"\nEach Persons Average Expense:,"+this.getOverallPersonAverageExpense();
        data+=""
    }
}

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UI {
    private static Scanner inputReader = new Scanner(System.in);

    public static void initialize() {
        boolean runProgram = true;
        while (runProgram) {
            printLoginOrRegisterMenu();
            int userActionType = getUserIntInput(": ");
            switch (userActionType) {
                case 1:
                    showRegisterUserMenu();
                    initialize();
                    break;
                case 2:
                    showLoginUserMenu();
                    initialize();
                    break;
                case 3:
                    runProgram = false;
                    break;
                default:
                    printInvalidChoice();
                    initialize();
                    break;
            }
        }
    }

    private static void showRegisterUserMenu() {
        printTitle("Register");
        String username = getUserStringInput("\nUsername: ");
        if (User.doesUsernameExist(username)) {
            System.out.println("User with this username is already exist try another one.");
            showRegisterUserMenu();
        }
        String password = getUserStringInput("Password: ");
        String name = getUserStringInput("Name: ");
        if (Utils.isStringEmptyOrNull(username) || Utils.isStringEmptyOrNull(name)
                || Utils.isStringEmptyOrNull(password)) {
            System.out.println("username or password or name cannot be empty.");
            showRegisterUserMenu();
        } else {
            User.registerUser(username, password, name);
            printSuccessfullyCreatedMessage("User");
        }
    }

    private static void showLoginUserMenu() {
        printTitle("Login");
        String username = getUserStringInput("Username: ");
        String password = getUserStringInput("Password: ");
        if (User.isUserAuthorized(username, password)) {
            User user = User.getUserObject(username, password);
            if (user != null) {
                User.setLoggedInUser(user);
                startUserMainMenuSection();

            } else {
                System.out.println("An error occurred in the process of logging yo in please try again.");
                initialize();
            }
        } else {
            System.out.println("invalid username or password. Try again");
            initialize();
        }
    }

    private static void printInvalidChoice() {
        System.out.println("Invalid choice, please try again!");
    }

    private static void printLoginOrRegisterMenu() {
        printTitle("Welcome");
        System.out.print("\n1- Register\n2- Login\n3- Exit\n");
    }

    private static String getUserStringInput(String message) {
        System.out.print(message);
        String userInput = inputReader.nextLine();
        return userInput;
    }

    private static int getUserIntInput(String message) {
        while (true) {
            System.out.print(message);
            String userInput = inputReader.nextLine();
            try {
                int userIntInput = Integer.parseInt(userInput);
                return userIntInput;
            } catch (NumberFormatException e) {
                System.out.println("invalid input. input should be an integer.");
            }
        }
    }

    public static void printTitle(String message) {
        System.out.print(String.format("%n%08d%0" + message.length() + "d%08d", 0, 0, 0).replace("0", "-"));
        System.out.print("\n\t" + message + "\n");
        System.out.print(String.format("%08d%0" + message.length() + "d%08d%n", 0, 0, 0).replace("0", "-"));

    }

    private static void printUserMainMenu() {
        printTitle("Main Menu");
        System.out.println(
                "1- List of Periods\n2- New Period\n3- Periods Details\n4- Edit Period\n5- Remove period\n6- Export data\n7- Save And Logout");
    }

    private static void startUserMainMenuSection() {
        printUserMainMenu();
        int userChoice = getUserIntInput(": ");
        switch (userChoice) {
            case 1:
                printListOfPeriods();
                startUserMainMenuSection();
                break;
            case 2:
                showCreatePeriodMenu();
                startUserMainMenuSection();
                break;
            case 3:
                startPeriodDetailMenu(); // TODO: ADD DETAILS
                startUserMainMenuSection();
                break;
            case 4:
                startEditPeriodMenu();
                startUserMainMenuSection();
                break;
            case 5:
                startRemovePeriodSection();
                startUserMainMenuSection();
                break;
            case 6:

                break;
            case 7:

                break;
            default:
                printInvalidChoice();
                startUserMainMenuSection();
                break;
        }
    }

    public static void printListOfPeriods() {
        printTitle("List of Periods");
        ArrayList<Period> userPeriods = User.getLoggedInUser().getPeriods();
        Period.printListOfPeriods(userPeriods);
        startUserMainMenuSection();
    }

    public static void printPurchasesInPeriod(Period period) {
        printTitle("Purchases in this period: ");
        if (period.getPurchases() == null) {
            System.out.println("No purchases");
        } else {
            for (int i = 0; i < period.getPurchases().size(); i++) {
                System.out.print(period.getPurchases().get(i).getTitle() + ", ");
            }
        }
    }

    public static void showCreatePeriodMenu() {
        printTitle("Create Period");
        String name = getUserStringInput("Name: ");
        String startDateAndTimeInput = getUserStringInput("Start Date and Time (in 'yyyy-MM-dd HH:mm' format): ");
        Date startDateAndTime = Period.getDateByDateString(startDateAndTimeInput);
        if (startDateAndTime == null) {
            printInvalidDateAndTime();
            startUserMainMenuSection();
        }
        Period period = new Period(name, startDateAndTime);
        User.getLoggedInUser().addToPeriods(period);
        printSuccessfullyCreatedMessage("Period");
        startUserMainMenuSection();

    }

    public static void printInvalidDateAndTime() {
        System.out.println("Invalid date and time. try again.");
    }

    public static Period getUserPeriodChoice(String title, ArrayList<Period> periods) {
        System.out.println(title);
        Period.printListOfPeriods(periods);
        if (periods.size() == 0) {
            return null;
        }
        int periodIndexInput = getUserIntInput("Enter Period number: ") - 1;
        if (Period.isInvalidIndex(periods, periodIndexInput)) {
            printInvalidChoice();
            return null;
        } else {
            return periods.get(periodIndexInput);
        }
    }

    public static void startPeriodDetailMenu() {
        printTitle("Period Details");
        Period period = getUserPeriodChoice("Choose a period to see details", User.getLoggedInUser().getPeriods());
        if (period == null) {
            startUserMainMenuSection();
        } else {
            Period.printPeriodDetail(period);
            if (period.getPurchases().size() != 0) {
                startPurchaseSortAndFilterMenu(period);
            }
        }
    }

    public static void startPurchaseSortAndFilterMenu(Period period) {
        int userActionChoice = getUserIntInput(
                "\nApply sort and filter on purchases\n1- Filter by buyer\n2- Filter by purchase user\n3- Sort by date and time\n4- Sort by expense\n5- Back\n: ");
        switch (userActionChoice) {
            case 1:
                printPurchasesFilteredByBuyer(period);
                startPurchaseSortAndFilterMenu(period);
                break;
            case 2:
                printPurchasesFilteredByPurchaseUsers(period);
                startPurchaseSortAndFilterMenu(period);
                break;
            case 3:
                printDateAndTimeSortedPurchases(period);
                startPurchaseSortAndFilterMenu(period);
                break;
            case 4:
                printExpenseSortedPurchases(period);
                startPurchaseSortAndFilterMenu(period);
                break;
            case 5:
                startUserMainMenuSection();
                break;
            default:
                printInvalidChoice();
                startPurchaseSortAndFilterMenu(period);
                break;
        }
    }

    public static void printPurchasesFilteredByBuyer(Period period) {
        Person person = getUserPersonChoice("Based on which person do you want to filter buyer results? ",
                period.getPersons());
        if (person == null) {
            startPurchaseSortAndFilterMenu(period);
        } else {
            ArrayList<Purchase> filteredPurchases = Purchase.getBuyerFilteredPurchases(period, person);
            Purchase.printListOfPurchases(filteredPurchases);
        }
    }

    public static void printPurchasesFilteredByPurchaseUsers(Period period) {
        Person purchaseUser = getUserPersonChoice("Based on which person do you want to filter purchase Users results?",
                period.getPersons());
        if (purchaseUser == null) {
            startPurchaseSortAndFilterMenu(period);
        } else {
            ArrayList<Purchase> filteredPurchases = Purchase.getPurchaseUsersFilteredPurchases(period, purchaseUser);
            Purchase.printListOfPurchases(filteredPurchases);
        }
    }

    public static void printExpenseSortedPurchases(Period period) {
        ArrayList<Purchase> purchases = period.getExpenseSortedPurchases();
        Purchase.printListOfPurchases(purchases);
    }

    public static void printDateAndTimeSortedPurchases(Period period) {
        ArrayList<Purchase> purchases = period.getDateAndTimeSortedPurchases();
        Purchase.printListOfPurchases(purchases);
    }

    public static void startEditPeriodMenu() {
        printTitle("Edit Period");
        Period period = getUserPeriodChoice("Which period do you want to edit? ", User.getLoggedInUser().getPeriods());
        if (period == null) {
            startUserMainMenuSection();
        } else {
            int userEditChoice = getUserIntInput(
                    "What do you want to edit?\n1- Period name\n2- Periods start date\n3- Add purchase\n4- Edit purchase\n5- Remove purchase\n6- Add person\n7- Remove person\n8- Back\n: ");
            switch (userEditChoice) {
                case 1:
                    showEditPeriodNameMenu(period);
                    startEditPeriodMenu();
                    break;
                case 2:
                    showEditStartDateMenu(period);
                    startEditPeriodMenu();
                    break;
                case 3:
                    startAddPurchaseToPeriodSection(period);
                    startEditPeriodMenu();
                    break;
                case 4:
                    startChoosePurchaseSection(period);
                    startEditPeriodMenu();
                    break;
                case 5:
                    showRemovePurchaseMenu(period);
                    startEditPeriodMenu();
                    break;
                case 6:
                    startAddPersonToPeriodSection(period);
                    startEditPeriodMenu();
                    break;
                case 7:
                    showRemovePersonMenu(period);
                    startEditPeriodMenu();
                    break;
                case 8:
                    startUserMainMenuSection();
                    break;
                default:
                    printInvalidChoice();
                    startEditPeriodMenu();
                    break;
            }
        }
    }

    public static void showEditPeriodNameMenu(Period period) {
        printTitle("Edit Name");
        String newPeriodName = getUserStringInput("what is your new period name? ");
        if (Period.isNameDuplicated(newPeriodName)) {
            System.out.println("another period with this name is already exist. try another name.");
            startEditPeriodMenu();
        }
        if (Utils.isStringEmptyOrNull(newPeriodName)) {
            System.out.println("invalid name. name of period cant be blank.");
            startEditPeriodMenu();
        }
        period.setName(newPeriodName);
        printSuccessfullyEditedMessage("Period name");

    }

    public static void showEditStartDateMenu(Period period) {
        printTitle("Edit Start Date");
        String newStartDateInput = getUserStringInput("What is your period's new start date? ");
        Date startDateAndTime = Period.getDateByDateString(newStartDateInput);
        if (startDateAndTime == null) {
            printInvalidDateAndTime();
            startEditPeriodMenu();
        }
        period.setStartDate(startDateAndTime);
        printSuccessfullyEditedMessage("Start Date and Time");
    }

    public static void showRemovePurchaseMenu(Period period) {
        printTitle("Delete Purchase");
        System.out.println("List of all purchases in " + period.getName() + " period:");
        if (period.getPurchases().size() != 0) {
            Purchase.printListOfPurchases(period.getPurchases());
            int userDeletionChoice = getUserIntInput("Enter the number associated with purchase to delete it: ") - 1;
            if (Purchase.isInvalidIndex(period.getPurchases(), userDeletionChoice)) {
                printInvalidChoice();
                showRemovePurchaseMenu(period);
            } else {
                period.getPurchases().remove(period.getPurchases().get(userDeletionChoice));
                System.out.println("chose purchase was deleted successfully!");
            }
        }
    }

    public static void showRemovePersonMenu(Period period) {
        printTitle("Delete Person");
        Person person = getUserPersonChoice(
                "Which person do you want to remove from period? enter the number associated with it: ",
                period.getPersons());
        if (person == null) {
            startEditPeriodMenu();
        } else {
            if (Period.isPersonInvolvedInPurchases(period, person)) {
                System.out.println(
                        "This person is involved in purchases of this period. you cant delete it. try removing it from purchases and try again.");
                startEditPeriodMenu();
            } else {
                period.getPersons().remove(person);
                printSuccessfullyDeletedMessage("Person");
            }
        }
    }

    public static void printSuccessfullyDeletedMessage(String title) {
        System.out.println(title + "Deleted successfully!");
    }

    public static void startChoosePurchaseSection(Period period) {
        printTitle("Edit Purchase");
        System.out.println("List of all purchases in " + period.getName() + " period:");
        if (period.getPurchases().size()!=0) {
            Purchase.printListOfPurchases(period.getPurchases());
            int userEditPurchaseChoice = getUserIntInput("Enter the number associated with purchase to edit it: ") - 1;
            if (Purchase.isInvalidIndex(period.getPurchases(), userEditPurchaseChoice)) {
                printInvalidChoice();
                startChoosePurchaseSection(period);
            } else {
                Purchase purchase = period.getPurchases().get(userEditPurchaseChoice);
                startEditPurchaseMenuSection(period, purchase);
            }
        }
    }

    public static void printSuccessfullyEditedMessage(String title) {
        System.out.println(title + " Edited successfully!");
    }

    public static void printSuccessfullyCreatedMessage(String title) {
        System.out.println(title + " Created successfully!");
    }

    public static void startEditPurchaseMenuSection(Period period, Purchase purchase) {
        int userEditingChoice = getUserIntInput(
                "What do you want to edit?\n1- Title\n2- Expense\n3- Date and time\n4- Buyer\n5- Purchase Users\n6- Back\n: ");
        switch (userEditingChoice) {
            case 1:
                showEditPurchaseTitleSection(period, purchase);
                startEditPurchaseMenuSection(period, purchase);
                break;
            case 2:
                showEditPurchaseExpenseSection(purchase);
                startEditPurchaseMenuSection(period, purchase);
                break;
            case 3:
                showEditPurchaseDateAndTimeSection(period, purchase);
                startEditPurchaseMenuSection(period, purchase);
                break;
            case 4:
                showEditBuyerSection(period, purchase);
                startEditPurchaseMenuSection(period, purchase);
                break;
            case 5:
                showEditPurchaseUsersSection(period, purchase);
                startEditPurchaseMenuSection(period, purchase);
                break;
            case 6:
                startEditPeriodMenu();
                break;
            default:
                printInvalidChoice();
                startEditPurchaseMenuSection(period, purchase);
                break;
        }
    }

    public static void showEditPurchaseTitleSection(Period period, Purchase purchase) {
        String newTitle = getUserStringInput("Enter new title: ");
        if (Purchase.isTitleDuplicated(period, newTitle)) {
            System.out.println("another purchase with this title exists in this period. try another title.");
            startEditPurchaseMenuSection(period, purchase);
        } else {
            purchase.setTitle(newTitle);
            printSuccessfullyEditedMessage("Title");
        }
    }

    public static void showEditPurchaseExpenseSection(Purchase purchase) {
        int newExpense = getUserIntInput("Enter new Expense: ");
        purchase.setExpense(newExpense);
        printSuccessfullyEditedMessage("Expense");
    }

    public static void showEditPurchaseDateAndTimeSection(Period period, Purchase purchase) {
        String newDateAndTimeInput = getUserStringInput("Enter new Date and Time: ");
        Date newDateAndTime = Period.getDateByDateString(newDateAndTimeInput);
        if (newDateAndTime == null) {
            printInvalidDateAndTime();
            startEditPurchaseMenuSection(period, purchase);
        } else {
            purchase.setDateAndTime(newDateAndTime);
            printSuccessfullyEditedMessage("Date and Time");
        }
    }

    public static void showEditBuyerSection(Period period, Purchase purchase) {
        Person person = getUserPersonChoice("Which person do you want to be purchase's new buyer? ",
                period.getPersons());
        if (person == null) {
            startEditPurchaseMenuSection(period, purchase);
        } else {
            purchase.setBuyer(person);
            printSuccessfullyEditedMessage("Buyer");
        }
    }

    public static void showEditPurchaseUsersSection(Period period, Purchase purchase) {
        int purchaseUserEditChoice = getUserIntInput("1- Add new purchase User\n2- Remove purchase user\n3- Back\n: ");
        switch (purchaseUserEditChoice) {
            case 1:
                printAddPurchaseUserSection(period, purchase);
                showEditPurchaseUsersSection(period, purchase);
                break;
            case 2:
                printRemovePurchaseUserSection(period, purchase);
                showEditPurchaseUsersSection(period, purchase);
                break;
            case 3:
                startEditPurchaseMenuSection(period, purchase);
                break;
            default:
                printInvalidChoice();
                showEditPurchaseUsersSection(period, purchase);
                break;
        }
    }

    public static void printAddPurchaseUserSection(Period period, Purchase purchase) {
        printTitle("Add person to purchase users");
        Person person = getUserPersonChoice("Choose person to add it to purchase users: ", period.getPersons());
        int purchaseUserCoefficientChoice = getUserIntInput("What is this persons coefficient? ");
        if (purchaseUserCoefficientChoice < 1) {
            System.out.println("Invalid coefficient. coefficient should be a positive integer.");
        }
        if (person == null) {
            showEditPurchaseUsersSection(period, purchase);
        } else {
            if (PersonCoefficient.doesPersonExistInPurchaseUsers(purchase, person)) {
                System.out.println("This person is already in purchase users. try another person.");
                showEditPurchaseUsersSection(period, purchase);
            } else {
                PersonCoefficient newPurchaseUser = new PersonCoefficient(person, purchaseUserCoefficientChoice);
                purchase.addToPurchaseUsers(newPurchaseUser);
                printSuccessfullyCreatedMessage("Purchase User");
            }
        }
    }

    public static void printRemovePurchaseUserSection(Period period, Purchase purchase) {
        printTitle("Remove Purchase Users");
        ArrayList<PersonCoefficient> purchaseUsers = purchase.getPurchaseUsers();
        PersonCoefficient.printPersonCoefficients(purchaseUsers);
        int userDeleteChoice = getUserIntInput("Which purchase user do you want to remove (enter the number)? ") - 1;
        if (PersonCoefficient.isInvalidIndex(purchaseUsers, userDeleteChoice)) {
            printInvalidChoice();
            showEditPurchaseUsersSection(period, purchase);
        } else {
            PersonCoefficient personCoefficient = purchaseUsers.get(userDeleteChoice);
            purchase.removeFromPurchaseUsers(personCoefficient);
            System.out.println("Purchase user removed successfully!");
        }
    }

    public static void startRemovePeriodSection() {
        Period period = getUserPeriodChoice("Remove Period", User.getLoggedInUser().getPeriods());
        if (period == null) {
            printUserMainMenu();
        } else {
            User.getLoggedInUser().removePeriod(period);
            System.out.println("Period removed successfully!");
        }
    }

    public static void startAddPersonToPeriodSection(Period period) {
        printTitle("Add Person");
        String userPersonNameInput = getUserStringInput("What is the new person's name? ");
        if (Person.isNameDuplicated(period.getPersons(), userPersonNameInput)) {
            System.out.println("Another person with this name already exist in this period. try another name.");
            startEditPeriodMenu();
        } else {
            Person person = new Person(userPersonNameInput);
            period.addPerson(person);
            printSuccessfullyCreatedMessage("Person");
        }
    }

    public static Person getUserPersonChoice(String title, ArrayList<Person> persons) {
        System.out.println(title);
        Person.printPersons(persons);
        int personIndexInput = getUserIntInput("Enter Person number: ") - 1;
        if (Person.isInvalidIndex(persons, personIndexInput)) {
            printInvalidChoice();
            return null;
        } else {
            return persons.get(personIndexInput);
        }
    }

    public static void startAddPurchaseToPeriodSection(Period period) {
        printTitle("Create Purchase");
        if (period.getPersons().size() == 0) {
            System.out.println("There are no person exist in this period yet. try adding person to period first.");
            startEditPeriodMenu();
        } else {
            String title = getUserStringInput("Title: ");
            int expense = getUserIntInput("Expense: ");
            String dateAndTimeInput = getUserStringInput("Date And Time (in 'yyyy-MM-dd HH:mm' format): ");
            Date dateAndTime = Period.getDateByDateString(dateAndTimeInput);
            if (dateAndTime == null) {
                printInvalidDateAndTime();
                startEditPeriodMenu();
            }
            Person buyer = getUserPersonChoice("Choose Buyer: ", period.getPersons());
            ArrayList<PersonCoefficient> purchaseUsers = getPurchaseUsers(period);
            Purchase purchase = new Purchase(title, expense, buyer, purchaseUsers, dateAndTime);
            period.addPurchase(purchase);
            printSuccessfullyCreatedMessage("Purchase");
        }
    }

    public static ArrayList<PersonCoefficient> getPurchaseUsers(Period period) {
        int purchaseUsersCount = getUserIntInput("How many purchase users are in this purchase? ");
        if (purchaseUsersCount > period.getPersons().size()) {
            System.out.println(
                    "the number of purchase users you want to add to purchase is more than persons in period. try adding more persons first.");
            startEditPeriodMenu();
        }
        ArrayList<PersonCoefficient> purchaseUsers = new ArrayList<PersonCoefficient>();
        for (int i = 0; i < purchaseUsersCount; i++) {
            Person person = getUserPersonChoice("Choose person from list to add to purchase users: ",
                    period.getPersons());
            if (person == null) {
                startEditPeriodMenu();
            } else {
                int coefficient = getUserIntInput("What is this persons coefficient in this purchase? ");
                if (coefficient < 1) {
                    System.out.println("Invalid coefficient. coefficient should be a positive integer.");
                } else {
                    PersonCoefficient purchaseUser = new PersonCoefficient(person, coefficient);
                    purchaseUsers.add(purchaseUser);
                }
            }
        }
        return purchaseUsers;
    }

}

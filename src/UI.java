import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UI {
    private static Scanner inputReader = new Scanner(System.in);
    public static String RESET = "\u001B[0m";
    public static String RED = "\u001B[31m";
    public static String GREEN = "\u001B[32m";
    public static String YELLOW = "\u001B[33m";

    public static void initializeUI() {
        printLoginOrRegisterMenu();
        int userActionType = getUserIntInput(": ");
        switch (userActionType) {
            case 1:
                showRegisterUserMenu();
                initializeUI();
                break;
            case 2:
                showLoginUserMenu();
                initializeUI();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                printInvalidChoice();
                initializeUI();
                break;
        }
    }

    private static void showRegisterUserMenu() {
        printTitle("Register");
        String username = getUserStringInput("\nUsername: ");
        if (User.doesUsernameExist(username)) {
            printAlreadyExistsMessage("User", "username");
            showRegisterUserMenu();
        }
        String password = getUserStringInput("Password: ");
        if (!User.isPasswordValid(password)) {
            System.out.println(RED+"Invalid password. minimum password length is 8 characters."+RESET);
            showRegisterUserMenu();
        }
        String name = getUserStringInput("Name: ");
        if (Utils.isStringEmptyOrNull(username) || Utils.isStringEmptyOrNull(name)
                || Utils.isStringEmptyOrNull(password)) {
            printCantBeBlank();
            showRegisterUserMenu();
        } else {
            User.registerUser(username, password, name);
            printSuccessfullyCreatedMessage("User");
            initializeUI();
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
                initializeUI();
            }
        } else {
            System.out.println(RED + "invalid username or password. Try again" + RESET);
            initializeUI();
        }
    }

    private static void printLoginOrRegisterMenu() {
        printTitle("Welcome to DONGEMOON");
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
                System.out.println(RED + "invalid input. input should be an integer." + RESET);
            }
        }
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
                startPeriodDetailMenu();
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
                startExportCsvSection();
                startUserMainMenuSection();
                break;
            case 7:
                Database.writeProgramDataToFile();
                initializeUI();
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

    public static void showCreatePeriodMenu() {
        printTitle("Create Period");
        String name = getUserStringInput("Name: ");
        if (Utils.isStringEmptyOrNull(name)) {
            printCantBeBlank();
            startUserMainMenuSection();
        }
        String startDateAndTimeInput = getUserStringInput("Start Date and Time (in 'yyyy-MM-dd HH:mm' format): ");
        Date startDateAndTime = Utils.getDateByDateString(startDateAndTimeInput);
        if (startDateAndTime == null) {
            printInvalidDateAndTime();
            startUserMainMenuSection();
        }
        Period period = new Period(name, startDateAndTime);
        User.getLoggedInUser().addToPeriods(period);
        printSuccessfullyCreatedMessage("Period");
        startUserMainMenuSection();

    }

    public static void startExportCsvSection() {
        printTitle("Export Period to CSV");
        Period period = getUserPeriodChoice("Which period Do you want to export? ",
                User.getLoggedInUser().getPeriods());
        if (period == null) {
            startUserMainMenuSection();
        }
        String fileName = getUserStringInput(
                "Enter Exported File name (Leave empty to name the file after period's name): ");
        if (fileName.isEmpty()) {
            fileName = period.getName();
        }
        fileName += ".csv";
        String exportData = period.getExportData();
        if (exportData == null) {
            printDontExistMessage("Purchase");
            System.out.println("Try adding a purchase first.");
            startUserMainMenuSection();
        }
        Database.writePeriodDataToFile(exportData, fileName);
        System.out.println(GREEN + "Period Data successfully exported to: " + fileName + RESET);
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
            period.printPeriodDetail();
            if (period.getPurchases().size() != 0) {
                startPurchaseSortAndFilterMenu(period);
            }
        }
    }

    public static void startPurchaseSortAndFilterMenu(Period period) {
        int userActionChoice = getUserIntInput(
                "Apply sort and filter on purchases\n1- Filter by buyer\n2- Filter by Consumers\n3- Sort by date and time\n4- Sort by expense\n5- Back\n: ");
        switch (userActionChoice) {
            case 1:
                printPurchasesFilteredByBuyer(period);
                startPurchaseSortAndFilterMenu(period);
                break;
            case 2:
                printPurchasesFilteredByConsumers(period);
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

    public static void printPurchasesFilteredByConsumers(Period period) {
        Person consumer = getUserPersonChoice("Based on which person do you want to filter Consumers results?",
                period.getPersons());
        if (consumer == null) {
            startPurchaseSortAndFilterMenu(period);
        } else {
            ArrayList<Purchase> filteredPurchases = Purchase.getConsumersFilteredPurchases(period, consumer);
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
            printAlreadyExistsMessage("Period", "name");
            startEditPeriodMenu();
        }
        if (Utils.isStringEmptyOrNull(newPeriodName)) {
            printCantBeBlank();
            startEditPeriodMenu();
        }
        period.setName(newPeriodName);
        printSuccessfullyEditedMessage("Period name");
    }

    public static void showEditStartDateMenu(Period period) {
        printTitle("Edit Start Date");
        String newStartDateInput = getUserStringInput("What is your period's new start date? ");
        Date startDateAndTime = Utils.getDateByDateString(newStartDateInput);
        if (startDateAndTime == null) {
            printInvalidDateAndTime();
            startEditPeriodMenu();
        }
        period.setStartDateAndTime(startDateAndTime);
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
                printSuccessfullyDeletedMessage("Purchase");
            }
        } else {
            printDontExistMessage("Purchase");
        }
    }

    public static void showRemovePersonMenu(Period period) {
        printTitle("Delete Person");
        Person person = getUserPersonChoice(
                "Which person do you want to remove from period? ",
                period.getPersons());
        if (person == null) {
            startEditPeriodMenu();
        } else {
            if (Period.isPersonInvolvedInPurchases(period, person)) {
                System.out.println(
                        RED + "This person is involved in purchases of this period. you cant delete it. try removing it from purchases and try again."
                                + RESET);
                startEditPeriodMenu();
            } else {
                period.getPersons().remove(person);
                printSuccessfullyDeletedMessage("Person");
            }
        }
    }

    public static void startChoosePurchaseSection(Period period) {
        printTitle("Edit Purchase");
        System.out.println("List of all purchases in " + period.getName() + " period:");
        if (period.getPurchases().size() != 0) {
            Purchase.printListOfPurchases(period.getPurchases());
            int userEditPurchaseChoice = getUserIntInput("Enter the number associated with purchase to edit it: ") - 1;
            if (Purchase.isInvalidIndex(period.getPurchases(), userEditPurchaseChoice)) {
                printInvalidChoice();
                startChoosePurchaseSection(period);
            } else {
                Purchase purchase = period.getPurchases().get(userEditPurchaseChoice);
                startEditPurchaseMenuSection(period, purchase);
            }
        } else {
            printDontExistMessage("Purchase");
        }
    }

    public static void startEditPurchaseMenuSection(Period period, Purchase purchase) {
        printTitle("Edit '" + purchase.getTitle() + "' Purchase");
        int userEditingChoice = getUserIntInput(
                "What do you want to edit?\n1- Title\n2- Expense\n3- Date and time\n4- Buyer\n5- Consumers\n6- Back\n: ");
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
                showEditConsumerSection(period, purchase);
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
        if (Purchase.isTitleDuplicated(period, newTitle) || Utils.isStringEmptyOrNull(newTitle)) {
            printAlreadyExistsMessage("Purchase", "title");
            startEditPurchaseMenuSection(period, purchase);
        } else {
            purchase.setTitle(newTitle);
            printSuccessfullyEditedMessage("Title");
        }
    }

    public static void showEditPurchaseExpenseSection(Purchase purchase) {
        int newExpense = getUserIntInput("Enter new Expense: ");
        if (Purchase.isExpenseValid(newExpense)) {
            purchase.setExpense(newExpense);
            printSuccessfullyEditedMessage("Expense");
        } else {
            printInputShouldBePositiveNumberMessage("Expense");
        }
    }

    public static void showEditPurchaseDateAndTimeSection(Period period, Purchase purchase) {
        String newDateAndTimeInput = getUserStringInput("Enter new Date and Time: ");
        Date newDateAndTime = Utils.getDateByDateString(newDateAndTimeInput);
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

    public static void showEditConsumerSection(Period period, Purchase purchase) {
        int consumerEditChoice = getUserIntInput("1- Add new Consumers\n2- Remove Consumers\n3- Back\n: ");
        switch (consumerEditChoice) {
            case 1:
                printAddConsumerSection(period, purchase);
                showEditConsumerSection(period, purchase);
                break;
            case 2:
                printRemoveConsumerSection(period, purchase);
                showEditConsumerSection(period, purchase);
                break;
            case 3:
                startEditPurchaseMenuSection(period, purchase);
                break;
            default:
                printInvalidChoice();
                showEditConsumerSection(period, purchase);
                break;
        }
    }

    public static void printAddConsumerSection(Period period, Purchase purchase) {
        printTitle("Add person to Consumers");
        Person person = getUserPersonChoice("Choose person to add it to Consumers: ", period.getPersons());
        int consumerCoefficientChoice = getUserIntInput("What is this persons coefficient? ");
        if (consumerCoefficientChoice < 1) {
            printInputShouldBePositiveNumberMessage("Coefficient");
            showEditConsumerSection(period, purchase);
        }
        if (person == null) {
            showEditConsumerSection(period, purchase);
        } else {
            if (PersonCoefficient.doesPersonExistInConsumers(purchase, person)) {
                printAlreadyExistsMessage("Person", "consumers");
                showEditConsumerSection(period, purchase);
            } else {
                PersonCoefficient newConsumer = new PersonCoefficient(person, consumerCoefficientChoice);
                purchase.addToConsumers(newConsumer);
                printSuccessfullyCreatedMessage("Consumer");
            }
        }
    }

    public static void printRemoveConsumerSection(Period period, Purchase purchase) {
        printTitle("Remove Consumer");
        ArrayList<PersonCoefficient> consumers = purchase.getConsumers();
        PersonCoefficient.printPersonCoefficients(consumers);
        int userDeleteChoice = getUserIntInput("Which Consumer do you want to remove (enter the number)? ") - 1;
        if (PersonCoefficient.isInvalidIndex(consumers, userDeleteChoice)) {
            printInvalidChoice();
            showEditConsumerSection(period, purchase);
        } else {
            PersonCoefficient personCoefficient = consumers.get(userDeleteChoice);
            purchase.removeFromConsumers(personCoefficient);
            printSuccessfullyDeletedMessage("Consumer");
        }
    }

    public static void startRemovePeriodSection() {
        printTitle("Remove Period");
        Period period = getUserPeriodChoice("Which period do you want to delete? ",
                User.getLoggedInUser().getPeriods());
        if (period == null) {
            startUserMainMenuSection();
        } else {
            User.getLoggedInUser().removePeriod(period);
            printSuccessfullyDeletedMessage("Period");
        }
    }

    public static void startAddPersonToPeriodSection(Period period) {
        printTitle("Add Person");
        String userPersonNameInput = getUserStringInput("What is the new person's name? ");
        if (Person.isNameDuplicated(period.getPersons(), userPersonNameInput)) {
            printAlreadyExistsMessage("Person", "name");
            startEditPeriodMenu();
        } else if (Utils.isStringEmptyOrNull(userPersonNameInput)) {
            printCantBeBlank();
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
        if (persons.size() == 0) {
            return null;
        }
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
            printDontExistMessage("Person");
            startEditPeriodMenu();
        } else {
            String title = getUserStringInput("Title: ");
            if (Purchase.isTitleDuplicated(period, title)) {
                printAlreadyExistsMessage("Purchase", "title");
                startEditPeriodMenu();
            }
            if (Utils.isStringEmptyOrNull(title)) {
                printCantBeBlank();
                startEditPeriodMenu();
            }
            int expense = getUserIntInput("Expense: ");
            if (!Purchase.isExpenseValid(expense)) {
                printInputShouldBePositiveNumberMessage("Expense");
                startEditPeriodMenu();
            }
            String dateAndTimeInput = getUserStringInput("Date And Time (in 'yyyy-MM-dd HH:mm' format): ");
            Date dateAndTime = Utils.getDateByDateString(dateAndTimeInput);
            if (dateAndTime == null) {
                printInvalidDateAndTime();
                startEditPeriodMenu();
            }
            Person buyer = getUserPersonChoice("Choose Buyer: ", period.getPersons());
            if (buyer == null) {
                startAddPurchaseToPeriodSection(period);
            }
            ArrayList<PersonCoefficient> consumers = getUsersConsumersChoice(period);
            Purchase purchase = new Purchase(title, expense, buyer, consumers, dateAndTime);
            period.addPurchase(purchase);
            printSuccessfullyCreatedMessage("Purchase");
        }
    }

    public static ArrayList<PersonCoefficient> getUsersConsumersChoice(Period period) {
        int consumersCount = getUserIntInput("How many Consumers are in this purchase? ");
        if (consumersCount > period.getPersons().size()) {
            System.out.println(RED +
                    "the number of Consumers you want to add to purchase is more than persons in period. try adding more persons first."
                    + RESET);
            startEditPeriodMenu();
        }
        ArrayList<PersonCoefficient> consumers = new ArrayList<PersonCoefficient>();
        for (int i = 0; i < consumersCount; i++) {
            Person person = getUserPersonChoice("Choose person from list to add to Consumers: ",
                    period.getPersons());
            if (person == null) {
                startEditPeriodMenu();
            } else {
                int coefficient = getUserIntInput("What is this persons coefficient in this purchase? ");
                if (coefficient < 1) {
                    printInputShouldBePositiveNumberMessage("Coefficient");
                    startEditPeriodMenu();
                } else {
                    PersonCoefficient consumer = new PersonCoefficient(person, coefficient);
                    consumers.add(consumer);
                }
            }
        }
        return consumers;
    }

    public static void printTitle(String message) {
        int terminalSize = 150;
        int eachSideSize = (terminalSize / 2) + message.length() / 2;
        System.out.print(String.format("%n%0" + terminalSize + "d%n", 0).replace("0", "-"));
        System.out.print(String.format("%" + eachSideSize + "s", message));
        System.out.print(String.format("%n%0" + terminalSize + "d%n", 0).replace("0", "-"));
    }

    private static void printInvalidChoice() {
        System.out.println(RED + "Invalid choice, please try again!" + RESET);
    }

    public static void printInvalidDateAndTime() {
        System.out.println(RED +
                "Invalid date and time (Invalid format or date and time in the future or date and time in far past). try again."
                + RESET);
    }

    public static void printCantBeBlank() {
        System.out.println(RED + "invalid input. this input cant be blank." + RESET);
    }

    public static void printAlreadyExistsMessage(String member, String field) {
        System.out.println(RED + "Another " + member + " with this " + field + " already exists. Try another " + field
                + "." + RESET);
    }

    public static void printInputShouldBePositiveNumberMessage(String title) {
        System.out.println(RED + "Invalid " + title + ". " + title + " should be a positive number." + RESET);
    }

    public static void printDontExistMessage(String title) {
        System.out.println(YELLOW + "No " + title + " exists yet." + RESET);
    }

    public static void printSuccessfullyEditedMessage(String title) {
        System.out.println(GREEN + title + " Edited successfully!" + RESET);
    }

    public static void printSuccessfullyCreatedMessage(String title) {
        System.out.println(GREEN + title + " Created successfully!" + RESET);
    }

    public static void printSuccessfullyDeletedMessage(String title) {
        System.out.println(GREEN + title + "Deleted successfully!" + RESET);
    }

}

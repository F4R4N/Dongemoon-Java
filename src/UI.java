import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UI {
    private static Scanner inputReader = new Scanner(System.in);

    public static void initialize() {
        boolean runProgram = true;
        while (runProgram) {
            printLoginOrRegisterMenu();
            int userActionType = getUserIntInput(":");
            switch (userActionType) {
                case 1:
                    showRegisterUserMenu();
                    break;
                case 2:
                    showLoginUserMenu();
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
            System.out.println("User created successfully");
        }
    }

    private static void showLoginUserMenu() {
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
        System.out.println("\n-------------------------\n\t" + message + "\n-------------------------");

    }

    private static void printUserMainMenu() {
        printTitle("User Menu");
        System.out.println(
    "1- List of Periods\n2- New Period\n3- Periods Details\n4- Edit Period\n5- Remove period\n6- Add person to period\n7- add purchase to period\n8- Export data\n9- Save And Logout");
    }

    private static void startUserMainMenuSection() {
        printUserMainMenu();
        int userChoice = getUserIntInput(": ");
        switch (userChoice) {
            case 1:
                printListOfPeriods();
                break;
            case 2:
                showCreatePeriodMenu();
                break;
            case 3:
                startPeriodDetailMenu(); // TODO:unfinished - have to add menu that in it we can implement sort and filter (filter for buyer and purchase user)(sort for expense and start datee and time)
                break;
            case 4:
                startEditPeriodMenu();
                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
                case 9:

                break;
            default:
                printInvalidChoice();
                startUserMainMenuSection();
                break;
        }
    }

    public static void printListOfPeriods() {
        System.out.println("-------------------------");
        ArrayList<Period> userPeriods = User.getLoggedInUser().getPeriods();
        if (userPeriods == null) {
            System.out.println("You have '0' periods");
            startUserMainMenuSection();
        }
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
            System.out.println("Invalid date and time. try again.");
            showCreatePeriodMenu();
        }
        Period period = new Period(name, startDateAndTime);
        User.getLoggedInUser().addToPeriods(period);
        System.out.println("Period created successfully!!");
        startUserMainMenuSection();

    }

    // public static ArrayList<Person> getPersonInputs() {
    //     ArrayList<Person> persons = new ArrayList<Person>();
    //     printTitle("Existing Persons");
    //     if (Person.getAllExistingPersons() == null) {
    //         System.out.println("There is no person exist.");
    //     } else {
    //         for (int index = 0; index < Person.getAllExistingPersons().size(); index++) {
    //             System.out.println(Person.getAllExistingPersons().get(index).getName());
    //         }
    //     }
    //     while (true) {
    //         String personInput = getUserStringInput(
    //                 "Write the person name from the list above or write another name to create new person (if you are done adding, enter '0'): ");
    //         if (personInput.equals("0")) {
    //             break;
    //         }
    //         Person person = Person.addOrCreatePerson(personInput);
    //         if (!persons.contains(person)) {
    //             persons.add(person);
    //         } else {
    //             System.out.println("This person is already added.");
    //         }
    //     }
    //     return persons;
    // }

    // public static ArrayList<Purchase> getPurchaseInputs() {
    // ArrayList<Purchase> purchases = new ArrayList<Purchase>();
    // while (true) {
    // String title = getUserStringInput("Title: ");
    // int expense = getUserIntInput("Expense: ");

    // }
    // }

    public static Period getUserPeriodChoice(String title) {
        printTitle(title);
        ArrayList<Period> periods = User.getLoggedInUser().getPeriods();
        Period.printListOfPeriods(periods);
        int periodIndexInput = getUserIntInput("Enter Period number: ") - 1;
        if (Period.isInvalidIndex(periods, periodIndexInput)) {
            printInvalidChoice();
            startPeriodDetailMenu();
        }
        // Period.printPeriodDetail(periods.get(periodIndexInput));
        return periods.get(periodIndexInput);

    }

    public static void startPeriodDetailMenu() {
        Period period = getUserPeriodChoice("Periods Details");
        Period.printPeriodDetail(period);
    }

    public static void startEditPeriodMenu() {

        Period period = getUserPeriodChoice("Edit Period");
        int userEditChoice = getUserIntInput(
                "What do you want to edit?\n1- Period name\n2- Periods start date\n3- Remove a purchase\n4- Remove a person\n5- Edit purchase\n6- Back\n: ");
        switch (userEditChoice) {
            case 1:
                showEditPeriodNameMenu(period);
                break;
            case 2:
                showEditStartDateMenu(period);
                break;
            case 3:
                showRemovePurchaseMenu(period);
                break;
                case 4:
                showRemovePersonMenu(period);
                break;
            case 5:
                startChoosePurchaseSection(period); // TODO:unfinished
                break;
            case 6:
                startUserMainMenuSection();
                break;

            default:
                printInvalidChoice();
                startEditPeriodMenu();
                break;
        }
    }

    public static void showEditPeriodNameMenu(Period period) {
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
        String newStartDateInput = getUserStringInput("What is your period's new start date? ");
        Date startDateAndTime = Period.getDateByDateString(newStartDateInput);
        if (startDateAndTime == null) {
            System.out.println("Invalid date and time. try again.");
            startEditPeriodMenu();
        }
        period.setStartDate(startDateAndTime);
        printSuccessfullyEditedMessage("Start Date and Time");
    }

    public static void showRemovePurchaseMenu(Period period) {
        System.out.println("List of all purchases in " + period.getName() + " period:");
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

    public static void showRemovePersonMenu(Period period){
        ArrayList<Person> persons = period.getPersons();
        Person.printPersons(persons);
        int userPersonRemoveChoice = getUserIntInput("Which person do you want to remove from period? enter the number associated with it: ")-1;
        if (Person.isInvalidIndex(persons, userPersonRemoveChoice)) {
            printInvalidChoice();
            startEditPeriodMenu();
        }else{
            persons.remove(persons.get(userPersonRemoveChoice));
            System.out.println("Person remove successfully!");
        }
    }

    public static void startChoosePurchaseSection(Period period) {
        System.out.println("List of all purchases in " + period.getName() + " period:");
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

    public static void printSuccessfullyEditedMessage(String title) {
        System.out.println(title + " Edited successfully!");
    }

    public static void startEditPurchaseMenuSection(Period period, Purchase purchase) {
        int userEditingChoice = getUserIntInput(
                "What do you want to edit?\n1- Title\n2- Expense\n3- Date and time\n4- Buyer\n5- Purchase Users\n6- Back\n: ");
        switch (userEditingChoice) {
            case 1:
                showEditPurchaseTitleSection(period, purchase);
                break;
            case 2:
                showEditPurchaseExpenseSection(purchase);
                break;
            case 3:
                showEditPurchaseDateAndTimeSection(period, purchase);
                break;
            case 4:
                showEditBuyerSection(period, purchase);
                break;
            case 5:
                showEditPurchaseUsersSection(period, purchase);
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
            System.out.println("Invalid date and time. try again.");
            startEditPurchaseMenuSection(period, purchase);
        } else {
            purchase.setDateAndTime(newDateAndTime);
            printSuccessfullyEditedMessage("Date and Time");
        }
    }

    public static void showEditBuyerSection(Period period, Purchase purchase) {
        ArrayList<Person> persons = period.getPersons();
        Person.printPersons(persons);
        int userNewBuyerChoice = getUserIntInput("Which person do you want to be purchase's new buyer? ") - 1;
        if (Person.isInvalidIndex(persons, userNewBuyerChoice)) {
            printInvalidChoice();
            startEditPurchaseMenuSection(period, purchase);
        } else {
            Person person = persons.get(userNewBuyerChoice);
            purchase.setBuyer(person);
            printSuccessfullyEditedMessage("Buyer");
        }
    }

    public static void showEditPurchaseUsersSection(Period period, Purchase purchase) {
        int purchaseUserEditChoice = getUserIntInput("1- Add new purchase User\n2- Remove purchase user\n3- Back\n: ");
        switch (purchaseUserEditChoice) {
            case 1:

                break;
            case 2:

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
}

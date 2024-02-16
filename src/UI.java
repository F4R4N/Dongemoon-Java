import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.jws.soap.SOAPBinding.Use;

public class UI {
    private static Scanner inputReader = new Scanner(System.in);

    public static void initialize() {
        boolean runProgram = true;
        while (runProgram) {
            printLoginOrRegisterMenu();
            int userActionType = getUserIntInput(":");
            switch (userActionType) {
                case 1:
                    registerUserMenu();
                    break;
                case 2:
                    loginUserMenu();
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

    public static void registerUserMenu() {
        String username = getUserStringInput("\nUsername: ");
        String password = getUserStringInput("Password: ");
        String name = getUserStringInput("Name: ");
        if (User.doesUsernameExist(username)) {
            System.out.println("User with this username is already exist try another one.");
            registerUserMenu();
        }
        User.registerUser(username, password, name);
        System.out.println("User created successfully");

    }

    public static void loginUserMenu() {
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

    public static void printInvalidChoice() {
        System.out.println("Invalid choice, please try again!");
    }

    private static void printLoginOrRegisterMenu() {
        printTitle("Welcome");
        System.out.print("\n1- Register\n2- Login\n3- Exit\n");
    }

    public static String getUserStringInput(String message) {
        System.out.print(message);
        String userInput = inputReader.nextLine();
        return userInput;
    }

    public static int getUserIntInput(String message) {
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

    public static void printUserMainMenu() {
        printTitle("User Menu");
        System.out.println(
                "1- List of Periods\n2- New Period\n3- Periods Details\n4- Edit Period\n5- add person to period\n6- add purchase to period\n7- \n8- \n9- \n10- \n11- Save And Logout");
    }

    public static void startUserMainMenuSection() {
        printUserMainMenu();
        int userChoice = getUserIntInput(": ");
        switch (userChoice) {
            case 1:
                listPeriods();
                break;
            case 2:
                createPeriodMenu();
                break;
            case 3:
                startPeriodDetailMenu();
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            default:
                break;
        }
    }

    public static void listPeriods() {
        System.out.println("-------------------------");
        ArrayList<Period> userPeriods = User.getLoggedInUser().getPeriods();
        if (userPeriods == null) {
            System.out.println("You have '0' periods");
            startUserMainMenuSection();
        }
        System.out.println("You have '" + userPeriods.size() + "' periods");
        printPeriods(userPeriods);
        startUserMainMenuSection();
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

                printTitle("Persons involved in this period:");
                if (period.getPersons() == null) {
                    System.out.println("No person");
                } else {
                    for (int i = 0; i < period.getPersons().size(); i++) {
                        System.out.print(period.getPersons().get(i).getName() + ", ");
                    }
                }
                printTitle("Purchases in this period: ");
                if (period.getPurchases() == null) {
                    System.out.println("No purchases");
                } else {
                    for (int i = 0; i < period.getPurchases().size(); i++) {
                        System.out.print(period.getPurchases().get(i).getTitle() + ", ");
                    }
                }
                System.out.println("---------------------------------------------------------------------------------------------------------");
            }
        }
    }
    public static void createPeriodMenu() {
        printTitle("Create Period");
        String name = getUserStringInput("Name: ");
        String startDateAndTimeInput = getUserStringInput("Start Date and Time (in 'yyyy-MM-dd HH:mm' format): ");
        Date startDateAndTime = Period.getDateByDateString(startDateAndTimeInput);
        if (startDateAndTime == null) {
            System.out.println("Invalid date and time. try again.");
            createPeriodMenu();
        }
        Period period = new Period(name, startDateAndTime);
        User.getLoggedInUser().addToPeriods(period);
        System.out.println("Period created successfully!!");
        startUserMainMenuSection();

    }

    public static ArrayList<Person> getPersonInputs() {
        ArrayList<Person> persons = new ArrayList<Person>();
        printTitle("Existing Persons");
        if (Person.getAllExistingPersons() == null) {
            System.out.println("There is no person exist.");
        } else {
            for (int index = 0; index < Person.getAllExistingPersons().size(); index++) {
                System.out.println(Person.getAllExistingPersons().get(index).getName());
            }
        }
        while (true) {
            String personInput = getUserStringInput(
                    "Write the person name from the list above or write another name to create new person (if you are done adding, enter '0'): ");
            if (personInput.equals("0")) {
                break;
            }
            Person person = Person.addOrCreatePerson(personInput);
            if (!persons.contains(person)) {
                persons.add(person);
            } else {
                System.out.println("This person is already added.");
            }
        }
        return persons;
    }

    public static ArrayList<Purchase> getPurchaseInputs() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        while (true) {
            String title = getUserStringInput("Title: ");
            int expense = getUserIntInput("Expense: ");

        }
    }

    public static void startPeriodDetailMenu() {
        printTitle("Period Detail");
        ArrayList<Period> periods = User.getLoggedInUser().getPeriods();
        printPeriods(periods);
        int periodIndexInput = getUserIntInput("Enter Period number to see details: ") - 1;
        if (periodIndexInput > periods.size() - 1 || periodIndexInput < 0) {
            printInvalidChoice();
            startPeriodDetailMenu();
        }
        Period.printPeriodDetail(periods.get(periodIndexInput));
    }
}

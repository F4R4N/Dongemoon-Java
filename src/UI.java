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
                "1- List of Periods\n2- New Period\n3- Periods Details\n4- Edit period\n5- \n6- \n7- \n8- \n9- \n10- \n11- Save And Logout");
    }

    public static void startUserMainMenuSection() {
        printUserMainMenu();
        int userChoice = getUserIntInput(": ");
        switch (userChoice) {
            case 1:

                break;

            default:
                break;
        }
    }

}

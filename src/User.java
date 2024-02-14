public class User {
    private String username;
    private String password;
    private String name;
    private static User loggedInUser;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        User.loggedInUser = loggedInUser;
    }

    public static void registerUser(String username, String password, String name) {
        User user = new User(username, password, name);
        Database.users.add(user);
    }

    public static boolean doesUsernameExist(String username) {
        for (int i = 0; i < Database.users.size(); i++) {
            if (Database.users.get(i).getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUserAuthorized(String username, String password) {
        for (int i = 0; i < Database.users.size(); i++) {
            User user = Database.users.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    
    public static User getUserObject(String username, String password) {
        for (int i = 0; i < Database.users.size(); i++) {
            User user = Database.users.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}

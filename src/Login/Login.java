package Login;

import java.util.Scanner;
import User_Types.StudentMenu;
import User_Types.UserType;

public class Login {
    private static final String CREDENTIALS_FILE = "registrar_credentials.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void loginMenu() {
        char choice;
        while (true) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("                                                                                        =====================================");
            System.out.println("                                                                                        ||              LOGIN MENU         ||");
            System.out.println("                                                                                        =====================================");
            System.out.println("                                                                                        ||  1. Login as Admin              ||");
            System.out.println("                                                                                        ||  2. Login as Student            ||");
            System.out.println("                                                                                        ||  0. Exit                        ||");
            System.out.println("                                                                                        =====================================");
            System.out.print("                                                                                                Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1':
                    adminLogin();
                    break;
                case '2':
                    studentLogin();
                    break;
                case '0':
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n                                                                                        Invalid input. Try again...");
                    System.out.println("                                                                                        Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    private static void adminLogin() {
        if (!new java.io.File(CREDENTIALS_FILE).exists()) {
            System.out.println("\n                                                                                        No admin account found.");
            System.out.print("                                                                                        Do you want to create an admin account? (y/n): ");
            char response = scanner.next().charAt(0);
            scanner.nextLine();
            while (response != 'y' && response != 'Y' && response != 'n' && response != 'N') {
                System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.");
                System.out.print("                                                                                        Do you want to create an admin account? (y/n): ");
                response = scanner.next().charAt(0);
                scanner.nextLine();
            }
            if (response == 'y' || response == 'Y') {
                createAdminAccount();
            } else {
                System.out.println("                                                                                        Returning to login menu...");
            }
        } else {
            System.out.print("\n                                                                                              Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("                                                                                                Enter Password: ");
            String password = scanner.nextLine();

            if (validateAdminLogin(username, password)) {
                System.out.println("\n                                                                                        Login Successful!");
                System.out.println("                                                                                        Press Enter to continue...");
                scanner.nextLine();
                UserType.user_type_menu(); // Redirecting to StudentMenu
            } else {
                System.out.println("\n                                                                                        Invalid Username or Password.");
            }
        }
    }

    private static boolean validateAdminLogin(String username, String password) {
        try (Scanner fileScanner = new Scanner(new java.io.File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                return username.equals(credentials[0]) && password.equals(credentials[1]);
            }
        } catch (java.io.IOException e) {
            System.out.println("                                                                                         Error: Could not open registrar credentials file.");
        }
        return false;
    }

    private static void createAdminAccount() {
        String newUsername, newPassword, confirmPassword, phone;

        // New username input
        System.out.print("\n                                                                                              Enter new username: ");
        newUsername = scanner.nextLine().trim();

        // Password Strength Check Loop - password must be at least 6 characters
        while (true) {
            System.out.print("                                                                                                Enter new password: ");
            newPassword = scanner.nextLine();
            if (newPassword.length() < 6) {
                System.out.println("\n                                                                                        Weak password! Password must be at least 6 characters long. Please try again.");
            } else {
                System.out.println("\n                                                                                        Strong password.");
                break;
            }
        }

        // Confirm Password loop - loop until both passwords match
        while (true) {
            System.out.print("                                                                                                Confirm password: ");
            confirmPassword = scanner.nextLine();
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("\n                                                                                        Passwords do not match. Try again.");
            } else {
                break;
            }
        }

        // Phone number validation loop
        while (true) {
            System.out.print("                                                                                                Enter an 11-digit phone number: ");
            phone = scanner.nextLine();
            if (phone.matches("\\d{11}")) {
                break;
            } else {
                System.out.println("\n                                                                                        Invalid phone number. It must be 11 digits.");
            }
        }

        // Now that everything is valid, save the credentials
        updateAdminCredentials(newUsername, newPassword, phone);
        System.out.println("\n                                                                                        Admin account created successfully!");
        UserType.user_type_menu();
    }

    private static void updateAdminCredentials(String username, String password, String phone) {
        try (java.io.FileWriter writer = new java.io.FileWriter(CREDENTIALS_FILE)) {
            writer.write(username + " " + password + " " + phone);
        } catch (java.io.IOException e) {
            System.out.println("                                                                                          Error: Could not update registrar credentials file.");
        }
    }

    private static void studentLogin() {
        System.out.println("\n                                                                                        Student Login Successful!");
        StudentMenu.student_menu();  // Direct to StudentMenu
    }
}

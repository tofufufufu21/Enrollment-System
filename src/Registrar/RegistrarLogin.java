package Registrar;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Admin_Portal.AdminPortal;
import Enrollment.InitializeStrands;
import Enrollment.Student;
import Login.Login;

public class RegistrarLogin {
    private static final String CREDENTIALS_FILE = "registrar_credentials.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_ATTEMPTS = 5;

    public static void registrar_menu() {
        char choice;

        do {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("=====================================================================================");
            System.out.println("||          REGISTRAR MENU         ||");
            System.out.println("=====================================================================================");
            System.out.println("||  1. Login                       ||");
            System.out.println("||  0. Go Back to Main Menu        ||");
            System.out.println("=====================================================================================");
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '0':
                    return; // Go back to main menu
                case '1':
                    registrarLogin(); // Proceed to login
                    break;
                default:
                    System.out.println("Invalid input. Try again...");
            }
        } while (true);
    }

    // Registrar login with attempts and forgot password feature
    private static void registrarLogin() {
        String username, password;
        int attempts = 0;

        InitializeStrands initializeStrands = new InitializeStrands();
        List<Student.Strand> strands = initializeStrands.initializeAllStrands();
        Student[] students = new Student[100];
        int[] studentCount = new int[1];

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("=====================================================================================");
            System.out.println("||        REGISTRAR LOGIN          ||");
            System.out.println("=====================================================================================");
            System.out.print("Enter Username: ");
            username = scanner.nextLine();

            // Password input with asterisks
            System.out.print("Enter Password: ");
            char[] passwordArray = System.console().readPassword();
            password = new String(passwordArray);

            if (userValidateRegistrarLogin(username, password)) {
                System.out.println("\nLogin Successful!\n");
                AdminPortal.adminPortal(username, students, studentCount);
                return;
            } else {
                attempts++;
                System.out.println("\nInvalid Username or Password. Attempt " + attempts + "/" + MAX_ATTEMPTS);
            }
        }

        // Options after 5 failed attempts
        System.out.println("\nYou have reached the maximum number of login attempts.");
        System.out.println("=====================================================================================");
        System.out.println("||  [1] Forgot Password?           ||");
        System.out.println("||  [0] Go Back to Menu            ||");
        System.out.println("=====================================================================================");
        System.out.print("Select your Choice: ");
        char choice = scanner.next().charAt(0);
        scanner.nextLine();

        if (choice == '1') {
            resetCredentials();
        }
    }

    // Validate registrar login credentials
    private static boolean userValidateRegistrarLogin(String username, String password) {
        String correctUsername;
        String correctPassword;

        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                correctUsername = credentials[0];
                correctPassword = credentials[1];
                return username.equals(correctUsername) && password.equals(correctPassword);
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }
        return false;
    }

    // Reset credentials when user selects "Forgot password"
    private static void resetCredentials() {
        System.out.println("\nWhat did you forget?");
        System.out.println("=====================================================================================");
        System.out.println("||  [s] Forgot Username            ||");
        System.out.println("||  [p] Forgot Password            ||");
        System.out.println("||  [b] Forgot Both                ||");
        System.out.println("||  [0] Go Back to Login           ||");
        System.out.println("=====================================================================================");
        System.out.print("Select your Choice: ");
        char choice = scanner.next().charAt(0);
        scanner.nextLine();

        switch (choice) {
            case 's':
                verifyPhoneNumber("username");
                break;
            case 'p':
                verifyPhoneNumber("password");
                break;
            case 'b':
                verifyPhoneNumber("both");
                break;
            case '0':
                Login.loginMenu(); // Directly call Login.loginMenu() instead of creating a new method
                break;
            default:
                System.out.println("Invalid choice. Returning to login...");
                Login.loginMenu(); // Directly call Login.loginMenu() instead of creating a new method
        }
    }

    // Verify phone number for security
    private static void verifyPhoneNumber(String resetType) {
        String phone;
        int attempts = 0;

        // Read the phone number from the registrar_credentials.txt file
        String correctPhoneNumber = null;

        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                if (credentials.length > 2) {
                    correctPhoneNumber = credentials[2]; // Assuming phone number is the 3rd item in the file (index 2)
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }

        if (correctPhoneNumber == null) {
            System.out.println("Error: Phone number not found in credentials file.");
            Login.loginMenu();
            return;
        }

        // Prompt user to input phone number and compare with the one in the file
        System.out.print("\nEnter your phone number for security: ");
        while (attempts < 5) {
            phone = scanner.nextLine();
            if (phone.equals(correctPhoneNumber)) {
                System.out.println("Phone number verified successfully.");
                resetAccount(resetType);
                return;
            } else {
                attempts++;
                System.out.println("Invalid phone number. Please enter the correct phone number.");
            }
        }

        // If the user fails 5 times, lock the account
        System.out.println("\nWrong phone number entered 5 times. Your account has been locked.");
        System.out.println("Please contact I.T support for more information.");
        Login.loginMenu(); // Redirect back to Login menu
    }

    // Reset the credentials if the phone number matches
    private static void resetAccount(String resetType) {
        String newUsername = null;
        String newPassword = null;

        if (resetType.equals("username") || resetType.equals("both")) {
            System.out.print("Enter new username: ");
            newUsername = scanner.nextLine().trim();
        }

        if (resetType.equals("password") || resetType.equals("both")) {
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine();
        }

        // Read current credentials
        String[] currentCredentials = readCurrentCredentials();
        if (currentCredentials == null) {
            System.out.println("Error: Could not read current credentials.");
            return;
        }

        String currentUsername = currentCredentials[0];
        String currentPassword = currentCredentials[1];
        String currentPhone = currentCredentials.length > 2 ? currentCredentials[2] : "";

        // Update the required field and leave the others unchanged
        if (resetType.equals("both")) {
            if (newUsername == null || newPassword == null) {
                System.out.println("\nError: Both username and password are required. Please try again.");
                resetAccount("both");
                return;
            }
        } else if (resetType.equals("username") && newUsername == null) {
            System.out.println("\nError: Username cannot be empty. Please try again.");
            resetAccount("username");
            return;
        } else if (resetType.equals("password") && newPassword == null) {
            System.out.println("\nError: Password cannot be empty. Please try again.");
            resetAccount("password");
            return;
        }

        // If username and password are the same, we will only update the necessary part
        if (resetType.equals("username")) {
            updateCredentials(newUsername, currentPassword, currentPhone);
        } else if (resetType.equals("password")) {
            updateCredentials(currentUsername, newPassword, currentPhone);
        } else if (resetType.equals("both")) {
            updateCredentials(newUsername, newPassword, currentPhone);
        }

        System.out.println("\nCredentials updated successfully!\n");
    }

    // Read current credentials from the file
    private static String[] readCurrentCredentials() {
        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                return fileScanner.nextLine().split(" ");
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }
        return null;
    }

    // Update credentials in the file
    private static void updateCredentials(String username, String password, String phone) {
        File file = new File(CREDENTIALS_FILE);

        try (FileWriter writer = new FileWriter(file, false)) {  // Set `false` to overwrite the file
            // Write the new username, password, and phone number (if available)
            writer.write(username);
            if (password != null) {
                writer.write(" " + password);
            }
            if (phone != null && !phone.isEmpty()) {
                writer.write(" " + phone);
            }
            writer.flush();  // Ensures data is written to the file
        } catch (IOException e) {
            System.out.println("Error: Could not update registrar credentials file.");
            e.printStackTrace();
        }
    }
}

package Registrar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import Login.Login;

public class Register {
    public static final String CREDENTIALS_FILE = "registrar_credentials.txt"; // Adjust path if necessary
    public static final Scanner scanner = new Scanner(System.in);

    public static void verifyPhoneNumber(String resetType) {
        String phone;
        int attempts = 0;
        String correctPhoneNumber = null;

        // Reading the correct phone number from the file
        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                if (credentials.length > 2) {
                    correctPhoneNumber = credentials[2];
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }

        if (correctPhoneNumber == null) {
            System.out.println("Error: Phone number not found in credentials file.");
            Login.loginMenu(); // Adjust if necessary
            return;
        }

        // Asking user for phone number and validating the input
        System.out.print("\nEnter your phone number for security: ");
        while (attempts < 5) {
            phone = scanner.nextLine();
            attempts++; // Incrementing the attempt counter

            if (phone.equals(correctPhoneNumber)) {
                System.out.println("Phone number verified successfully.");
                resetAccount(resetType);
                return;
            } else {
                System.out.println("Invalid phone number. Attempt " + attempts + " of 5. Please enter the correct phone number.");
                System.out.print("\nEnter your phone number for security: ");
            }
        }

        // After 5 failed attempts
        System.out.println("\nWrong phone number entered 5 times. Your account has been locked.");
        System.out.println("Please contact I.T support for more information.");
        Login.loginMenu(); // Adjust if necessary
    }

    public static void resetAccount(String resetType) {
        String newUsername = null;
        String newPassword = null;

        if (resetType.equals("username") || resetType.equals("both")) {
            System.out.print("Enter new username: ");
            newUsername = scanner.nextLine().trim();
        }

        if (resetType.equals("password") || resetType.equals("both")) {
            // Loop until a strong password is entered
            while (true) {
                System.out.print("Enter new password (at least 6 characters): ");
                newPassword = scanner.nextLine();

                if (newPassword.length() < 6) {
                    System.out.println("\nWeak password. Password must be at least 6 characters long.");
                } else {
                    System.out.println("\nStrong password.");
                    break; // Exit the loop once a strong password is entered
                }
            }
        }

        String[] currentCredentials = readCurrentCredentials();
        if (currentCredentials == null) {
            System.out.println("\nError: Could not read current credentials.");
            return;
        }

        String currentUsername = currentCredentials[0];
        String currentPassword = currentCredentials[1];
        String currentPhone = currentCredentials.length > 2 ? currentCredentials[2] : "";

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

        if (resetType.equals("username")) {
            updateCredentials(newUsername, currentPassword, currentPhone);
        } else if (resetType.equals("password")) {
            updateCredentials(currentUsername, newPassword, currentPhone);
        } else if (resetType.equals("both")) {
            updateCredentials(newUsername, newPassword, currentPhone);
        }

        System.out.println("\nCredentials updated successfully!\n");
    }

    public static String[] readCurrentCredentials() {
        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                return fileScanner.nextLine().split(" ");
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }
        return null;
    }

    public static void updateCredentials(String username, String password, String phone) {
        File file = new File(CREDENTIALS_FILE);

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(username);
            if (password != null) {
                writer.write(" " + password);
            }
            if (phone != null && !phone.isEmpty()) {
                writer.write(" " + phone);
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error: Could not update registrar credentials file.");
            e.printStackTrace();
        }
    }

    public static void showMaxAttemptsMenu() {
        System.out.println("\nYou have reached the maximum number of login attempts.");
        System.out.println("=====================================================================================");
        System.out.println("||  [1] Forgot Password?           ||");
        System.out.println("||  [0] Go Back to Menu            ||");
        System.out.println("=====================================================================================");

        char choice;
        do {
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1':
                    resetCredentials();
                    return;
                case '0':
                    Login.loginMenu();
                    return;
                default:
                    System.out.println("\nInvalid input. Please select a valid option.");
            }
        } while (true);
    }

    public static void resetCredentials() {
        char choice;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("\nWhat did you forget?");
            System.out.println("=====================================================================================");
            System.out.println("||  [s] Forgot Username            ||");
            System.out.println("||  [p] Forgot Password            ||");
            System.out.println("||  [b] Forgot Both                ||");
            System.out.println("||  [0] Go Back to Login           ||");
            System.out.println("=====================================================================================");
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case 's':
                    verifyPhoneNumber("username");
                    validChoice = true;
                    break;
                case 'p':
                    verifyPhoneNumber("password");
                    validChoice = true;
                    break;
                case 'b':
                    verifyPhoneNumber("both");
                    validChoice = true;
                    break;
                case '0':
                    Login.loginMenu();
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }
}

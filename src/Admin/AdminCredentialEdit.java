package Admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import Enrollment.Student;

public class AdminCredentialEdit {
    private static final String CREDENTIALS_FILE = "registrar_credentials.txt"; // Adjust path if needed

    public static void editCredentials(Scanner scanner) {
        String[] credentials = readCredentials();

        if (credentials == null || credentials.length < 3) {
            System.out.println("Error: Unable to read registrar credentials.");
            return;
        }

        String currentUsername = credentials[0];
        String currentPassword = credentials[1];
        String currentPhone = credentials[2];

        System.out.println("Current Credentials:");
        System.out.println("Username: " + currentUsername);
        System.out.println("Password: " + maskPassword(currentPassword));
        System.out.println("Contact Number: " + currentPhone);

        // Edit username
        currentUsername = editField(scanner, "username", currentUsername);

        // Edit password
        currentPassword = editField(scanner, "password", currentPassword);

        // Edit contact number
        currentPhone = editField(scanner, "contact number", currentPhone);

        // Update credentials
        updateCredentials(currentUsername, currentPassword, currentPhone);
        System.out.println("\nCredentials updated successfully!");

        // Return to role-specific menu
        Student.promptReturnBasedOnRole(scanner);
    }

    private static String editField(Scanner scanner, String fieldName, String currentValue) {
        String newValue = currentValue;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Do you want to edit the " + fieldName + "? (y/n): ");
            String response = scanner.nextLine().trim();

            if (response.equalsIgnoreCase("y")) {
                System.out.print("Enter new " + fieldName + ": ");
                newValue = scanner.nextLine().trim();
                validInput = true;
            } else if (response.equalsIgnoreCase("n")) {
                validInput = true;
            } else {
                System.out.println("\nInvalid input. Please enter 'y' or 'n'.");
            }
        }

        return newValue;
    }

    private static String[] readCredentials() {
        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                return fileScanner.nextLine().split(" ");
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read the credentials file.");
        }
        return null;
    }

    private static void updateCredentials(String username, String password, String phone) {
        try (FileWriter writer = new FileWriter(CREDENTIALS_FILE, false)) {
            writer.write(username + " " + password + " " + phone);
        } catch (IOException e) {
            System.out.println("Error: Could not update the credentials file.");
            e.printStackTrace();
        }
    }

    private static String maskPassword(String password) {
        return "*".repeat(password.length());
    }
}

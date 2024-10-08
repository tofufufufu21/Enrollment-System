package Registrar;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import User_Types.*;

public class RegistrarLogin {
    private static final String CREDENTIALS_FILE = "registrar_credentials.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void registrar_menu() {
        char choice;
        generateDefaultAdminCredentials(); // Generate credentials if not exist

        do {
            System.out.println("\nREGISTRAR MENU");
            System.out.println("1. Login");
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);

            switch (choice) {
                case '0':
                    return; // Go back to the main menu
                case '1':
                    registrarLogin();
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine(); // Wait for user to press Enter
                    scanner.nextLine(); // Handle newline
            }
        } while (choice != '0');
    }

    // Function to perform registrar login
    private static void registrarLogin() {
        String username, password;

        while (true) { // Loop until valid login
            System.out.println("\nRegistrar Login");
            System.out.print("Enter Username: ");
            username = scanner.next(); // Get username input

            System.out.print("Enter Password: ");
            password = readPassword(); // Get password input

            // Validate login
            if (userValidateRegistrarLogin(username, password)) {
                System.out.println("\nLogin Successful!\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine(); // Wait for user to press Enter
                scanner.nextLine(); // Handle newline
                adminPortal(username); // Proceed to admin portal
                break; // Exit loop on successful login
            } else {
                System.out.println("\nInvalid Username or Password. Please Try Again.\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine(); // Wait for user to press Enter
                scanner.nextLine(); // Handle newline
            }
        }
    }

    // Function to read password securely
    private static String readPassword() {
        StringBuilder password = new StringBuilder();
        char ch;

        // Hide password input
        while (true) {
            ch = scanner.nextLine().charAt(0);
            if (ch == '\r') {
                break; // End input on Enter key
            } else {
                password.append(ch); // Append character to password
                System.out.print("*"); // Print asterisk for each character
            }
        }
        return password.toString(); // Return entered password
    }

    // Function to validate registrar login
    private static boolean userValidateRegistrarLogin(String username, String password) {
        String correctUsername;
        String correctPassword;

        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                correctUsername = credentials[0];
                correctPassword = credentials[1];

                // Compare entered credentials with correct ones
                return username.equals(correctUsername) && password.equals(correctPassword);
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }

        return false; // Return false if login is invalid
    }

    // Function to generate default admin credentials
    private static void generateDefaultAdminCredentials() {
        try (FileWriter writer = new FileWriter(CREDENTIALS_FILE)) {
            writer.write("admin admin"); // Username: admin, Password: admin
        } catch (IOException e) {
            System.out.println("Error: Could not create registrar credentials file.");
        }
    }

    // Placeholder method for admin portal
    private static void adminPortal(String username) {
        // Logic for admin portal goes here
        System.out.println("Welcome to the Admin Portal, " + username + "!");
    }
}

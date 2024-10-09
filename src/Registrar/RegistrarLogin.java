package Registrar;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Admin_Portal.*;

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
            scanner.nextLine(); // Handle newline

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
            }
        } while (choice != '0');
    }

    // Function to perform registrar login
    private static void registrarLogin() {
        String username, password;

        // Example data for strands, students, and student count.
        Enroll_Student.Strand[] strands11 = {}; // Initialize with actual data
        Enroll_Student.Strand[] strands12 = {}; // Initialize with actual data
        Enroll_Student.Student[] students = new Enroll_Student.Student[100]; // Adjust size as needed
        int[] studentCount = new int[1]; // Adjust as needed

        while (true) { // Loop until valid login
            System.out.println("\nRegistrar Login");
            System.out.print("Enter Username: ");
            username = scanner.nextLine(); // Get username input

            System.out.print("Enter Password: ");
            password = scanner.nextLine(); // Get password input

            // Validate login
            if (userValidateRegistrarLogin(username, password)) {
                System.out.println("\nLogin Successful!\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine(); // Wait for user to press Enter
                AdminPortal.adminPortal(username, strands11, strands12, students, studentCount); // Call with all required arguments
                break; // Exit loop on successful login
            } else {
                System.out.println("\nInvalid Username or Password. Please Try Again.\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine(); // Wait for user to press Enter
            }
        }
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
}

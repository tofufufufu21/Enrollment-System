package Registrar;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Admin_Portal.AdminPortal;
import Enrollment.InitializeStrands;
import Enrollment.Student;

public class RegistrarLogin {
    private static final String CREDENTIALS_FILE = "registrar_credentials.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_ATTEMPTS = 5;

    public static void registrar_menu() {
        char choice;
        generateDefaultAdminCredentials();

        do {
            System.out.println("\nREGISTRAR MENU");
            System.out.println("1. Login");
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '0':
                    return;
                case '1':
                    registrarLogin();
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
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
            System.out.println("\nRegistrar Login");
            System.out.print("Enter Username: ");
            username = scanner.nextLine();

            System.out.print("Enter Password: ");
            password = scanner.nextLine();

            if (userValidateRegistrarLogin(username, password)) {
                System.out.println("\nLogin Successful!\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                AdminPortal.adminPortal(username, students, studentCount);
                return;
            } else {
                attempts++;
                System.out.println("\nInvalid Username or Password. Attempt " + attempts + "/" + MAX_ATTEMPTS);
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }

        // Options after 5 failed attempts
        System.out.println("\nYou have reached the maximum number of login attempts.");
        System.out.println("[1] Forgot password?");
        System.out.println("[0] Go back to menu");
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

    // Generate default admin credentials if file does not exist
    private static void generateDefaultAdminCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(CREDENTIALS_FILE)) {
                writer.write("admin admin");
            } catch (IOException e) {
                System.out.println("Error: Could not create registrar credentials file.");
            }
        }
    }

    // Reset credentials when user selects "Forgot password"
    // Reset credentials when user selects "Forgot password"
    private static void resetCredentials() {
        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
            updateCredentials(newUsername, newPassword);
            System.out.println("\nCredentials updated successfully!\n");
        } else {
            System.out.println("\nError: Username and password cannot be empty. Please try again.");
        }
    }


    // Update credentials in the file
    private static void updateCredentials(String username, String password) {
        File file = new File(CREDENTIALS_FILE);

        try (FileWriter writer = new FileWriter(file, false)) {  // Set `false` to overwrite the file
            writer.write(username + " " + password);
            writer.flush();  // Ensures data is written to the file
        } catch (IOException e) {
            System.out.println("Error: Could not update registrar credentials file.");
            e.printStackTrace();
        }
    }
}

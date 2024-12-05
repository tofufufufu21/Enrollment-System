package Registrar;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import Enrollment.InitializeStrands;
import Enrollment.Student;
import Admin_Portal.AdminPortal;

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
            password = maskPasswordInput();

            if (userValidateRegistrarLogin(username, password)) {
                System.out.println("\nLogin Successful!\n");
                AdminPortal.adminPortal(username, students, studentCount);
                return;
            } else {
                attempts++;
                System.out.println("\nInvalid Username or Password. Attempt " + attempts + "/" + MAX_ATTEMPTS);
            }
        }

        // If attempts are exhausted
        Register.showMaxAttemptsMenu();
    }

    private static String maskPasswordInput() {
        char[] passwordArray = System.console().readPassword();
        String password = new String(passwordArray);
        return maskPassword(password);
    }

    private static String maskPassword(String password) {
        return "*".repeat(password.length());
    }

    private static boolean userValidateRegistrarLogin(String username, String password) {
        try (Scanner fileScanner = new Scanner(new File(CREDENTIALS_FILE))) {
            if (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(" ");
                if (credentials.length >= 2) {
                    return username.equals(credentials[0]) && password.equals(credentials[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open registrar credentials file.");
        }
        return false;
    }
}

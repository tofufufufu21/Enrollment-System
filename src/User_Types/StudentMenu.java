package User_Types;

import java.util.List;
import java.util.Scanner;
import Enrollment.Student;
import Login.Login;
import User_Accounting.Accounting; // Import the Accounting class
import StudentDashboard.Dashboard; // Import the Dashboard class
import Enrollment.InitializeStrands; // Import InitializeStrands to load strands

public class StudentMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void student_menu() {
        InitializeStrands initializeStrands = new InitializeStrands();
        // Load all strands once at the beginning
        List<Student.Strand> strands = initializeStrands.initializeAllStrands();
        char choice = ' ';

        while (choice != '0') {
            System.out.println("\n                                                                                        =====================================");
            System.out.println("                                                                                        ||       STUDENT MENU              ||");
            System.out.println("                                                                                        =====================================");
            System.out.println("                                                                                        ||  1. Accounting                  ||");
            System.out.println("                                                                                        ||  2. Student Dashboard           ||");
            System.out.println("                                                                                        ||  0. Logout                      ||");
            System.out.println("                                                                                        =====================================");
            System.out.print("                                                                                                Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1':
                    if ("Student".equals(Login.currentUserType)) {  // Check if logged in as Student
                        System.out.println("                                                                                        Redirecting to Accounting...");
                        Accounting accounting = new Accounting(); // Create an instance of Accounting
                        accounting.studentLogin();
                    } else {
                        System.out.println("                                                                                        Access denied. You are logged in as " + Login.currentUserType + ".");
                    }
                    break;
                case '2':
                    if ("Student".equals(Login.currentUserType)) {  // Check if logged in as Student
                        System.out.println("                                                                                        Redirecting to Student Dashboard...");
                        Dashboard dashboard = new Dashboard(strands);
                        dashboard.login(); // Add call to Student Dashboard
                    } else {
                        System.out.println("                                                                                        Access denied. You are logged in as " + Login.currentUserType + ".");
                    }
                    break;
                case '0':
                    while (true) {
                        System.out.print("\n                                                                                        Do you really want to go back to Login (y/n): ");
                        char option = scanner.next().charAt(0);
                        scanner.nextLine(); // Handle newline

                        if (option == 'y' || option == 'Y') {
                            System.out.println("\n                                                                                        Thank you.....");
                            Login.loginMenu();
                        } else if (option == 'n' || option == 'N') {
                            break;  // Exit the confirmation loop and return to the main menu
                        } else {
                            System.out.println("                                                                                        Invalid option. Press Enter to continue...");
                            scanner.nextLine();
                        }
                    }
                    break;

                default:
                    System.out.println("\nInvalid input. Press Enter to continue...");
                    scanner.nextLine();
                    break;
            }
        }
    }
}

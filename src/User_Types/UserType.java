package User_Types;

import java.util.List; // Import List
import java.util.Scanner;

import Enrollment.Student;
import Registrar.*;
import User_Accounting.Accounting; // Import the Accounting class
import StudentDashboard.Dashboard; // Import the Dashboard class
import Enrollment.InitializeStrands; // Import InitializeStrands to load strands

public class UserType {
    public static void user_type_menu() {
        Scanner scanner = new Scanner(System.in);
        InitializeStrands initializeStrands = new InitializeStrands();

        // Load all strands once at the beginning
        List<Student.Strand> strands = initializeStrands.initializeAllStrands();

        while (true) {
            // Simulate centering with blank lines and spaces
            System.out.println("\n\n\n\n\n"); // Push the menu down (adjust for centering)
            System.out.println("                                                                                        ===========================================");
            System.out.println("                                                                                        ||          WELCOME TO MAIN MENU         ||");
            System.out.println("                                                                                        ===========================================");
            System.out.println("                                                                                        ||  1. Registrar                         ||");
            System.out.println("                                                                                        ||  2. Accounting                        ||");
            System.out.println("                                                                                        ||  3. Student Dashboard                 ||");
            System.out.println("                                                                                        ||                                       ||");
            System.out.println("                                                                                        ||  0. Exit                              ||");
            System.out.println("                                                                                        ===========================================");
            System.out.print("                                                                                                Select your choice: ");



            char userType = scanner.next().charAt(0);
            scanner.nextLine(); // Handle newline

            switch (userType) {
                case '1':
                    System.out.println("                                                                                        Registrar Module");
                    RegistrarLogin registrarlogin = new RegistrarLogin();
                    registrarlogin.registrar_menu();
                    break;

                case '2':
                    System.out.println("                                                                                        Accounting Module");
                    Accounting accounting = new Accounting(); // Create an instance of Accounting
                    accounting.studentLogin(); // Call the studentLogin method
                    break;

                case '3':
                    System.out.println("                                                                                        Student Dashboard Module");
                    // Create an instance of Dashboard and pass the strands
                    Dashboard dashboard = new Dashboard(strands);
                    dashboard.login(); // Call the login method to start the dashboard
                    break;

                case '0':
                    while (true) {
                        System.out.print("\n                                                                                        Do you really want to exit the program (y/n): ");
                        char option = scanner.next().charAt(0);
                        scanner.nextLine(); // Handle newline

                        if (option == 'y' || option == 'Y') {
                            System.out.println("\n                                                                                        Thank you.....");
                            System.exit(0);
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
            System.out.println();  // For a cleaner new line
        }
    }
}

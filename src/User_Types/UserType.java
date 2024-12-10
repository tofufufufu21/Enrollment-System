package User_Types;

import java.util.List;
import java.util.Scanner;
import Enrollment.Student;
import Login.Login;
import Registrar.RegistrarLogin;
import User_Accounting.Accounting;
import StudentDashboard.Dashboard;
import Enrollment.InitializeStrands;
import Admin.AdminCredentialEdit;

public class UserType {
    public static void user_type_menu() {
        Scanner scanner = new Scanner(System.in);
        InitializeStrands initializeStrands = new InitializeStrands();
        List<Student.Strand> strands = initializeStrands.initializeAllStrands();

        System.out.println("\n\n\n\n\n");
        System.out.println("                                                                                        ===========================================");
        System.out.println("                                                                                        ||          WELCOME TO MAIN MENU         ||");
        System.out.println("                                                                                        ===========================================");
        System.out.println("                                                                                        ||  Logged in as: " + Login.currentUserType + " ||");  // Display user type
        System.out.println("                                                                                        ===========================================");
        System.out.println("                                                                                        ||  1. Registrar                         ||");
        System.out.println("                                                                                        ||  2. Accounting                        ||");
        System.out.println("                                                                                        ||  3. Student Dashboard                 ||");
        System.out.println("                                                                                        ||  4. Admin                              ||");
        System.out.println("                                                                                        ||                                       ||");
        System.out.println("                                                                                        ||  0. Exit                              ||");
        System.out.println("                                                                                        ===========================================");
        System.out.print("                                                                                                Select your choice: ");

        char userType = scanner.next().charAt(0);
        scanner.nextLine();

        switch (userType) {
            case '1':
                System.out.println("                                                                                        Registrar Module");
                RegistrarLogin registrarlogin = new RegistrarLogin();
                registrarlogin.registrar_menu();
                break;
            case '2':
                System.out.println("                                                                                        Accounting Module");
                Accounting accounting = new Accounting();
                accounting.studentLogin();
                break;
            case '3':
                System.out.println("                                                                                        Student Dashboard Module");
                Dashboard dashboard = new Dashboard(strands);
                dashboard.login();
                break;
            case '4':
                System.out.println("                                                                                        Admin Credential Edit Module");
                AdminCredentialEdit.editCredentials(scanner);
                break;
            case '0':
                while (true) {
                    System.out.print("\n                                                                                        Do you really want to go back to Login (y/n): ");
                    char option = scanner.next().charAt(0);
                    scanner.nextLine();

                    if (option == 'y' || option == 'Y') {
                        System.out.println("\n                                                                                        Going back to menu.....");
                        Login.loginMenu();
                    } else if (option == 'n' || option == 'N') {
                        user_type_menu();
                    } else {
                        System.out.println("                                                                                        Invalid option. Press Enter to continue...");
                        scanner.nextLine();
                    }
                }

            default:
                System.out.println("\n                                                                                        Invalid input. Press Enter to continue...");
                scanner.nextLine();
                user_type_menu();
        }
        System.out.println();  // For a cleaner new line
    }
}

package User_Types;

import java.util.Scanner;
import Registrar.*;

public class UserType {
    public static void user_type_menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("MAIN MENU");
            System.out.println("1. Registrar");
            System.out.println("2. Accounting");
            System.out.println("3. Student Dashboard");
            System.out.println("\n0. Exit");
            System.out.print("Select your choice: ");

            char userType = scanner.next().charAt(0);
            scanner.nextLine(); // Handle newline

            switch (userType) {
                case '1':
                    System.out.println("Registrar Module");
                    RegistrarLogin registrarlogin = new RegistrarLogin();
                    registrarlogin.registrar_menu();
                    break;

                case '2':
                    System.out.println("Accounting Module");
                    break;

                case '3':
                    System.out.println("Student Dashboard Module");
                    break;

                case '0':
                    while (true) {
                        System.out.print("\nDo you really want to exit the program (y/n): ");
                        char option = scanner.next().charAt(0);
                        scanner.nextLine(); // Handle newline

                        if (option == 'y' || option == 'Y') {
                            System.out.println("\nThank you.....");
                            System.exit(0);
                        } else if (option == 'n' || option == 'N') {
                            break;  // Exit the confirmation loop and return to the main menu
                        } else {
                            System.out.println("Invalid option. Press Enter to continue...");
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

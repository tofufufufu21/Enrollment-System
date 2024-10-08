package User_Types;

import java.util.Scanner;
import Registrar.*;

public class UserType {
    public static void user_type_menu() {
        Scanner scanner = new Scanner(System.in);
        char userType, option;

        do {
            System.out.println("MAIN MENU");
            System.out.println("1. Registrar");
            System.out.println("2. Accounting");
            System.out.println("3. Student Dashboard");
            System.out.println("\n0. Exit");
            System.out.print("Select your choice: ");
            userType = scanner.next().charAt(0);

            switch (userType) {
                case '0':
                    do {
                        System.out.print("\nDo you really want to exit the program (y/n): ");
                        option = scanner.next().charAt(0);

                        if (option == 'y' || option == 'Y') {
                            System.out.println("\nThank you.....");
                            System.exit(0);
                        } else if (option == 'n' || option == 'N') {
                            user_type_menu();  // Call the static method again
                        } else {
                            System.out.println("Invalid option.");
                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();  // Press any key
                            scanner.nextLine();  // Handle newline
                        }
                    } while (true);

                case '1':
                    System.out.println("Registrar Module");
                    System.out.println("Press Enter to continue...");
                    RegistrarLogin registrarlogin = new RegistrarLogin();
                    registrarlogin.registrar_menu();
                    scanner.nextLine();
                    scanner.nextLine();
                    break;

                case '2':
                    System.out.println("Accounting Module");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;

                case '3':
                    System.out.println("Student Dashboard Module");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;

                default:
                    System.out.println("\nInvalid input. Try again...");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
            }
        } while (userType != '0');
    }
}

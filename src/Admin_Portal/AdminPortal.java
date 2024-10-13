package Admin_Portal;

import Enrollment.Enroll_Student;
import Enrollment.ShowAllStudents;
import Enrollment.SearchStudent; // Import the SearchStudent class

import java.util.Scanner;

public class AdminPortal {
    private static Scanner scanner = new Scanner(System.in);

    public static void adminPortal(String name, Enroll_Student.Strand[] strands11, Enroll_Student.Strand[] strands12, Enroll_Student.Student[] students, int[] studentCount) {
        char choice, option;
        Enroll_Student enrollStudent = new Enroll_Student(); // Create an instance of Enroll_Student
        ShowAllStudents showAllStudents = new ShowAllStudents(); // Create an instance of ShowAllStudents
        SearchStudent searchStudent = new SearchStudent(); // Create an instance of SearchStudent

        while (true) {
            System.out.println("Welcome, " + name + "!\n");
            System.out.println("REGISTRAR DASHBOARD");
            System.out.println("1. Enroll a New Student");
            System.out.println("2. Show all Students");
            System.out.println("3. Search a Student");
            System.out.println("4. Edit Student Information");
            System.out.println("5. Delete a Student");
            System.out.println("\n0. Log Out\n");
            System.out.print("Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Handle newline

            switch (choice) {
                case '1':
                    // Call the enrollStudent method from Enroll_Student class
                    enrollStudent.enrollStudent(strands11, strands12, students, studentCount);
                    break;
                case '2':
                    System.out.println("Showing all students from CSV...");
                    showAllStudents.showAllFromCSV(); // Call the new method to show all students
                    pressAnyKey();
                    break;
                case '3':
                    System.out.println("Searching a student...");
                    searchStudent.searchStudentById(); // Call the searchStudentById method
                    pressAnyKey();
                    break;
                case '4':
                    System.out.println("Editing student information...");
                    // Code for editing student information goes here
                    break;
                case '5':
                    System.out.println("Do you really want to delete a student? (y/n): ");
                    option = scanner.next().charAt(0);
                    if (option == 'y' || option == 'Y') {
                        // Code for deleting a student goes here
                    } else if (option == 'n' || option == 'N') {
                        pressAnyKey();
                    } else {
                        System.out.println("Invalid option.");
                        pressAnyKey();
                    }
                    break;
                case '0':
                    System.out.println("Do you really want to log out? (y/n): ");
                    option = scanner.next().charAt(0);
                    if (option == 'y' || option == 'Y') {
                        return; // Log out and exit the method
                    } else if (option == 'n' || option == 'N') {
                        continue; // Stay in the loop
                    } else {
                        System.out.println("Invalid option.");
                        pressAnyKey();
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
                    pressAnyKey();
            }
        }
    }

    // Method to simulate "Press any key to continue"
    private static void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}

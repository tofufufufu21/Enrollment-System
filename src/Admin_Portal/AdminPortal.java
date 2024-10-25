package Admin_Portal;

import Enrollment.Enroll_Student;
import Enrollment.ShowAllStudents;
import Enrollment.SearchStudent;
import Enrollment.EditStudent;
import Enrollment.DeleteStudent;
import Enrollment.InitializeStrands;
import Enrollment.Strand;
import Enrollment.Student; // Import the Student class from the Enrollment package

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AdminPortal {
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminPortal(String name, Student[] students, int[] studentCount) { // Use the correct Student type
        char choice;
        Enroll_Student enrollStudent = new Enroll_Student();
        ShowAllStudents showAllStudents = new ShowAllStudents();
        SearchStudent searchStudent = new SearchStudent();
        EditStudent editStudent = new EditStudent();

        InitializeStrands initializeStrands = new InitializeStrands();
        // Load all strands without grade parameter
        List<Strand> strands = initializeStrands.initializeAllStrands();

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
            scanner.nextLine();

            switch (choice) {
                case '1':
                    // Call enrollStudent with List<Strand>
                    enrollStudent.enrollStudent(strands, students, studentCount);
                    break;
                case '2':
                    System.out.println("Showing all students from CSV...");
                    showAllStudents.showAllFromCSV();
                    pressAnyKey();
                    break;
                case '3':
                    System.out.println("Searching a student...");
                    System.out.print("Enter Student ID to search: ");
                    try {
                        int studentId = scanner.nextInt();
                        searchStudent.searchStudentById(studentId);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // Clear invalid input
                    }
                    pressAnyKey();
                    break;
                case '4':
                    System.out.println("Editing student information...");
                    editStudent.editStudentDetails();
                    pressAnyKey();
                    break;
                case '5':
                    System.out.println("Do you really want to delete a student? (y/n): ");
                    char option = scanner.next().charAt(0);
                    if (option == 'y' || option == 'Y') {
                        DeleteStudent deleteStudent = new DeleteStudent();
                        deleteStudent.deleteStudent();
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
                        return; // Log out
                    } else if (option == 'n' || option == 'N') {
                        continue; // Stay logged in
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

    private static void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}

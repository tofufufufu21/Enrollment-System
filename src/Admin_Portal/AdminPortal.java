package Admin_Portal;

import Enrollment.Enroll_Student;
import Enrollment.ShowAllStudents;
import Enrollment.SearchStudent;
import Enrollment.EditStudent;
import Enrollment.DeleteStudent;
import Enrollment.InitializeStrands;
import Enrollment.Student; // Import the Student class from the Enrollment package
import User_Types.UserType;

import java.util.List;
import java.util.Scanner;

public class AdminPortal {
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminPortal(String name, Student[] students, int[] studentCount) {
        char choice;
        Enroll_Student enrollStudent = new Enroll_Student();
        ShowAllStudents showAllStudents = new ShowAllStudents();
        SearchStudent searchStudent = new SearchStudent();
        EditStudent editStudent = new EditStudent();
        InitializeStrands initializeStrands = new InitializeStrands();
        DeleteStudent deleteStudent = new DeleteStudent();

        List<Student.Strand> strands = initializeStrands.initializeAllStrands();

        while (true) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("                                                                                        ===========================================");
            System.out.println("                                                                                        ||            REGISTRAR DASHBOARD        ||");
            System.out.println("                                                                                        ===========================================");
            System.out.println("                                                                                        ||           Welcome, " + name + "!             ||");
            System.out.println("                                                                                        ||                                       ||");
            System.out.println("                                                                                        ||      1. Enroll a New Student          ||");
            System.out.println("                                                                                        ||      2. Show All Students             ||");
            System.out.println("                                                                                        ||      3. Search a Student              ||");
            System.out.println("                                                                                        ||      4. Edit Student Information      ||");
            System.out.println("                                                                                        ||      5. Delete a Student              ||");
            System.out.println("                                                                                        ||      0. Log Out                       ||");
            System.out.println("                                                                                        ===========================================");
            System.out.print("                                                                                               Select your Choice: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1': // Enroll a new student
                    enrollStudent.enrollStudent(strands, students, studentCount);
                    break;

                case '2': // Show all students
                    System.out.println("\nShowing all students from CSV...");
                    showAllStudents.showAllFromCSV();
                    Student.pressAnyKey();
                    break;

                case '3': // Search for a student
                    System.out.println("\nSearching for a student...");
                    searchStudent.searchStudentById(name, students, studentCount);
                    break;

                case '4': // Edit student information
                    System.out.println("\nEditing student information...");
                    editStudent.editStudentDetails(name, students, studentCount);
                    Student.pressAnyKey();
                    break;

                case '5': // Delete a student
                    System.out.println("\nDeleting a student...");
                    deleteStudent.deleteStudent(name, students, studentCount);
                    break;

                case '0': // Log out
                    if (confirmLogout()) {
                        UserType.user_type_menu(); // Redirect to user type menu
                        return;
                    }
                    break;

                default: // Invalid choice
                    System.out.println("\nInvalid choice. Please try again.");
                    Student.pressAnyKey();
                    break;
            }
        }
    }

    // Helper method to confirm logout
    private static boolean confirmLogout() {
        char option;
        while (true) {
            System.out.print("\n                                                                                        Do you really want to log out? (y/n): ");
            option = scanner.next().charAt(0);

            if (option == 'y' || option == 'Y') {
                return true;
            } else if (option == 'n' || option == 'N') {
                return false;
            } else {
                System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }
}

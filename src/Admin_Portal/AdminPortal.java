package Admin_Portal;

import Enrollment.Enroll_Student;
import Enrollment.ShowAllStudents;
import Enrollment.SearchStudent;
import Enrollment.EditStudent;
import Enrollment.DeleteStudent;
import Enrollment.InitializeStrands;
import Enrollment.Student; // Import the Student class from the Enrollment package
import User_Types.UserType;

import java.util.InputMismatchException;
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
            System.out.println("\nWelcome, " + name + "!\n");
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
                    enrollStudent.enrollStudent(strands, students, studentCount);
                    break;
                case '2':
                    System.out.println("Showing all students from CSV...");
                    showAllStudents.showAllFromCSV();
                    Student.pressAnyKey();
                    break;
                case '3':
                    System.out.println("Searching a student...");
                    searchStudent.searchStudentById(name, students, studentCount);  // No need to ask for ID here, it will be handled in the SearchStudent class
                    break;
                case '4':
                    System.out.println("Editing student information...");
                    editStudent.editStudentDetails(name, students, studentCount);
                    Student.pressAnyKey();
                    break;
                case '5':
                    deleteStudent.deleteStudent(name, students, studentCount);  // Calls the method for confirmation and deletion
                    break;
                case '0':
                    char option;
                    while (true) {  // Loop until a valid input is received
                        System.out.println("Do you really want to log out? (y/n): ");
                        option = scanner.next().charAt(0);

                        if (option == 'y' || option == 'Y') {
                            UserType.user_type_menu(); // Log out and go to user type menu
                            return; // Exit the method after logging out
                        } else if (option == 'n' || option == 'N') {
                            adminPortal(name, students, studentCount); // Simply break out of the loop to return to the dashboard
                            return;
                        } else {
                            System.out.println("\nInvalid option. Please enter 'y' or 'n'.\n");
                        }
                    }
                default:
                    System.out.println("\nInvalid choice.");
                    Student.pressAnyKey(); // Use pressAnyKey() method to pause and wait for user to continue
            }
        }
    }

}


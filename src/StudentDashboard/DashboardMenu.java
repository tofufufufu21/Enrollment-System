package StudentDashboard;

import Enrollment.Student;
import Enrollment.Strand;
import Enrollment.Subject;
import java.io.*;
import java.util.Scanner;

public class DashboardMenu {
    private static Scanner scanner = new Scanner(System.in);

    public void showMenu(Student student) {
        if (student == null) {
            System.out.println("No student data available.");
            return;
        }

        System.out.println("Welcome, " + student.getName());
        int choice;

        do {
            System.out.println("\n1. Display Student Details");
            System.out.println("2. Drop or Add Subjects");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    displayStudentInfo(student);
                    break;
                case 2:
                    DashboardDropAddSubject subjectManager = new DashboardDropAddSubject();
                    subjectManager.dropAddSubjects(student); // Call dropAddSubjects
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private void displayStudentInfo(Student student) {
        System.out.println("Student ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Phone Number: " + student.getPhoneNumber());

        Strand selectedStrand = student.getSelectedStrand();
        System.out.println("Strand: " + (selectedStrand != null ? selectedStrand.getName() : "Unknown Strand"));
        System.out.println("Payment Status: " + student.getPaymentStatus());
        System.out.printf("Remaining Balance: %.2f%n", student.getBalance());

        // Display enrolled subjects
        System.out.println("Enrolled Subjects:");
        if (!student.getEnrolledSubjects().isEmpty()) {
            for (Subject subject : student.getEnrolledSubjects()) {
                System.out.println("- " + subject.getSubjectName());
            }
        } else {
            System.out.println("No subjects enrolled.");
        }
    }
}

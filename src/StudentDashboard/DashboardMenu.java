package StudentDashboard;

import Enrollment.Student;
import Enrollment.Strand;
import Enrollment.Subject;

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
            System.out.println("2. Add Subjects");
            System.out.println("3. Drop Subjects");
            System.out.println("4. Exit");
            System.out.print("Please choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    displayStudentInfo(student);
                    break;
                case 2:
                    DashboardAddSubject subjectAdder = new DashboardAddSubject();
                    subjectAdder.addSubject(student); // Call addSubject
                    break;
                case 3:
                    DashboardDropSubject subjectDropper = new DashboardDropSubject();
                    subjectDropper.dropSubject(student); // Call dropSubject
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
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

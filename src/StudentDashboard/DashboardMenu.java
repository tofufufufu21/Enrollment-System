package StudentDashboard;

import Enrollment.Student;
import Enrollment.Subject; // Ensure Subject is imported
import java.util.Scanner;

public class DashboardMenu {
    private static Scanner scanner = new Scanner(System.in);

    public void showMenu(Student student) {
        while (true) {
            System.out.println("\n--- Dashboard Menu ---");
            System.out.println("1. View Student Details");
            System.out.println("2. Drop/Add Subjects");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
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
                    System.out.println("Exiting the dashboard.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayStudentInfo(Student student) {
        System.out.println("\n--- Student Details ---");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Phone Number: " + student.getPhoneNumber());

        // Check if the selected strand is not null before accessing its name
        if (student.getSelectedStrand() != null) {
            String strandName = student.getSelectedStrand().getName(); // Assuming getSelectedStrand() returns a Strand object
            System.out.println("Strand: " + strandName);
        } else {
            System.out.println("Strand: Not selected");
        }

        System.out.println("Payment Status: " + student.getPaymentStatus());
        System.out.printf("Balance: %.2f\n", student.getBalance());

        // Display enrolled subjects
        System.out.println("Enrolled Subjects:");
        if (student.getNumEnrolledSubjects() > 0) {
            for (Subject subject : student.getEnrolledSubjects()) {
                System.out.println(" - " + subject.getSubjectName()); // Use getSubjectName() instead of getName()
            }
        } else {
            System.out.println("No subjects enrolled.");
        }
    }
}

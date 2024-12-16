package StudentDashboard;

import Enrollment.Student;
import Login.Login;

import java.util.Scanner;

public class DashboardMenu {
    private static Scanner scanner = new Scanner(System.in);

    public void showMenu(Student student) {
        if (student == null) {
            System.out.println("\n=======================================");
            System.out.println("⚠ No student data available. Please log in again.");
            System.out.println("=======================================\n");
            return;
        }

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n                                                                                         =======================================");
        System.out.println("                                                                                     🎓 Welcome to the Student Dashboard, " + student.getName() + "!");
        System.out.println("                                                                                         =======================================\n");

        String choice;

        do {

            System.out.println("                                                                                        =====================================\n");
            System.out.println("                                                                                        1. Display Student Details");
            System.out.println("                                                                                        2. Add Subjects");
            System.out.println("                                                                                        3. Drop Subjects");
            System.out.println("                                                                                        4. Exit");
            System.out.println("                                                                                        =====================================\n");
            System.out.print("                                                                                           Please choose an option: ");
            choice = scanner.nextLine();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case "1":
                    displayStudentInfo(student);
                    break;
                case "2":
                    DashboardAddSubject subjectAdder = new DashboardAddSubject();
                    subjectAdder.addSubject(student); // Call addSubject
                    break;
                case "3":
                    DashboardDropSubject subjectDropper = new DashboardDropSubject();
                    subjectDropper.dropSubject(student); // Call dropSubject
                    break;
                case "4":
                    System.out.println("                                                                                           Exiting...");
                    Student.promptToGoBackOrRestartDashboard(Login.currentUserType, student);
                    break;
                default:
                    System.out.println("                                                                                           Invalid choice. Please try again.");
            }
        } while (!choice.equals("4"));
    }

    private void displayStudentInfo(Student student) {
        System.out.println("\n=======================================");
        System.out.println("📄 Student Details:");
        System.out.println("---------------------------------------");
        System.out.println("🆔 Student ID: " + student.getId());
        System.out.println("👤 Name: " + student.getName());
        System.out.println("📞 Phone Number: " + student.getPhoneNumber());

        Student.Strand selectedStrand = student.getSelectedStrand();
        System.out.println("📚 Strand: " + (selectedStrand != null ? selectedStrand.getName() : "Unknown Strand"));
        System.out.println("💳 Payment Status: " + student.getPaymentStatus());
        System.out.printf("💰 Remaining Balance: %.2f%n", student.getBalance());

        // New fields
        System.out.println("📝 Enrollment Status: " + student.getEnrollmentStatus());
        System.out.println("🚦 Student Status: " + student.getStudentStatus());

        // Display enrolled subjects
        System.out.println("\n📚 Enrolled Subjects:");
        if (!student.getEnrolledSubjects().isEmpty()) {
            for (Student.Subject subject : student.getEnrolledSubjects()) {
                System.out.println("- " + subject.getSubjectName());
            }
        } else {
            System.out.println("⚠ No subjects enrolled.");
        }
        System.out.println("=======================================\n");
    }
}
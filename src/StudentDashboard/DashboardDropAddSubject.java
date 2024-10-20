package StudentDashboard;

import Enrollment.InitializeStrands;
import Enrollment.Student;
import Enrollment.Subject;

import java.util.List;
import java.util.Scanner;

public class DashboardDropAddSubject {
    private Scanner scanner = new Scanner(System.in);
    private InitializeStrands initializeStrands = new InitializeStrands();

    public void dropAddSubjects(Student student) {
        // Get the strand name from the selected strand of the student
        String strandName = student.getSelectedStrand().getName(); // Updated method call

        // Load available subjects for the selected strand
        List<Subject> availableSubjects = initializeStrands.loadAvailableSubjects(strandName);

        if (availableSubjects.isEmpty()) {
            System.out.println("No subjects available for this strand.");
            return;
        }

        System.out.println("Available Subjects:");
        for (Subject subject : availableSubjects) {
            System.out.println(subject.getSubjectName());
        }

        // Code for dropping/adding subjects
        System.out.print("Enter the name of the subject to drop or add (or 'exit' to return): ");
        String input = scanner.nextLine();

        // Add logic to handle adding or dropping the subject
        if ("exit".equalsIgnoreCase(input)) {
            return; // Exit the drop/add process
        }

        // Check if the subject is already enrolled
        boolean isEnrolled = student.getEnrolledSubjects().stream()
                .anyMatch(subject -> subject.getSubjectName().equalsIgnoreCase(input));

        if (isEnrolled) {
            // Logic for dropping the subject
            System.out.println("Dropping subject: " + input);
            student.getEnrolledSubjects().removeIf(subject -> subject.getSubjectName().equalsIgnoreCase(input));
            System.out.println("Subject dropped successfully.");
        } else {
            // Logic for adding the subject
            boolean isAvailable = availableSubjects.stream()
                    .anyMatch(subject -> subject.getSubjectName().equalsIgnoreCase(input));

            if (isAvailable) {
                student.addSubject(new Subject(input)); // Add subject to the student's enrolled subjects
                System.out.println("Subject added successfully.");
            } else {
                System.out.println("Subject not available.");
            }
        }
    }
}

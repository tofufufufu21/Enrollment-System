package StudentDashboard;

import Enrollment.Student;
import Enrollment.Subject;

import java.util.Scanner;

public class DashboardDropSubject {
    private Scanner scanner = new Scanner(System.in);

    public void dropSubject(Student student) {
        System.out.println("Current Enrolled Subjects:");
        if (student.getEnrolledSubjects().isEmpty()) {
            System.out.println("No subjects enrolled.");
            return;
        }

        for (Subject subject : student.getEnrolledSubjects()) {
            System.out.println("- " + subject.getSubjectName());
        }

        System.out.print("Enter the name of the subject to drop: ");
        String dropSubject = scanner.nextLine().trim();

        // Check if the student is currently enrolled in the selected subject
        boolean isEnrolledForDrop = student.getEnrolledSubjects().stream()
                .anyMatch(subject -> subject.getSubjectName().trim().equalsIgnoreCase(dropSubject));

        if (isEnrolledForDrop) {
            // Remove the subject from the student's enrolled subjects
            student.getEnrolledSubjects().removeIf(subject -> subject.getSubjectName().trim().equalsIgnoreCase(dropSubject));
            System.out.println("Successfully dropped " + dropSubject);
        } else {
            System.out.println("You are not enrolled in " + dropSubject + ".");
        }
    }
}

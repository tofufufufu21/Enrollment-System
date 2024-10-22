package StudentDashboard;

import Enrollment.InitializeStrands;
import Enrollment.Student;
import Enrollment.Subject;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DashboardAddSubject {
    private Scanner scanner = new Scanner(System.in);
    private InitializeStrands initializeStrands = new InitializeStrands();

    public void addSubject(Student student) {
        // Get the strand name from the selected strand of the student
        String strandName = student.getSelectedStrand().getName();

        // Load available subjects for the selected strand
        List<Subject> availableSubjects = initializeStrands.loadAvailableSubjects(strandName);

        if (availableSubjects.isEmpty()) {
            System.out.println("No subjects available for this strand.");
            return;
        }

        // Get the subjects the student is currently enrolled in
        List<String> enrolledSubjectNames = student.getEnrolledSubjects().stream()
                .map(Subject::getSubjectName)
                .map(String::trim)
                .collect(Collectors.toList());

        // Filter available subjects to only those the student is not enrolled in
        List<Subject> notEnrolledSubjects = availableSubjects.stream()
                .filter(subject -> !enrolledSubjectNames.contains(subject.getSubjectName().trim()))
                .collect(Collectors.toList());

        // Check if there are any subjects to add
        if (notEnrolledSubjects.isEmpty()) {
            System.out.println("You are already enrolled in all available subjects for your strand.");
            return;
        }

        // Display subjects that the student is not enrolled in
        displayNotEnrolledSubjects(notEnrolledSubjects);

        System.out.print("Enter the name of the subject to add: ");
        String addSubject = scanner.nextLine().trim();

        // Check if the subject is available to add
        boolean isAvailableForAdd = notEnrolledSubjects.stream()
                .anyMatch(subject -> subject.getSubjectName().trim().equalsIgnoreCase(addSubject));

        // Check if the student is already enrolled in the subject
        boolean isAlreadyEnrolled = student.getEnrolledSubjects().stream()
                .anyMatch(subject -> subject.getSubjectName().trim().equalsIgnoreCase(addSubject));

        if (isAvailableForAdd && !isAlreadyEnrolled) {
            // Add the subject to the student's enrolled subjects
            student.addSubject(new Subject(addSubject));
            System.out.println("Successfully added " + addSubject);
        } else if (isAlreadyEnrolled) {
            System.out.println("You are already enrolled in " + addSubject + ".");
        } else {
            System.out.println(addSubject + " is not available.");
        }
    }

    // Helper method to display subjects that the student is not enrolled in
    private void displayNotEnrolledSubjects(List<Subject> notEnrolledSubjects) {
        System.out.println("Subjects Available for Enrollment:");
        for (Subject subject : notEnrolledSubjects) {
            System.out.println("- " + subject.getSubjectName());
        }
        System.out.println(); // Add a blank line for better readability
    }
}

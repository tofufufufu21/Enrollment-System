package StudentDashboard;

import Enrollment.InitializeStrands;
import Enrollment.Student;
import Enrollment.Subject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DashboardAddSubject {
    private Scanner scanner = new Scanner(System.in);
    private InitializeStrands initializeStrands = new InitializeStrands();
    private static final int SUBJECT_COST = 1000; // Addition for each added subject

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
                .map(this::cleanSubjectName) // Clean subject names before comparison
                .collect(Collectors.toList());

        // Filter available subjects to only those the student is not enrolled in
        List<Subject> notEnrolledSubjects = availableSubjects.stream()
                .filter(subject -> !enrolledSubjectNames.contains(cleanSubjectName(subject.getSubjectName()))) // Clean subject names
                .collect(Collectors.toList());

        // Check if there are any subjects to add
        if (notEnrolledSubjects.isEmpty()) {
            System.out.println("You are already enrolled in all available subjects for your strand.");
            return;
        }

        // Display student's enrolled subjects
        displayEnrolledSubjects(student.getEnrolledSubjects());

        // Display subjects that the student is not enrolled in
        displayNotEnrolledSubjects(notEnrolledSubjects);

        System.out.print("Enter the name of the subject to add: ");
        String addSubject = scanner.nextLine().trim(); // Trim user input

        // Clean up and remove any unwanted characters (quotes, commas) from the user input
        String cleanAddSubject = cleanSubjectName(addSubject); // Clean user input

        // Check if the subject is available to add
        boolean isAvailableForAdd = notEnrolledSubjects.stream()
                .anyMatch(subject -> cleanSubjectName(subject.getSubjectName()).equalsIgnoreCase(cleanAddSubject));

        if (isAvailableForAdd) {
            // Add the subject to the student's enrolled subjects
            student.addSubject(new Subject(cleanAddSubject));

            // Add the subject cost to the student's balance
            student.setBalance(student.getBalance() + SUBJECT_COST);
            System.out.println("Successfully added " + cleanAddSubject);
            System.out.println("Updated balance: " + student.getBalance());

            // Save the updated student data to the file
            saveStudentToFile(student, strandName);
        } else {
            System.out.println(cleanAddSubject + " is not available.");
        }
    }

    // Helper method to clean the subject names by removing unwanted characters
    private String cleanSubjectName(String subjectName) {
        return subjectName.replaceAll("[\"',]", "").trim(); // Remove quotes, commas, and trim
    }

    // Helper method to display enrolled subjects
    private void displayEnrolledSubjects(List<Subject> enrolledSubjects) {
        System.out.println("Enrolled Subjects:");
        if (enrolledSubjects.isEmpty()) {
            System.out.println("No subjects enrolled.");
        } else {
            for (Subject subject : enrolledSubjects) {
                System.out.println("- " + subject.getSubjectName());
            }
        }
    }

    // Helper method to display subjects that the student is not enrolled in
    private void displayNotEnrolledSubjects(List<Subject> notEnrolledSubjects) {
        System.out.println("Subjects Available for Enrollment:");
        for (Subject subject : notEnrolledSubjects) {
            System.out.println("- " + cleanSubjectName(subject.getSubjectName())); // Clean before display
        }
        System.out.println(); // Add a blank line for better readability
    }

    // Save student data to a CSV file
    private void saveStudentToFile(Student student, String selectedStrandFileName) {
        String fileName = "student_" + student.getId() + ".csv"; // Create a unique filename for each student
        try (FileWriter fileWriter = new FileWriter(fileName, false); // false to overwrite the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Write header
            printWriter.printf("ID,Name,Phone Number,Strand,Payment Status,Balance,Enrolled Subjects\n");

            // Prepare the enrolled subjects string
            StringBuilder subjectsString = new StringBuilder();
            for (Subject subject : student.getEnrolledSubjects()) {
                if (subject != null) {
                    subjectsString.append(subject.getSubjectName()).append("; ");
                }
            }

            // Remove the last semicolon and space if any subjects were added
            if (subjectsString.length() > 0) {
                subjectsString.setLength(subjectsString.length() - 2); // Remove the last "; "
            }

            // Remove the ".csv" extension from the selected strand file name
            String strandNameOnly = selectedStrandFileName.replace(".csv", ""); // Exclude the CSV extension

            // Write student information along with enrolled subjects
            printWriter.printf("%d,%s,%s,%s,%s,%.2f,%s\n",
                    student.getId(),
                    student.getName(),
                    student.getPhoneNumber(),
                    strandNameOnly, // Use the strand name without the .csv extension
                    student.getPaymentStatus(),
                    student.getBalance(),
                    subjectsString.toString()); // Add enrolled subjects here

        } catch (IOException e) {
            System.out.println("Error saving student to file: " + e.getMessage());
        }
    }
}

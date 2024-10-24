package StudentDashboard;

import Enrollment.Student;
import Enrollment.Subject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DashboardDropSubject {
    private Scanner scanner = new Scanner(System.in);
    private static final int SUBJECT_COST = 1000; // Deduction for each dropped subject

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

            // Deduct the subject cost from the student's balance
            student.setBalance(student.getBalance() - SUBJECT_COST);
            System.out.println("Successfully dropped " + dropSubject);
            System.out.println("Updated balance: " + student.getBalance());

            // Save the updated student data to the file
            saveStudentToFile(student, student.getSelectedStrand().getName());
        } else {
            System.out.println("You are not enrolled in " + dropSubject + ".");
        }
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

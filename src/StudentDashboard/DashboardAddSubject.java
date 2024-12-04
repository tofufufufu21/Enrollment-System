package StudentDashboard;

import Enrollment.InitializeStrands;
import Enrollment.Student;

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
        String strandName = student.getSelectedStrand().getName();
        List<Student.Subject> availableSubjects = initializeStrands.loadAvailableSubjects(strandName);

        if (availableSubjects.isEmpty()) {
            System.out.println("No subjects available for this strand.");
            return;
        }

        // Get the list of already enrolled subjects
        List<String> enrolledSubjectNames = student.getEnrolledSubjects().stream()
                .map(Student.Subject::getSubjectName)
                .map(this::cleanSubjectName)
                .collect(Collectors.toList());

        // Filter out subjects that the student has already enrolled in
        List<Student.Subject> notEnrolledSubjects = availableSubjects.stream()
                .filter(subject -> !enrolledSubjectNames.contains(cleanSubjectName(subject.getSubjectName())))
                .collect(Collectors.toList());

        if (notEnrolledSubjects.isEmpty()) {
            System.out.println("                                                                               You are already enrolled in all available subjects for your strand.");
            return;
        }

        displayEnrolledSubjects(student.getEnrolledSubjects());

        boolean addMore = true;
        while (addMore) {
            // Check if there are no more subjects to enroll in
            if (notEnrolledSubjects.isEmpty()) {
                System.out.println("                                                                                        All subjects have been enrolled. Exiting.");
                break;
            }

            displayNotEnrolledSubjects(notEnrolledSubjects);

            System.out.print("                                                                                        Enter the name of the subject to add: ");
            String addSubject = scanner.nextLine().trim();
            String cleanAddSubject = cleanSubjectName(addSubject);

            // Check if the subject is already enrolled
            if (enrolledSubjectNames.contains(cleanAddSubject)) {
                System.out.println(cleanAddSubject + " is already enrolled.");
                System.out.print("                                                                                        Do you want to add another subject? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                addMore = response.equals("yes");
                continue; // Skip the current iteration if subject is already enrolled
            }

            boolean isAvailableForAdd = notEnrolledSubjects.stream()
                    .anyMatch(subject -> cleanSubjectName(subject.getSubjectName()).equalsIgnoreCase(cleanAddSubject));

            if (isAvailableForAdd) {
                student.addSubject(new Student.Subject(cleanAddSubject));
                student.setBalance(student.getBalance() + SUBJECT_COST);
                System.out.println("Successfully added " + cleanAddSubject);
                System.out.println("Updated balance: " + student.getBalance());
                saveStudentToFile(student, strandName);

                // Re-calculate notEnrolledSubjects after adding a new subject
                enrolledSubjectNames.add(cleanAddSubject); // Add the newly enrolled subject to the list
                notEnrolledSubjects = availableSubjects.stream()
                        .filter(subject -> !enrolledSubjectNames.contains(cleanSubjectName(subject.getSubjectName())))
                        .collect(Collectors.toList());

                System.out.print("                                                                                        Do you want to add another subject? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                addMore = response.equals("yes");
            } else {
                System.out.println(cleanAddSubject + "is not available for enrollment.");
                System.out.print("                                                                                        Do you want to add another subject? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                addMore = response.equals("yes");
            }
        }
    }
//what a sigma

    // Helper method to clean the subject names by removing unwanted characters
    private String cleanSubjectName(String subjectName) {
        // Remove quotes and unnecessary spaces
        subjectName = subjectName.replace("\"", "").trim();

        String[] words = subjectName.split(" "); // Split the input into words
        StringBuilder cleanedSubjectName = new StringBuilder();

        // Iterate over each word, capitalizing the first letter
        for (String word : words) {
            if (!word.isEmpty()) {
                cleanedSubjectName.append(word.substring(0, 1).toUpperCase()) // Capitalize the first letter
                        .append(word.substring(1).toLowerCase()) // Keep the rest of the word in lowercase
                        .append(" "); // Add space between words
            }
        }

        return cleanedSubjectName.toString().trim(); // Return the cleaned and formatted subject name
    }

    // Helper method to display enrolled subjects
    private void displayEnrolledSubjects(List<Student.Subject> enrolledSubjects) {
        System.out.println("Enrolled Subjects:");
        if (enrolledSubjects.isEmpty()) {
            System.out.println("No subjects enrolled.");
        } else {
            for (Student.Subject subject : enrolledSubjects) {
                System.out.println("- " + subject.getSubjectName());
            }
        }
    }

    // Helper method to display subjects that the student is not enrolled in
    private void displayNotEnrolledSubjects(List<Student.Subject> notEnrolledSubjects) {
        System.out.println("Subjects Available for Enrollment:");
        for (Student.Subject subject : notEnrolledSubjects) {
            // Display the subject name as it is, without modification
            System.out.println("- " + subject.getSubjectName());
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
            for (Student.Subject subject : student.getEnrolledSubjects()) {
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

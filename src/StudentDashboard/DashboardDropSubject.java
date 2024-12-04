package StudentDashboard;

import Enrollment.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class DashboardDropSubject {
    private Scanner scanner = new Scanner(System.in);
    private static final int SUBJECT_COST = 1000; // Deduction for each dropped subject

    public void dropSubject(Student student) {

        if (student.getPaymentStatus().equalsIgnoreCase("Fully Paid")) {
            System.out.println("You are fully paid. You can't access this field. Please contact the manager or admin for more information.");
            return; // Exit the method if the student is fully paid
        }


        boolean dropMore = true; // Control variable for dropping multiple subjects

        while (dropMore) {
            System.out.println("Current Enrolled Subjects:");
            if (student.getEnrolledSubjects().isEmpty()) {
                System.out.println("No subjects enrolled.");
                return;
            }

            // Display enrolled subjects with numbers
            List<Student.Subject> enrolledSubjects = student.getEnrolledSubjects();
            for (int i = 0; i < enrolledSubjects.size(); i++) {
                System.out.println((i + 1) + ". " + enrolledSubjects.get(i).getSubjectName());
            }

            int subjectNumber = -1;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Enter the number of the subject to drop (or press 'c' to cancel): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("c")) {
                    System.out.println("\nCancelling subject drop and returning to the dashboard menu.");
                    return; // Exit the method to return to the dashboard
                }
                try {
                    subjectNumber = Integer.parseInt(input); // Use input directly here

                    // Check if the number is valid
                    if (subjectNumber < 1 || subjectNumber > enrolledSubjects.size()) {
                        System.out.println("\nInvalid number. Please choose a valid subject number.");
                    } else {
                        validInput = true; // Exit loop when valid input is provided
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Please enter a valid number.");
                }
            }

            // Get the subject to be dropped based on the number
            Student.Subject subjectToDrop = enrolledSubjects.get(subjectNumber - 1);

            // Remove the subject from the student's enrolled subjects
            student.getEnrolledSubjects().remove(subjectToDrop);

            // Deduct the subject cost from the student's balance
            student.setBalance(student.getBalance() - SUBJECT_COST);
            System.out.println("Successfully dropped " + subjectToDrop.getSubjectName());
            System.out.println("Updated balance: " + student.getBalance());

            // Save the updated student data to the file
            saveStudentToFile(student, student.getSelectedStrand().getName());

            // Ask if the user wants to drop more subjects
            boolean validResponse = false;
            String response = "";
            while (!validResponse) {
                System.out.print("\nDo you want to drop another subject? (yes/no): ");
                response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes") || response.equals("no")) {
                    validResponse = true;
                } else {
                    System.out.println("\nInvalid input. Please enter 'yes' or 'no'.");
                }
            }

            dropMore = response.equals("yes");
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

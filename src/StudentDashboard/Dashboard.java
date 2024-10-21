package StudentDashboard;

import Enrollment.Student;
import Enrollment.Strand;
import Enrollment.Subject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Dashboard {
    private static Scanner scanner = new Scanner(System.in);
    private List<Strand> strands; // To hold strands

    // Constructor that accepts strands
    public Dashboard(List<Strand> strands) {
        this.strands = strands; // Initialize the strands
    }

    public void login() {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Fetch student by ID
        Student student = getStudentById(studentId);
        if (student != null) {
            System.out.println("Login successful.");

            // Pass the student object to the dashboard menu
            DashboardMenu menu = new DashboardMenu();
            menu.showMenu(student); // Call showMenu and pass student object
        } else {
            System.out.println("Student not found. Please try again.");
        }
    }

    // Method to retrieve a Student by ID from a CSV file
    private Student getStudentById(int studentId) {
        String csvFile = "student_" + studentId + ".csv"; // Path to the student's CSV file
        String line;
        String csvSplitBy = ","; // Adjust if your CSV uses a different delimiter

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                // Split the line by the CSV delimiter
                String[] data = line.split(csvSplitBy);
                int id = Integer.parseInt(data[0].trim());

                // Check if the ID matches
                if (id == studentId) {
                    // Create a new Student object from the CSV data
                    return createStudentFromData(data); // Directly return the student object
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student data: " + e.getMessage());
        }

        return null; // Return null if no matching student was found
    }

    // Helper method to create a Student object from the CSV data
    private Student createStudentFromData(String[] data) {
        int id = Integer.parseInt(data[0].trim());
        String name = data[1].trim();
        String phoneNumber = data[2].trim();
        String strandName = data[3].trim(); // Strand name from the CSV
        String paymentStatus = data[4].trim(); // Payment status from the CSV
        double balance = Double.parseDouble(data[5].trim()); // Balance from the CSV

        // Create Strand object based on the strandName from the CSV
        Strand selectedStrand = new Strand(strandName); // Create a new Strand object

        // Create the student object with the Strand object
        Student student = new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);

        // Load enrolled subjects
        if (data.length > 6) { // Check if enrolled subjects exist
            String enrolledSubjects = data[6].trim(); // Get the enrolled subjects
            String[] subjects = enrolledSubjects.split(";"); // Split by semicolon
            for (String subjectName : subjects) {
                subjectName = subjectName.replaceAll("\"", "").trim(); // Remove quotes and trim spaces
                student.addSubject(new Subject(subjectName)); // Add subject
            }
        }

        return student;
    }
}

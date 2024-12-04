package StudentDashboard;

import Enrollment.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Dashboard {
    private static Scanner scanner = new Scanner(System.in);
    private List<Student.Strand> strands; // To hold strands

    // Constructor that accepts strands
    public Dashboard(List<Student.Strand> strands) {
        this.strands = strands; // Initialize the strands
    }

    public void login() {
        while (true) {
            System.out.print("                                                                                           =====================================");
            System.out.print("\n                                                                                                Enter Student ID: ");

            // Validate input type
            try {
                int studentId = Integer.parseInt(scanner.nextLine().trim());

                // Fetch student by ID
                Student student = getStudentById(studentId);
                if (student != null) {
                    System.out.println("\n                                                                                                Login successful.");

                    // Pass the student object to the dashboard menu
                    DashboardMenu menu = new DashboardMenu();
                    menu.showMenu(student);
                    break; // Exit the loop after successful login
                } else {
                    System.out.println("\n                                                                                           Student not found. Please try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n                                                                                   Invalid input. Please enter a valid numeric Student ID.\n");
            }
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
            System.out.println("\n                                                                    Error reading student data: " + e.getMessage());
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
        Student.Strand selectedStrand = new Student.Strand(strandName); // Create a new Strand object

        // Create the student object with the Strand object
        Student student = new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);

        // Load enrolled subjects
        if (data.length > 6) { // Check if enrolled subjects exist
            String enrolledSubjects = data[6].trim(); // Get the enrolled subjects
            String[] subjects = enrolledSubjects.split(";"); // Split by semicolon
            for (String subjectName : subjects) {
                subjectName = subjectName.replaceAll("\"", "").trim(); // Remove quotes and trim spaces
                student.addSubject(new Student.Subject(subjectName)); // Add subject
            }
        }

        return student;
    }
}

package StudentDashboard;

import Enrollment.Student;
import Enrollment.Strand;

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
            DashboardMenu menu = new DashboardMenu();
            menu.showMenu(student); // Pass student to the dashboard menu
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
        double balance = Double.parseDouble(data[2].trim());
        String phoneNumber = data[3].trim();
        // Assuming Strand can be retrieved or passed accordingly
        Strand selectedStrand = findStrandByName(data[4].trim()); // Assuming the strand name is in the CSV
        String paymentStatus = data[5].trim();

        return new Student(id, name, balance, phoneNumber, selectedStrand, paymentStatus);
    }

    // Method to find a Strand by name (implement according to your logic)
    private Strand findStrandByName(String strandName) {
        // Your logic to find and return the Strand object by its name
        // This is a placeholder; you'll need to implement it
        for (Strand strand : strands) {
            if (strand.getName().equalsIgnoreCase(strandName)) {
                return strand;
            }
        }
        return null; // Return null if strand is not found
    }
}

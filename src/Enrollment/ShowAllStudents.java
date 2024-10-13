package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShowAllStudents {
    // Method to show all enrolled students from the individual CSV files
    public void showAllFromCSV() {
        File directory = new File("."); // Get current directory
        File[] files = directory.listFiles((dir, name) -> name.startsWith("student_") && name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("Enrolled Students:");
            System.out.printf("%-5s %-20s %-15s %-10s %-15s %-10s%n", "ID", "Name", "Phone Number", "Strand", "Payment Status", "Balance");

            for (File file : files) {
                try (Scanner scanner = new Scanner(file)) {
                    // Read and display the contents of each student's CSV file
                    if (scanner.hasNextLine()) {
                        String header = scanner.nextLine(); // Skip the header
                    }
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] data = line.split(",");

                        // Ensure to handle the right indices according to your CSV structure
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        String phoneNumber = data[2].trim();
                        String selectedStrand = data[3].trim();
                        String paymentStatus = data[4].trim();
                        int balance = Integer.parseInt(data[5].trim());

                        // Adjust formatting to ensure proper alignment
                        System.out.printf("%-5d %-20s %-15s %-10s %-15s %-10d%n", id, name, phoneNumber, selectedStrand, paymentStatus, balance);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No enrolled students found.");
        }
    }
}
package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StudentRecords {
    // Method to show all enrolled students from the individual CSV files
    public void showAllFromCSV() {
        File directory = new File("."); // Get current directory
        File[] files = directory.listFiles((dir, name) -> name.startsWith("student_") && name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n\n\n\n\n"); // Push the menu down (adjust for centering)
            System.out.println("Enrolled Students:");

            for (File file : files) {
                System.out.println("===========================");
                System.out.println("Registration Card:");
                System.out.println("===========================");
                try (Scanner scanner = new Scanner(file)) {
                    // Read and parse the contents of each student's CSV file
                    if (scanner.hasNextLine()) {
                        String header = scanner.nextLine(); // Skip the header
                    }
                    if (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] data = line.split(",");

                        // Parse data based on the new CSV format
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        String phoneNumber = data[2].trim();
                        String strand = data[3].trim();
                        String paymentStatus = data[4].trim();
                        double balance = Double.parseDouble(data[5].trim());
                        String enrolledSubjects = data[6].trim();
                        String enrollmentStatus = data[7].trim();
                        String studentStatus = data[8].trim();

                        // Display student details
                        System.out.printf("ID: %d%n", id);
                        System.out.printf("Name: %s%n", name);
                        System.out.printf("Phone Number: %s%n", phoneNumber);
                        System.out.printf("Strand: %s%n", strand);
                        System.out.printf("Payment Status: %s%n", paymentStatus);
                        System.out.printf("Balance: %.2f%n", balance);
                        System.out.printf("Enrollment Status: %s%n", enrollmentStatus);
                        System.out.printf("Student Status: %s%n", studentStatus);

                        // Display enrolled subjects as a list
                        System.out.println("Enrolled Subjects:");
                        if (!enrolledSubjects.isEmpty()) {
                            String[] subjects = enrolledSubjects.split(";");
                            for (String subject : subjects) {
                                System.out.printf("  - %s%n", subject.trim().replace("\"", ""));
                            }
                        } else {
                            System.out.println("  None");
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error parsing data: " + e.getMessage());
                }
                System.out.println("===========================\n");
            }
        } else {
            System.out.println("No enrolled students found.");
        }
    }
}

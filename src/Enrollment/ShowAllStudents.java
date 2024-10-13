package Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShowAllStudents {
    // Method to show all enrolled students from the CSV file
    public void showAllFromCSV() {
        File file = new File("students.csv");
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("Enrolled Students:");
            System.out.println("ID\tName\t\tPhone Number\tStrand\tPayment Status\tBalance");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                // Make sure to handle the right indices according to your CSV structure
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                String phoneNumber = data[2].trim();
                String selectedStrand = data[3].trim();
                String paymentStatus = data[4].trim();
                int balance = Integer.parseInt(data[5].trim());

                System.out.printf("%d\t%s\t\t%s\t\t%s\t\t%s\t%d\n", id, name, phoneNumber, selectedStrand, paymentStatus, balance);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
}

package Enrollment;

import java.io.*;
import java.util.Scanner;

public class StudentStatus {
    private static Scanner scanner = new Scanner(System.in);

    public void deleteStudent(String adminName, Student[] students, int[] studentCount) {
        int idToDeactivate = getValidStudentId(adminName, students, studentCount); // Get valid student ID input

        // Construct the filename based on student ID
        String fileName = "student_" + idToDeactivate + ".csv"; // Ensure this file exists in the working directory
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error: Student file '" + fileName + "' not found!");
            System.out.println("                                                                                        =======================================");
            returnToDashboard(adminName, students, studentCount);
            return;
        }

        // Check the current status of the student
        String currentStatus = getCurrentStatus(file);
        if (currentStatus == null) {
            return;
        }

        if ("Active".equalsIgnoreCase(currentStatus)) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Student is already ACTIVE.");
            System.out.println("                                                                                        =======================================");
        } else if ("INACTIVE".equalsIgnoreCase(currentStatus)) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Student is already INACTIVE.");
            System.out.println("                                                                                        =======================================");
        }

        // Ask for confirmation before updating status
        char confirm;
        while (true) {
            try {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                           Do you want to make this student ACTIVE or INACTIVE?");
                System.out.println("                                                                                        =======================================");
                System.out.println("                                                                                        0. Make INACTIVE");
                System.out.println("                                                                                        1. Make ACTIVE");
                System.out.print("                                                                                        Enter your choice (1/0): ");
                confirm = scanner.next().charAt(0);
                scanner.nextLine(); // Consume newline

                if (confirm == '1') {
                    if ("Active".equalsIgnoreCase(currentStatus)) {
                        System.out.println("\n                                                                                        Student is already ACTIVE.");
                        returnToDashboard(adminName, students, studentCount);
                        return;
                    }
                    if (updateStudentStatus(file, "Active")) {
                        System.out.println("\n                                                                                        =======================================");
                        System.out.println("                                                                                           Student with ID " + idToDeactivate + " has been marked as ACTIVE.");
                        System.out.println("                                                                                        =======================================");
                    } else {
                        System.out.println("\n                                                                                        =======================================");
                        System.out.println("                                                                                           Error updating the student status.");
                        System.out.println("                                                                                        =======================================");
                    }
                    break;
                } else if (confirm == '0') {
                    if ("INACTIVE".equalsIgnoreCase(currentStatus)) {
                        System.out.println("\n                                                                                        Student is already INACTIVE.");
                        returnToDashboard(adminName, students, studentCount);
                        return;
                    }
                    if (updateStudentStatus(file, "INACTIVE")) {
                        System.out.println("\n                                                                                        =======================================");
                        System.out.println("                                                                                           Student with ID " + idToDeactivate + " has been marked as INACTIVE.");
                        System.out.println("                                                                                        =======================================");
                    } else {
                        System.out.println("\n                                                                                        =======================================");
                        System.out.println("                                                                                           Error updating the student status.");
                        System.out.println("                                                                                        =======================================");
                    }
                    break;
                } else {
                    System.out.println("\n                                                                                        Invalid input. Please enter '1' to activate or '0' to deactivate.");
                }
            } catch (Exception e) {
                System.out.println("\n                                                                                        Invalid input. Please enter '1' or '0'.");
                scanner.nextLine(); // Clear the buffer to prevent infinite loop
            }
        }

        // Ask if user wants to go back to the dashboard
        returnToDashboard(adminName, students, studentCount);
    }

    private boolean updateStudentStatus(File studentFile, String newStatus) {
        File tempFile = new File("temp_update.csv");
        boolean statusUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(studentFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line = reader.readLine(); // Read the header
            if (line != null) {
                String[] headers = line.split(","); // Split header line to find column names
                int statusIndex = -1;

                // Find the column index for 'StudentStatus'
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].trim().equalsIgnoreCase("StudentStatus")) {
                        statusIndex = i;
                        break;
                    }
                }

                if (statusIndex == -1) {
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                           Error: 'StudentStatus' column not found in the CSV file.");
                    System.out.println("                                                                                        =======================================");
                    return false;
                }

                writer.write(line + System.lineSeparator()); // Write the header to temp file

                line = reader.readLine(); // Read the student data
                if (line != null) {
                    String[] studentData = line.split(","); // Split the student data by commas

                    // Update the 'StudentStatus' field
                    studentData[statusIndex] = newStatus; // Change status to the new status
                    statusUpdated = true;

                    // Write the updated student data to the temp file
                    writer.write(String.join(",", studentData) + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error updating student status: " + e.getMessage());
            System.out.println("                                                                                        =======================================");
        }

        // Replace the original file with the updated file
        if (studentFile.delete() && tempFile.renameTo(studentFile)) {
            return statusUpdated;
        } else {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error replacing the student file.");
            System.out.println("                                                                                        =======================================");
            return false;
        }
    }

    private String getCurrentStatus(File studentFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
            String line = reader.readLine(); // Read the header
            if (line != null) {
                String[] headers = line.split(","); // Split header line to find column names
                int statusIndex = -1;

                // Find the column index for 'StudentStatus'
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].trim().equalsIgnoreCase("StudentStatus")) {
                        statusIndex = i;
                        break;
                    }
                }

                if (statusIndex == -1) {
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                           Error: 'StudentStatus' column not found in the CSV file.");
                    System.out.println("                                                                                        =======================================");
                    return null;
                }

                line = reader.readLine(); // Read the student data
                if (line != null) {
                    String[] studentData = line.split(","); // Split the student data by commas
                    return studentData[statusIndex].trim(); // Return the current status
                }
            }

        } catch (IOException e) {
            System.out.println("\n                                                                                        =======================================");
            System.out.println("                                                                                           Error reading student file: " + e.getMessage());
            System.out.println("                                                                                        =======================================");
        }
        return null;
    }

    private int getValidStudentId(String adminName, Student[] students, int[] studentCount) {
        int idToDeactivate = -1;
        while (true) {
            try {
                System.out.print("\n                                                                                        =======================================");
                System.out.print("\n                                                                                        Enter Student ID to deactivate: ");
                idToDeactivate = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (idToDeactivate < 0) {
                    System.out.println("\n                                                                                        =======================================");
                    System.out.println("                                                                                           Invalid input. Please enter a valid student ID.");
                    System.out.println("                                                                                        =======================================");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                           Invalid input. Please enter a valid student ID.");
                System.out.println("                                                                                        =======================================");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return idToDeactivate;
    }

    private void returnToDashboard(String adminName, Student[] students, int[] studentCount) {
        while (true) {
            System.out.print("\n                                                                                        Would you like to go back to the dashboard? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                System.out.println("\n\n\n\n\n");
                System.out.println("\n                                                                                        =======================================");
                System.out.println("                                                                                           Returning to the Dashboard...\n");
                System.out.println("                                                                                        =======================================");
                return;
            } else if (response.equals("n")) {
                System.out.println("\n                                                                                        Continuing the process...\n");
                break;
            } else {
                System.out.println("\n                                                                                        Invalid input. Please enter 'y' or 'n'.\n");
            }
        }
    }
}

package Enrollment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitializeStrands {

    // Load strands from the specified CSV file
    public Student.Strand loadStrands(String filename) {
        List<Student.Subject> subjects = new ArrayList<>();
        String strandName = ""; // Initialize strand name

        // Load subjects from the specified CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Read the first line (strand name)
            if ((strandName = br.readLine()) != null) {
                strandName = strandName.trim(); // Get strand name
            }

            // Read each subsequent line as a subject
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split by comma
                if (parts.length > 0) {
                    String subjectName = parts[0].trim(); // Get subject name from the first index
                    if (!subjectName.isEmpty()) {
                        subjects.add(new Student.Subject(subjectName)); // Create and add Subject object
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading strands from file: " + filename);
            e.printStackTrace();
            return null; // Return null if there's an error
        }

        // Return the Strand object with subjects
        return new Student.Strand(strandName, subjects); // Return strand name for reference
    }

    // Initialize strands for both grades and return as a list
    public List<Student.Strand> initializeAllStrands() {
        List<Student.Strand> strands = new ArrayList<>();
        String[] csvFiles = {
                "ABM_11.csv", "STEM_11.csv", "HUMSS_11.csv", "GAS_11.csv",
                "ABM_12.csv", "STEM_12.csv", "HUMSS_12.csv", "GAS_12.csv"
        };

        for (String csvFile : csvFiles) {
            Student.Strand strand = loadStrands(csvFile);
            if (strand != null) {
                strands.add(strand); // Add the loaded strand to the list
            }
        }

        return strands; // Return the list of all strands
    }

    // Method to load available subjects for a specific strand name
    public List<Student.Subject> loadAvailableSubjects(String strandName) {
        // Construct the corresponding CSV filename
        String filename = strandName.replace(" ", "_") + ".csv"; // Example: "ABM_11" for "ABM 11"
        Student.Strand strand = loadStrands(filename); // Load the strand

        return (strand != null) ? strand.getSubjects() : new ArrayList<>(); // Return subjects if found
    }
}

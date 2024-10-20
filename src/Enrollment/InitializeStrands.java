package Enrollment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitializeStrands {

    // Load strands from the specified CSV file
    public Strand loadStrands(String filename) {
        List<Subject> subjects = new ArrayList<>();
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
                String subjectName = line.trim(); // Get subject name
                if (!subjectName.isEmpty()) {
                    subjects.add(new Subject(subjectName)); // Create and add Subject object
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading strands from file: " + filename);
            e.printStackTrace();
            return null; // Return null if there's an error
        }

        // Return the Strand object with subjects
        return new Strand(strandName, subjects); // Removed grade
    }

    // Initialize strands for both grades and return as a list
    public List<Strand> initializeAllStrands() {
        List<Strand> strands = new ArrayList<>();
        String[] csvFiles = {
                "ABM_11.csv", "STEM_11.csv", "HUMSS_11.csv", "GAS_11.csv",
                "ABM_12.csv", "STEM_12.csv", "HUMSS_12.csv", "GAS_12.csv"
        };

        for (String csvFile : csvFiles) {
            Strand strand = loadStrands(csvFile);
            if (strand != null) {
                strands.add(strand); // Add the loaded strand to the list
            }
        }

        return strands; // Return the list of all strands
    }
}

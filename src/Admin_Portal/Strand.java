package Admin_Portal;

public class Strand {
    public String name;
    public Enroll_Student.Subject[] subjects; // Reference to Subject inside Enroll_Student
    public int numSubjects;

    public Strand(String name, Enroll_Student.Subject[] subjects) {
        this.name = name;
        this.subjects = subjects;
        this.numSubjects = subjects.length;
    }

    // Getter for name
    public String getName() {
        return name; // Return the name of the strand
    }
}

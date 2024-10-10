package Admin_Portal;

public class Strand {
    public String name;
    public Enroll_Student.Subject[] subjects;
    public int numSubjects;

    public Strand(String name, Enroll_Student.Subject[] subjects) {
        this.name = name;
        this.subjects = subjects;
        this.numSubjects = subjects.length;
    }
}

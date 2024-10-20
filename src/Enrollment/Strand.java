package Enrollment;

import java.util.List;

public class Strand {
    private final String name;
    private final List<Subject> subjects;

    public Strand(String name, List<Subject> subjects) {
        this.name = name;
        this.subjects = subjects;
    }

    public String getName() {
        return name;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}


package student.management.system;

import java.io.Serializable; 

public class Student implements Serializable {
    private String name;
    private String rollNumber; 
    private String grade;
    private String contactNumber; 

    public Student(String name, String rollNumber, String grade, String contactNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.contactNumber = contactNumber;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    // Setters (for editing)
    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Roll No: %s, Grade: %s, Contact: %s", name, rollNumber, grade, contactNumber);
    }

    public String toFileString() {
        return String.join(",", name, rollNumber, grade, contactNumber);
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new Student(parts[0], parts[1], parts[2], parts[3]);
        }
        return null; 
    }
}

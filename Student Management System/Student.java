
package student.management.system;

import java.io.Serializable; // For saving/loading objects (though we'll use text file here, good practice)

public class Student implements Serializable {
    private String name;
    private String rollNumber; // Unique identifier
    private String grade;
    private String contactNumber; // Added for more realism

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
        // Format for display and for saving to file
        return String.format("Name: %s, Roll No: %s, Grade: %s, Contact: %s", name, rollNumber, grade, contactNumber);
    }

    // Method to convert student object to a savable string format (for text file)
    public String toFileString() {
        return String.join(",", name, rollNumber, grade, contactNumber);
    }

    // Static method to parse a line from the file back into a Student object
    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new Student(parts[0], parts[1], parts[2], parts[3]);
        }
        return null; // Or throw an exception for malformed data
    }
}
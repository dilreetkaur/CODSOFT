
package student.management.system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentManagementSystem {
    private List<Student> students;
    private static final String DATA_FILE = "students.txt"; // File to store data

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        loadStudentsFromFile(); // Load existing students when the system starts
    }

    // --- Core Management Methods ---

    // Add a student (Requirement 2)
    public boolean addStudent(Student student) {
        // Check for duplicate roll number (Requirement 6 - validation)
        if (getStudentByRollNumber(student.getRollNumber()).isPresent()) {
            return false; // Student with this roll number already exists
        }
        students.add(student);
        saveStudentsToFile(); // Save immediately after adding
        return true;
    }

    // Remove a student (Requirement 2)
    public boolean removeStudent(String rollNumber) {
        boolean removed = students.removeIf(s -> s.getRollNumber().equalsIgnoreCase(rollNumber));
        if (removed) {
            saveStudentsToFile(); // Save immediately after removing
        }
        return removed;
    }

    // Search for a student (Requirement 2)
    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return students.stream()
                .filter(s -> s.getRollNumber().equalsIgnoreCase(rollNumber))
                .findFirst();
    }

    // Get all students (Requirement 2)
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return a copy to prevent external modification
    }

    // Edit student information (Requirement 5)
    public boolean updateStudent(String rollNumber, String newName, String newGrade, String newContact) {
        Optional<Student> studentOptional = getStudentByRollNumber(rollNumber);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setName(newName);
            student.setGrade(newGrade);
            student.setContactNumber(newContact);
            saveStudentsToFile(); // Save after updating
            return true;
        }
        return false;
    }


    // --- Data Storage Methods (Requirement 4) ---

    // Load students from a file
    private void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = Student.fromFileString(line);
                if (student != null) {
                    students.add(student);
                }
            }
            System.out.println("Students loaded successfully from " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with an empty student list.");
            // File will be created when the first student is saved
        } catch (IOException e) {
            System.err.println("Error reading student data from file: " + e.getMessage());
        }
    }

    // Save students to a file
    private void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Student student : students) {
                writer.write(student.toFileString());
                writer.newLine();
            }
            System.out.println("Students saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error writing student data to file: " + e.getMessage());
        }
    }
}

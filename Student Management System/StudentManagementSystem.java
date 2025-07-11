
package student.management.system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentManagementSystem {
    private List<Student> students;
    private static final String DATA_FILE = "students.txt";

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        loadStudentsFromFile(); 
    }


    public boolean addStudent(Student student) {
        if (getStudentByRollNumber(student.getRollNumber()).isPresent()) {
            return false;
        }
        students.add(student);
        saveStudentsToFile(); 
        return true;
    }

    public boolean removeStudent(String rollNumber) {
        boolean removed = students.removeIf(s -> s.getRollNumber().equalsIgnoreCase(rollNumber));
        if (removed) {
            saveStudentsToFile(); 
        }
        return removed;
    }

    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return students.stream()
                .filter(s -> s.getRollNumber().equalsIgnoreCase(rollNumber))
                .findFirst();
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students); 
    }

    public boolean updateStudent(String rollNumber, String newName, String newGrade, String newContact) {
        Optional<Student> studentOptional = getStudentByRollNumber(rollNumber);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setName(newName);
            student.setGrade(newGrade);
            student.setContactNumber(newContact);
            saveStudentsToFile(); 
            return true;
        }
        return false;
    }


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
        } catch (IOException e) {
            System.err.println("Error reading student data from file: " + e.getMessage());
        }
    }

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


package student.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class StudentApp extends JFrame {

    private StudentManagementSystem sms;
    private JTextField nameField, rollNumberField, gradeField, contactField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentApp() {
        sms = new StudentManagementSystem();
        setTitle("Simple Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        initComponents();
        refreshStudentTable(); 

        pack(); 
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5)); 
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        nameField = new JTextField(20);
        rollNumberField = new JTextField(20);
        gradeField = new JTextField(20);
        contactField = new JTextField(20);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number (Unique):"));
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("Contact Number:"));
        inputPanel.add(contactField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Student");
        JButton searchButton = new JButton("Search Student");
        JButton updateButton = new JButton("Update Student");
        JButton removeButton = new JButton("Remove Student");
        JButton displayAllButton = new JButton("Display All"); 

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(displayAllButton);

        String[] columnNames = {"Name", "Roll Number", "Grade", "Contact Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true); 
        JScrollPane scrollPane = new JScrollPane(studentTable); 
        scrollPane.setPreferredSize(new Dimension(750, 250));

        addButton.addActionListener(e -> addStudent());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        removeButton.addActionListener(e -> removeStudent());
        displayAllButton.addActionListener(e -> refreshStudentTable()); 

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER); 
        mainPanel.add(scrollPane, BorderLayout.SOUTH); 

        add(mainPanel); 
    }


    private void addStudent() {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String grade = gradeField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(name, rollNumber, grade, contact);
        if (sms.addStudent(newStudent)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearFields();
            refreshStudentTable(); 
        } else {
            JOptionPane.showMessageDialog(this, "Student with Roll Number " + rollNumber + " already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent() {
        String rollNumber = JOptionPane.showInputDialog(this, "Enter Roll Number to search:");
        if (rollNumber != null && !rollNumber.trim().isEmpty()) {
            Optional<Student> studentOptional = sms.getStudentByRollNumber(rollNumber.trim());
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                JOptionPane.showMessageDialog(this, "Student Found:\n" + student.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Student with Roll Number " + rollNumber + " not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void updateStudent() {
        String rollNumberToUpdate = JOptionPane.showInputDialog(this, "Enter Roll Number of student to update:");
        if (rollNumberToUpdate == null || rollNumberToUpdate.trim().isEmpty()) {
            return;
        }

        Optional<Student> studentOptional = sms.getStudentByRollNumber(rollNumberToUpdate.trim());
        if (!studentOptional.isPresent()) {
            JOptionPane.showMessageDialog(this, "Student with Roll Number " + rollNumberToUpdate + " not found.", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student studentToEdit = studentOptional.get();

        JTextField editNameField = new JTextField(studentToEdit.getName());
        JTextField editGradeField = new JTextField(studentToEdit.getGrade());
        JTextField editContactField = new JTextField(studentToEdit.getContactNumber());

        JPanel editPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        editPanel.add(new JLabel("Name:"));
        editPanel.add(editNameField);
        editPanel.add(new JLabel("Grade:"));
        editPanel.add(editGradeField);
        editPanel.add(new JLabel("Contact Number:"));
        editPanel.add(editContactField);

        int response = JOptionPane.showConfirmDialog(this, editPanel,
                "Edit Student (Roll No: " + rollNumberToUpdate + ")",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
            String newName = editNameField.getText().trim();
            String newGrade = editGradeField.getText().trim();
            String newContact = editContactField.getText().trim();

            if (newName.isEmpty() || newGrade.isEmpty() || newContact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Grade, and Contact cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sms.updateStudent(rollNumberToUpdate.trim(), newName, newGrade, newContact)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                refreshStudentTable(); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Update cancelled.");
        }
        clearFields();
    }


    private void removeStudent() {
        String rollNumber = JOptionPane.showInputDialog(this, "Enter Roll Number to remove:");
        if (rollNumber != null && !rollNumber.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove student with Roll No: " + rollNumber + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (sms.removeStudent(rollNumber.trim())) {
                    JOptionPane.showMessageDialog(this, "Student removed successfully!");
                    refreshStudentTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Student with Roll Number " + rollNumber + " not found.", "Removal Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refreshStudentTable() {
        tableModel.setRowCount(0); 
        List<Student> students = sms.getAllStudents(); 

        for (Student student : students) {
            Object[] rowData = {student.getName(), student.getRollNumber(), student.getGrade(), student.getContactNumber()};
            tableModel.addRow(rowData);
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        });
    }
}

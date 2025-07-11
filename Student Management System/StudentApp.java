
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
        setLocationRelativeTo(null); // Center the window

        initComponents();
        refreshStudentTable(); // Populate table on startup

        // This is crucial: pack() sizes the frame to fit its components
        // setVisible(true) makes the frame appear
        pack(); // Adjusts frame size based on preferred sizes of its components
        setVisible(true);
    }

    private void initComponents() {
        // Use a JPanel for the main content to better organize sub-panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); // Outer layout with gaps
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the edges

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5)); // 4 rows for 4 fields
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

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center buttons with gaps
        JButton addButton = new JButton("Add Student");
        JButton searchButton = new JButton("Search Student");
        JButton updateButton = new JButton("Update Student");
        JButton removeButton = new JButton("Remove Student");
        JButton displayAllButton = new JButton("Display All"); // This button will explicitly refresh the table

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(displayAllButton);

        // --- Display Area (Table) ---
        String[] columnNames = {"Name", "Roll Number", "Grade", "Contact Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make table cells non-editable by default
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true); // Table fills the height of the scrollpane
        JScrollPane scrollPane = new JScrollPane(studentTable); // Make table scrollable
        scrollPane.setPreferredSize(new Dimension(750, 250)); // Give the table a preferred size

        // --- Action Listeners for Buttons ---
        addButton.addActionListener(e -> addStudent());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        removeButton.addActionListener(e -> removeStudent());
        displayAllButton.addActionListener(e -> refreshStudentTable()); // This refreshes the table

        // Add components to the mainPanel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER); // Buttons in the center area
        mainPanel.add(scrollPane, BorderLayout.SOUTH); // Table at the bottom

        add(mainPanel); // Add the mainPanel to the JFrame
    }

    // --- Button Action Methods ---

    private void addStudent() {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String grade = gradeField.getText().trim();
        String contact = contactField.getText().trim();

        // Input Validation (Requirement 6)
        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(name, rollNumber, grade, contact);
        if (sms.addStudent(newStudent)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearFields();
            refreshStudentTable(); // <--- CRUCIAL: Refresh table after adding
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

        // Use new dialog for editing to avoid interfering with main fields
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

            // Basic validation for updated fields
            if (newName.isEmpty() || newGrade.isEmpty() || newContact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Grade, and Contact cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sms.updateStudent(rollNumberToUpdate.trim(), newName, newGrade, newContact)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                refreshStudentTable(); // <--- CRUCIAL: Refresh table after updating
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Update cancelled.");
        }
        clearFields(); // Clear main input fields
    }


    private void removeStudent() {
        String rollNumber = JOptionPane.showInputDialog(this, "Enter Roll Number to remove:");
        if (rollNumber != null && !rollNumber.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove student with Roll No: " + rollNumber + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (sms.removeStudent(rollNumber.trim())) {
                    JOptionPane.showMessageDialog(this, "Student removed successfully!");
                    refreshStudentTable(); // <--- CRUCIAL: Refresh table after removing
                } else {
                    JOptionPane.showMessageDialog(this, "Student with Roll Number " + rollNumber + " not found.", "Removal Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // This method is called to populate/repopulate the JTable
    private void refreshStudentTable() {
        tableModel.setRowCount(0); // Clear existing rows in the table model
        List<Student> students = sms.getAllStudents(); // Get the current list of students

        for (Student student : students) {
            // Add each student as a new row to the table model
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
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            new StudentApp(); // The constructor now handles setVisible(true) and pack()
        });
    }
}
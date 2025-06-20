import java.util.Scanner; 
class StudentGradeCalculator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); 

        System.out.println("--- Student Grade Calculator ---");
        System.out.println("Enter marks for each subject (out of 100).");

        int numberOfSubjects;
        while (true) {
            System.out.print("Enter the total number of subjects: ");
            if (input.hasNextInt()) {
                numberOfSubjects = input.nextInt();
                if (numberOfSubjects > 0) {
                    break; 
                } else {
                    System.out.println("Number of subjects must be greater than 0. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a whole number for subjects.");
                input.next(); 
            }
        }


        int totalMarks = 0;
        for (int i = 1; i <= numberOfSubjects; i++) {
            int marks;
            while (true) {
                System.out.print("Enter marks for Subject " + i + ": ");
                if (input.hasNextInt()) {
                    marks = input.nextInt();
                    if (marks >= 0 && marks <= 100) {
                        totalMarks += marks; 
                        break; 
                    } else {
                        System.out.println("Marks must be between 0 and 100. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a whole number for marks.");
                    input.next(); 
                }
            }
        }
        double averagePercentage = (double) totalMarks / (numberOfSubjects * 100) * 100;
        String grade;
        if (averagePercentage >= 90) {
            grade = "A+";
        } else if (averagePercentage >= 80) {
            grade = "A";
        } else if (averagePercentage >= 70) {
            grade = "B+";
        } else if (averagePercentage >= 60) {
            grade = "B";
        } else if (averagePercentage >= 50) {
            grade = "C";
        } else if (averagePercentage >= 40) {
            grade = "D";
        } else {
            grade = "F"; 
        }

        System.out.println("\n--- Results ---");
        System.out.println("Total Marks: " + totalMarks + " / " + (numberOfSubjects * 100));
        System.out.printf("Average Percentage: %.2f%%\n", averagePercentage); 
        System.out.println("Grade: " + grade);

        input.close();
    }
}
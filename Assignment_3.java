import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// =============================
// CUSTOM EXCEPTION
// =============================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// =============================
// LOADER THREAD (Runnable)
// =============================
class Loader implements Runnable {
    private String message;

    public Loader(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            System.out.print(message);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted");
        }
    }
}

// =============================
// STUDENT CLASS
// =============================
class Student {
    private Integer rollNo;     // Wrapper Class
    private String name;
    private String email;
    private String course;
    private Double marks;       // Wrapper Class
    private String grade;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = "A";
        else if (marks >= 75) grade = "B";
        else if (marks >= 60) grade = "C";
        else grade = "D";
    }

    public Integer getRollNo() { return rollNo; }

    public void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println();
    }
}

// =============================
// RECORD ACTIONS INTERFACE
// =============================
interface RecordActions {
    void addStudent(Student s);
    void viewAllStudents();
    Student searchStudent(int rollNo) throws StudentNotFoundException;
}

// =============================
// STUDENT MANAGER CLASS
// =============================
class StudentManager implements RecordActions {

    private Map<Integer, Student> map = new HashMap<>();

    @Override
    public void addStudent(Student s) {
        map.put(s.getRollNo(), s);
    }

    @Override
    public void viewAllStudents() {
        if (map.isEmpty()) {
            System.out.println("No students found.\n");
            return;
        }
        for (Student s : map.values()) {
            s.display();
        }
    }

    @Override
    public Student searchStudent(int rollNo) throws StudentNotFoundException {
        if (!map.containsKey(rollNo)) {
            throw new StudentNotFoundException("Student with Roll No " + rollNo + " not found!");
        }
        return map.get(rollNo);
    }
}

// =============================
// MAIN CLASS
// =============================
public class StudentManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer roll = Integer.parseInt(sc.nextLine());  // Wrapper + AutoBoxing

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            if (name.trim().isEmpty()) throw new Exception("Name cannot be empty!");

            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            if (email.trim().isEmpty()) throw new Exception("Email cannot be empty!");

            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            if (course.trim().isEmpty()) throw new Exception("Course cannot be empty!");

            System.out.print("Enter Marks (Double): ");
            Double marks = Double.parseDouble(sc.nextLine()); // Wrapper + AutoBoxing
            if (marks < 0 || marks > 100)
                throw new Exception("Marks must be between 0 and 100!");

            // Simulate loading using thread
            Thread t = new Thread(new Loader("Loading"));
            t.start();
            t.join();

            // Add student after loading
            Student s = new Student(roll, name, email, course, marks);
            manager.addStudent(s);

            // Display stored student
            System.out.println("Saved Student Record:\n");
            s.display();

        } catch (NumberFormatException e) {
            System.out.println("Error: You must enter numeric values for Roll No and Marks!");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Input Error: " + e.getMessage());
        } finally {
            System.out.println("\nProgram execution completed.");
        }
    }
}

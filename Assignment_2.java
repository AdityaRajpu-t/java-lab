import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// =============================
// ABSTRACT CLASS PERSON
// =============================
abstract class Person {
    protected String name;
    protected String email;

    Person() {}

    Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();

    public final void finalMethod() {
        System.out.println("This is a final method in a final class.");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalize method called before object is garbage collected.");
    }
}

// =============================
// STUDENT CLASS
// =============================
class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private String grade;

    // Constructor Overloading
    public Student() {}

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
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

    // Method Overloading
    public void displayInfo(String note) {
        displayInfo();
        System.out.println(note);
    }

    // Method Overriding
    @Override
    public void displayInfo() {
        System.out.println("Student Info:");
        System.out.println("Roll No : " + rollNo);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Course  : " + course);
        System.out.println("Marks   : " + marks);
        System.out.println("Grade   : " + grade);
        System.out.println();
    }

    public int getRollNo() {
        return rollNo;
    }
}

// =============================
// INTERFACE
// =============================
interface RecordActions {
    void addStudent(Student s);
    void deleteStudent(int rollNo);
    void updateStudent(int rollNo, Student updated);
    Student searchStudent(int rollNo);
    void viewAllStudents();
}

// =============================
// STUDENT MANAGER
// =============================
class StudentManager implements RecordActions {

    private Map<Integer, Student> students = new HashMap<>();

    @Override
    public void addStudent(Student s) {
        if (students.containsKey(s.getRollNo())) {
            System.out.println("Error: Duplicate Roll Number!\n");
            return;
        }
        students.put(s.getRollNo(), s);
        System.out.println("Student Added Successfully!\n");
    }

    @Override
    public void deleteStudent(int rollNo) {
        if (students.remove(rollNo) != null)
            System.out.println("Student Deleted Successfully!\n");
        else
            System.out.println("Student Not Found.\n");
    }

    @Override
    public void updateStudent(int rollNo, Student updated) {
        if (students.containsKey(rollNo)) {
            students.put(rollNo, updated);
            System.out.println("Student Updated Successfully!\n");
        } else {
            System.out.println("Student Not Found.\n");
        }
    }

    @Override
    public Student searchStudent(int rollNo) {
        return students.get(rollNo);
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No Student Records Found.\n");
            return;
        }
        for (Student s : students.values()) {
            s.displayInfo();
        }
    }
}

// =============================
// MAIN CLASS
// =============================
public class StudentManagement {
    public static void main(String[] args) {

        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Roll No: ");
                    int r = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Name: ");
                    String n = sc.nextLine();

                    System.out.print("Email: ");
                    String e = sc.nextLine();

                    System.out.print("Course: ");
                    String c = sc.nextLine();

                    System.out.print("Marks: ");
                    double m = sc.nextDouble();

                    Student s = new Student(r, n, e, c, m);
                    manager.addStudent(s);
                    break;

                case 2:
                    System.out.print("Enter Roll No to Delete: ");
                    manager.deleteStudent(sc.nextInt());
                    break;

                case 3:
                    System.out.print("Roll No to Update: ");
                    int r2 = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Name: ");
                    String n2 = sc.nextLine();

                    System.out.print("New Email: ");
                    String e2 = sc.nextLine();

                    System.out.print("New Course: ");
                    String c2 = sc.nextLine();

                    System.out.print("New Marks: ");
                    double m2 = sc.nextDouble();

                    manager.updateStudent(r2, new Student(r2, n2, e2, c2, m2));
                    break;

                case 4:
                    System.out.print("Enter Roll No: ");
                    Student found = manager.searchStudent(sc.nextInt());
                    if (found != null)
                        found.displayInfo();
                    else
                        System.out.println("Student Not Found.\n");
                    break;

                case 5:
                    manager.viewAllStudents();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid Choice!\n");
            }
        }
    }
}

import java.io.*;
import java.util.*;

// ========================== PERSON (ABSTRACT) ==========================
abstract class Person {
    String name;
    String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    abstract void displayInfo();
}

// ============================ STUDENT CLASS =============================
class Student extends Person {
    int rollNo;
    String course;
    double marks;
    String grade;

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    void calculateGrade() {
        if (marks >= 90) grade = "A";
        else if (marks >= 75) grade = "B";
        else if (marks >= 60) grade = "C";
        else grade = "D";
    }

    void inputDetails() { }

    void displayDetails() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade + "\n");
    }

    @Override
    void displayInfo() {
        displayDetails();
    }
}

// ======================== CUSTOM EXCEPTION ==============================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// =========================== INTERFACE =================================
interface RecordActions {
    void addStudent(Scanner sc);
    void deleteStudent(Scanner sc);
    void updateStudent(Scanner sc);
    void searchStudent(Scanner sc);
    void viewAllStudents();
}

// =========================== LOADER THREAD ==============================
class Loader implements Runnable {
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println("\n");
        } catch (Exception e) {
            System.out.println("Loading interrupted.");
        }
    }
}

// ======================= STUDENT MANAGER CLASS ========================
class StudentManager implements RecordActions {

    List<Student> students = new ArrayList<>();
    Map<Integer, Student> studentMap = new HashMap<>();
    final String FILE_NAME = "students.txt";

    public StudentManager() {
        loadFromFile();
    }

    // ---------------------- LOAD FROM FILE ---------------------------
    void loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int roll = Integer.parseInt(data[0]);
                String name = data[1];
                String email = data[2];
                String course = data[3];
                double marks = Double.parseDouble(data[4]);

                Student s = new Student(roll, name, email, course, marks);
                students.add(s);
                studentMap.put(roll, s);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // ---------------------- SAVE TO FILE -----------------------------
    void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));

            for (Student s : students) {
                bw.write(s.rollNo + "," + s.name + "," + s.email + "," + s.course + "," + s.marks);
                bw.newLine();
            }
            bw.close();
            System.out.println("Saved successfully.");

        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // ---------------------- ADD STUDENT ------------------------------
    @Override
    public void addStudent(Scanner sc) {
        try {
            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt();
            sc.nextLine();

            if (studentMap.containsKey(roll))
                throw new Exception("Roll number already exists!");

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            if (marks < 0 || marks > 100)
                throw new Exception("Marks must be between 0 and 100!");

            Student s = new Student(roll, name, email, course, marks);
            students.add(s);
            studentMap.put(roll, s);

            System.out.println("Student added!\n");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --------------------- DELETE STUDENT ----------------------------
    @Override
    public void deleteStudent(Scanner sc) {
        try {
            System.out.print("Enter roll number to delete: ");
            int roll = sc.nextInt();

            if (!studentMap.containsKey(roll))
                throw new StudentNotFoundException("Student not found!");

            Student s = studentMap.get(roll);
            students.remove(s);
            studentMap.remove(roll);

            System.out.println("Student deleted.\n");

        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // --------------------- UPDATE STUDENT ----------------------------
    @Override
    public void updateStudent(Scanner sc) {
        try {
            System.out.print("Enter roll number to update: ");
            int roll = sc.nextInt();
            sc.nextLine();

            if (!studentMap.containsKey(roll))
                throw new StudentNotFoundException("Student not found!");

            Student s = studentMap.get(roll);

            System.out.print("Enter new Email: ");
            s.email = sc.nextLine();

            System.out.print("Enter new Marks: ");
            double marks = sc.nextDouble();

            if (marks < 0 || marks > 100)
                throw new Exception("Invalid marks!");

            s.marks = marks;
            s.calculateGrade();

            System.out.println("Record updated.\n");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------- SEARCH STUDENT ---------------------------
    @Override
    public void searchStudent(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter name to search: ");
        String name = sc.nextLine();

        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                s.displayDetails();
                return;
            }
        }
        System.out.println("Student not found.\n");
    }

    // ---------------------- VIEW ALL STUDENTS ------------------------
    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No records available.\n");
            return;
        }

        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            it.next().displayDetails();
        }
    }

    // ---------------------- SORT BY MARKS ----------------------------
    void sortByMarks() {
        students.sort((a, b) -> Double.compare(b.marks, a.marks));
        System.out.println("Sorted by marks (desc):\n");

        for (Student s : students)
            s.displayDetails();
    }
}

// ============================ MAIN CLASS ================================
public class StudentManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager sm = new StudentManager();

        while (true) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Roll No");
            System.out.println("5. Update Student");
            System.out.println("6. Sort by Marks");
            System.out.println("7. Save & Exit");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> sm.addStudent(sc);
                case 2 -> sm.viewAllStudents();
                case 3 -> sm.searchStudent(sc);
                case 4 -> sm.deleteStudent(sc);
                case 5 -> sm.updateStudent(sc);
                case 6 -> sm.sortByMarks();
                case 7 -> {
                    sm.saveToFile();
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!\n");
            }
        }
    }
}

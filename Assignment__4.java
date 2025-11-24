import java.io.*;
import java.util.*;

public class StudentManagement {

    // ----------------- Student CLASS -----------------
    static class Student {
        int rollNo;
        String name, email, course;
        double marks;

        Student(int rollNo, String name, String email, String course, double marks) {
            this.rollNo = rollNo;
            this.name = name;
            this.email = email;
            this.course = course;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return "Roll No: " + rollNo +
                    "\nName: " + name +
                    "\nEmail: " + email +
                    "\nCourse: " + course +
                    "\nMarks: " + marks + "\n";
        }
    }

    // ----------------- FileUtil CLASS -----------------
    static class FileUtil {
        private static final String FILE_NAME = "students.txt";

        // Read students from file
        static ArrayList<Student> loadStudents() {
            ArrayList<Student> list = new ArrayList<>();
            File file = new File(FILE_NAME);

            try {
                if (!file.exists()) {
                    file.createNewFile();
                    return list;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        int roll = Integer.parseInt(data[0]);
                        double marks = Double.parseDouble(data[4]);
                        list.add(new Student(roll, data[1], data[2], data[3], marks));
                    }
                }
                br.close();

                System.out.println("Loaded students from file:");
                for (Student s : list) System.out.println(s);

            } catch (Exception e) {
                System.out.println("Error loading students: " + e);
            }

            return list;
        }

        // Save students to file
        static void saveStudents(ArrayList<Student> list) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
                for (Student s : list) {
                    bw.write(s.rollNo + "," + s.name + "," + s.email + "," + s.course + "," + s.marks);
                    bw.newLine();
                }
                bw.close();
                System.out.println("Records saved successfully.");
            } catch (Exception e) {
                System.out.println("Error saving file: " + e);
            }
        }

        // Demonstrate RandomAccessFile
        static void randomAccessDemo() {
            try {
                RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r");
                System.out.println("\nReading first 50 bytes using RandomAccessFile:");
                byte[] buffer = new byte[50];
                raf.read(buffer);
                System.out.println(new String(buffer));
                raf.close();
            } catch (Exception e) {
                System.out.println("RandomAccessFile Error: " + e);
            }
        }
    }

    // ----------------- StudentManager CLASS -----------------
    static class StudentManager {
        private ArrayList<Student> students = new ArrayList<>();

        StudentManager() {
            students = FileUtil.loadStudents();
        }

        void addStudent(Scanner sc) {
            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            students.add(new Student(roll, name, email, course, marks));
            System.out.println("Student Added.\n");
        }

        void viewAllStudents() {
            if (students.isEmpty()) {
                System.out.println("No records available.");
                return;
            }
            Iterator<Student> it = students.iterator();
            while (it.hasNext()) System.out.println(it.next());
        }

        void searchByName(Scanner sc) {
            System.out.print("Enter Name to Search: ");
            sc.nextLine();
            String name = sc.nextLine();

            for (Student s : students) {
                if (s.name.equalsIgnoreCase(name)) {
                    System.out.println(s);
                    return;
                }
            }
            System.out.println("Record not found.");
        }

        void deleteByName(Scanner sc) {
            System.out.print("Enter Name to Delete: ");
            sc.nextLine();
            String name = sc.nextLine();

            Iterator<Student> it = students.iterator();
            while (it.hasNext()) {
                Student s = it.next();
                if (s.name.equalsIgnoreCase(name)) {
                    it.remove();
                    System.out.println("Record deleted.");
                    return;
                }
            }
            System.out.println("Record not found.");
        }

        void sortByMarks() {
            students.sort(Comparator.comparingDouble(s -> s.marks));

            System.out.println("Sorted Student List by Marks:");
            for (Student s : students) System.out.println(s);
        }

        void showFileAttributes() {
            File file = new File("students.txt");
            System.out.println("\nFile Attributes:");
            System.out.println("Path: " + file.getAbsolutePath());
            System.out.println("Readable: " + file.canRead());
            System.out.println("Writable: " + file.canWrite());
            System.out.println("Size: " + file.length() + " bytes\n");
        }

        void saveAndExit() {
            FileUtil.saveStudents(students);
            FileUtil.randomAccessDemo();
            System.out.println("Exiting...");
        }
    }

    // ----------------- MAIN METHOD (Menu) -----------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Show File Attributes");
            System.out.println("7. Save and Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> manager.addStudent(sc);
                case 2 -> manager.viewAllStudents();
                case 3 -> manager.searchByName(sc);
                case 4 -> manager.deleteByName(sc);
                case 5 -> manager.sortByMarks();
                case 6 -> manager.showFileAttributes();
                case 7 -> {
                    manager.saveAndExit();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

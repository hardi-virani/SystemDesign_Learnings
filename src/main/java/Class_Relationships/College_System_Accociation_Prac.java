package Class_Relationships;

import java.sql.SQLOutput;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

class Professor {
    private final String name;
    private final List<Course> courses = new ArrayList<>();

    public Professor(String name) {
        this.name = name;
    }

    //One to many(Both side)
    //Took inspo from the algomaster sample code.
    public void addCourse(Course course){
        if(!courses.contains(course)) {
            courses.add(course);
//            courses.addProf(this); //Had so check this with original code.
            course.setProfessor(this);
        }
    }

    public String getName() { return name;}
    public List<Course> getCourses() { return List.copyOf(courses);}
}


class Course {
    private final String title;
    private Professor professor;
    private List<Student> students = new ArrayList<>();

    public Course(String title) {
        this.title = title;
    }

    void setProfessor(Professor professor) {
        this.professor = professor;
    }

    //Many to Many
    public void addStudent(Student student) {
        if(!students.contains(student)) {
            students.add(student);
            student.enroll(this);
        }
    }

    public String getTitle() {
        return title;
    }

    public Professor getProfessor() {
        return professor;
    }

    public List<Student> getStudents() {return List.copyOf(students);}
}

class Student {
    private final String name;
    private List<Course> courses = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    void enroll(Course course){
        this.courses.add(course);
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return List.copyOf(courses);
    }

}

public class College_System_Accociation_Prac {
    public static void main(String[] args) {
        Professor prof = new Professor("Mann");
        Course math = new Course("Math");
        Course com = new Course("Computer");

        prof.addCourse(math);
        prof.addCourse(com);

        Student hardi = new Student("Hardi");
        Student star = new Student("Star");
        Student marry = new Student ("Marry");



        hardi.enroll(math);
        star.enroll(com);
        marry.enroll(math);


        for(Course c : prof.getCourses()) {
            System.out.println(c.getTitle() + "(taught by " + c.getProfessor().getName() + ")");
            for(Student s : c.getStudents()) {
                System.out.println("  - " + s.getName());
            }
        }

        for(Student s : List.of(hardi, star, marry)) {
            System.out.print(s.getName() + " takes: ");
            for(Course c : s.getCourses()) {
                System.out.print(c.getTitle() + " ");
            }
            System.out.println();
        }
    }
}


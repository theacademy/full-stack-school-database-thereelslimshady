package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.StudentDao;
import mthree.com.fullstackschool.model.Course;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    //YOUR CODE STARTS HERE
    StudentDao studentDao;
    CourseServiceInterface courseServiceInterface;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, CourseServiceInterface courseServiceInterface) {
        this.studentDao = studentDao;
        this.courseServiceInterface = courseServiceInterface;
    }


    //YOUR CODE ENDS HERE

    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        return studentDao.getAllStudents();

        //YOUR CODE ENDS HERE
    }

    public Student getStudentById(int id) {
        //YOUR CODE STARTS HERE
        Student student = studentDao.findStudentById(id);
        if (student == null){
            student = new Student();
            student.setStudentFirstName("Student Not Found");
            student.setStudentLastName("Student Not Found");
        }
        return student;
        //YOUR CODE ENDS HERE
    }

    public Student addNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        if (student.getStudentFirstName().trim().isEmpty() || student.getStudentLastName().trim().isEmpty()){
            student.setStudentFirstName("First Name blank, student NOT added");
            student.setStudentLastName("Last Name blank, student NOT added");
        }else {
            student = studentDao.createNewStudent(student);
        }
        return student;

        //YOUR CODE ENDS HERE
    }

    public Student updateStudentData(int id, Student student) {
        //YOUR CODE STARTS HERE
        // If id is not equal to object id, then set FirstName and LastName = "IDs do not match, student not updated".
        if (id != student.getStudentId()){
            student.setStudentFirstName("IDs do not match, student not updated");
            student.setStudentLastName("IDs do not match, student not updated");

        }else {
            studentDao.updateStudent(student);
        }
        return student;

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentById(int id) {
        //YOUR CODE STARTS HERE

        studentDao.deleteStudent(id);

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        Student student = getStudentById(studentId);
        Course course = courseServiceInterface.getCourseById(courseId);
        if (student.getStudentFirstName().equals("Student Not Found")){
            System.out.println("Student Not Found");
        }else if (course.getCourseName().equals("Course Not Found")){
            System.out.println("Course not found");
        }else{
            studentDao.deleteStudentFromCourse(studentId, courseId);
            System.out.printf("Student: %d deleted from course: %d", studentId, courseId);
        }

        //YOUR CODE ENDS HERE
    }

    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        Student student = getStudentById(studentId);
        Course course = courseServiceInterface.getCourseById(courseId);
        if (student.getStudentFirstName().equals("Student Not Found") ){
            System.out.println("Student Not Found");
        }else if (course.getCourseName().equals("Course Not Found") ){
            System.out.println("Course not found");
        }else {
            try {
                studentDao.addStudentToCourse(studentId, courseId);
                System.out.printf("Student: %d added to course: %d\n", studentId, courseId);
            }catch (IllegalArgumentException e){
                System.out.printf("Student: %d already enrolled to course: %d\n", studentId, courseId);
            }
        }

        //YOUR CODE ENDS HERE
    }
}

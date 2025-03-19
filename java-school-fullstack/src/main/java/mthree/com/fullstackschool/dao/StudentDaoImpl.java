package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        // todo: verify if ids autoincrememt - yes, but DB = H2
        final String INSERT_STUDENT = "insert into student(fName, lName) values (?,?)";
        jdbcTemplate.update(INSERT_STUDENT, student.getStudentFirstName(), student.getStudentLastName());

        // here, since it's a H2 db, using last_insert_id() did not seem to work, neither the equivalent identity() or scope_identity()
        // since it's transactional, there is no risk of conflict
        int newId = jdbcTemplate.queryForObject("SELECT MAX(sid) FROM student", Integer.class);
        student.setStudentId(newId);
        return student;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        final String SELECT_ALL_STUDENTS = "select * from student";
        return jdbcTemplate.query(SELECT_ALL_STUDENTS, new StudentMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE

        try{
            final String SELECT_STUDENT_BY_ID = "select * from student where sid = ?";
            return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID, new StudentMapper(),id );
        }
        catch (DataAccessException e){
            return null;
        }

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE

        final String UPDATE_STUDENT= "update student set fName=?, lName= ? where sid= ?";
        jdbcTemplate.update(UPDATE_STUDENT, student.getStudentFirstName(), student.getStudentLastName(), student.getStudentId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE
        //this should be transactional

        final String DELETE_STUDENT_FROM_COURSE_STUDENT = "delete from course_student where student_id = ?";
        jdbcTemplate.update(DELETE_STUDENT_FROM_COURSE_STUDENT, id);

        final String DELETE_STUDENT = "delete from student where sid=?";
        jdbcTemplate.update(DELETE_STUDENT, id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        final String INSERT_STUDENT_COURSE_STUDENT = "insert into course_student(student_id, course_id) values (?,?)";
        jdbcTemplate.update(INSERT_STUDENT_COURSE_STUDENT, studentId,courseId);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        final String DELETE_STUDENT_COURSE_STUDENT = "delete from course_student where (student_id=? and course_id=?)";
        jdbcTemplate.update(DELETE_STUDENT_COURSE_STUDENT, studentId,courseId);

        //YOUR CODE ENDS HERE
    }
}

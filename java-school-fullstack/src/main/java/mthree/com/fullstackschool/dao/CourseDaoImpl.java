package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.CourseMapper;
import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Course;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course createNewCourse(Course course) {
        //YOUR CODE STARTS HERE
        // here, since the function is not transactional, we need to use the connection, prepared statement and generated key golder to get the key information in one transaction
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_COURSE = "insert into course(courseCode, courseDesc, teacherId) values (?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement pStmt = connection.prepareStatement(INSERT_COURSE, Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, course.getCourseName());
            pStmt.setString(2,course.getCourseDesc());
            pStmt.setInt(3,course.getTeacherId());
            return pStmt;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        course.setCourseId(newId);
        return course;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE

        final String SELECT_ALL_COURSES = "select * from course";
        return jdbcTemplate.query(SELECT_ALL_COURSES, new CourseMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Course findCourseById(int id) {
        //YOUR CODE STARTS HERE
        try{
            final String SELECT_COURSE_BY_ID = "select * from course where cid = ?";
            return jdbcTemplate.queryForObject(SELECT_COURSE_BY_ID, new CourseMapper(),id );
        }
        catch (DataAccessException e){
            return null;
        }
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateCourse(Course course) {
        //YOUR CODE STARTS HERE

        final String UPDATE_COURSE= "update course set courseCode=?, courseDesc= ?, teacherId=? where cid= ?";
        jdbcTemplate.update(UPDATE_COURSE, course.getCourseName(), course.getCourseDesc(), course.getTeacherId(), course.getCourseId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteCourse(int id) {
        //YOUR CODE STARTS HERE

        final String DELETE_COURSE = "delete from course where cid=?";
        jdbcTemplate.update(DELETE_COURSE, id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteAllStudentsFromCourse(int courseId) {
        //YOUR CODE STARTS HERE

        final String DELETE_ALL_STUDENT_FROM_COURSE = "delete from course_student where course_id=?";
        jdbcTemplate.update(DELETE_ALL_STUDENT_FROM_COURSE, courseId);

        //YOUR CODE ENDS HERE
    }
}

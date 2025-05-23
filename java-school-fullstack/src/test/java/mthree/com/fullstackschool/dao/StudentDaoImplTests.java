package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentDaoImplTests {
    private JdbcTemplate jdbcTemplate;
    private StudentDao studentDao;

    @Autowired
    public void StudentDaoImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        studentDao = new StudentDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Add New Student Test")
    public void addNewStudentTest() {
        Student student = new Student();
        student.setStudentFirstName("New Student First Name");
        student.setStudentLastName("New Student Last Name");
        studentDao.createNewStudent(student);
        List<Student> newList = studentDao.getAllStudents();
        assertNotNull(newList);
        assertEquals(9, newList.size());
    }

    @Test
    @DisplayName("Get A List Of All Students")
    public void getListOfAllStudentsTest() {
        List<Student> newList = studentDao.getAllStudents();
        assertNotNull(newList);
        assertEquals(8, newList.size());
    }

    @Test
    @DisplayName("Find Student By Id")
    public void findStudentByIdTest() {
        Student student = studentDao.findStudentById(2);
        assertNotNull(student);
        assertEquals("Tabby", student.getStudentFirstName());
        assertEquals("Daniell", student.getStudentLastName());
    }

    @Test
    @DisplayName("Update Student Info")
    public void updateCourseInfoTest() {
        Student student = new Student();
        student.setStudentId(1);
        student.setStudentFirstName("William");
        student.setStudentLastName("Gates");
        studentDao.updateStudent(student);
        List<Student> newList = studentDao.getAllStudents();
        assertNotNull(newList);
        int i = 0;
        for (Student st : newList) {
            if(st.getStudentFirstName().contains("William")) {
                i++;
            }
        }
        assertTrue(i != 0);
    }

    @Test
    @DisplayName("Delete Student Test")
    @Transactional
    public void deleteStudentTest() {
        studentDao.deleteStudent(8);
        assertNotNull(studentDao.getAllStudents());
        assertEquals(7, studentDao.getAllStudents().size());
    }
}

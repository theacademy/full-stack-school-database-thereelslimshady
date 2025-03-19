package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        // here, since the function is not transactional, we need to use the connection, prepared statement and generated key golder to get the key information in one transaction
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_TEACHER = "insert into teacher(tFName, tLName, dept) values (?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement pStmt = connection.prepareStatement(INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, teacher.getTeacherFName());
            pStmt.setString(2,teacher.getTeacherLName());
            pStmt.setString(3,teacher.getDept());
            return pStmt;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        teacher.setTeacherId(newId);
        return teacher;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        final String SELECT_ALL_TEACHERS = "select * from teacher";
        return jdbcTemplate.query(SELECT_ALL_TEACHERS, new TeacherMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE

        try{
            final String SELECT_TEACHER_BY_ID = "select * from teacher where tid = ?";
            return jdbcTemplate.queryForObject(SELECT_TEACHER_BY_ID, new TeacherMapper(),id );
        }
        catch (DataAccessException e){
            return null;
        }

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE

        final String UPDATE_TEACHER= "update teacher set tFName=?, tLName= ?, dept=? where tid= ?";
        jdbcTemplate.update(UPDATE_TEACHER, t.getTeacherFName(), t.getTeacherLName(), t.getDept(), t.getTeacherId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE

        //todo verify if delete cascade or we need to delete from the bridge table as well
        final String DELETE_TEACHER = "delete from teacher where tid=?";
        jdbcTemplate.update(DELETE_TEACHER, id);

        //YOUR CODE ENDS HERE
    }
}

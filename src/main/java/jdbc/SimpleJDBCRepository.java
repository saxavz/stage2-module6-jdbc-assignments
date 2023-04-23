package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.DocFlavor;
import java.sql.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {
    public static final String USER_TABLE_NAME = "PUBLIC.MYUSERS";


    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;


    private static final String createUserSQL = "INSERT INTO " + USER_TABLE_NAME + "(firstname,lastname,age) values(?,?,?) RETURNING id";
    private static final String updateUserSQL = "UPDATE " + USER_TABLE_NAME + " SET firstname = ?, lastname = ?, age = ? where id = ? RETURNING id,firstname,lastname,age";
    private static final String deleteUser = "DELETE FROM " + USER_TABLE_NAME + " where id = ?";
    private static final String findUserByIdSQL = "SELECT firstname,lastname,age from " + USER_TABLE_NAME + " where id = ?";
    private static final String findUserByNameSQL = "SELECT id,firstname,lastname,age from " + USER_TABLE_NAME + " where firstname = ?";;
    private static final String findAllUserSQL = "SELECT id,firstname,lastname,age from " + USER_TABLE_NAME;

    public SimpleJDBCRepository(Connection connection) {
        this.connection = connection;
    }

    public Long createUser(User u) {
        long retVal = 0;
        try(PreparedStatement ps = connection.prepareStatement(createUserSQL)){
            ps.setString(1,u.getFirstName());
            ps.setString(2,u.getLastName());
            ps.setInt(3,u.getAge());
            ResultSet rs = ps.executeQuery();

            retVal = rs.next() ? rs.getLong(1) : -1L;

        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on user insert ", e);
        }
            return retVal;
    }

    public User findUserById(Long userId) {
        User u = new User();
        try(PreparedStatement ps = connection.prepareStatement(findUserByIdSQL)){
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                u.setId(userId);
                u.setFirstName(rs.getString(1));
                u.setLastName(rs.getString(2));
                u.setAge(rs.getInt(3));
            } else {
                throw new SQLException("No data found");
            }

        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on user data select for id: " + userId , e);
        }

        return u;
    }

    public User findUserByName(String userName) {
        User u = new User();
        try(PreparedStatement ps = connection.prepareStatement(findUserByNameSQL)){
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                u.setId(rs.getLong(1));
                u.setFirstName(rs.getString(2));
                u.setLastName(rs.getString(3));
                u.setAge(rs.getInt(4));
            } else {
                throw new SQLException("No data found");
            }

        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on user data select for name: " + userName , e);
        }

        return u;
    }

    public List<User> findAllUser() {
        List<User> ret = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(findAllUserSQL)){
            ps.setFetchSize(5000);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User u = new User();
                u.setId(rs.getLong(1));
                u.setFirstName(rs.getString(2));
                u.setLastName(rs.getString(3));
                u.setAge(rs.getInt(4));
                ret.add(u);
            }
        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on find all users data", e);
        }
        return ret;
    }

    public User updateUser(User u) {
        try(PreparedStatement ps = connection.prepareStatement(updateUserSQL)){
            ps.setString(1,u.getFirstName());
            ps.setString(2,u.getLastName());
            ps.setInt(3,u.getAge());
            ps.setLong(4,u.getId()); //id

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                u.setId(rs.getLong(1));
                u.setFirstName(rs.getString(2));
                u.setLastName(rs.getString(3));
                u.setAge(rs.getInt(4));
            } else {
                throw new SQLException("No data found");
            }

        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on user data update for id: " + 1 , e);
        }

        return u;
    }

    public void deleteUser(Long userId) {
        try(PreparedStatement ps = connection.prepareStatement(deleteUser)){
            ps.setLong(1,userId);
            ps.execute();
        } catch (SQLException e){
            throw new RuntimeException("Problem occurred on user data delete for id: " + userId , e);
        }
    }
}

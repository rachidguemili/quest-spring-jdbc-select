package com.wildcodeschool.wildandwizard.repository;

import com.wildcodeschool.wildandwizard.entity.School;
import com.wildcodeschool.wildandwizard.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SchoolRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/spring_jdbc_quest?serverTimezone=GMT";
    private final static String DB_USER = "h4rryp0tt3r";
    private final static String DB_PASSWORD = "Horcrux4life!";


    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    public List<School> findAll() {

        // TODO : find all schools



        try {
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );
            statement = connection.prepareStatement(
                    "SELECT * FROM school;"
            );
            resultSet = statement.executeQuery();

            List<School> schools = new ArrayList<>();

            String name;
            Long  capacity;
            Long id;
            String country;
            while (resultSet.next()) {
                id = resultSet.getLong("id");
                name  = resultSet.getString("name");
                capacity = resultSet.getLong("capacity");
                country = resultSet.getString("country");


                schools.add(new School(id, name, capacity, country));
            }
            return schools;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }


        return null;
    }

    public School findById(Long id) {

        // TODO : find a school by id
        try {
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );
            statement = connection.prepareStatement(
                    "SELECT * FROM school WHERE id = ? ;"
            );
            statement.setLong(1,id);
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                String firstName = resultSet.getString("name");
                Long capacity = resultSet.getLong("capacity");
                String country = resultSet.getString("country");

                return new School(id, firstName, capacity, country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }


        return null;
    }
    public List<School> findByCountry(String country) {

        // TODO : search schools by country
        try {
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );
            statement = connection.prepareStatement(
                    "SELECT * FROM school WHERE country like ? ;"
            );
            statement.setString(1,country);
            resultSet = statement.executeQuery();

            List<School>  schools = new ArrayList<>();
            if (resultSet.next()) {

                String firstName = resultSet.getString("name");
                Long capacity = resultSet.getLong("capacity");
                Long id = resultSet.getLong("id");
                schools.add(new School(id,firstName, capacity, country));

            }

            return  schools;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }


        return null;
    }

    public  School createSchool(String name,long capacity, String country){

        ResultSet resid;

        // on prepare la requete
        // on execute la requete
        try {

            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );

            System.out.println(" avant query ");
            String query = "insert into school (name,capacity,country) values (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,name);
            statement.setLong(2,capacity);
            statement.setString(3,country);

            if (statement.executeUpdate() != 1) {
                throw new SQLException("failed to insert data");
            }
            resid = statement.getGeneratedKeys();


            if (resid.next()) {
                long id = resid.getLong(1);
                System.out.println("===iddddddddddddd"  + id);

                statement.close();
                School sc = new School(id,name,capacity,country);
                System.out.println(sc);
                return    sc;
            }
        }
        catch (SQLException se)
        {
            System.out.println(se);
        }

          return    null;

    }



}

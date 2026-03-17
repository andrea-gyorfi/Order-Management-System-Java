package com.example.assignment3.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.assignment3.connection.ConnectionFactory;

/**
 * This is a generic class which interacts with the database and provides CRUD operations.
 * It uses reflection to generate SQL queries based on the class.
 * @param <T> type of the objects it manages.
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * This method creates a SQL SELECT query string to find a record by a specified field.
     * @param field is the field name used in the WHERE clause.
     * @return the constructed SQL SELECT query string.
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * This method creates a SQL SELECT query string to find all the records from a table.
     * @return the constructed SQL SELECT ALL query string
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * This method create a SQL INSERT query string to add a new record.
     * @return the constructed SQL INSERT query string
     */
    private String createInsertQuery() {
        Field[] fields = type.getDeclaredFields();

        String columns = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String values = Arrays.stream(fields)
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", type.getSimpleName(), columns, values);
    }


    /**
     * This method creates a SQL UPDATE query string to update the fields of a record (except the specified field).
     * @param field is the field used in the WHERE clause
     * @return the constructed SQL UPDATE query string
     */
    private String createUpdateQuery(String field) {
        Field[] fields = type.getDeclaredFields();

        String setClause = IntStream.range(1, fields.length)
                .mapToObj(i -> fields[i].getName() + " = ?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?", type.getSimpleName(), setClause, field);
    }

    /**
     * This method creates a SQL DELETE query string to delete a record by a specified field.
     * @param field is the field used in the WHERE clause.
     * @return the constructed SQL DELETE query string
     */
    private String createDeleteQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        sb.append(field);
        sb.append(" = ?");
        return sb.toString();
    }

    /**
     * This method connects to the database and finds all the records from a table of type T.
     * @return a list of all the records found (can be empty).
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        List<T> resultList = new ArrayList<>();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            resultList = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return resultList;
    }

    /**
     * This method connects to the database and finds only 1 record based on the id.
     * @param id is the id of the record to find.
     * @return the found record with the given id (can be null).
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> elem = createObjects(resultSet);
            if(elem.isEmpty()) {
                return null;
            }
            return elem.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * This method creates a list of objects of type T from the resultSet it receives from the database.
     * @param resultSet is the resultSet containing data from the database.
     * @return a list of objects of type T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * This method connects to the database and inserts a new record into the database.
     * @param t is the object to be inserted.
     * @return the inserted object or null if it failed.
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery();

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(t);
                statement.setObject(index++, value);
            }

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return t;
            }
        }catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * This method connects to the database and updates an existing record.
     * @param t is the object to update.
     * @return the updated object or null if it failed
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                if (field.getName().equals("id")) continue;
                field.setAccessible(true);
                Object value = field.get(t);
                statement.setObject(index++, value);
            }

            Field idField = type.getDeclaredFields()[0];
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            statement.setObject(index, idValue);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return t;
            }
        } catch (SQLException | IllegalAccessException e) {
                LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * This method connects to the database and deletes a record.
     * @param t is the object to delete.
     * @return the deleted object or null if it failed.
     */
    public T delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery("id");

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            Field idField = type.getDeclaredFields()[0];
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            statement.setObject(1, idValue);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return t;
            }
            else{
                return null;
            }
        }catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        }finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}

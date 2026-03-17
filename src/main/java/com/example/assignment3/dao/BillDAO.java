package com.example.assignment3.dao;

import com.example.assignment3.connection.ConnectionFactory;
import com.example.assignment3.model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class interacts with the database and provides CRUD operations for Bill objects.
 */
public class BillDAO {

    /**
     * This method connects to the database and inserts a new record into the database.
     * @param bill is the Bill object that will be inserted.
     * @return the inserted Bill object or null if it failed.
     */
    public Bill insertBill(Bill bill)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO log (clientName, amount) VALUES (?, ?)";

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1,bill.clientName());
            statement.setDouble(2,bill.amount());
            int rows = statement.executeUpdate();
            if(rows > 0)
            {
                return bill;
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * This method connects to the database and finds all the records from the log table.
     * @return a list of all the records found (can be empty).
     */
    public List<Bill> findAll()
    {
        List<Bill> bills = new ArrayList<Bill>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM log";

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                int billId = resultSet.getInt("billId");
                String clientName = resultSet.getString("clientName");
                double amount = resultSet.getDouble("amount");

                Bill bill = new Bill(billId,clientName,amount);
                bills.add(bill);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return bills;
    }
}

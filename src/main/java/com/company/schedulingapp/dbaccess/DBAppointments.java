package com.company.schedulingapp.dbaccess;

import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;


public class DBAppointments {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getCustomerAppointments(Integer customerID) throws SQLException {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments where Customer_ID = ?";
        PreparedStatement customerAppointmentsStatement = connection.prepareStatement(sql);
        customerAppointmentsStatement.setString(1, customerID.toString());
        ResultSet appointmentsSet = customerAppointmentsStatement.executeQuery();

        while (appointmentsSet.next()) {

            Appointment appointment = new Appointment(appointmentsSet.getInt("Appointment_ID"),
                                                       appointmentsSet.getString("Title"),
                                                        appointmentsSet.getString("Description"),
                                                        appointmentsSet.getString("Location"),
                                                        appointmentsSet.getString("Type"),
                                                        appointmentsSet.getTimestamp("Start"),
                                                        appointmentsSet.getTimestamp("End"),
                                                        appointmentsSet.getInt("Customer_ID"),
                                                        appointmentsSet.getInt("User_ID"),
                                                        appointmentsSet.getInt("Contact_ID"));

            customerAppointments.add(appointment);
        }

        return customerAppointments;
    }

    private static void getAllAppointmentsFromDatabase() throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "SELECT * FROM appointments";
        PreparedStatement allAppointmentsStatement = connection.prepareStatement(sql);
        ResultSet allAppointmentsSet = allAppointmentsStatement.executeQuery();

        while (allAppointmentsSet.next()) {
            Appointment appointment = new Appointment(allAppointmentsSet.getInt("Appointment_ID"),
                    allAppointmentsSet.getString("Title"),
                    allAppointmentsSet.getString("Description"),
                    allAppointmentsSet.getString("Location"),
                    allAppointmentsSet.getString("Type"),
                    allAppointmentsSet.getTimestamp("Start"),
                    allAppointmentsSet.getTimestamp("End"),
                    allAppointmentsSet.getInt("Customer_ID"),
                    allAppointmentsSet.getInt("User_ID"),
                    allAppointmentsSet.getInt("Contact_ID"));

            allAppointments.add(appointment);
        }
    }

    private static Integer createNewAppointmentID() throws SQLException {
        if (allAppointments.isEmpty()) {
            getAllAppointmentsFromDatabase();
        }

        Appointment currentLastAppointment = allAppointments.get(allAppointments.size() - 1);

        return currentLastAppointment.getAppointmentID() + 1;
    }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        if (allAppointments.isEmpty()) {
            getAllAppointmentsFromDatabase();
        } else {
            allAppointments.clear();
            getAllAppointmentsFromDatabase();
        }

        return allAppointments;
    }

    public static void addNewAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, Integer customerID, Integer userID, String contactName) throws SQLException {
        Connection connection = JDBC.getConnection();
        String sql = "INSERT INTO appointments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement newAppointmentStatement = connection.prepareStatement(sql);
        newAppointmentStatement.setInt(1, createNewAppointmentID()); // Appointment_ID
        newAppointmentStatement.setString(2, title);// Title (String)
        newAppointmentStatement.setString(3, description);// Description (String)
        newAppointmentStatement.setString(4, location);// Location (String)
        newAppointmentStatement.setString(5, type);// Type (String)
        newAppointmentStatement.setTimestamp(6, start);// Start (DateTime) 6
        newAppointmentStatement.setTimestamp(7, end);// End (DateTime) 7
        newAppointmentStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));// Create_Date (DateTime) 8
        newAppointmentStatement.setString(9, DBUsers.getCurrentUserName());// Created_By (String - UserName) 9
        newAppointmentStatement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));// Last_Update (Timestamp) 10
        newAppointmentStatement.setString(11, DBUsers.getCurrentUserName());// Last_Update_By (String - Username) 11
        newAppointmentStatement.setInt(12, customerID);// Customer ID (Integer) 12
        newAppointmentStatement.setInt(13, userID);// User ID (Integer) 13
        newAppointmentStatement.setInt(14, DBContacts.getContactIDFromContactName(contactName)); // Contact_ID (Integer) 14
        newAppointmentStatement.executeUpdate();

    }



}

package com.company.schedulingapp.controller;

import com.company.schedulingapp.dbaccess.DBContacts;
import com.company.schedulingapp.dbaccess.DBCustomers;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.model.Contact;
import com.company.schedulingapp.model.Customer;
import com.company.schedulingapp.model.User;
import com.company.schedulingapp.util.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {

    private SceneController sceneController = SceneController.getSceneControllerInstance();
    private Appointment appointmentToModify = MainController.getSelectedAppointment();
    private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    private ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
    private ObservableList<Integer> userIDs = FXCollections.observableArrayList();
    private ObservableList<String> contactNames = FXCollections.observableArrayList();

    @FXML TextField titleTextField;
    @FXML TextField descriptionTextField;
    @FXML TextField locationTextField;
    @FXML TextField typeTextField;
    @FXML DatePicker startDatePicker;
    @FXML ComboBox<String> startTimeComboBox;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox<String> endTimeComboBox;
    @FXML ComboBox<Integer> customerComboBox;
    @FXML ComboBox<Integer> userComboBox;
    @FXML ComboBox<String> contactComboBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAppointmentTimes();
        getCustomerIDs();
        getUserIDs();
        getContactNames();
        startTimeComboBox.setItems(appointmentTimes);
        endTimeComboBox.setItems(appointmentTimes);
        customerComboBox.setItems(customerIDs);
        userComboBox.setItems(userIDs);
        contactComboBox.setItems(contactNames);
    }

    private void setFields() {
        titleTextField.setText(appointmentToModify.getTitle());
        descriptionTextField.setText(appointmentToModify.getDescription());
        locationTextField.setText(appointmentToModify.getLocation());
        typeTextField.setText(appointmentToModify.getType());



    }

    private void createAppointmentTimes() {
        LocalTime businessHoursStart = LocalTime.of(8,00);
        LocalTime businessHoursEnd = LocalTime.of(22,00);
        Duration quarterHour = Duration.ofMinutes(15);

        LocalTime appointment = businessHoursStart;

        appointmentTimes.clear();
        while (appointment.isBefore(businessHoursEnd)) {
            appointmentTimes.add(appointment.toString());
            appointment = appointment.plus(quarterHour);
        }

        appointmentTimes.add(businessHoursEnd.toString());
    }

    private void getCustomerIDs() {
        try {
            for (Customer customer : DBCustomers.getAllCustomers()) {
                customerIDs.add(customer.getCustomerID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getUserIDs() {
        for (User user : DBUsers.getUsers()) {
            userIDs.add(user.getUserID());
        }

    }

    private void getContactNames() {
        for (Contact contact : DBContacts.getContacts()) {
            contactNames.add(contact.getContactName());
        }
    }


}

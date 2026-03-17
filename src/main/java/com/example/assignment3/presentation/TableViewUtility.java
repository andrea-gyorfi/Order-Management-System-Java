package com.example.assignment3.presentation;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * This class is a utility class for generating the header of a table using reflection and populating it with objects.
 */
public class TableViewUtility {

    /**
     * This method populates the table with the given list of elements.
     * It generates the columns based on the fields of the object's class and fills the rows with the values of those fields.
     * @param tableView is the TableView to populate.
     * @param listElem is the list of elements to display in the TableView.
     * @param <T> the type of the objects from the list
     */
    public static <T> void putInTable(TableView<T> tableView, List<T> listElem) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        if (listElem == null || listElem.isEmpty()) {
            return;
        }

        Class<?> table = listElem.get(0).getClass();
        Field[] fields = table.getDeclaredFields();
        Arrays.stream(fields)
                .map(field -> {
                    field.setAccessible(true);
                    TableColumn<T, String> column = new TableColumn<>(field.getName());
                    column.setCellValueFactory(cellData -> {
                        try {
                            Object value = field.get(cellData.getValue());
                            return new SimpleStringProperty(value != null ? value.toString() : "");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return new SimpleStringProperty("");
                        }
                    });
                    return column;
                })
                .forEach(tableView.getColumns()::add);
        tableView.getItems().addAll(listElem);
    }
}

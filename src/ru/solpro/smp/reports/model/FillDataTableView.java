package ru.solpro.smp.reports.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.solpro.smp.reports.Database;

import java.sql.ResultSet;

/**
 * Created by Администратор on 24.10.2016.
 */
public class FillDataTableView {
    private TableView tableView;
    private String SQL = "SELECT TOP 5 * FROM dbo.UA#Johnson_Fat";

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    public FillDataTableView() {
        tableView = new TableView();
    }

    public TableView getTableView() {
        return tableView;
    }

    public void buildData() {
        try {
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            Database database = new Database();
            database.dbConnect();
            ResultSet resultSet = database.execSQL(SQL);
            database.dbDisconnect();

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn tableColumn = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
                tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView.getColumns().addAll(tableColumn);
                System.out.println("Column ["+i+"]");
            }

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                System.out.println("Row [1] added "+row);
                data.add(row);
            }
            tableView.setItems(data);
            System.out.println("Данные добавлены в таблицу.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data.");
        }
    }
}
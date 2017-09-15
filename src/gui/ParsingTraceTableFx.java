package gui;

import java.text.ParseException;

import common.tag.Tag;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ParsingTraceTableFx {

  private String[][] rowData;
  private Tag tag;

  private TableView<ParsingStep> table = new TableView<ParsingStep>();

  private String[] columnNames =
    new String[] {"Id", "Item", "Rules", "Backpointers"};
  private final Timeline showTimer;
  private final Timeline disposeTimer;
  private TableRow popupRow;
  private TableColumn<ParsingStep, String> popupColumn;
  private DisplayTreeFx popup;

  public ParsingTraceTableFx(String[][] rowData, String[] columnNames,
    Tag tag) {
    this.rowData = rowData;
    this.columnNames = columnNames;
    this.tag = tag;
    if (tag == null) {
      showTimer = null;
      disposeTimer = null;
      displayTable();
    } else {
      showTimer = new Timeline(new KeyFrame(
        Duration.millis(500),
        ae -> showPopup()));
      showTimer.play();
      showTimer.setAutoReverse(false);
      disposeTimer = new Timeline(new KeyFrame(
        Duration.millis(2500),
        ae -> disposePopup()));
      showTimer.setAutoReverse(false);
      displayTableWithHover();
    }
  }

  private Object disposePopup() {
    DisplayTreeFx popup = getTreePopup();
   // popup.setVisible(false);
    return popup;
  }

  private Object showPopup() {
    if (popupRow != null) {
      disposeTimer.stop();
      DisplayTreeFx popup = getTreePopup();
      if (popup != null) {
        disposeTimer.playFromStart();
      }
    }
    return popup;
  }
  

  private DisplayTreeFx getTreePopup() {
    if (popup != null) {
      popup.dispose();
      popup = null;
    }
    String value = (String) popupColumn.getCellData(popupRow.getIndex());
    if (value.charAt(0) == '[') {
      String treeName = value.substring(1, value.indexOf(','));
      try {
        popup = new DisplayTreeFx(
          new String[] {tag.getTree(treeName).toString(), value});
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return popup;
  }

  private void displayTableWithHover() {
    Stage stage = new Stage();
    final VBox vbox = new VBox();
    Scene scene = new Scene(vbox);
    stage.setTitle("Parsing Trace Table");
    stage.setWidth(850);
    stage.setHeight(750);

    for (String columnName : columnNames) {
      TableColumn<ParsingStep, String> col =
        new TableColumn<ParsingStep, String>(columnName);
      col.setCellValueFactory(
        new PropertyValueFactory<ParsingStep, String>(columnName));
      col.setCellFactory(tc -> new HoverCell(this));
      col.setMinWidth(200);
      table.getColumns().add(col);
    }
    ObservableList<ParsingStep> data = FXCollections.observableArrayList();
    for (String[] date : rowData) {
      data.add(new ParsingStep(date));
    }
    table.setItems(data);
    table.setPrefHeight(stage.getHeight() - 40);
    table.setPrefWidth(stage.getWidth() - 40);

    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 0, 0, 10));
    vbox.getChildren().addAll(table);

    VBox.setVgrow(table, Priority.ALWAYS);

    stage.setScene(scene);
    stage.show();
  }

  private void displayTable() {
    Stage stage = new Stage();
    final VBox vbox = new VBox();
    Scene scene = new Scene(vbox);
    stage.setTitle("Parsing Trace Table");
    stage.setWidth(850);
    stage.setHeight(750);

    for (String columnName : columnNames) {
      TableColumn<ParsingStep, String> col =
        new TableColumn<ParsingStep, String>(columnName);
      col.setCellValueFactory(
        new PropertyValueFactory<ParsingStep, String>(columnName));
      col.setMinWidth(200);
      table.getColumns().add(col);
    }
    ObservableList<ParsingStep> data = FXCollections.observableArrayList();
    for (String[] date : rowData) {
      data.add(new ParsingStep(date));
    }
    table.setItems(data);
    table.setPrefHeight(stage.getHeight() - 40);
    table.setPrefWidth(stage.getWidth() - 40);

    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 0, 0, 10));
    vbox.getChildren().addAll(table);

    VBox.setVgrow(table, Priority.ALWAYS);

    stage.setScene(scene);
    stage.show();
  }

  public static class HoverCell extends TableCell<ParsingStep, String> {

    public HoverCell(ParsingTraceTableFx pttf) {
      setOnMouseMoved(e -> showPopup(pttf));
    }

    protected void showPopup(ParsingTraceTableFx pttf) {
      TableRow row = this.getTableRow();
      TableColumn<ParsingStep, String> col = this.getTableColumn();
      if ((row.getIndex() > -1) // && row < table.getRowCount()
        && (this.getIndex() > -1) // && col < table.getColumnCount()
        && (pttf.getPopupRow() == null || pttf.getPopupCol() != col
          || pttf.getPopupRow() != row)) {
        pttf.setPopupRow(row);
        pttf.setPopupCol(col);
        pttf.getRestartShowTimer();
      }
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : item);
    }
  }

  public TableRow getPopupRow() {
    return this.popupRow;
  }

  public TableColumn<ParsingStep, String> getPopupCol() {
    return this.popupColumn;
  }

  public void getRestartShowTimer() {
    this.showTimer.stop();
    this.showTimer.playFromStart();
  }

  public void setPopupCol(TableColumn<ParsingStep, String> col) {
    this.popupColumn = col;
  }

  public void setPopupRow(TableRow row) {
    this.popupRow = row;
  }

}

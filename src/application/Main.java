// Title: Main.java
// Files: BTree.java, BTreeTest.java, Main.java, PaleontologyCollection.java,
// Specimen.java, UniqueIdentifier.java, DuplicateKeyException.java
// Course: Programming III, Fall 2019
//
// Author: George Li
// Email: gli245@wisc.edu
// Lecturer's Name: Andrew Kuemmel
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: None
// Online Sources:
// https://stackoverflow.com/questions/29710492/how-can-i-fire-internal-close-request

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
  private File inputFile;
  private PaleontologyCollection plc;

  @Override
  public void start(Stage primaryStage) {
    try {

      BorderPane bPane = new BorderPane();

      plc = new PaleontologyCollection();


      /*************************** Data Display ***************************/
      VBox dataDisplay = new VBox(8);
      dataDisplay.setAlignment(Pos.TOP_CENTER);

      // tables and columns
      TableView<Specimen> tableView = new TableView<Specimen>();
      TableColumn<Specimen, Integer> spcKeyColumn = new TableColumn<>("Specimen Key");
      spcKeyColumn.setCellValueFactory(new PropertyValueFactory<>("Key"));

      TableColumn<Specimen, String> speciesColumn = new TableColumn<>("Species");
      speciesColumn.setCellValueFactory(new PropertyValueFactory<>("speciesName"));

      TableColumn<Specimen, Integer> occKeyColumn = new TableColumn<>("Occurrence Key");
      occKeyColumn.setCellValueFactory(new PropertyValueFactory<>("occurrenceKey"));

      TableColumn<Specimen, String> spcPartColumn = new TableColumn<>("Specimen Part");
      spcPartColumn.setCellValueFactory(new PropertyValueFactory<>("specimenPart"));

      TableColumn<Specimen, Double> maxMaColumn = new TableColumn<>("Maximum Age (Ma)");
      maxMaColumn.setCellValueFactory(new PropertyValueFactory<>("maxMa"));

      TableColumn<Specimen, Double> minMaColumn = new TableColumn<>("Minimum Age (Ma)");
      minMaColumn.setCellValueFactory(new PropertyValueFactory<>("minMa"));

      tableView.getColumns().add(spcKeyColumn);
      tableView.getColumns().add(speciesColumn);
      tableView.getColumns().add(occKeyColumn);
      tableView.getColumns().add(spcPartColumn);
      tableView.getColumns().add(maxMaColumn);
      tableView.getColumns().add(minMaColumn);

      tableView.getItems().addAll(plc.toList());

      tableView.setEditable(false);
      tableView.setPlaceholder(new Label("No rows to display, please import data and refresh"));

      // TextField for estimate data
      TextField estimateField = new TextField("No estimates to display");
      estimateField.setEditable(false);

      final ImageView selectedImage = new ImageView();
      Image image1 = new Image(new FileInputStream(
          "src\\application\\images\\philosoraptor.jpg"));

      selectedImage.setImage(image1);
      selectedImage.setFitHeight(300.0);
      selectedImage.setPreserveRatio(true);

      dataDisplay.getChildren().addAll(tableView, estimateField, selectedImage);

      /*************************** Toolbar ***************************/
      // Top Toolbar
      Button importButton = new Button("New File");
      Button exportButton = new Button("Save");

      ToolBar toolbar = new ToolBar(importButton, exportButton);

      FileChooser fileChooser = new FileChooser();

      importButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            // Set extension filter
            FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            inputFile = fileChooser.showOpenDialog(primaryStage);
            plc = new PaleontologyCollection(inputFile);

            tableView.getItems().clear();
            tableView.getItems().addAll(plc.toList());
            tableView.refresh();
          } catch (NullPointerException e1) {
            System.out.println("Please select a file");
          } catch (Exception e1) {
            System.out.println("Import failed");
            tableView.setPlaceholder(new Label("Import failed. Please validate input data."));
          }
        }
      });

      exportButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            // Set extension filter
            FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show save file dialog
            File outputFile = fileChooser.showSaveDialog(primaryStage);

            if (outputFile != null) {
              saveFile(outputFile);
            }
          } catch (Exception e1) {
            System.out.println("Export failed.");
          }
        }

        /**
         * Helper method to write paleontology collection to file
         * 
         * @param outputFile selected output file
         */
        private void saveFile(File outputFile) {
          try {

            FileWriter fw = new FileWriter(outputFile);
            fw.write("specimenKey,occurrenceKey,specimenPart,speciesName,maxMa,minMa\n");

            for (Specimen spc : plc.toList()) {
              fw.write(
                  spc.getKey() + "," + spc.getOccurrenceKey() + "," + spc.getSpecimenPart() + ","
                      + spc.getSpeciesName() + "," + spc.getMaxMa() + "," + spc.getMinMa() + "\n");
            }
            fw.close();
          } catch (IOException ex) {
            System.out.println("IOException on export. Please validate data format before exporting.");
          }
        }
      });

      /*************************** User Controls ***************************/

      VBox userControls = new VBox(8);

      /*************************** Insertion ***************************/
      // insert fields
      TextField idField = new TextField();
      idField.setPromptText("Specimen Key");

      TextField speciesField = new TextField();
      speciesField.setPromptText("Species");

      TextField occurrenceField = new TextField();
      occurrenceField.setPromptText("Occurrence Key");

      TextField partField = new TextField();
      partField.setPromptText("Specimen Part");

      TextField maxMaField = new TextField();
      maxMaField.setPromptText("Maximum Ma");

      TextField minMaField = new TextField();
      minMaField.setPromptText("Minimum Ma");

      Button insertButton = new Button("Insert");
      Label insertLabel = new Label("No specimens added yet");

      insertButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            // create specimen
            Specimen insertSpc = new Specimen();
            insertSpc.setSpecimenKey(Integer.parseInt(idField.getText()));
            insertSpc.setSpeciesName(speciesField.getText());
            insertSpc.setOccurrenceKey(Integer.parseInt(occurrenceField.getText()));
            insertSpc.setSpecimenPart(partField.getText());
            insertSpc.setMaxMa(Integer.parseInt(maxMaField.getText()));
            insertSpc.setMinMa(Integer.parseInt(minMaField.getText()));
            plc.insert(insertSpc);
            insertLabel.setText(insertSpc.getKey() + " successfully added!");

          } catch (DuplicateKeyException e1) {
            System.out.println("Duplicate keys not allowed.");
            insertLabel.setText("No duplicate keys allowed.");
          } catch (NumberFormatException e2) {
            System.out.println("Use numeric inputs when appropriate");
            insertLabel.setText("Use numeric inputs for IDs and age.");
          } catch (IllegalArgumentException e3) {
            System.out.println("Illegal arguments: don't use null or empty Strings for IDs");
          }
        }
      });

      /*************************** Removal ***************************/
      // remove field and button
      TextField removeId = new TextField();
      removeId.setPromptText("Remove Specimen Key");

      Button removeButton = new Button("Remove");
      Label removeLabel = new Label("No specimens removed yet");

      removeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            int removeKey = Integer.parseInt(removeId.getText());
            Specimen removeSpc = plc.find(removeKey);
            plc.remove(removeKey);
            removeLabel.setText("Specimen " + removeKey + " removed!");
            tableView.getItems().remove(removeSpc);
            tableView.refresh();
          } catch (NumberFormatException e2) {
            System.out.println("Use numeric inputs when appropriate");
            removeLabel.setText("Use numeric inputs for IDs and age.");
          }
        }
      });

      /*************************** Search ***************************/
      // search field and button
      TextField searchId = new TextField();
      searchId.setPromptText("Search Specimen Key");
      Label searchLabel = new Label("No specimens searched yet");

      Button searchButton = new Button("Search");

      searchButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            int searchKey = Integer.parseInt(searchId.getText());
            Specimen searchVal = plc.find(searchKey);
            tableView.getItems().clear();
            if (searchVal == null) {
              tableView
                  .setPlaceholder(new Label("No rows found. Refresh to return to your data set."));
              searchLabel.setText("Search returned no results.");
            } else {
              tableView.getItems().add(searchVal);
              searchLabel.setText("Search successful!");
            }
          } catch (NumberFormatException e1) {
            System.out.println("Use numeric inputs when appropriate");
            searchLabel.setText("Use numeric inputs for IDs.");
          }
        }
      });

      /*************************** Filters ***************************/
      // occurrence filter field and button
      TextField occurrenceFilterField = new TextField();
      occurrenceFilterField.setPromptText("Filter Occurrence Key");

      Button occurrenceFilterButton = new Button("Filter Occurrence");

      occurrenceFilterButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            int occurrenceKey = Integer.parseInt(occurrenceFilterField.getText());
            List<Specimen> filterOccurrenceKeyList = plc.toOccurrenceFilterList(occurrenceKey);
            tableView.getItems().clear();
            if (filterOccurrenceKeyList.size() == 0) {
              tableView
                  .setPlaceholder(new Label("No rows found. Refresh to return to your data set."));
            } else {
              tableView.getItems().addAll(filterOccurrenceKeyList);
            }
          } catch (NumberFormatException e1) {
            System.out.println("Use numeric inputs for IDs");
          }
        }
      });

      // species and age filter
      TextField speciesFilterField = new TextField();
      speciesFilterField.setPromptText("Filter Species");

      TextField maxMaFilterField = new TextField();
      maxMaFilterField.setPromptText("Filter Maximum Ma");

      TextField minMaFilterField = new TextField();
      minMaFilterField.setPromptText("Filter Minimum Ma");

      Button speciesMaFilterButton = new Button("Filter by Species and Age");

      speciesMaFilterButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          try {
            String speciesName = speciesFilterField.getText();
            double maxMaFilter = Double.parseDouble(maxMaFilterField.getText());
            double minMaFilter = Double.parseDouble(minMaFilterField.getText());
            List<Specimen> filterSpeciesMaList =
                plc.toSpeciesMaFilterList(speciesName, maxMaFilter, minMaFilter);
            tableView.getItems().clear();
            if (filterSpeciesMaList.size() == 0) {
              tableView
                  .setPlaceholder(new Label("No rows found. Refresh to return to your data set."));
            } else {
              tableView.getItems().addAll(filterSpeciesMaList);
            }
          } catch (NumberFormatException e1) {
            System.out.println("Use numeric inputs when appropriate");
          }
        }
      });

      /*************************** Calculation ***************************/
      // Species age estimator
      TextField speciesAgeEstimateField = new TextField();
      speciesAgeEstimateField.setPromptText("Species Age Estimate");

      Button speciesAgeEstimateButton = new Button("Estimate Species Age");

      speciesAgeEstimateButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          String speciesName = speciesAgeEstimateField.getText();
          NumberFormat formatter = new DecimalFormat("#0.00");
          double estimateMaxMa = plc.averageSpeciesMaxMa(speciesName);
          double estimateMinMa = plc.averageSpeciesMinMa(speciesName);

          if (estimateMaxMa < 1 || estimateMinMa < 1) {
            estimateField.setText(speciesName
                + " is not valid, please check your spelling or enter another species.");
          } else {

            estimateField.setText(speciesName + " evolved " + formatter.format(estimateMaxMa)
                + " million years ago and went extinct " + formatter.format(estimateMinMa)
                + " million years ago.");
          }
        }
      });


      /*************************** Refresh ***************************/
      Button refreshButton = new Button("Refresh");

      refreshButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          tableView.getItems().clear();
          tableView.getItems().addAll(plc.toList());
          tableView.refresh();
        }
      });

      userControls.getChildren().addAll(
          // insert fields
          idField, speciesField, occurrenceField, partField, maxMaField, minMaField, insertButton,
          insertLabel,

          // remove fields
          removeId, removeButton, removeLabel,

          // search and filter fields
          searchId, searchButton, searchLabel, occurrenceFilterField, occurrenceFilterButton,
          speciesFilterField, maxMaFilterField, minMaFilterField, speciesMaFilterButton,

          // calculation fields
          speciesAgeEstimateField, speciesAgeEstimateButton,

          // refresh
          refreshButton);

      bPane.setLeft(userControls);
      bPane.setTop(toolbar);
      bPane.setCenter(dataDisplay);
      Scene scene = new Scene(bPane, 1280, 800);

      // EventHandler for closing program
      EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        // alert pop-up
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to exit? If yes, remember to save your work.");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        // modality to block actions on other areas of the program
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(primaryStage);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
          // if no confirmation, remove pop-up and continue
          event.consume();
        }
      };

      primaryStage.setOnCloseRequest(confirmCloseEventHandler);

      // set title
      primaryStage.setTitle("Philosoraptor's Paleontology Collection");

      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

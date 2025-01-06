package org.kriss.dicombuddy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class DicomTagHighlighter extends Application {

    @Override
    public void start(Stage stage) {
        // Create the TreeTableView
        TreeTableView<DicomTag> treeTableView = new TreeTableView<>();

        // Define columns
        TreeTableColumn<DicomTag, String> tagColumn = new TreeTableColumn<>("Tag");
        TreeTableColumn<DicomTag, String> descriptionColumn = new TreeTableColumn<>("Description");
        TreeTableColumn<DicomTag, String> typeColumn = new TreeTableColumn<>("Type");
        TreeTableColumn<DicomTag, String> valueColumn = new TreeTableColumn<>("Value");

        // Set cell value factories
        tagColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("tag"));
        descriptionColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));

        // Set columns to TreeTableView
        treeTableView.getColumns().addAll(tagColumn, descriptionColumn, typeColumn, valueColumn);

        // Add sample data
        TreeItem<DicomTag> root = new TreeItem<>(new DicomTag("", "", "", ""));
        root.getChildren().addAll(
                new TreeItem<>(new DicomTag("(300A,0121)", "Patient Support Angle", "DS", "5")),
                new TreeItem<>(new DicomTag("(300A,0122)", "Table Top Vertical Position", "DS", "0")),
                new TreeItem<>(new DicomTag("(300A,0123)", "Table Top Angle Position", "DS", "0"))
        );
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter text to search");

        // Highlight search text in the "Description" column
        descriptionColumn.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Keep all rows visible but highlight matching text
                    String searchText = searchField.getText().toLowerCase();
                    if (!searchText.isEmpty() && item.toLowerCase().contains(searchText)) {
                        // Highlight matching text
                        TextFlow textFlow = createHighlightedText(item, searchText);
                        setText(null);

                        // Create a Pane and set the border
                        Pane pane = new Pane();
                        pane.getChildren().add(textFlow);
                        setText(null);
                        setGraphic(pane);
                    } else {
                        // Display item as normal if it doesn't match
                        setText(item);
                        setGraphic(null);
                    }
                }
            }
        });

        // Button to trigger search
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            treeTableView.refresh(); // Ensure the table updates
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            treeTableView.refresh();
        });

        // Layout
        VBox layout = new VBox(10, searchField, searchButton, treeTableView);
        Scene scene = new Scene(layout, 600, 400);

        stage.setScene(scene);
        stage.setTitle("DICOM Tag Highlighter");
        stage.show();
    }


    private TextFlow createHighlightedText(String fullText, String searchText) {
        TextFlow textFlow = new TextFlow();
        int startIndex = fullText.toLowerCase().indexOf(searchText);
        if (startIndex < 0) {
            textFlow.getChildren().add(new Text(fullText));
            return textFlow;
        }

        int endIndex = startIndex + searchText.length();

        Text before = new Text(fullText.substring(0, startIndex));
        Text match = new Text(fullText.substring(startIndex, endIndex));
        Text after = new Text(fullText.substring(endIndex));
        System.out.println("before: " + fullText.substring(0, startIndex));
        System.out.println("match: " + fullText.substring(startIndex, endIndex));
        System.out.println("after: " + fullText.substring(endIndex));

        match.setFill(Color.BLUE);
        match.setStyle("-fx-font-weight: bold;");

        textFlow.getChildren().addAll(before, match, after);
        return textFlow;
    }

    public static class DicomTag {
        private final String tag;
        private final String description;
        private final String type;
        private final String value;

        public DicomTag(String tag, String description, String type, String value) {
            this.tag = tag;
            this.description = description;
            this.type = type;
            this.value = value;
        }

        public String getTag() {
            return tag;
        }

        public String getDescription() {
            return description;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package org.kriss.dicombuddy;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SearchableTreeTableCellUtil {
    public static <S, T> void updateItem(TreeTableCell<S, T> treeTableCell, String item, boolean empty, TextField searchField, BooleanProperty isSearchCondition) {
        if (empty || item == null) {
            treeTableCell.setText(null);
            treeTableCell.setGraphic(null);
        } else {
            // Keep all rows visible but highlight matching text
            String searchText = searchField.getText().toLowerCase();
            if (!searchText.isEmpty() && isSearchCondition.get() && item.toLowerCase().contains(searchText)) {
                Pane pane = createHighlightedText(item, searchText);

                treeTableCell.setText(null);
                treeTableCell.setGraphic(pane);
            } else {
                // Display item as normal if it doesn't match
                treeTableCell.setText(item);
                treeTableCell.setGraphic(null);
            }
        }
    }

    private static Pane createHighlightedText(String fullText, String searchText) {
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

        match.setFill(Color.BLUE);
        match.setStyle("-fx-font-weight: bold;");

        textFlow.getChildren().addAll(before, match, after);

        Pane pane = new Pane();
        pane.getChildren().add(textFlow);

        return pane;
    }

    private static void expandTreeItem(TreeItem<?> item) {
        DicomAttribute value = (DicomAttribute) item.getValue();
        if (item != null) {
            item.setExpanded(true);
            TreeItem<?> parent = item.getParent();
            while (parent != null) {
                parent.setExpanded(true);
                parent = parent.getParent();
            }
        }
    }
}

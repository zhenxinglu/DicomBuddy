package org.kriss.dicombuddy;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class SearchableTextFieldTableCellFactory implements Callback<TreeTableColumn<DicomAttribute,String>, TreeTableCell<DicomAttribute,String>> {
    private final TextField searchField;
    private final BooleanProperty isSearchCondition;

    public SearchableTextFieldTableCellFactory(TextField searchField, BooleanProperty isSearchCondition) {
        this.searchField = searchField;
        this.isSearchCondition = isSearchCondition;
    }
    @Override
    public TreeTableCell<DicomAttribute, String> call(TreeTableColumn<DicomAttribute, String> param) {
        return new TextFieldTreeTableCell<>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                SearchableTreeTableCellUtil.updateItem(this, item, empty, searchField, isSearchCondition);
            }
        };
    }
}

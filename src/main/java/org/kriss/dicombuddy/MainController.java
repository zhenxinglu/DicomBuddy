package org.kriss.dicombuddy;

import javafx.animation.PauseTransition;
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dcm4che3.data.*;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static javafx.beans.binding.Bindings.createBooleanBinding;
import static javafx.beans.binding.Bindings.createStringBinding;

public class MainController {
    @FXML
    public TreeTableView<DicomAttribute> treeTableView;
    @FXML
    public TreeTableColumn<DicomAttribute, String> tagColumn;
    @FXML
    public TreeTableColumn<DicomAttribute, String> vrColumn;
    @FXML
    public TreeTableColumn<DicomAttribute, String> valueColumn;
    @FXML
    public TreeTableColumn<DicomAttribute, String> nameColumn;
    @FXML
    public TextField searchField;
    @FXML
    public Button previousMatchButton;
    @FXML
    private Button nextMatchButton;
    @FXML
    private Label searchResult;
    @FXML
    private CheckBox searchByValueCheckBox;
    @FXML
    private CheckBox searchByNameCheckBox;
    @FXML
    private CheckBox searchByTagCheckBox;

    private ObjectBinding<SearchCriteria> searchCriteriaProp;
    private ObservableList<TreeItem<DicomAttribute>> matchedItemsProp = FXCollections.observableArrayList();
    private IntegerProperty currentIndexProp = new SimpleIntegerProperty(0);

    /**
     * Called by JavaFX automatically when the FXML file is loaded.
     */
    public void initialize() {
        initTreeTable();

        PauseTransition pause = new PauseTransition(Duration.millis(300));
        searchField.textProperty().addListener((_, _, newValue) -> {
            pause.setOnFinished(_ -> {
                matchedItemsProp.clear();
                expandMatchingItems(treeTableView.getRoot(), newValue.toLowerCase());
                treeTableView.refresh();
            });
            pause.playFromStart();
        });

        searchCriteriaProp = Bindings.createObjectBinding(() -> new SearchCriteria(searchByTagCheckBox.isSelected(),
                        searchByNameCheckBox.isSelected(), searchByValueCheckBox.isSelected()),
                searchByTagCheckBox.selectedProperty(), searchByNameCheckBox.selectedProperty(), searchByValueCheckBox.selectedProperty());
        searchCriteriaProp.addListener((_, _, newValue) -> {
            matchedItemsProp.clear();
            expandMatchingItems(treeTableView.getRoot(), searchField.getText());
            treeTableView.refresh();
        });

        searchResult.managedProperty().bind(createBooleanBinding(() -> !matchedItemsProp.isEmpty(), matchedItemsProp));
        searchResult.visibleProperty().bind(createBooleanBinding(() -> !matchedItemsProp.isEmpty(), matchedItemsProp));
        searchResult.textProperty().bind(createStringBinding(() -> String.format("%d / %d item(s)", currentIndexProp.get(), matchedItemsProp.size()),
                currentIndexProp, matchedItemsProp));

        previousMatchButton.disableProperty().bind(createBooleanBinding(() -> matchedItemsProp.isEmpty(), matchedItemsProp));
        nextMatchButton.disableProperty().bind(createBooleanBinding(() -> matchedItemsProp.isEmpty(), matchedItemsProp));

        treeTableView.getParent().addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);

        // Drag and drop support
        treeTableView.setOnDragOver(this::handleDragOver);
        treeTableView.setOnDragDropped(this::handleDragDropped);


//        openDicomFile();
    }

    private void handleDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            List<File> files = db.getFiles();
            //currently only support open one file at a time
            var file = files.get(0);
            if (file.getName().toLowerCase().endsWith(".dcm")) {
                clearTreeTableView();

                try {
                    Attributes attrs = readDicomFile(file);
                    addAttributes(treeTableView.getRoot(), attrs);
                    setWindowTitle(file.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void clearTreeTableView() {
        TreeItem<DicomAttribute> root = treeTableView.getRoot();
        if (root != null) {
            root.getChildren().clear();
        }
    }

    @FXML
    private void onOpenDicomFileButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open DICOM File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DICOM Files", "*.dcm"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Attributes attrs = readDicomFile(file);
                addAttributes(treeTableView.getRoot(), attrs);
                setWindowTitle(file.getAbsolutePath()); // Added: Set window title with file path
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void onSaveButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DICOM File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DICOM Files", "*.dcm"));
        File file = fileChooser.showSaveDialog(((Button) actionEvent.getSource()).getScene().getWindow());

        if (file != null) {
            try {
                Attributes attrs = getCurrentAttributes();
                writeDicomFile(file, attrs);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * For quick test only
     */
    private void openDicomFile() {
        String dicomFile = "d:\\tmp\\RP1.2.752.243.1.1.20220721191223551.3500.35069.dcm";
        Attributes attrs = null;
        try {
            attrs = readDicomFile(new File(dicomFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addAttributes(treeTableView.getRoot(), attrs);
    }

    private Attributes readDicomFile(File file) throws IOException {
        try (DicomInputStream dis = new DicomInputStream(file)) {
            return dis.readDataset();
        }
    }

    private void initTreeTable() {
        tagColumn.setCellValueFactory(param -> param.getValue().getValue().tagProperty());
        tagColumn.setCellFactory(new SearchableTableCellFactory(searchField, searchByTagCheckBox.selectedProperty()));

        vrColumn.setCellValueFactory(param -> param.getValue().getValue().vrProperty());

        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        nameColumn.setCellFactory(new SearchableTableCellFactory(searchField, searchByNameCheckBox.selectedProperty()));

        valueColumn.setCellValueFactory(param -> param.getValue().getValue().valueProperty());
        valueColumn.setEditable(true);
        valueColumn.setCellFactory(new SearchableTextFieldTableCellFactory(searchField, searchByValueCheckBox.selectedProperty()));

        valueColumn.setOnEditCommit(event -> {
            TreeItem<DicomAttribute> item = event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
            DicomAttribute attribute = item.getValue();
            attribute.setValue(event.getNewValue());
            updateDicomValue(attribute);
        });

        TreeItem<DicomAttribute> root = new TreeItem<>(new DicomAttribute(0, "root", VR.UN, ""));
        treeTableView.setEditable(true);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
    }


    private void updateDicomValue(DicomAttribute attribute) {
        //System.out.println("Updated attribute: " + attribute.getTag() + " to value: " + attribute.getValue());
    }

    private void addAttributes(TreeItem<DicomAttribute> parent, Attributes attrs) {
        for (int tag : attrs.tags()) {
            VR vr = attrs.getVR(tag);
            String name = Keyword.valueOf(tag);
            String value = attrs.getString(tag);

            DicomAttribute attribute = new DicomAttribute(tag, name, vr, value);
            TreeItem<DicomAttribute> item = new TreeItem<>(attribute);
            parent.getChildren().add(item);

            if (vr == VR.SQ) {
                Sequence sequence = attrs.getSequence(tag);
                if (sequence != null) {
                    for (int i = 0; i < sequence.size(); i++) {
                        Attributes sequenceItem = sequence.get(i);
                        TreeItem<DicomAttribute> seqItem = new TreeItem<>(new DicomAttribute(Tag.Item, "Item " + (i + 1), VR.UN, ""));
                        item.getChildren().add(seqItem);
                        addAttributes(seqItem, sequenceItem);
                    }
                }
            }
        }
    }

    private void expandMatchingItems(TreeItem<DicomAttribute> parent, String searchText) {
        if (parent == null || searchText.isEmpty()) return;

        // Clear the matched items list before starting the search
        if (parent.getParent() == null) {
            matchedItemsProp.clear();
        }

        // Check if the current item contains the search text
        DicomAttribute attribute = parent.getValue();
        if (attribute != null && attribute.findText(searchCriteriaProp.get(), searchText)) {
            // Ensure this item is visible
            parent.setExpanded(true);
            // Add the matched item to the list
            //matchedItemsProp.set(matchedItemsProp.get().add(parent));
            matchedItemsProp.add(parent);
        }

        // Recursively check children
        for (TreeItem<DicomAttribute> child : parent.getChildren()) {
            expandMatchingItems(child, searchText);
        }
    }

    @FXML
    private void onContactAuthorClick(ActionEvent actionEvent) {
        HostServices hostServices = DicomBuddy.getInstance().getHostServices();
        hostServices.showDocument("mailto:zhenxinglu@gmail.com");
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.F3) {
            onNextMatchButtonClick(null);
        } else if (event.getCode() == KeyCode.F3 && event.isShiftDown()) {
            onPreviousMatchButtonClick(null);
        }
    }

    @FXML
    private void onNextMatchButtonClick(ActionEvent actionEvent) {
        int index = currentIndexProp.get() + 1;
        currentIndexProp.set(index > matchedItemsProp.size() ? 1 : index);

        highlightItem(index);
    }

    @FXML
    public void onPreviousMatchButtonClick(ActionEvent actionEvent) {
        int index = currentIndexProp.get() - 1;
        currentIndexProp.set(index < 1 ? matchedItemsProp.size() : index);

        highlightItem(index);
    }

    private void highlightItem(int index) {
        if (index >= 1 && index <= matchedItemsProp.size()) {
            TreeItem<DicomAttribute> itemToHighlight = matchedItemsProp.get(index - 1);
            expandItslefAndAllParents(itemToHighlight);
            treeTableView.getSelectionModel().select(itemToHighlight);

            treeTableView.scrollTo(treeTableView.getRow(itemToHighlight));
        }
    }

    private void expandItslefAndAllParents(TreeItem<?> item) {
        while (item != null) {
            item.setExpanded(true);
            item = item.getParent();
        }
    }

    private Attributes getCurrentAttributes() {
        Attributes attrs = new Attributes();
        addAttributesToAttributes(attrs, treeTableView.getRoot());
        return attrs;
    }

    private void addAttributesToAttributes(Attributes attrs, TreeItem<DicomAttribute> parent) {
        for (TreeItem<DicomAttribute> child : parent.getChildren()) {
            DicomAttribute attribute = child.getValue();
            int tag = attribute.getTag();
            VR vr = attribute.getVr();
            Object value = attribute.getValue();

            if (vr == VR.SQ) {
                Sequence sequence = attrs.newSequence(tag, 10);
                addAttributesToSequence(sequence, child);
            } else {
                try {
                    switch (vr) {
                        case UN:
                        case OB:
                        case OW:
                            // Handle binary data appropriately, e.g., convert to byte array
                            if (value != null) {
                                byte[] bytes = value instanceof byte[] ? (byte[]) value : value.toString().getBytes(StandardCharsets.UTF_8);
                                attrs.setBytes(tag, vr, bytes);
                            } else {
                                attrs.setNull(tag, vr);
                            }
                            break;
                        default:
                            attrs.setString(tag, vr, value == null ? "" : value.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addAttributesToSequence(Sequence sequence, TreeItem<DicomAttribute> parent) {
        for (TreeItem<DicomAttribute> child : parent.getChildren()) {
            DicomAttribute attribute = child.getValue();
            Attributes itemAttrs = new Attributes();
            if (attribute.getVr() == VR.SQ) {
                Sequence subSequence = itemAttrs.newSequence(attribute.getTag(), 10);
                addAttributesToSequence(subSequence, child);
                sequence.add(itemAttrs);
            } else {
                itemAttrs.setString(attribute.getTag(), attribute.getVr(), attribute.getValue());
                sequence.add(itemAttrs);
            }
        }
    }

    private void writeDicomFile(File file, Attributes attrs) throws IOException {
        try (DicomOutputStream dos = new DicomOutputStream(file)) {
            dos.writeDataset(null, attrs);
        }
    }

    private void setWindowTitle(String filePath) {
        Stage stage = (Stage) treeTableView.getScene().getWindow();
        stage.setTitle("DicomBuddy : " + filePath);
    }
}

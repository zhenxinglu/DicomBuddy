package org.kriss.dicombuddy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dcm4che3.data.VR;
import org.dcm4che3.util.TagUtils;

public class DicomAttribute {
    private final int tag;
    private final StringProperty tagProp;

    private final StringProperty vrProp;
    private final StringProperty name;
    private final VR vr;
    private StringProperty value;

    public DicomAttribute(int tag, String name, VR vr, String value) {
        this.tag = tag;
        this.tagProp = new SimpleStringProperty(TagUtils.toString(tag));

        this.vr = vr;
        this.vrProp = new SimpleStringProperty(vr.name());

        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public StringProperty tagProperty() {
        return tagProp;
    }

    public StringProperty vrProperty() {
        return vrProp;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public int getTag() {
        return tag;
    }

    public VR getVr() {
        return vr;
    }

    public boolean findText(SearchCriteria criteria, String textToSearch) {
        if (criteria.isSearchTag() && tagProp.get().toLowerCase().contains(textToSearch)) {
            return true;
        }

        if (criteria.isSearchName() && nameProperty().get()!=null && name.get().toLowerCase().contains(textToSearch)) {
            return true;
        }

        return criteria.isSearchValue() && valueProperty().get() != null && value.get().toLowerCase().contains(textToSearch);
    }
}
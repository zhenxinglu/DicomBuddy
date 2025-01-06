package org.kriss.dicombuddy;

public class SearchCriteria {
    private boolean searchTag;
    private boolean searchName;
    private boolean searchValue;

    public SearchCriteria(boolean searchTag, boolean searchDescription, boolean searchValue) {
        this.searchTag = searchTag;
        this.searchName = searchDescription;
        this.searchValue = searchValue;
    }

    public boolean isSearchTag() {
        return searchTag;
    }

    public boolean isSearchName() {
        return searchName;
    }

    public boolean isSearchValue() {
        return searchValue;
    }
}
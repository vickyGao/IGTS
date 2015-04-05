package com.ntu.igts.enums;

public enum SortByEnum {

    USER_NAME("userName"), ADMIN_NAME("adminName"), CREATED_TIME("createdTime"), LAST_UPDATED_TIME("lastUpdatedTime"), COMMODITY_TITILE(
                    "title"), PRICE("price"), COLLECTION_NUMBER("collectionNumber"), DISPLAY_SEQUENCE("displaySequence"), WORD(
                    "word"), RELEASE_DATE("releaseDate");

    private String value;

    private SortByEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static SortByEnum fromValue(String value) {
        for (SortByEnum sortByEnum : SortByEnum.values()) {
            if (sortByEnum.value.equals(value)) {
                return sortByEnum;
            }
        }
        throw new IllegalArgumentException();
    }

}

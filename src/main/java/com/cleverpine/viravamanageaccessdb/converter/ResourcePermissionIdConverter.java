package com.cleverpine.viravamanageaccessdb.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.cleverpine.viravamanageaccessdb.util.ViravaConstants.DELIMETER;

@Converter
public class ResourcePermissionIdConverter implements AttributeConverter<String[], String> {
    @Override
    public String convertToDatabaseColumn(String[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        return String.join(DELIMETER, ids);
    }

    @Override
    public String[] convertToEntityAttribute(String idString) {
        if (idString == null || idString.isBlank()) {
            return null;
        }

        return idString.split(DELIMETER);
    }
}

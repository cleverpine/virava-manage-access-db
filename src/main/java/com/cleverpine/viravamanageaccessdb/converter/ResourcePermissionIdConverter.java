package com.cleverpine.viravamanageaccessdb.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Converter
public class ResourcePermissionIdConverter implements AttributeConverter<String[], String> {
    @Override
    public String convertToDatabaseColumn(String[] ids) {
        if (ids == null) {
            return null;
        }

        return String.join(",", ids);
    }

    @Override
    public String[] convertToEntityAttribute(String idString) {
        if (idString == null) {
            return null;
        }

        return idString.split(",");
    }
}

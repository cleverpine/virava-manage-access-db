package com.cleverpine.viravamanageaccessdb.util;

public final class ViravaConstants {
    // Strings
    public static final String ALL = "ALL";
    public static final String DELIMETER = ", ";
    //Error Messages
    public static final String ID_DOES_NOT_MATCH_ERROR = "Id in path and body do not match";
    public static final String USER_NOT_FOUND_ERROR = "User with id [%d] not found";
    public static final String USER_NOT_FOUND_BY_USERNAME_ERROR = "User with username [%s] not found";
    public static final String ID_CANNOT_BE_NEGATIVE_ERROR = "Id cannot be negative";
    public static final String USERNAME_CANNOT_BE_NULL_ERROR = "Username cannot be null or empty";
    public static final String USER_CANNOT_BE_NULL = "User cannot be null";
    public static final String PERMISSION_IS_NULL_ERROR = "Permission cannot be null";
    public static final String PERMISSION_NOT_FOUND_ERROR = "Permission with id %d not found";
    public static final String PERMISSION_NOT_FOUND_BY_NAME_ERROR = "Permission with name %s not found";
    public static final String PERMISSION_NAME_CANNOT_BE_NULL_ERROR = "Permission name cannot be null or empty";
    public static final String USER_ALREADY_EXISTS_ERROR = "User with username [%s] already exists";
    public static final String RESOURCE_NOT_FOUND_ERROR = "Resource with id [%d] not found";
    public static final String RESOURCE_CANNOT_BE_NULL = "Resource cannot be null";
    public static final String RESOURCE_NAME_CANNOT_BE_NULL = "Resource name cannot be null or empty";
    public static final String USER_PERMISSION_RESOURCE_NOT_FOUND_ERROR = "Resource with name [%s] not found";
    public static final String USER_ALREADY_HAS_RESOURCE_PERMISSION_ERROR = "User already has assigned resource permission for resource(s) [%s]";
    public static final String USER_ALREADY_HAS_PERMISSION_ERROR = "User already has assigned permission for resource(s) [%s]";
    public static final String PERMISSION_NAME_ALREADY_EXISTS_ERROR = "Permission with name [%s] already exists";
    public static final String RESOURCE_NAME_ALREADY_EXISTS_ERROR = "Resource with name [%s] already exists";
    public static final String UNABLE_TO_PARSE_RESOURCE_PERMISSIONS_ERROR = "Unable to parse resource permissions with ids [%s]";
    public static final String RESOURCE_PERMISSION_ERROR_MESSAGE = "[%s: [%s]]";

    private ViravaConstants() {
        throw new AssertionError("Cannot create instances of this class");
    }
}

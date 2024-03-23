package enums;

public enum UserRoleEnum {
    REGULAR, ARTIST, HOST;

    public static UserRoleEnum getRoleByIndex(int index) {
        return UserRoleEnum.values()[index];
    }

    public int toIndex() {
        return this.ordinal();
    }
}

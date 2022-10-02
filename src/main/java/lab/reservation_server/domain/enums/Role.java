package lab.reservation_server.domain.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),

    GRADUATE("ROLE_GRADUATE"),

    TAKEOFF("ROLE_TAKEOFF");



    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

}

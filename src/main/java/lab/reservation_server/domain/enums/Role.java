package lab.reservation_server.domain.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),

    PROF("PROF"),

    USER("USER"),

    USER_GRADUATE("USER_GRADUATE"),

    USER_TAKEOFF("USER_TAKEOFF");



    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

}

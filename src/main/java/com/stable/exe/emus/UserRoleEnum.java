package com.stable.exe.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    ADMIN_USER(0,"管理员"),
    REGULAR_USER(1,"普通用户")
    ;

    private final int code;

    private final String desc;

    public static UserRoleEnum byCode(Integer code){
        if (Objects.isNull(code))
            return null;
        return Stream.of(UserRoleEnum.values())
                .filter(e-> Objects.equals(e.code,code))
                .findFirst().orElse(null);
    }
}

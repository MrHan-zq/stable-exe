package com.stable.exe.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum DmTrainStatusEnum {

    DM_TRAIN_NO_RUNNING(0,"未执行"),
    DM_TRAIN_RUNNING(1,"执行中")
    ;


    private final int code;

    private final String desc;

    public static DmTrainStatusEnum byCode(Integer code){
       return Arrays.stream(DmTrainStatusEnum.values())
               .filter(e->Objects.equals(e.code,code))
               .findFirst().orElse(null);
    }
}

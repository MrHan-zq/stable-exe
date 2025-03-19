package com.stable.exe.model;

import com.stable.exe.emus.DmTrainStatusEnum;

/**
 * Dreambooth Train status
 * @author hanjun
 * @time 2024-04-14
 */
public class DmTrain {

    private static final DmTrain train=new DmTrain();

    private DmTrainStatusEnum status;


    private DmTrain(){
        super();
        this.status=DmTrainStatusEnum.DM_TRAIN_NO_RUNNING;
    }

    public static DmTrain getInstance(){
        return train;
    }

    public  void setStatus(DmTrainStatusEnum statusEnum){
        this.setStatusEnum(statusEnum);
    }
    private synchronized void setStatusEnum(DmTrainStatusEnum statusEnum){
        this.status=statusEnum;
    }

    public DmTrainStatusEnum getStatus(){
        return this.status;
    }

    public int getStatusOfInt(){
        return this.status.getCode();
    }

}

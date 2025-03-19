package com.stable.exe.extensions;

import com.stable.exe.model.ControlNet;

import java.util.ArrayList;
import java.util.List;

public class ControlNetUnit implements  Plugin {

    private  List<ControlNet> controlNets=new ArrayList<>();
    public ControlNetUnit(List<ControlNet> net){
        controlNets.addAll(net);
    }
    public ControlNetUnit(ControlNet net){
        controlNets.add(net);
    }

    @Override
    public Object toDict() {
        return controlNets;
    }

    @Override
    public String getName() {
        return "ControlNet";
    }


}


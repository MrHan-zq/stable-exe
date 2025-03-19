package com.stable.exe.web.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConceptsEntity implements Serializable {

    private String classDataDir = "";
    private BigDecimal classGuidanceScale = new BigDecimal("7.5");
    private Integer classInferSteps = 40;
    private String classNegativePrompt = "";
    private String classPrompt = "";
    private String classToken = "";
    private String instanceDataDir = "";
    private String instancePrompt = "";
    private String instanceToken = "";
    private Boolean isValid = true;
    private Integer nSaveSample = 1;
    private Integer numClassImagesPer = 0;
    private Integer sampleSeed = -1;
    private BigDecimal saveGuidanceScale = new BigDecimal("7.5");
    private Integer saveInferSteps = 20;
    private String saveSampleNegativePrompt = "";
    private String saveSamplePrompt = "";
    private String saveSampleTemplate = "";
}

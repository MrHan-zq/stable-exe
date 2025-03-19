package com.stable.exe.web.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class DreamBoothConfigEntity implements Serializable {
    private BigDecimal weightDecay = new BigDecimal("0.01");
    private List<ConceptsEntity> conceptsList;
    private String attention = "xformers";  //Memory attention
    private Boolean cacheLatents = true;
    private Integer clipSkip = 2;
    private String conceptsPath = "";
    private String customModelName = "";
    private Boolean deterministic = false;
    private Boolean disableClassMatching = false;
    private Boolean disableLogging = false;
    private Boolean emaPredict = false;
    private Integer epoch = 15;
    private Integer epochPauseFrequency = 0;
    private Integer epochPauseTime = 0;
    private Boolean freezeClipNormalization = false;
    private Boolean fullMixedPrecision = true;
    private Integer gradientAccumulationSteps = 1;
    private Boolean gradientCheckpointing = true;
    private Boolean gradientSetToNone = true;
    private Integer graphSmoothing = 50;
    private Boolean halfModel = false;
    private Boolean hasEma = false;
    private Boolean hflip = false;
    private Boolean inferEma = false;
    private Integer initialRevision = 0;
    private Boolean inputPertubation = true;
    private BigDecimal learningRate = new BigDecimal("0.000002");
    private BigDecimal learningRateMin = new BigDecimal("0.000001");
    private Integer lifetimeRevision = 0;
    private BigDecimal loraLearningRate = new BigDecimal("0.0001");
    private String loraModelName = "";
    private BigDecimal loraTxtLearningRate = new BigDecimal("0.00005");
    private Integer loraTxtRank = 4;
    private Integer loraUnetRank = 4;
    private BigDecimal loraWeight = new BigDecimal("0.8");
    private Boolean loraUseBuggyRequiresGrad = false;
    private Integer lrCycles = 1;
    private BigDecimal lrFactor = new BigDecimal("0.5");
    private Integer lrPower = 1;
    private BigDecimal lrScalePos = new BigDecimal("0.5");
    private String lrScheduler = "constant_with_warmup";
    private Integer lrWarmupSteps = 500;
    private Integer maxTokenLength = 75;
    private Integer minSnrGamma = 0;
    private Boolean useDream = false;
    private BigDecimal dreamDetailPreservation = new BigDecimal("0.5");
    private Boolean freezeSpectralNorm = false;
    private String mixedPrecision = "bf16";
    private String modelDir = "";
    private String modelName = "";
    private String modelPath = "";
    private String modelType = "v1x";
    private String noiseScheduler = "DDPM";
    private Integer numTrainEpochs = 100;  //Training Steps Per Image Epochs
    private Integer offsetNoise = 0;
    private String optimizer = "8bit AdamW";
    private Boolean padTokens = true;
    private String pretrainedModelNameOrPath = "";
    private String pretrainedVaeNameOrPath = "";
    private Boolean priorLossScale = false;
    private Integer priorLossTarget = 100;
    private BigDecimal priorLossWeight = new BigDecimal("0.75");
    private BigDecimal priorLossWeightMin = new BigDecimal("0.1");
    private Integer resolution = 512; //Max resolution
    private Integer revision = 125;
    private Integer sampleBatchSize = 1;
    private String sanityPrompt = "";
    private Integer sanitySeed = 420420;
    private Boolean saveCkptAfter = true;
    private Boolean saveCkptCancel = false;
    private Boolean saveCkptDuring = false;
    private Boolean saveEma = true;
    private Integer saveEmbeddingEvery = 25; //save Model Frequency Epochs
    private Boolean saveLoraAfter = true;
    private Boolean saveLoraCancel = false;
    private Boolean saveLoraDuring = false;
    private Boolean saveLoraForExtraNet = false;
    private Integer savePreviewEvery = 5;  //Save preview Frequency
    private Boolean saveSafetensors = true;
    private Boolean saveStateAfter = false;
    private Boolean saveStateCancel = false;
    private Boolean saveStateDuring = false;
    private String scheduler = "DEISMultistep";
    private String sharedDiffusersPath = "";
    private Boolean shuffleTags = true;
    private String snapshot = "";
    private Boolean splitLoss = true;
    private String src = "v1-5-pruned-emaonly";
    private Integer stopTextEncoder = 1; //step Ratio of Text Encoder Training
    private Boolean strictTokens = false;
    private Boolean dynamicImgNorm = false;
    private BigDecimal tencWeightDecay = new BigDecimal("0.01");
    private Integer tencGradClipNorm = 0;
    private Integer tomesd = 0;
    private Integer trainBatchSize = 1;
    private Boolean trainImagic = false;
    private Boolean trainUnet = true;
    private Boolean trainUnfrozen = true;
    private BigDecimal txtLearningRate = new BigDecimal("0.000001"); //txt Encoder learning rate
    private Boolean useConcepts = false;
    private Boolean useEma = false;
    private Boolean useLora = false;
    private Boolean useLoraExtended = false;
    private List<Boolean> useSharedSrc =new ArrayList<>();
    private Boolean useSubdir = true;
    private Boolean v2 = false;


//    public static void main(String[] args) {
//        DreamBoothConfig model = new DreamBoothConfig();
//        model.setModel_dir("/user/hanjun/test");
//        model.setCustom_model_name("myDemo");
//        List<Concepts> list = new ArrayList<>();
//        Concepts concepts = new Concepts();
//        concepts.setInstance_prompt("a girl");
//        concepts.setClass_prompt("girl");
//        list.add(concepts);
//        model.setConcepts_list(list);
//        DreamBoothConfigEntity entity = new DreamBoothConfigEntity().model2Entity(model);
//        System.out.println(JacksonUtil.toJsonString(entity));
//    }
}


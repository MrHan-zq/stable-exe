package com.stable.exe.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DreamBoothModel extends DreamBoothBase {

    private String new_model_name;  //"The name of the model to create."

    private String new_model_src;   // Query(description="The source checkpoint to extract to create this model.", ),

    private String new_model_shared_src;    //Query(None, description="The shared diffusers source to use for this differs model.", ),

    private Boolean create_from_hub=false;  //Query(False, description="Create this model from the hub", ),

    private String new_model_url;   //Query(None,description="The hub URL to use for this model. Must contain diffusers model.", ),

    private String model_type="v1x";    // Query("v1x",description="Model type (v1x/v2x-512/v2x/sdxl)", ),

    private Boolean train_unfrozen=true;    // Query(True,description="Un-freeze the model.", ),

    private String new_model_token; // Query(None, description="Your huggingface hub token.", ),

    private Boolean new_model_extract_ema=false;    //Query(False, description="Whether to extract EMA weights if present.", ),
}

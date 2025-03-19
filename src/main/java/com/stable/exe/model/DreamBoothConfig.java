package com.stable.exe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DreamBoothConfig {

    private BigDecimal weight_decay = new BigDecimal("0.01");
    private String attention = "xformers"; //Memory attention
    private Boolean cache_latents = true;
    private Integer clip_skip = 2;
    private List<Concepts> concepts_list;
    private String concepts_path = "";
    private String custom_model_name = "";
    private Boolean deterministic = false;
    private Boolean disable_class_matching = false;
    private Boolean disable_logging = false;
    private Boolean ema_predict = false;
    private Integer epoch = 15;
    private Integer epoch_pause_frequency = 0;
    private Integer epoch_pause_time = 0;
    private Boolean freeze_clip_normalization = false;
    private Boolean full_mixed_precision = true;
    private Integer gradient_accumulation_steps = 1;
    private Boolean gradient_checkpointing = true;
    private Boolean gradient_set_to_none = true;
    private Integer graph_smoothing = 50;
    private Boolean half_model = false;
    private Boolean has_ema = false;
    private Boolean hflip = false;
    private Boolean infer_ema = false;
    private Integer initial_revision = 0;
    private Boolean input_pertubation = true;
    private BigDecimal learning_rate = new BigDecimal("0.000002");
    private BigDecimal learning_rate_min = new BigDecimal("0.000001");
    private Integer lifetime_revision = 0;
    private BigDecimal lora_learning_rate = new BigDecimal("0.0001");
    private String lora_model_name = "";
    private BigDecimal lora_txt_learning_rate = new BigDecimal("0.00005");
    private Integer lora_txt_rank = 4;
    private Integer lora_unet_rank = 4;
    private BigDecimal lora_weight = new BigDecimal("0.8");
    private Boolean lora_use_buggy_requires_grad = false;
    private Integer lr_cycles = 1;
    private BigDecimal lr_factor = new BigDecimal("0.5");
    private Integer lr_power = 1;
    private BigDecimal lr_scale_pos = new BigDecimal("0.5");
    private String lr_scheduler = "constant_with_warmup";
    private Integer lr_warmup_steps = 500;
    private Integer max_token_length = 75;
    private Integer min_snr_gamma = 0;
    private Boolean use_dream = false;
    private BigDecimal dream_detail_preservation = new BigDecimal("0.5");
    private Boolean freeze_spectral_norm = false;
    private String mixed_precision = "bf16";
    private String model_dir = "";
    private String model_name = "";
    private String model_path = "";
    private String model_type = "v1x";
    private String noise_scheduler = "DDPM";
    private Integer num_train_epochs = 100; //Training Steps Per Image Epochs
    private Integer offset_noise = 0;
    private String optimizer = "8bit AdamW";
    private Boolean pad_tokens = true;
    private String pretrained_model_name_or_path = "";
    private String pretrained_vae_name_or_path = "";
    private Boolean prior_loss_scale = false;
    private Integer prior_loss_target = 100;
    private BigDecimal prior_loss_weight = new BigDecimal("0.75");
    private BigDecimal prior_loss_weight_min = new BigDecimal("0.1");
    private Integer resolution = 512; //Max resolution
    private Integer revision = 125;
    private Integer sample_batch_size = 1;
    private String sanity_prompt = "";
    private Integer sanity_seed = 420420;
    private Boolean save_ckpt_after = true;
    private Boolean save_ckpt_cancel = false;
    private Boolean save_ckpt_during = false;
    private Boolean save_ema = true;
    private Integer save_embedding_every = 25; //save Model Frequency Epochs
    private Boolean save_lora_after = true;
    private Boolean save_lora_cancel = false;
    private Boolean save_lora_during = false;
    private Boolean save_lora_for_extra_net = false;
    private Integer save_preview_every = 5; //Save preview Frequency
    private Boolean save_safetensors = true;
    private Boolean save_state_after = false;
    private Boolean save_state_cancel = false;
    private Boolean save_state_during = false;
    private String scheduler = "DEISMultistep";
    private String shared_diffusers_path = "";
    private Boolean shuffle_tags = true;
    private String snapshot = "";
    private Boolean split_loss = true;
    private String src = "v1-5-pruned-emaonly";
    private Integer stop_text_encoder = 1; //step Ratio of Text Encoder Training
    private Boolean strict_tokens = false;
    private Boolean dynamic_img_norm = false;
    private BigDecimal tenc_weight_decay = new BigDecimal("0.01");
    private Integer tenc_grad_clip_norm = 0;
    private Integer tomesd = 0;
    private Integer train_batch_size = 1;
    private Boolean train_imagic = false;
    private Boolean train_unet = true;
    private Boolean train_unfrozen = true;
    private BigDecimal txt_learning_rate = new BigDecimal("0.000001"); // txt Encoder learning rate
    private Boolean use_concepts = false;
    private Boolean use_ema = false;
    private Boolean use_lora = false;
    private Boolean use_lora_extended = false;
    private List<Boolean> use_shared_src =new ArrayList<>();
    private Boolean use_subdir = true;
    private Boolean v2 = false;
}

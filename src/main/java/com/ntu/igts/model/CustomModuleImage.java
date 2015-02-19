package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "custom_module_image")
@JsonRootName("custommoduleimage")
public class CustomModuleImage extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1479030798110953804L;

    @Column(name = "custom_module_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("custommoduleid")
    private String customModuleId;
    @Column(name = "image_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("imageid")
    private String imageId;

    public String getCustomModuleId() {
        return customModuleId;
    }

    public void setCustomModuleId(String customModuleId) {
        this.customModuleId = customModuleId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}

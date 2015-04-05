package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "sensitive_word")
@JsonRootName("sensitiveword")
@QueryField({ "word" })
public class SensitiveWord extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1181080028496268314L;

    @Column(name = "word")
    @JsonProperty("word")
    private String word;
    @Column(name = "active_yn", columnDefinition = "VARCHAR(2)")
    @JsonProperty("activeyn")
    private String activeYN = "Y";

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public void setActiveYN(String activeYN) {
        this.activeYN = activeYN;
    }

}

package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Resolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;
import com.ntu.igts.search.CommodityActiveYnFilterFactory;
import com.ntu.igts.search.CommodityDistrictFilterFactory;
import com.ntu.igts.search.CommodityStatusFilterFactory;
import com.ntu.igts.search.CommodityTagFilterFactory;
import com.ntu.igts.search.IndexWhenNotDeletedInterceptor;
import com.ntu.igts.utils.StringUtil;

@FullTextFilterDefs({ @FullTextFilterDef(name = "districtFilter", impl = CommodityDistrictFilterFactory.class),
                @FullTextFilterDef(name = "statusFilter", impl = CommodityStatusFilterFactory.class),
                @FullTextFilterDef(name = "tagFilter", impl = CommodityTagFilterFactory.class),
                @FullTextFilterDef(name = "activeYnFilter", impl = CommodityActiveYnFilterFactory.class) })
@Entity
@Indexed(interceptor = IndexWhenNotDeletedInterceptor.class)
@Table(name = "commodity")
@JsonRootName("commodity")
@QueryField({ "title", "description" })
public class Commodity extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7857399618945670063L;

    @Field
    @Boost(2.0f)
    @Column(name = "title")
    @JsonProperty("title")
    private String title;
    @Field
    @Boost(1.8f)
    @Column(name = "description")
    @JsonProperty("description")
    private String description;
    @Field(analyze = Analyze.NO)
    @Column(name = "active_yn", columnDefinition = "VARCHAR(2)")
    @JsonProperty("activeyn")
    private String activeYN = "Y";
    @Column(name = "carriage")
    @JsonProperty("carriage")
    private double carriage;
    @Field(analyze = Analyze.NO)
    @NumericField
    @Column(name = "price")
    @JsonProperty("price")
    private double price;
    @Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.DAY)
    @Column(name = "release_date")
    @JsonProperty("releasedate")
    private Date releaseDate;
    @Field(analyze = Analyze.NO)
    @Column(name = "district")
    @JsonProperty("district")
    private String district;
    @Field(analyze = Analyze.NO)
    @NumericField
    @Column(name = "collection_number")
    @JsonProperty("collectionnumber")
    private int collectionNumber;
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;
    @Field(analyze = Analyze.NO)
    @Column(name = "status")
    @JsonProperty("status")
    private String status = StringUtil.EMPTY;
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @IndexedEmbedded(includeEmbeddedObjectId = true, depth = 1)
    @Transient
    @JsonProperty("tags")
    private List<Tag> tags = new ArrayList<Tag>();
    @Transient
    @JsonProperty("covers")
    private List<Cover> covers = new ArrayList<Cover>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public void setActiveYN(String activeYN) {
        this.activeYN = activeYN;
    }

    public double getCarriage() {
        return carriage;
    }

    public void setCarriage(double carriage) {
        this.carriage = carriage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(int collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Cover> getCovers() {
        return covers;
    }

    public void setCovers(List<Cover> covers) {
        this.covers = covers;
    }

}

package com.myway.myway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Place.
 */
@Table("place")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("display_name")
    private String displayName;

    @NotNull(message = "must not be null")
    @Column("lat")
    private Double lat;

    @NotNull(message = "must not be null")
    @Column("lon")
    private Double lon;

    @Column("image")
    private String image;

    @Transient
    private User owner;

    @Transient
    private PlaceCategory category;

    @Transient
    @JsonIgnoreProperties(value = { "places", "owner" }, allowSetters = true)
    private Itinary itinary;

    @Column("owner_id")
    private Long ownerId;

    @Column("category_id")
    private Long categoryId;

    @Column("itinary_id")
    private Long itinaryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Place id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Place name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Place displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getLat() {
        return this.lat;
    }

    public Place lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public Place lon(Double lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getImage() {
        return this.image;
    }

    public Place image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
        this.ownerId = user != null ? user.getId() : null;
    }

    public Place owner(User user) {
        this.setOwner(user);
        return this;
    }

    public PlaceCategory getCategory() {
        return this.category;
    }

    public void setCategory(PlaceCategory placeCategory) {
        this.category = placeCategory;
        this.categoryId = placeCategory != null ? placeCategory.getId() : null;
    }

    public Place category(PlaceCategory placeCategory) {
        this.setCategory(placeCategory);
        return this;
    }

    public Itinary getItinary() {
        return this.itinary;
    }

    public void setItinary(Itinary itinary) {
        this.itinary = itinary;
        this.itinaryId = itinary != null ? itinary.getId() : null;
    }

    public Place itinary(Itinary itinary) {
        this.setItinary(itinary);
        return this;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long user) {
        this.ownerId = user;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long placeCategory) {
        this.categoryId = placeCategory;
    }

    public Long getItinaryId() {
        return this.itinaryId;
    }

    public void setItinaryId(Long itinary) {
        this.itinaryId = itinary;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Place)) {
            return false;
        }
        return getId() != null && getId().equals(((Place) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Place{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", image='" + getImage() + "'" +
            "}";
    }
}

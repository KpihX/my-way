package com.kpihx_labs.myway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Location.
 */
@Table("location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("latitude")
    private Double latitude;

    @NotNull(message = "must not be null")
    @Column("longitude")
    private Double longitude;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("address")
    private String address;

    @Transient
    private LocationCategory category;

    @Transient
    @JsonIgnoreProperties(value = { "locations" }, allowSetters = true)
    private Set<Itinary> itinaries = new HashSet<>();

    @Column("category_id")
    private Long categoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Location latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Location longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Location description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return this.address;
    }

    public Location address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocationCategory getCategory() {
        return this.category;
    }

    public void setCategory(LocationCategory locationCategory) {
        this.category = locationCategory;
        this.categoryId = locationCategory != null ? locationCategory.getId() : null;
    }

    public Location category(LocationCategory locationCategory) {
        this.setCategory(locationCategory);
        return this;
    }

    public Set<Itinary> getItinaries() {
        return this.itinaries;
    }

    public void setItinaries(Set<Itinary> itinaries) {
        if (this.itinaries != null) {
            this.itinaries.forEach(i -> i.removeLocation(this));
        }
        if (itinaries != null) {
            itinaries.forEach(i -> i.addLocation(this));
        }
        this.itinaries = itinaries;
    }

    public Location itinaries(Set<Itinary> itinaries) {
        this.setItinaries(itinaries);
        return this;
    }

    public Location addItinary(Itinary itinary) {
        this.itinaries.add(itinary);
        itinary.getLocations().add(this);
        return this;
    }

    public Location removeItinary(Itinary itinary) {
        this.itinaries.remove(itinary);
        itinary.getLocations().remove(this);
        return this;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long locationCategory) {
        this.categoryId = locationCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}

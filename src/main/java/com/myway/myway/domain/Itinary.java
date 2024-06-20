package com.myway.myway.domain;

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
 * A Itinary.
 */
@Table("itinary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Itinary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("pref_name")
    private String prefName;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("total_distance")
    private Double totalDistance;

    @NotNull(message = "must not be null")
    @Column("total_time")
    private Double totalTime;

    @Column("image")
    private String image;

    @Transient
    @JsonIgnoreProperties(value = { "owner", "category", "itinary" }, allowSetters = true)
    private Set<Place> places = new HashSet<>();

    @Transient
    private User owner;

    @Column("owner_id")
    private Long ownerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Itinary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Itinary name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefName() {
        return this.prefName;
    }

    public Itinary prefName(String prefName) {
        this.setPrefName(prefName);
        return this;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public String getDescription() {
        return this.description;
    }

    public Itinary description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalDistance() {
        return this.totalDistance;
    }

    public Itinary totalDistance(Double totalDistance) {
        this.setTotalDistance(totalDistance);
        return this;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalTime() {
        return this.totalTime;
    }

    public Itinary totalTime(Double totalTime) {
        this.setTotalTime(totalTime);
        return this;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public String getImage() {
        return this.image;
    }

    public Itinary image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Place> getPlaces() {
        return this.places;
    }

    public void setPlaces(Set<Place> places) {
        if (this.places != null) {
            this.places.forEach(i -> i.setItinary(null));
        }
        if (places != null) {
            places.forEach(i -> i.setItinary(this));
        }
        this.places = places;
    }

    public Itinary places(Set<Place> places) {
        this.setPlaces(places);
        return this;
    }

    public Itinary addPlaces(Place place) {
        this.places.add(place);
        place.setItinary(this);
        return this;
    }

    public Itinary removePlaces(Place place) {
        this.places.remove(place);
        place.setItinary(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
        this.ownerId = user != null ? user.getId() : null;
    }

    public Itinary owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long user) {
        this.ownerId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Itinary)) {
            return false;
        }
        return getId() != null && getId().equals(((Itinary) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Itinary{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prefName='" + getPrefName() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalDistance=" + getTotalDistance() +
            ", totalTime=" + getTotalTime() +
            ", image='" + getImage() + "'" +
            "}";
    }
}

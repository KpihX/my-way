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

    @Column("description")
    private String description;

    @Column("distance")
    private Double distance;

    @Column("duration")
    private Integer duration;

    @NotNull(message = "must not be null")
    @Column("polyline")
    private String polyline;

    @Transient
    @JsonIgnoreProperties(value = { "category", "itinaries" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

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

    public Double getDistance() {
        return this.distance;
    }

    public Itinary distance(Double distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Itinary duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPolyline() {
        return this.polyline;
    }

    public Itinary polyline(String polyline) {
        this.setPolyline(polyline);
        return this;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Itinary locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Itinary addLocation(Location location) {
        this.locations.add(location);
        return this;
    }

    public Itinary removeLocation(Location location) {
        this.locations.remove(location);
        return this;
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
            ", description='" + getDescription() + "'" +
            ", distance=" + getDistance() +
            ", duration=" + getDuration() +
            ", polyline='" + getPolyline() + "'" +
            "}";
    }
}

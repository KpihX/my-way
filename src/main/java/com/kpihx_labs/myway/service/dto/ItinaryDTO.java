package com.kpihx_labs.myway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.kpihx_labs.myway.domain.Itinary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItinaryDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String description;

    private Double distance;

    private Integer duration;

    @NotNull(message = "must not be null")
    private String polyline;

    private Set<LocationDTO> locations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public Set<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationDTO> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItinaryDTO)) {
            return false;
        }

        ItinaryDTO itinaryDTO = (ItinaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itinaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItinaryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", distance=" + getDistance() +
            ", duration=" + getDuration() +
            ", polyline='" + getPolyline() + "'" +
            ", locations=" + getLocations() +
            "}";
    }
}

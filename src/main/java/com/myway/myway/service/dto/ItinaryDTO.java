package com.myway.myway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myway.myway.domain.Itinary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItinaryDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String prefName;

    private String description;

    @NotNull(message = "must not be null")
    private Double totalDistance;

    @NotNull(message = "must not be null")
    private Double totalTime;

    private String image;

    private UserDTO owner;

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

    public String getPrefName() {
        return prefName;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
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
            ", prefName='" + getPrefName() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalDistance=" + getTotalDistance() +
            ", totalTime=" + getTotalTime() +
            ", image='" + getImage() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}

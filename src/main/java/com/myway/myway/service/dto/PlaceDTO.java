package com.myway.myway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myway.myway.domain.Place} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaceDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String displayName;

    @NotNull(message = "must not be null")
    private Double lat;

    @NotNull(message = "must not be null")
    private Double lon;

    private String image;

    private UserDTO owner;

    private PlaceCategoryDTO category;

    private ItinaryDTO itinary;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
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

    public PlaceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(PlaceCategoryDTO category) {
        this.category = category;
    }

    public ItinaryDTO getItinary() {
        return itinary;
    }

    public void setItinary(ItinaryDTO itinary) {
        this.itinary = itinary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaceDTO)) {
            return false;
        }

        PlaceDTO placeDTO = (PlaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", image='" + getImage() + "'" +
            ", owner=" + getOwner() +
            ", category=" + getCategory() +
            ", itinary=" + getItinary() +
            "}";
    }
}

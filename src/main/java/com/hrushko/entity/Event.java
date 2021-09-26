package com.hrushko.entity;

import java.util.Date;
import java.util.Objects;

public class Event extends Entity {
    private String name;
    private String description;
    private String pictureLink;
    private Value theme;
    private Date date;
    private Address address;
    private Integer author_id;
    private Integer capacity;
    private Date duration;
    private String shortDescription;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Value getTheme() {
        return theme;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTheme(Value theme) {
        this.theme = theme;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public void setShortDescription(String shortDescription) {
        int sizeShortDescription = 140;
        if (description.length() > sizeShortDescription) {
            shortDescription = description.substring(0, sizeShortDescription);
            shortDescription +="...";
        } else {
            shortDescription = description;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(pictureLink, event.pictureLink) &&
                Objects.equals(theme, event.theme) &&
                Objects.equals(date, event.date) &&
                Objects.equals(address, event.address) &&
                Objects.equals(author_id, event.author_id) &&
                Objects.equals(capacity, event.capacity) &&
                Objects.equals(duration, event.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, pictureLink, theme, date, address, author_id, capacity, duration);
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pictureLink='" + pictureLink + '\'' +
                ", theme=" + theme +
                ", date=" + new Date(date.getTime()) +
                ", address=" + address +
                ", author_id=" + author_id +
                ", capacity=" + capacity +
                ", duration=" + duration.getTime() +
                '}';
    }
}


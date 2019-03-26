package io.softserve.goadventures.event.dto;

import io.softserve.goadventures.gallery.model.Gallery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private int id;

    private String topic;

    private String startDate;

    private String endDate;

    private String location;

    private String description;

    private int statusId;

    private String category;

    private int gallery;
}
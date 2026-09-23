package com.karainc.dailytalk.domain.utils.popup.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Popup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long popupId;

    @Column(name = "popup_name")
    private String popupName;

    @Column(name ="popup_status")
    private Boolean popupStatus;

    @Column(name="popup_date")
    private String popupDate;

    @Column(name="popup_link")
    private String popupLink;

    @Column(name="popup_image")
    private String popupImage;
}

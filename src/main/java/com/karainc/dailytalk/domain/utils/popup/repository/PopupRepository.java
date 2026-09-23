package com.karainc.dailytalk.domain.utils.popup.repository;

import com.karainc.dailytalk.domain.utils.popup.entity.Popup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup,Long> {

    Popup findByPopupId(long popupId);
}

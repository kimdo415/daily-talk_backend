package com.karainc.dailytalk.domain.language.contoller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LangInfo {
    List<String> languages;
    List<String> languageEnum;
    String duringDate;
    String limitDate;
}

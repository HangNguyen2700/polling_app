package com.polling_app.polling_app.dto.response.poll;

import com.polling_app.polling_app.entity.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOptionResponse {
    private String content;

    public static CreateOptionResponse convert(Option option) {
        return CreateOptionResponse.builder()
                .content(option.getContent())
                .build();
    }
}

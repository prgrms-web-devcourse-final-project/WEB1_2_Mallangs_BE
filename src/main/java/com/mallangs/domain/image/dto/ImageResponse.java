package com.mallangs.domain.image.dto;

import com.mallangs.domain.image.entity.Image;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImageResponse {
    private String url;
    private Integer width;
    private Integer height;

    public ImageResponse(Image image){
//        this.url = image.getUrl();
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
}

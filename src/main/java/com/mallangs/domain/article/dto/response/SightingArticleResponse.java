package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.SightingArticle;
import com.mallangs.domain.article.validation.ValidationGroups;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@RequiredArgsConstructor
public class SightingArticleResponse extends ArticleResponse {

    @NotNull
    private Long lostArticleId;

    @NotNull(message = "동물 타입은 필수입니다.", groups = ValidationGroups.CreateGroup.class)
    private PetType petType;

    private String breed;

    private PetGender petGender;

    private String petColor;

    private String chipNumber;

    private LocalDateTime sightDate;

    @NotBlank(message = "목격 장소는 필수입니다.", groups = ValidationGroups.CreateGroup.class)
    private String sightLocation;

    public SightingArticleResponse(SightingArticle article){
        super(article);
        this.lostArticleId = article.getLostArticleId();
        this.petType = article.getPetType();
        this.breed = article.getBreed();
        this.petGender = article.getPetGender();
        this.petColor = article.getPetColor();
        this.chipNumber = article.getChipNumber();
        this.sightDate = article.getSightDate();
        this.sightLocation = article.getSightLocation();
    }
}

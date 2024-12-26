package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.SightingArticleCreateRequest;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("sight")
@SuperBuilder
public class SightingArticle extends Article {

    @NotNull
    private Long lostArticleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender petGender;

    private String petColor;

    private String chipNumber;

    private LocalDateTime sightDate;

    private String sightLocation;

    @Override
    public void applyChanges(Article updatedArticle) {
        super.applyChanges(updatedArticle);

        if (updatedArticle instanceof SightingArticle) {
            SightingArticle updatedSightingArticle = (SightingArticle) updatedArticle;
            if (updatedSightingArticle.getLostArticleId() != null) {
                this.lostArticleId = updatedSightingArticle.getLostArticleId();
            }
            if (updatedSightingArticle.getPetType() != null) {
                this.petType = updatedSightingArticle.getPetType();
            }
            if (updatedSightingArticle.getBreed() != null) {
                this.breed = updatedSightingArticle.getBreed();
            }
            if (updatedSightingArticle.getPetGender() != null) {
                this.petGender = updatedSightingArticle.getPetGender();
            }
            if (updatedSightingArticle.getPetColor() != null) {
                this.petColor = updatedSightingArticle.getPetColor();
            }
            if (updatedSightingArticle.getChipNumber() != null) {
                this.chipNumber = updatedSightingArticle.getChipNumber();
            }
            if (updatedSightingArticle.getSightDate() != null) {
                this.sightDate = updatedSightingArticle.getSightDate();
            }
            if (updatedSightingArticle.getSightLocation() != null) {
                this.sightLocation = updatedSightingArticle.getSightLocation();
            }
        }
    }

    public static SightingArticle createSightingArticle(Member member, SightingArticleCreateRequest createRequest) {
        // GeometryFactory 객체 생성
        GeometryFactory geometryFactory = new GeometryFactory();

        // 위도와 경도를 기반으로 Coordinate 객체 생성
        Coordinate coordinate = new Coordinate(createRequest.getLongitude(),
                createRequest.getLatitude());

        // Point 객체 생성
        Point geography = geometryFactory.createPoint(coordinate);
        geography.setSRID(4326);  // SRID 4326 (WGS 84) 설정

        return SightingArticle.builder()
                .lostArticleId(createRequest.getLostArticleId())
                .petType(createRequest.getPetType())
                .breed(createRequest.getBreed())
                .petGender(createRequest.getPetGender())
                .petColor(createRequest.getPetColor())
                .chipNumber(createRequest.getChipNumber())
                .sightDate(createRequest.getSightDate())
                .sightLocation(createRequest.getSightLocation())
                .member(member)
                .articleType(createRequest.getArticleType())
                .articleStatus(createRequest.getArticleStatus())
                .title(createRequest.getTitle())
                .geography(geography)
                .description(createRequest.getDescription())
                .image(createRequest.getImage())
                .build();
    }
}

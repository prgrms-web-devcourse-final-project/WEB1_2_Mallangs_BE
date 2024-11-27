package com.mallangs.domain.article.service;
import com.mallangs.domain.article.entity.MapVisibility;
import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.article.repository.PlaceArticleRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceArticleCsvService {
    @Value("${csv.file.path}")
    private String csvFilePath;

    private final PlaceArticleRepository placeArticleRepository;

    private final MemberRepository memberRepository;

    @PostConstruct
    @Transactional
    public void loadCsvData() {
        List<PlaceArticle> placeArticles = readCsvData(csvFilePath);
        log.info("placeArticles.size() = {}", placeArticles.size());
        placeArticleRepository.saveAll(placeArticles);
    }

    public List<PlaceArticle> readCsvData(String filePath) {
        List<PlaceArticle> placeArticles = new ArrayList<>();
        Member member = memberRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        GeometryFactory geometryFactory = new GeometryFactory();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1) // 헤더 라인 스킵
                .build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                PlaceArticle placeArticle = createPlaceArticleFromCsvLine(line, member, geometryFactory);
                if (placeArticle != null) {
                    placeArticles.add(placeArticle);
                }
            }
        } catch (IOException e) {
            log.error("Failed to read CSV file: {}", filePath, e);
            e.printStackTrace();
            // 파일 읽기 실패 시 예외 처리 로직 추가
        } catch (CsvValidationException e) {
            log.error("CSV validation error: {}", e.getMessage());
            log.error("Error at line: {}", e.getLineNumber());
            e.printStackTrace();
            // CSV 유효성 검사 실패 시 예외 처리 로직 추가
        }
        return placeArticles;
    }

    private PlaceArticle createPlaceArticleFromCsvLine(String[] line, Member member, GeometryFactory geometryFactory) {
        // CSV 데이터 파싱 및 PlaceArticle 객체 생성 로직
        String title = line[0]; // 시설명
        String category = line[3]; // 카테고리3
        String address = line[14]; // 지번주소
        String roadAddress = line[15]; // 도로명 주소
        String basicInfo = line[28]; // 기본 정보 필드 (String)
        String contact = line[16]; // 전화번호
        double longitude = Double.parseDouble(line[12]); // 경도
        double latitude = Double.parseDouble(line[11]); // 위도
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        Boolean hasParking = "Y".equalsIgnoreCase(line[20]); // 주차 가능 여부
        Boolean isPetFriendly = "Y".equalsIgnoreCase(line[22]); // 반려동물 동반 여부
        String businessHours = line[19];
        String closeDays = line[18];


        return PlaceArticle.builder()
                .member(member)
                .mapVisibility(MapVisibility.VISIBLE) // 공개 여부 설정 (예시)
                .title(title)
                .geography(point)
                .category(category)
                .address(address)
                .roadAddress(roadAddress)
                .basicInfo(basicInfo)
                .contact(contact)
                .hasParking(hasParking)
                .isPetFriendly(isPetFriendly)
                .businessHours(businessHours)
                .closeDays(closeDays)
                .build();
    }
}

//package com.mallangs.domain.article.service;
//
//import com.mallangs.domain.article.entity.ArticleType;
//import com.mallangs.domain.article.entity.MapVisibility;
//import com.mallangs.domain.article.entity.PlaceArticle;
//import com.mallangs.domain.article.repository.PlaceArticleRepository;
//import com.mallangs.domain.board.entity.BoardStatus;
//import com.mallangs.domain.member.entity.Member;
//import com.mallangs.domain.member.repository.MemberRepository;
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import com.opencsv.exceptions.CsvValidationException;
//import jakarta.annotation.PostConstruct;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Point;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
///*
//장소 정보를 DB에 저장하기 위해서는 memberId가 필수입니다 (장소 글타래가 공통 글타래를 상속받기 때문에) memberId는 null 값을 가질 수 없게 되었습니다.
//따라서 장소 정보를 저장할 때 memberId를 1로 고정하고, 앞으로도 memberId 1은 관리자 계정 또는 비활성화된 객체로 활용해야 합니다.
// */
//public class PlaceArticleCsvService {
//
//  @Value("${csv.file.path}")
//  private String csvFilePath;
//
//  private final PlaceArticleRepository placeArticleRepository;
//
//  private final MemberRepository memberRepository;
//
//  @PostConstruct
//  @Transactional
//  public void loadCsvData() {
//    List<PlaceArticle> placeArticles = readCsvData(csvFilePath);
//    log.info("placeArticles.size() = {}", placeArticles.size());
//    placeArticleRepository.saveAll(placeArticles);
//  }
//
//  public List<PlaceArticle> readCsvData(String filePath) {
//    List<PlaceArticle> placeArticles = new ArrayList<>();
//    Member member = memberRepository.findById(1L)
//        .orElseThrow(() -> new IllegalArgumentException("Member not found"));
//    GeometryFactory geometryFactory = new GeometryFactory();
//    try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath, StandardCharsets.UTF_8))
//        .withSkipLines(1) // 헤더 라인 스킵
//        .build()) {
//      String[] line;
//      while ((line = reader.readNext()) != null) {
//        PlaceArticle placeArticle = createPlaceArticleFromCsvLine(line, member, geometryFactory);
//        if (placeArticle != null) {
//          placeArticles.add(placeArticle);
//        }
//      }
//    } catch (IOException e) {
//      log.error("Failed to read CSV file: {}", filePath, e);
//      e.printStackTrace();
//      // 파일 읽기 실패 시 예외 처리 로직 추가
//    } catch (CsvValidationException e) {
//      log.error("CSV validation error: {}", e.getMessage());
//      log.error("Error at line: {}", e.getLineNumber());
//      e.printStackTrace();
//      // CSV 유효성 검사 실패 시 예외 처리 로직 추가
//    }
//    return placeArticles;
//  }
//
//  private PlaceArticle createPlaceArticleFromCsvLine(String[] line, Member member,
//      GeometryFactory geometryFactory) {
//    // CSV 데이터 파싱 및 PlaceArticle 객체 생성 로직
//    String title = line[0]; // 시설명
//    String category = line[3]; // 카테고리3
//    String address = line[14]; // 지번주소
//    String roadAddress = line[15]; // 도로명 주소
//    String basicInfo = line[28]; // 기본 정보 필드 (String)
//    String contact = line[16]; // 전화번호
//    double longitude = Double.parseDouble(line[12]); // 경도
//    double latitude = Double.parseDouble(line[11]); // 위도
//    Coordinate coordinate = new Coordinate(longitude, latitude);
//    Point point = geometryFactory.createPoint(coordinate);
//    point.setSRID(4326);
//    Boolean hasParking = "Y".equalsIgnoreCase(line[20]); // 주차 가능 여부
//    Boolean isPetFriendly = "Y".equalsIgnoreCase(line[22]); // 반려동물 동반 여부
//    String businessHours = line[19];
//    String closeDays = line[18];
//    String webSite = line[17];
//
//    boolean exists = placeArticleRepository.existsByTitle(title);
//    if (exists) {
//      return null; // 이미 존재하면 null 반환
//    }
//
//    return PlaceArticle.builder()
//        .member(member)
//        .mapVisibility(MapVisibility.VISIBLE) // 공개 여부 설정 (예시)
//        .title(title)
//        .geography(point)
//        .category(category)
//        .address(address)
//        .roadAddress(roadAddress)
//        .description(basicInfo)
//        .contact(contact)
//        .hasParking(hasParking)
//        .isPetFriendly(isPetFriendly)
//        .businessHours(businessHours)
//        .closeDays(closeDays)
//        .createdAt(LocalDateTime.now())
//        .website(webSite)
//        .articleStatus(BoardStatus.PUBLISHED)
//        .articleType(ArticleType.PLACE)
//        .isPublicData(true)
//        .build();
//  }
//}

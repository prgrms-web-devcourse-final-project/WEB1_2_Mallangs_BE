package com.mallangs.domain.article.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*
Service Key 사용시 인코딩된 Key를 사용하여야 에러가 안납니다!!
 */

@Log4j2 // Lombok을 사용한 로깅 기능 활성화
@RestController // REST API 컨트롤러로 지정
@RequestMapping("/tourapi") // "/tourapi" 경로로 들어오는 요청 처리
@Tag(name = "Tour API", description = " 한국관광공사 반려동물 동반여행_서비스1.0.0 API 호출 컨트롤러")
public class TourApiController {

    @Value("${tourapi.serviceKey}") // application.properties(또는 yml) 파일에서 설정 값 주입
    private String serviceKey;
    @Value("${tourapi.mobileOS}")
    private String mobileOS;
    @Value("${tourapi.mobileApp}")
    private String mobileApp;
    private final String BASE_URL = "http://apis.data.go.kr/B551011/KorPetTourService"; // API 기본 URL
    private final String TYPE = "json"; // 응답 데이터 타입 (JSON)
    private final RestTemplate restTemplate; // REST API 호출을 위한 객체



    // 생성자에서 serviceKey 인코딩 및 RestTemplate 의존성 주입
    public TourApiController(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 지역코드 조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param _type 응답 타입 (_type 파라미터로 전달, 없을 시 기본값 json)
     * @param areaCode 지역 코드
     * @return 지역코드 조회 결과 (JSON 형태)
     */

    @GetMapping("/areaCode") // GET 요청 처리, "/areaCode" 경로
    public ResponseEntity<String> getAreaCode(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String _type,
            @RequestParam(required = false) String areaCode
    ){
        // API 요청 URL 생성
        String url = BASE_URL + "/areaCode"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&_type="+ TYPE;

        // _type 파라미터가 있는 경우 URL에 추가
        if (_type != null) {
            url += "&_type=" + _type;
        }
        // areaCode 파라미터가 있는 경우 URL에 추가
        if (areaCode != null) {
            url += "&areaCode=" + areaCode;
        }


            URI uri = URI.create(url);

            // REST API 호출 및 결과 반환
            String response = restTemplate.getForObject(uri, String.class);
            return ResponseEntity.ok(response); // HTTP 응답으로 결과 반환 (상태 코드: 200 OK)

    }

    /**
     * 지역기반 관광정보 조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param listYN 목록 구분 (Y: 목록, N: 개수, 기본값: Y)
     * @param arrange 정렬 구분 (A: 제목순, B: 조회순, C: 수정일순, D: 생성일순, E: 거리순, 기본값: A)
     * @param contentTypeId 콘텐츠 타입 ID
     * @param areaCode 지역 코드
     * @param sigunguCode 시군구 코드
     * @param cat1 대분류 카테고리
     * @param cat2 중분류 카테고리
     * @param cat3 소분류 카테고리
     * @param modifiedtime 수정일
     * @param _type 응답 타입
     * @return 지역기반 관광정보 조회 결과 (JSON 형태)
     */
    @Operation(summary = "지역코드 조회", description = "반려 동물 동반여행지의 지역 및 시군구를 기반으로 관광정보 \n" +
            "목록을 조회하는 기능입니다.\n" +
            "파라미터에 따라 제목순, 수정일순(최신순), 등록일순 정렬검색을 \n" +
            "제공합니다.\n")
    @GetMapping("/areaBasedList") // GET 요청 처리, "/areaBasedList" 경로
    public ResponseEntity<String> getAreaBasedList(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false, defaultValue = "Y") String listYN,
            @RequestParam(required = false, defaultValue = "A") String arrange,
            @RequestParam(required = false) String contentTypeId,
            @RequestParam(required = false) String areaCode,
            @RequestParam(required = false) String sigunguCode,
            @RequestParam(required = false) String cat1,
            @RequestParam(required = false) String cat2,
            @RequestParam(required = false) String cat3,
            @RequestParam(required = false) String modifiedtime,
            @RequestParam(required = false) String _type
    ) {

        // 서비스 키를 URL 인코딩, 인코딩된 키를 사용하므로 주석처리
        //String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        // API 요청 URL 생성
        log.info("Used Service Key in getAreaBasedList: {}", serviceKey); // serviceKey 값 로깅
        String url = BASE_URL + "/areaBasedList"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&listYN=" + listYN
                + "&arrange=" + arrange
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp;

        // 각 파라미터가 있는 경우 URL에 추가
        if (contentTypeId != null) url += "&contentTypeId=" + contentTypeId;
        if (areaCode != null) url += "&areaCode=" + areaCode;
        if (sigunguCode != null) url += "&sigunguCode=" + sigunguCode;
        if (cat1 != null) url += "&cat1=" + cat1;
        if (cat2 != null) url += "&cat2=" + cat2;
        if (cat3 != null) url += "&cat3=" + cat3;
        if (modifiedtime != null) url += "&modifiedtime=" + modifiedtime;
        if (_type != null) url += "&_type=" + _type;

        URI uri = URI.create(url);
        log.info("Service Key: {}", serviceKey); // serviceKey 값 로깅
        // REST API 호출 및 결과 반환
        String response = restTemplate.getForObject(uri, String.class);
        log.info("uri: {}", uri); // serviceKey 값 로깅
        return ResponseEntity.ok(response); // HTTP 응답으로 결과 반환 (상태 코드: 200 OK)
    }

    /**
     * 위치기반 관광정보 조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param listYN 목록 구분 (Y: 목록, N: 개수, 기본값: Y)
     * @param arrange 정렬 구분 (A: 제목순, C: 수정일순, D: 생성일순, E: 거리순, O: 제목순(대표이미지有), Q: 수정일순(대표이미지有), R: 생성일순(대표이미지有), S: 거리순(대표이미지有), 기본값: C)
     * @param contentTypeId 관광타입 ID
     * @param mapX X좌표 (필수)
     * @param mapY Y좌표 (필수)
     * @param radius 거리 반경 (필수)
     * @param modifiedtime 수정일
     * @param _type 응답 타입
     * @return 위치기반 관광정보 조회 결과 (JSON 형태)
     */
    @GetMapping("/locationBasedList")
    @Operation(summary = "지역기반 관광정보 조회", description = "반려 동물 동반여행지의 주변 좌표를 기반으로 관광정보 \n" +
            "목록을 조회하는 기능입니다.\n" +
            "파라미터에 따라 제목순, 수정일순(최신순), 등록일순, 거리순 정렬검색을 \n" +
            "제공합니다.\n.")
    public ResponseEntity<String> getLocationBasedList(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false, defaultValue = "Y") String listYN,
            @RequestParam(required = false, defaultValue = "C") String arrange,
            @RequestParam(required = false) String contentTypeId,
            @RequestParam(required = true) String mapX,
            @RequestParam(required = true) String mapY,
            @RequestParam(required = true) int radius,
            @RequestParam(required = false) String modifiedtime,
            @RequestParam(required = false) String _type
    ) {
        // API 요청 URL 생성
        String url = BASE_URL + "/locationBasedList"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&listYN=" + listYN
                + "&arrange=" + arrange
                + "&mapX=" + mapX
                + "&mapY=" + mapY
                + "&radius=" + radius;

        // 각 파라미터가 있는 경우 URL에 추가
        if (contentTypeId != null) url += "&contentTypeId=" + contentTypeId;
        if (modifiedtime != null) url += "&modifiedtime=" + modifiedtime;
        if (_type != null) url += "&_type=" + _type;
        else url+= "&_type="+TYPE;

        URI uri = URI.create(url);

        // REST API 호출 및 결과 반환
        String response = restTemplate.getForObject(uri, String.class);
        return ResponseEntity.ok(response);
    }

    /**
     * 키워드검색조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param listYN 목록 구분 (Y: 목록, N: 개수, 기본값: Y)
     * @param arrange 정렬 구분 (A: 제목순, C: 수정일순, D: 생성일순, O: 제목순(대표이미지有), Q: 수정일순(대표이미지有), R: 생성일순(대표이미지有), 기본값: C)
     * @param contentTypeId 관광타입 ID
     * @param areaCode 지역 코드
     * @param sigunguCode 시군구 코드
     * @param cat1 대분류 코드
     * @param cat2 중분류 코드
     * @param cat3 소분류 코드
     * @param keyword 요청 키워드 (필수)
     * @param _type 응답 타입
     * @return 키워드검색조회 결과 (JSON 형태)
     */
    @GetMapping("/searchKeyword")
    @Operation(summary = "키워드 검색 조회", description = "반려 동물 동반여행지를 키워드로 검색을 하여 관광타입별 또는 \n" +
            "전체 목록을 조회하는 기능입니다.\n" +
            "파라미터에 따라 제목순, 수정일순(최신순), 등록일순 정렬검색을 \n" +
            "제공합니다.\n반려 동물 동반여행지를 키워드로 검색을 하여 관광타입별 또는 \n" +
            "전체 목록을 조회하는 기능입니다.\n" +
            "파라미터에 따라 제목순, 수정일순(최신순), 등록일순 정렬검색을 \n" +
            "제공합니다.\n")
    public ResponseEntity<String> searchKeyword(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false, defaultValue = "Y") String listYN,
            @RequestParam(required = false, defaultValue = "C") String arrange,
            @RequestParam(required = false) String contentTypeId,
            @RequestParam(required = false) String areaCode,
            @RequestParam(required = false) String sigunguCode,
            @RequestParam(required = false) String cat1,
            @RequestParam(required = false) String cat2,
            @RequestParam(required = false) String cat3,
            @RequestParam(required = true) String keyword,
            @RequestParam(required = false) String _type
    ) {

        // API 요청 URL 생성
        String url = BASE_URL + "/searchKeyword"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&listYN=" + listYN
                + "&arrange=" + arrange;
        log.info("Used Service Key in getAreaBasedList: {}", url); // serviceKey 값 로깅

        // 각 파라미터가 있는 경우 URL에 추가
        if (contentTypeId != null) url += "&contentTypeId=" + contentTypeId;
        if (areaCode != null) url += "&areaCode=" + areaCode;
        if (sigunguCode != null) url += "&sigunguCode=" + sigunguCode;
        if (cat1 != null) url += "&cat1=" + cat1;
        if (cat2 != null) url += "&cat2=" + cat2;
        if (cat3 != null) url += "&cat3=" + cat3;

        // 키워드는 UTF-8로 인코딩
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        url += "&keyword=" + encodedKeyword;

        if (_type != null) url += "&_type=" + _type;
        else url += "&_type="+TYPE;

        URI uri = URI.create(url);
        // REST API 호출 및 결과 반환
        String response = restTemplate.getForObject(uri, String.class);
        log.info("Used Service Key in getAreaBasedList: {}", url); // serviceKey 값 로깅
        return ResponseEntity.ok(response);
    }

    /**
     * 이미지 정보 조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param _type 응답 타입 (_type 파라미터로 전달, 없을 시 기본값 json)
     * @param contentId 콘텐츠 ID (필수)
     * @param imageYN 이미지 조회 여부 (Y: 콘텐츠 이미지 조회, N: 음식점 메뉴 이미지, 기본값: Y)
     * @return 이미지 정보 조회 결과 (JSON 형태)
     */
    @GetMapping("/detailImage")
    @Operation(summary = "이미지 정보 조회", description = "반려 동물 동반여행지의 각 관광타입(관광지, 숙박 등)에 해당하는 \n" +
            "이미지URL 목록을 조회하는 기능입니다.\n contentId 값은 필수 입력값입니다.")
    public ResponseEntity<String> getDetailImage(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String _type,
            @RequestParam String contentId,
            @RequestParam(required = false, defaultValue = "Y") String imageYN
    ) {
        try {
            // API 요청 URL 생성
            String url = BASE_URL + "/detailImage"
                    + "?serviceKey=" + serviceKey
                    + "&numOfRows=" + numOfRows
                    + "&pageNo=" + pageNo
                    + "&MobileOS=" + mobileOS
                    + "&MobileApp=" + mobileApp
                    + "&contentId=" + contentId
                    + "&imageYN=" + imageYN;

            // _type 파라미터가 있는 경우 URL에 추가
            if (_type != null) {
                url += "&_type=" + _type;
            } else {
                url += "&_type=" + TYPE; // 기본값으로 json 설정
            }

            URI uri = URI.create(url);

            // REST API 호출 및 결과 반환
            String response = restTemplate.getForObject(uri, String.class);
            return ResponseEntity.ok(response); // HTTP 응답으로 결과 반환 (상태 코드: 200 OK)

        } catch (Exception e) {
            log.error("API call error: ", e);
            return ResponseEntity.badRequest().body("API 호출 중 오류가 발생했습니다."); // 오류 발생 시 HTTP 응답 (상태 코드: 400 Bad Request)
        }
    }

    /**
     * 반려동물 동반여행 상세 정보 조회 API
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param numOfRows 한 페이지 결과 수 (기본값: 10)
     * @param _type 응답 타입 (_type 파라미터로 전달, 없을 시 기본값 json)
     * @param contentId 콘텐츠 ID (필수)
     * @return 반려동물 동반여행 상세 정보 조회 결과 (JSON 형태)
     */
    @Operation(summary = "반려동물 동반 여행 상세 정보 조회", description = "반려동물 동반여행 상세정보를 조회하는 기능입니다.\n" +
            "반려동물 동반여행 항목을 제공합니다.\n contentId 값은 필수 입력값입니다.")
    @GetMapping("/detailPetTour")
    public ResponseEntity<String> getDetailPetTour(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String _type,
            @RequestParam String contentId
    ) {
        try {
            // API 요청 URL 생성
            String url = BASE_URL + "/detailPetTour"
                    + "?serviceKey=" + serviceKey
                    + "&numOfRows=" + numOfRows
                    + "&pageNo=" + pageNo
                    + "&MobileOS=" + mobileOS
                    + "&MobileApp=" + mobileApp
                    + "&contentId=" + contentId;

            // _type 파라미터가 있는 경우 URL에 추가
            if (_type != null) {
                url += "&_type=" + _type;
            } else {
                url += "&_type=" + TYPE; // 기본값으로 json 설정
            }

            URI uri = URI.create(url);

            // REST API 호출 및 결과 반환
            String response = restTemplate.getForObject(uri, String.class);
            return ResponseEntity.ok(response); // HTTP 응답으로 결과 반환 (상태 코드: 200 OK)

        } catch (Exception e) {
            log.error("API call error: ", e);
            return ResponseEntity.badRequest().body("API 호출 중 오류가 발생했습니다."); // 오류 발생 시 HTTP 응답 (상태 코드: 400 Bad Request)
        }
    }
}
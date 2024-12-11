
  <img src="https://github.com/user-attachments/assets/fb531d55-e879-4701-9838-02920ae65939" alt="이미지 설명" width="350">


## 💡 서비스 소개  
> '**말랑플레이스**' 는 반려동물을 키우는 반려인들을 위한 위치 기반 커뮤니티 서비스입니다.
> 지도를 기반으로 한 직관적인 인터페이스를 통해 내 주변의 반려동물 관련 정보를 손쉽게 확인하고 공유할 수 있습니다.
> 특히 사용자들이 자유롭게 생성하는 '**글타래**' 기능을 통해 실시간으로 지역 정보를 공유하고, 이웃들과 소통할 수 있는 새로운 형태의 반려동물 커뮤니티를 제공합니다.


#### 개발 기간 <br> 
> 2024/11/15 ~ 2024/12/10

## 기능 목록

- **글타래**: 말랑맵에 표기된 마커들을 클릭하면 실종/구조/장소로 분류된 글타래, 사용자가 자유롭게 작성가능합니다.
- **커뮤니티**: 말랑플렝이스의 게시판, 사용자들이 자유롭게 소통하고 커뮤니티를 형성할 수 있습니다.
- **자유로운 1:1 채팅 기능**: 다른 사용자와 자유롭게 실시간으로 대화가능합니다.


## 기술 스택

### Backend <br/>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.3.4-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA%203.3.2-6DB33F?style=flat-square&logo=&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring%20Security&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON-Web-Tokens&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white)
![Socket.io](https://img.shields.io/badge/Socket.io-black?style=flat-square&logo=socket.io&badgeColor=010101)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=flat-square&logo=Thymeleaf&logoColor=white)

### DB / Infra
![MySQL](https://img.shields.io/badge/MySQL%208.0.39-4479A1?style=flat-square&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-0db7ed?style=flat-square&logo=Docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white)

### 성능테스트
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=flat-square&logo=Grafana&logoColor=white)


### 문서/협업툴
![Notion](https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=discord&logoColor=white)

## 구조

전체 시스템 구조 - 사용자


전체 시스템 구조 - 관리자

<details>
  <summary> 패키지 구조 </summary>
  
```
📦 
├─ .gitattributes
├─ .github
│  ├─ ISSUE_TEMPLATE
│  │  └─ 📋-general-issue.md
│  ├─ PULL_REQUEST_TEMPLATE
│  └─ workflows
│     ├─ CICD.yml
│     └─ CICDdevelop.yml
├─ .gitignore
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ mallangs
│  │  │        ├─ MallangsApplication.java
│  │  │        ├─ domain
│  │  │        │  ├─ article
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  ├─ ArticleController.java
│  │  │        │  │  │  └─ TourApiController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  ├─ ArticleCreateRequest.java
│  │  │        │  │  │  │  ├─ LostCreateRequest.java
│  │  │        │  │  │  │  ├─ MapBoundsRequest.java
│  │  │        │  │  │  │  ├─ PlaceCreateRequest.java
│  │  │        │  │  │  │  └─ RescueCreateRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     ├─ ArticlePageResponse.java
│  │  │        │  │  │     ├─ ArticleResponse.java
│  │  │        │  │  │     ├─ LostResponse.java
│  │  │        │  │  │     ├─ MapBoundsResponse.java
│  │  │        │  │  │     ├─ PlaceResponse.java
│  │  │        │  │  │     └─ RescueResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ Article.java
│  │  │        │  │  │  ├─ ArticleType.java
│  │  │        │  │  │  ├─ CaseStatus.java
│  │  │        │  │  │  ├─ LostArticle.java
│  │  │        │  │  │  ├─ MapVisibility.java
│  │  │        │  │  │  ├─ PlaceArticle.java
│  │  │        │  │  │  └─ RescueArticle.java
│  │  │        │  │  ├─ factory
│  │  │        │  │  │  ├─ ArticleFactory.java
│  │  │        │  │  │  ├─ ArticleFactoryManager.java
│  │  │        │  │  │  ├─ LostArticleFactory.java
│  │  │        │  │  │  ├─ PlaceArticleFactory.java
│  │  │        │  │  │  └─ RescueArticleFactory.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  ├─ ArticleRepository.java
│  │  │        │  │  │  ├─ JdbcLocationRepository.java
│  │  │        │  │  │  ├─ LocationRepository.java
│  │  │        │  │  │  └─ PlaceArticleRepository.java
│  │  │        │  │  ├─ service
│  │  │        │  │  │  ├─ ArticleService.java
│  │  │        │  │  │  ├─ LocationService.java
│  │  │        │  │  │  └─ PlaceArticleCsvService.java
│  │  │        │  │  └─ validation
│  │  │        │  │     └─ ValidationGroups.java
│  │  │        │  ├─ board
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  ├─ BoardController.java
│  │  │        │  │  │  └─ CategoryController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  ├─ AdminBoardStatusRequest.java
│  │  │        │  │  │  │  ├─ CategoryCreateRequest.java
│  │  │        │  │  │  │  ├─ CategoryUpdateRequest.java
│  │  │        │  │  │  │  ├─ CommunityCreateRequest.java
│  │  │        │  │  │  │  ├─ CommunityUpdateRequest.java
│  │  │        │  │  │  │  ├─ SightingCreateRequest.java
│  │  │        │  │  │  │  └─ SightingUpdateRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     ├─ AdminBoardResponse.java
│  │  │        │  │  │     ├─ AdminBoardsResponse.java
│  │  │        │  │  │     ├─ AdminCategoryResponse.java
│  │  │        │  │  │     ├─ BoardStatusCount.java
│  │  │        │  │  │     ├─ CategoryResponse.java
│  │  │        │  │  │     ├─ CommunityDetailResponse.java
│  │  │        │  │  │     ├─ CommunityListResponse.java
│  │  │        │  │  │     ├─ PageResponse.java
│  │  │        │  │  │     ├─ SightingDetailResponse.java
│  │  │        │  │  │     └─ SightingListResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ Board.java
│  │  │        │  │  │  ├─ BoardStatus.java
│  │  │        │  │  │  ├─ BoardType.java
│  │  │        │  │  │  ├─ Category.java
│  │  │        │  │  │  └─ CategoryStatus.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  ├─ BoardRepository.java
│  │  │        │  │  │  └─ CategoryRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     ├─ BoardService.java
│  │  │        │  │     └─ CategoryService.java
│  │  │        │  ├─ chat
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  ├─ ChatMessageController.java
│  │  │        │  │  │  ├─ ChatRoomController.java
│  │  │        │  │  │  ├─ WebSocketDocumentationController.java
│  │  │        │  │  │  └─ test
│  │  │        │  │  │     ├─ ChatViewControllerTest.java
│  │  │        │  │  │     └─ WebSocketControllerTest.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  ├─ ChatMessageRequest.java
│  │  │        │  │  │  │  ├─ ChatRoomChangeNameRequest.java
│  │  │        │  │  │  │  └─ UpdateChatMessageRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     ├─ ChatMessageDeleteSuccessResponse.java
│  │  │        │  │  │     ├─ ChatMessageListResponse.java
│  │  │        │  │  │     ├─ ChatMessageResponse.java
│  │  │        │  │  │     ├─ ChatMessageSuccessResponse.java
│  │  │        │  │  │     ├─ ChatMessageToDTOResponse.java
│  │  │        │  │  │     ├─ ChatRoomResponse.java
│  │  │        │  │  │     └─ ParticipatedRoomListResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ ChatMessage.java
│  │  │        │  │  │  ├─ ChatRoom.java
│  │  │        │  │  │  ├─ MessageType.java
│  │  │        │  │  │  └─ ParticipatedRoom.java
│  │  │        │  │  ├─ redis
│  │  │        │  │  │  └─ RedisSubscriber.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  ├─ ChatMessageRepository.java
│  │  │        │  │  │  ├─ ChatRoomRepository.java
│  │  │        │  │  │  └─ ParticipatedRoomRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     ├─ ChatMessageService.java
│  │  │        │  │     └─ ChatRoomService.java
│  │  │        │  ├─ comment
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  └─ CommentController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  ├─ CommentArticleRequest.java
│  │  │        │  │  │  │  ├─ CommentBoardRequest.java
│  │  │        │  │  │  │  ├─ CommentDeleteRequest.java
│  │  │        │  │  │  │  ├─ CommentPageRequest.java
│  │  │        │  │  │  │  └─ CommentUpdateRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     └─ CommentResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  └─ Comment.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  └─ CommentRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     └─ CommentService.java
│  │  │        │  ├─ image
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  └─ ImageController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  └─ ImageDto.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  └─ Image.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  └─ ImageRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     └─ ImageService.java
│  │  │        │  ├─ member
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  ├─ AddressController.java
│  │  │        │  │  │  ├─ AuthController.java
│  │  │        │  │  │  ├─ MemberAdminController.java
│  │  │        │  │  │  ├─ MemberUserController.java
│  │  │        │  │  │  ├─ Oauth2Controller.java
│  │  │        │  │  │  └─ test
│  │  │        │  │  │     └─ MemberFileTestPageController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ AddressCreateSuccessResponse.java
│  │  │        │  │  │  ├─ AddressDeleteSuccessResponse.java
│  │  │        │  │  │  ├─ MemberAddressRequest.java
│  │  │        │  │  │  ├─ MemberAddressResponse.java
│  │  │        │  │  │  ├─ MemberBanRequest.java
│  │  │        │  │  │  ├─ MemberCheckPasswordResponse.java
│  │  │        │  │  │  ├─ MemberCreateRequest.java
│  │  │        │  │  │  ├─ MemberFindPasswordRequest.java
│  │  │        │  │  │  ├─ MemberFindUserIdRequest.java
│  │  │        │  │  │  ├─ MemberGetRequestByEmail.java
│  │  │        │  │  │  ├─ MemberGetRequestByNickname.java
│  │  │        │  │  │  ├─ MemberGetRequestByUserId.java
│  │  │        │  │  │  ├─ MemberGetResponse.java
│  │  │        │  │  │  ├─ MemberGetResponseOnlyMember.java
│  │  │        │  │  │  ├─ MemberRegisterRequest.java
│  │  │        │  │  │  ├─ MemberSendMailResponse.java
│  │  │        │  │  │  ├─ MemberUpdateRequest.java
│  │  │        │  │  │  ├─ PageRequestDTO.java
│  │  │        │  │  │  ├─ PasswordDTO.java
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  └─ LoginRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     └─ MemberGetByOtherResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ Address.java
│  │  │        │  │  │  ├─ Member.java
│  │  │        │  │  │  ├─ MemberRole.java
│  │  │        │  │  │  └─ embadded
│  │  │        │  │  │     ├─ Email.java
│  │  │        │  │  │     ├─ Nickname.java
│  │  │        │  │  │     ├─ Password.java
│  │  │        │  │  │     └─ UserId.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  ├─ AddressRepository.java
│  │  │        │  │  │  └─ MemberRepository.java
│  │  │        │  │  ├─ service
│  │  │        │  │  │  ├─ AddressService.java
│  │  │        │  │  │  ├─ MemberAdminService.java
│  │  │        │  │  │  └─ MemberUserService.java
│  │  │        │  │  └─ util
│  │  │        │  │     ├─ GeometryUtil.java
│  │  │        │  │     └─ PasswordGenerator.java
│  │  │        │  ├─ notification
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  └─ NotificationController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ NotificationSendDTO.java
│  │  │        │  │  │  ├─ request
│  │  │        │  │  │  │  └─ NotificationRequest.java
│  │  │        │  │  │  └─ response
│  │  │        │  │  │     └─ NotificationResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ Notification.java
│  │  │        │  │  │  └─ NotificationType.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  └─ NotificationRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     └─ NotificationService.java
│  │  │        │  ├─ pet
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  └─ PetController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ PageRequest.java
│  │  │        │  │  │  ├─ PetCreateRequest.java
│  │  │        │  │  │  ├─ PetLocationRequest.java
│  │  │        │  │  │  ├─ PetMemberProfileResponse.java
│  │  │        │  │  │  ├─ PetNearbyPageResponse.java
│  │  │        │  │  │  ├─ PetNearbyResponse.java
│  │  │        │  │  │  ├─ PetPageResponse.java
│  │  │        │  │  │  ├─ PetResponse.java
│  │  │        │  │  │  └─ PetUpdateRequest.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ Pet.java
│  │  │        │  │  │  ├─ PetGender.java
│  │  │        │  │  │  └─ PetType.java
│  │  │        │  │  ├─ repository
│  │  │        │  │  │  └─ PetRepository.java
│  │  │        │  │  └─ service
│  │  │        │  │     └─ PetService.java
│  │  │        │  └─ review
│  │  │        │     ├─ controller
│  │  │        │     │  └─ ReviewController.java
│  │  │        │     ├─ dto
│  │  │        │     │  ├─ PageRequest.java
│  │  │        │     │  ├─ ReviewCreateRequest.java
│  │  │        │     │  ├─ ReviewInfoResponse.java
│  │  │        │     │  ├─ ReviewPageResponse.java
│  │  │        │     │  └─ ReviewUpdateRequest.java
│  │  │        │     ├─ entity
│  │  │        │     │  ├─ Review.java
│  │  │        │     │  └─ ReviewStatus.java
│  │  │        │     ├─ repository
│  │  │        │     │  └─ ReviewRepository.java
│  │  │        │     └─ service
│  │  │        │        └─ ReviewService.java
│  │  │        ├─ global
│  │  │        │  ├─ advice
│  │  │        │  │  └─ GlobalExceptionHandler.java
│  │  │        │  ├─ common
│  │  │        │  │  └─ BaseTimeEntity.java
│  │  │        │  ├─ config
│  │  │        │  │  ├─ EmbeddedRedisConfig.java
│  │  │        │  │  ├─ GeometryConfig.java
│  │  │        │  │  ├─ MvcConfig.java
│  │  │        │  │  ├─ RedisCacheConfig.java
│  │  │        │  │  ├─ RedisConfig.java
│  │  │        │  │  ├─ RestTemplateConfig.java
│  │  │        │  │  ├─ SecurityBeansConfig.java
│  │  │        │  │  ├─ SecurityConfig.java
│  │  │        │  │  ├─ SwaggerConfig.java
│  │  │        │  │  └─ WebSocketConfig.java
│  │  │        │  ├─ exception
│  │  │        │  │  ├─ ErrorCode.java
│  │  │        │  │  ├─ ErrorResponse.java
│  │  │        │  │  └─ MallangsCustomException.java
│  │  │        │  ├─ handler
│  │  │        │  │  ├─ SseEmitters.java
│  │  │        │  │  ├─ StompFileHandler.java
│  │  │        │  │  └─ StompHandler.java
│  │  │        │  ├─ jwt
│  │  │        │  │  ├─ controller
│  │  │        │  │  │  └─ TokenController.java
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ TokensRequest.java
│  │  │        │  │  │  └─ TokensResponse.java
│  │  │        │  │  ├─ entity
│  │  │        │  │  │  ├─ CustomMemberDetails.java
│  │  │        │  │  │  └─ TokenCategory.java
│  │  │        │  │  ├─ filter
│  │  │        │  │  │  ├─ JWTFilter.java
│  │  │        │  │  │  ├─ LoginFilter.java
│  │  │        │  │  │  └─ LogoutFilter.java
│  │  │        │  │  ├─ handler
│  │  │        │  │  │  └─ CustomAuthenticationFailureHandler.java
│  │  │        │  │  ├─ service
│  │  │        │  │  │  ├─ AccessTokenBlackList.java
│  │  │        │  │  │  ├─ CustomerMemberDetailService.java
│  │  │        │  │  │  └─ RefreshTokenService.java
│  │  │        │  │  └─ util
│  │  │        │  │     └─ JWTUtil.java
│  │  │        │  ├─ oauth2
│  │  │        │  │  ├─ dto
│  │  │        │  │  │  ├─ CustomOAuth2Member.java
│  │  │        │  │  │  └─ MemberOAuth2DTO.java
│  │  │        │  │  ├─ handler
│  │  │        │  │  │  ├─ CustomFailureHandler.java
│  │  │        │  │  │  └─ CustomSuccessHandler.java
│  │  │        │  │  ├─ response
│  │  │        │  │  │  ├─ GoogleResponse.java
│  │  │        │  │  │  ├─ NaverResponse.java
│  │  │        │  │  │  └─ OAuth2Response.java
│  │  │        │  │  └─ service
│  │  │        │  │     └─ CustomOAuth2MemberService.java
│  │  │        │  ├─ s3
│  │  │        │  │  ├─ S3Config.java
│  │  │        │  │  ├─ S3Controller.java
│  │  │        │  │  └─ S3Service.java
│  │  │        │  └─ schedule
│  │  │        │     └─ CustomTaskScheduler.java
│  │  │        └─ web
│  │  │           └─ HomeController.java
│  │  └─ resources
│  │     ├─ application-dev.properties
│  │     ├─ messages.properties
│  │     └─ templates
│  │        └─ websocket_test.html
│  └─ test
│     └─ java
│        └─ com
│           └─ mallangs
│              ├─ MallangsApplicationTests.java
│              └─ domain
│                 ├─ article
│                 │  ├─ repository
│                 │  │  └─ ArticleRepositoryTest.java
│                 │  └─ service
│                 │     └─ ArticleServiceTest.java
│                 ├─ board
│                 │  └─ repository
│                 │     ├─ BoardRepositoryTest.java
│                 │     └─ CategoryRepositoryTest.java
│                 ├─ chat
│                 │  ├─ repository
│                 │  │  ├─ ChatRoomRepositoryTest.java
│                 │  │  ├─ ChatRoomRepositoryTests.java
│                 │  │  └─ ParticipatedRoomRepositoryTest.java
│                 │  └─ service
│                 │     ├─ ChatMessageServiceTests.java
│                 │     └─ ChatRoomServiceTests.java
│                 ├─ member
│                 │  ├─ repository
│                 │  │  ├─ AddressRepositoryTests.java
│                 │  │  └─ MemberRepositoryTests.java
│                 │  └─ service
│                 │     ├─ AddressServiceTests.java
│                 │     └─ MemberServiceTests.java
│                 ├─ pet
│                 │  ├─ repository
│                 │  │  └─ PetRepositoryTest.java
│                 │  └─ service
│                 │     └─ PetServiceTest.java
│                 └─ review
│                    ├─ repository
│                    │  └─ ReviewRepositoryTest.java
│                    └─ service
│                       └─ ReviewServiceTest.java
└─ upload
   ├─ baa22335-a605-4d1e-8735-ea775a4bd056_스크린샷 2024-08-07 165513.png
   └─ s_baa22335-a605-4d1e-8735-ea775a4bd056_스크린샷 2024-08-07 165513.png

```
</details>


<details> 
<summary>펫</summary>
</details>

<details>
<summary>글타래</summary>
</details>

<details>
<summary>커뮤니티</summary>
</details>

<details>
<summary>게시판</summary>
</details>

<details>
<summary>댓글</summary>
</details>

<details>
<summary>리뷰</summary>
</details>

<details>
<summary>알림</summary>
</details>

<details>
<summary>채팅</summary>
</details>

<details>
<summary>서버 아키텍쳐 </summary>

  ![image](https://github.com/user-attachments/assets/09f96ad4-88cb-411d-b80a-7facf78797c1)

  
</details>

## 시스템 설계 및 다이어그램

<details>
<summary>ERD</summary>
</details>

<details>
<summary>와이어 프레임</summary>
</details>

<details>
<summary>유저 스토리</summary>
  
  ![image](https://github.com/user-attachments/assets/5948ab5e-8ffb-4892-ab95-ca0027405335)

</details>

<details>
<summary>흐름도</summary>
  
  ![Untitled diagram-2024-12-11-014121](https://github.com/user-attachments/assets/e64847d1-7a45-4930-9115-a86d8ececd68)


</details>


## 프로젝트 협업 규칙

<details>
<summary>Convention </summary>


### 깃 전략  

#### Feature-Branch 전략(GitHub Flow)  
![image](https://github.com/user-attachments/assets/96903560-e01e-4d17-8219-2cf187dea064)

### Branch 관리  

#### Main Branch  
- 배포 브랜치, 운영 서버  
- 직접적인 PUSH **금지**  
- `develop` → `main` Pull Request만 허용  

#### Develop Branch  
- 개발 통합 브랜치: 다음 배포 버전을 위한 개발 코드 통합  
- 기능 개발이 완료된 `feature` 브랜치들의 병합 지점  
- QA/테스트 진행 시 기본 브랜치  

#### Feature Branch (branch명: feature/기능명)  
- 기능 개발 작업용 브랜치  
- **Issue 생성 → Branch 생성 → 개발 → PR 요청 → 코드 리뷰 → Merge**  


### Merge 방식  

- 마지막 승인자(Merge Approver)가 머지 수행  
- `feature` 브랜치: 1명 이상 승인 시  
- `develop` 브랜치: 모든 팀원 승인 시  


### Git Convention  

💡 **프로세스:**  
- Issue 생성 → 브랜치 생성 → 해당 브랜치 이동 → `develop` Pull → 커밋 → PR 생성  

🚨 **주의사항:**  
- **커밋 메시지 템플릿**을 반드시 지킬 것  
- **충돌 발생 주의**  
- **main 브랜치에 직접 PR 금지** (`develop`으로만 PR 가능)  


### [type] 커밋 메시지 형식  

- `feat:` 새로운 기능 구현  
- `mod:` 코드 및 파일 수정  
- `add:` 라이브러리 추가 및 코드 추가  
- `del:` 불필요한 코드/파일 삭제  
- `fix:` 버그 및 오류 해결  
- `ui:` UI 관련 작업  
- `chore:` 작은 작업 (버전 관리 등)  
- `hotfix:` 긴급 배포 수정  
- `rename:` 파일 및 폴더명 수정  
- `docs:` 문서 작업  
- `refactor:` 코드 리팩토링  
- `merge:` 브랜치 병합  
- `comment:` 주석 추가 및 변경

### Code Convention

```java
//도메인 지정
private Long memberId;

private Long userId; 

//엔티티 수정 메서드 : chanegeMethod
public void changeName(String name){
    this.name = name;
}

//엔티티 수정 메서드 : chanegeMethod
public void changeWeight(Integer weight){
    this.weight = weight;
}

domainId로 통일
```

  </details>




## 팀원 소개

<table>
  <tr>
    <td>
        <a href="https://github.com/username0w">
            <img src="https://avatars.githubusercontent.com/u/163955522?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/iam52">
            <img src="https://avatars.githubusercontent.com/u/131854898?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/Dom1046">
            <img src="https://avatars.githubusercontent.com/u/173169283?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/HanJae-Jae">
            <img src="https://avatars.githubusercontent.com/u/177859651?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/DongWooKim4343">
            <img src="https://avatars.githubusercontent.com/u/106728608?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr>
    <td><b>강수민</b></td>
    <td><b>오익수</b></td>
    <td><b>김동현</b></td>
    <td><b>한재재</b></td>
    <td><b>김동우</b></td>
  </tr>
  <tr>
    <td><b>글타래 <br /></b></td>
    <td><b>커뮤니티, 이미지 <br />명세서 관리</b></td>
    <td><b>채팅, 회원 <br /></b></td>
    <td><b>댓글, 알림 <br /></b></td>
    <td><b>펫, 리뷰, 공공데이터 DB 적재</td>
  </tr>
</table>

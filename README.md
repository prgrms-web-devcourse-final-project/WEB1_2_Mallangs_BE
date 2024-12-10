<p align="center">
  <img src="https://github.com/user-attachments/assets/fb531d55-e879-4701-9838-02920ae65939" alt="이미지 설명" width="350">
</p>

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

![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.3.4-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA%203.3.2-6DB33F?style=for-the-badge&logo=&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON-Web-Tokens&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white)
![Socket.io](https://img.shields.io/badge/Socket.io-black?style=for-the-badge&logo=socket.io&badgeColor=010101)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

### DB / Infra
![MySQL](https://img.shields.io/badge/MySQL%208.0.39-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-0db7ed?style=for-the-badge&logo=Docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

### 성능테스트
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=Grafana&logoColor=white)


### 문서/협업툴
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-4A154B?style=for-the-badge&logo=intellijidea&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)

## 구조

전체 시스템 구조 - 사용자


전체 시스템 구조 - 관리자



<summary>펫</summary>

<summary>글타래</summary>

<summary>커뮤니티</summary>

<summary>게시판</summary>

<summary>댓글</summary>

<summary>리뷰</summary>

<summary>알림</summary>

<summary>채팅</summary>

## 다이어그램

<summary>ERD</summary>

<summary>와이어 프레임</summary>

<summary>유저 스토리</summary>
  
<summary>API 명세서</summary> 

## 프로젝트 협업 규칙



### [협업 방식]

## 📅 주기적인 팀 일정
**<월~금>**  

- 🕘 **오전 팀장 미팅:** 9:00am  
- 🕔 **오후 전체 미팅:** 5:00pm  


## 🤝 협업 툴  

- Notion  
- GitHub  
- Zoom  
- Figma  
- Discord  


## 📋 API 문서화 프로세스  

### API 설계 단계  

- **Notion을 활용한 초기 API 명세서 작성**  
  - Endpoint 정의  
  - Request/Response 스키마  

### 배포 및 연동 단계  

- **Swagger/OpenAPI를 통한 자동화된 문서 관리**  
  - API 변경사항 실시간 반영  
  - Request/Response 스키마 동기화  
  - Status Code 자동화  
  - 필수 파라미터 및 제약 조건 자동화  

### API 연동 방식  

1. **로컬 개발 환경 연동**  
   - 백엔드 프로젝트 빌드 파일 로컬 다운로드  
   - 로컬 환경에서 API 서버 구동  

2. **AWS 프리티어 계정 활용**  
   - 개인 AWS 계정으로 API 서버 배포  

3. **IAM 계정 연동**  
   - 그렙 IAM 계정 발급 후 사용  


## 🔀 깃 전략  

### 프론트와 백엔드 분리된 GitHub 저장소 운영  

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

### 코드 리뷰  

- 최소 2일에 한 번 코드 리뷰  
- 모든 팀원이 참여  


## 🧪 테스트 일정  

- **프론트 공개 테스트:** 최소 1회  
- **백엔드 공개 테스트:** 최소 1회  
- **프론트 + 백엔드 중간/최종 테스트:** 각 1회  


## 📚 Git / 코드 컨벤션  

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


### 패키지 구조
````
domain
├── member
│ ├── dto
│ ├── entity
│ ├── service
│ ├── controller
│ └── repository
├── pet
├── article
├── review
├── comment
├── report
└── notification
````



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
    <td><b>커뮤니티, 이미지 <br />ngrok 스웨거</b></td>
    <td><b>채팅, 회원 <br /></b></td>
    <td><b>댓글, 알림 <br /></b></td>
    <td><b>펫, 리뷰, 공공데이터<br />배포, S3</b></td>
  </tr>
</table>

# 소셜 미디어 통합 Feed 서비스
![bgimg](https://drive.google.com/uc?export=view&id=1FPwKX0OXlH0NaelkLNx1JYn764DUZpFr)
## 팀 소개
<div align="center">

| <img src="https://drive.google.com/uc?export=view&id=1zV9DywkNWbgT5dJIuMNNHMfft0GnkoDU" width="140" height="140"> | <img src="https://drive.google.com/uc?export=view&id=1xZq17TkXxbKIMou_1N8HI5jJ1hGuKmD4" width="140" height="140"> | <img src="https://drive.google.com/uc?export=view&id=1W6rZe96xwdXJeNULFtXOm8Iip6tzN0B6" width="140" height="140"> | <img src="https://drive.google.com/uc?export=view&id=1fBa0aPyXRkrijdG6o3RQcj5ahm_dSktb" width="140" height="140"> |  
|------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|  
| Back-End                                                                                                   | Back-End                                                                                                        | Back-End                                                                                                                                  | Back-End                                                                                                        |                                                                                                 |
| [이준규](https://github.com/junkyu92)                                                                         | [이현정](https://github.com/12hyeon)                                                                               | [최승호](https://github.com/madst0614)                                                                                                    | [조현수](https://github.com/HyunsooZo)                                                                            |

</div>

## 목차
- [개요](#개요)
- [사용기술](#사용기술)
- [느낀 점](#느낀-점)
- [API 문서](#API-문서)
- [구현 기능](#구현기능)
- [시스템 구성도](#시스템-구성도)
- [ERD](#ERD)



## 개요

본 서비스는 유저 계정의 해시태그(`#kim_wanted`) 를 기반으로 `Instagram`, `Thread`, `Facebook`, `Twitter`등 <br>
복수의 SNS에 게시된 게시물 중 유저의 해시태그가 포함된 게시물들을 하나의 서비스에서 확인할 수 있는  <br>
통합 Feed 어플리케이션 입니다. 이를 통해 본 서비스의 고객은 하나의 채널로 유저(`#kim_wanted`) <br>
또는 브랜드(`#nike`) 의 SNS 노출 게시물 및 통계를 확인할 수 있습니다.
<br/>


## 프로젝트 관리 및 회고
[![Notion](https://img.shields.io/badge/Notion_문서로_확인하기_(클릭!)-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)](https://www.notion.so/75c5a817b6b24a98850ff8ca17a8f929?v=8562a6e059c64b7a86d03497f5012cb3&pvs=4)


## 사용기술

#### 개발환경
<img src="https://img.shields.io/badge/java-007396?&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring boot-6DB33F?&logo=Spring boot&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/MariaDB-003545?&logo=mariaDB&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Spring JPA-6DB33F?&logo=Spring JPA&logoColor=white"> <img src="https://img.shields.io/badge/querydsl-2599ED?&logo=querydsl&logoColor=white">  <img src="https://img.shields.io/badge/SMTP-CC0000?&logo=Gmail&logoColor=white">
<br>
<img src="https://img.shields.io/badge/AssertJ-25A162?&logo=AssertJ&logoColor=white"> <img src="https://img.shields.io/badge/Mockito-008D62?&logo=Mockito&logoColor=white">
<br>
<img src="https://img.shields.io/badge/intellijidea-000000?&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/postman-FF6C37?&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?&logo=swagger&logoColor=white">

#### 배포환경
<image src="https://img.shields.io/badge/Docker-2496ED?&logo=Docker&logoColor=white"><img src="https://img.shields.io/badge/aws-232F3E?&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/ec2-FF9900?&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/rds-527FFF?&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/ElasticCache-201d90?&logo=amazonelasticcache&logoColor=white" alt="actions">
<br>
<img src="https://img.shields.io/badge/github-181717?&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Jenkins-2088FF?&logo=Jenkins&logoColor=white" alt="actions">

#### 협업도구
<img src="https://img.shields.io/badge/discord-4A154B?&logo=discord&logoColor=white"> <img src="https://img.shields.io/badge/notion-000000?&logo=notion&logoColor=white">
<br/>

## 느낀 점
이번 프로젝트는 게시글 기능과 통계 기능을 맡아 구현했다.첫 팀 프로젝트 였기에 배울 것이 너무 많았다.우선 기존에 쓰던 이클립스 IDE에서 IntelliJ IDEA를 쓰기 시작했고, main 브랜치만 쓰던 Github 방식을 바꿔야 했다.  
또한 처음 JPA를 써봐야 했고 두 개 이상의 테이블을 조인해서 쿼리를 짜야기 했기에 QueryDSL까지 학습 해야 했다. 우선 하루는 IntelliJ와 깃헙을 사용하기 위해 관련 정보를 찾아 봤고 개발 환경을 세팅했다. 그리고 다음 날부터 JPA 기초를 찾아 봤고 개념이 좀 와닿을때쯤
QueryDSL에 대해서 찾아봤고 학습했다. 충분히 관련 정보를 찾아본 후에 JPA와 QueryDSL를 이용해서 게시글 기능과 통계 기능을 아슬아슬하게 기한 내에 제출 했다.  
N+1 문제 등 JPA와 QueryDSL를 사용했을 때 조심해야할 사항에 대해 충분히 숙지하고 구현하고 싶었지만, 5일 내에 JPA와 QueryDSL을 배우고 관련 기능을 구현하는 것은 너무 어려웠다. 통계 기능 또한 성능을 고려하여 구현하지 못하고 아쉬움이 많은 프로젝트 였다.  
처음 팀 프로젝트를 진행하는 팀원을 배려해주고 기다려준 팀원들이 고마웠다.

## API 문서
[![Swagger](https://img.shields.io/badge/swagger_문서로_확인하기_(클릭!)-85EA2D?&logo=swagger&logoColor=white)](http://54.225.40.161:8090/swagger-ui/index.html#/)


| API Type        | Http Method| URL                                      | Description    |
|-----------------|-------------|------------------------------------------|----------------|
| **Auth API**    | POST | `/api/v1/auth/refresh`                   | 엑세스토큰 재발급      | 
| **User API**    | POST | `/api/v1/users/sign-up`                  | 회원가입           |
| **User API**    | PATCH | `/api/v1/users/verification`             | 회원가입 인증        |
| **User API**    | POST | `/api/v1/users/verification/otp-reissue` | OTP 재발급        |
| **User API**    | POST | `/api/v1/users/sign-in`                  | 로그인            |
| **User API**    | PATCH | `/api/v1/users/password/modify`          | 비밀번호 변경        |
| **User API**    | PATCH | `/api/v1/users/password/reset`           | 비밀번호 재설정       |
| **User API**    | POST | `/api/v1/users/sign-out`                 | 로그아웃           |
| **Posting API** | GET | `/api/v1/postings`                       | 조건 검색          |
| **Posting API** | GET | `/api/v1/postings/{id}`                  | 포스팅 상세 가져오기    |
| **Posting API** | PATCH | `/api/v1/postings/like/{id}`             | 포스팅 좋아요        |
| **Posting API** | PATCH | `/api/v1/postings/share/{id}`            | 포스팅 공유         |
| **Stats API**   | PATCH | `/api/v1/statistics/hashtags`            | 해시태그 통계        |
| **Log API**     | GET | `/api/v1/logs/hashtags`                  | hot hashtag 조회 |


## 구현기능

<details>
  <summary>회원가입 및 인증 기능</summary>

- **구현 기능** <br>
  사용자 회원가입 및 이메일 인증 기능을 구현했습니다.

- **구현 방법** <br>
- 이메일 형식 확인 -> `javax.validation`의 `@Email` 어노테이션 사용
- 계정 중복 확인 -> `UserRepository`조회하여 중복 시 예외 던짐
- 비밀번호 검증 -> 별도의 `ValidationUtil` 클래스 만들어 4개 케이스 검증

```  
  자주쓰는 비밀번호
  연속된 문자 혹은 숫자(3회이상)
  동일한 문자,숫자 반복 (3회이상)
  숫자문자특문 중 2가지 이상 포함)
  추가로 `javax.validation`의 `@size`사용하여 길이 검증 
```
- 회원가입 완료 시 인증메일 전송 (`java-mail-sender` 사용)
    - 멀티쓰레딩을 이용한 비동기처리로 응답 속도 향상 (`@Async`사용)
    - 메일로 보낸 OTP(6자리 난수)는 계정을 키, OTP를 값으로 하여 `redis` 에 저장 (10분 유효)
    - 사용자 상태는 미인증
- 인증번호,계정,비밀번호 입력 시 `redis`에서 찾아 비교 후 상태 업데이트 (인증)

- 추가로 인증번호(OTP) 재전송 기능 구현하여 메일서버 불안정에 대처할 수 있는 api구현
</details>

<details>
  <summary>로그인 및 SpringSecurity + JWT 토큰</summary>

- **구현 기능** <br>
    - SpringSecurity와 JWT , 그리고 로그인 기능 구현

- **구현 방법** <br>
    - 사용자 로그인 진행 시 엑세스/리프레시 토큰 각각 발급
    - 발급된 리프레시 토큰은 계정을 키로하여 `redis`에 저장하고
    - api호출 시점마다 헤더에서 `accessToken`을 추출해 JWT커스텀필터에서 검증 (Bearer 토큰 사용)
    - `accessToken`만료시 사용할수 있는 토큰 재발급 api를 호출하면
      `redis`에 저장되어있는 `refreshToken`과 비교, 토큰 유효성검증 후 유효할 경우 `accessToken`재발급하여 반환
    - `SpringSecurity`에서 사용되는 `UserDetails`데이터의 경우 `UserDetailsServiceImpl`로 `UserDetailsService`를 구현하여처리
    - 참고이미지(출처:본인)
      ![image](https://drive.google.com/uc?id=1YYiXNdWHMfrcpCIv2FUoLUIOJnuYXf_S)
</details>


<details>
  <summary>회원관리기능 (비밀번호 초기화 / 비밀번호 변경 / 로그아웃)</summary>

- **구현 기능** <br>
    - 회원 정보 관리기능을 구현했습니다.

- **구현 방법** <br>
    - 비밀번호 초기화
        - 비밀번호 분실 시 사용할 수 있는 API로 입력된 정보의 유효성을 검증한 뒤 난수 10자를 생성해 비밀번호를 업데이트한 뒤 해당 임시비밀번호를 이메일 전송모듈을 사용하여 이메일로 전송했습니다.
    - 비밀번호 변경
        - 본인의 기존 비밀번호와 신규비밀번호를 입력받아 기존비밀번호 일치 시 신규비밀번호로 업데이트 했습니다.
    - 로그아웃
        - 로그아웃 시 redis에 저장해두었던 `refreshToken` 을 삭제했습니다.
</details>

<details>
  <summary>Posting 기능</summary>

- **구현 기능** <br>
    - 외부 게시글을 받아와 해시 태그를 사용하여 목록 / 상세 / 좋아요 / 공유 기능들을 제공합니다.

- **구현 방법**<br>
    - 외부 게시글을 받아와 Posting 형태로 DB에 저장하고 요청에 따라 목록 혹은 Posting을 가져옵니다. View, Like, Share 요청을 받는 경우, DB에 해당 내용을 업데이트 하는 식으로 구현했습니다.
</details>

<details>
  <summary>통계 기능</summary>

- **구현 기능** <br>
    - 1Hour or 1Day 단위로 Posting/View/Like/Share Count 통계를 가져옵니다

- **구현 방법**<br>
    - 스프링 스케줄링 기능을 사용하여 @Scheduled 어노테이션을 이용해 1H 단위로 통계 메서드를 실행합니다. 사용자 통계 요청 시, DB에서 start~end 기간 중 해당 타입의 데이터를 가져옵니다.

</details>
<details>
  <summary>검색 빈도가 높은 Tag 데이터 저장 기능</summary>

- **구현 기능**<br>
    - 주기적으로 검색에 사용하는 Tag 데이터를 저장하는 기능을 구현했습니다.

- **구현 방법**<br>
    - 스프링 스케줄링 기능을 사용하여 @Scheduled 어노테이션을 이용해 주기적으로 메서드를 실행합니다.
        - saveScheduledTag() 메서드: 매 시간마다 실행되며, 최근 3시간 동안의 빈도 높은 태그를 저장합니다.
</details>
<details>
  <summary>단기간 조회수 급상승 알림 기능</summary>

- **구현 기능**<br>
    - 주기적으로 상세검색에 사용하는 postingId 데이터를 저장하는 기능을 구현했습니다.

- **구현 방법**<br>
    - 스프링 스케줄링 기능을 사용하여 @Scheduled 어노테이션을 이용해 주기적으로 메서드를 실행합니다.
        - checkOnFire() 메서드: 매 12시간마다 실행되며, 단기간에 급상승한 게시물을 확인하고 알림을 보냅니다.
    - 단기간 조회수에 대한 조건을 추가로 설정했습니다.
        - 조건 1) 12시간 동안 100번 이상 조회한 경우
        - 조건 2) 12시간 동안 전체의 50% 보다 많은 경우 (생성된지 3시간 이상인 posting 조건이 포함)
        - 조건 3) preView 높은 순으로 최대 10개

</details>


<details>
  <summary>CI 구축</summary>

- **구현 기능** <br>
    - Github Actions를 통해 PR생성시 빌드, 테스트 자동화

- **구현 방법**<br>
  ![image](https://images-ext-2.discordapp.net/external/GuvYegAOwRmMTS-TCOOW4TnFy0sH3lVc_sibcPZlctk/https/github.com/7th-wanted-pre-onboarding-teamN/sns-feed/assets/86291408/3f849dc0-4710-4985-b2c0-459bd897c210?width=576&height=1056)
</details>

<details>
  <summary>Webhook</summary>

- **구현 기능** <br>
    - Github push시 Webhook작동

- **구현 방법**<br>
  ![image](https://images-ext-1.discordapp.net/external/-ocE9N4j4sT8LGgyOQMnI_YlZzqfa8stQdHk-CCYuOM/https/github.com/7th-wanted-pre-onboarding-teamN/sns-feed/assets/86291408/7ea4fccf-5dc3-4396-b86c-075b48107bb9?width=1086&height=1056)
</details>

<details>
  <summary>CD 구축</summary>

- **구현 기능** <br>
    - Jenkins에서 Webhook을 받아서 main branch일 경우 배포

- **구현 방법**<br>
    - generic webhook trigger 플러그인 사용

      ![image](https://images-ext-2.discordapp.net/external/Task9kTlYJSlh_a_mNV2nvNy2rZXKdt5xoYmzH0f2x8/https/github.com/7th-wanted-pre-onboarding-teamN/sns-feed/assets/86291408/9d24d542-9890-444b-82f6-3b7713eb654a?width=530&height=474)

    - 위 설정을 통해 main branch일 경우 pipeline script 실행

      ![image](https://images-ext-1.discordapp.net/external/XL2q0OO6g1QNTcUUGPyZ77zMCAdGUc6KJ-Z7mglbWcQ/https/github.com/7th-wanted-pre-onboarding-teamN/sns-feed/assets/86291408/5de286e7-05ad-4563-b773-ae615b0030af?width=614&height=542)

      ![image](https://images-ext-1.discordapp.net/external/lXYOEDnrmnSxfRpyV6Dg66LeFHO3SWG9YFk-sb_o0Fw/https/github.com/7th-wanted-pre-onboarding-teamN/sns-feed/assets/86291408/f9ec8584-c212-4f34-bbc3-5fbd90a35a36?width=726&height=1056)

</details>

## 시스템 구성도
![시스템 구성도](https://drive.google.com/uc?export=view&id=1k0sQtQ5S5BhZoroljc43S4tmxW5yyacz)

## ERD
![ERD](https://drive.google.com/uc?export=view&id=1aYq6CCC___1LNizJXIKTGTxxJM5qBSEI)


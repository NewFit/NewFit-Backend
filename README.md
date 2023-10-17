# NewFit

> 헬스러들을 위한 기구 예약 서비스, NewFit! → <b>백엔드</b> 레포지토리

Newfit은 헬스기구 예약 관리 시스템을 제공하는 모바일 어플리케이션입니다.<br>
사용자는 본인이 다니는 헬스장의 기구 예약 및 나만의 루틴 등록, 크레딧 시스템 등의 서비스를 통하여 자신의 계획대로 운동을 예약하고 진행할 수 있습니다.<br>
매니저 권한의 사용자에게는 소유한 헬스장을 관리할 수 있는 서비스가 제공됩니다.

## 📚 사용 스택
<div>
<div>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">
</div>

<div>
<img src="https://img.shields.io/badge/PostgreSQL-4479A1.svg?style=flat-square&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white">
</div>

<div>
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=Amazon AWS&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-FF4F8B?style=flat-square&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=flat-square&logo=JSON Web Tokens&logoColor=white">
<img src="https://img.shields.io/badge/FireBaseCloudMessaging-F3702A?style=flat-square&logo=FireBase&logoColor=white">
</div>
</div>

## 📁 Project Structure

```bash
├ common     # 공통으로 쓰이는 인증/인가 관련, 설정 파일, 에러 코드등
│     └── auth
│         ├── config     # Spring Security Config
│         ├── jwt        # jwt 관련 및 토큰 필터
│         ├── oauth      # oAuth 관련 설정
│         └── util       # Cookie 관련 util 클래스
│     └── config     # Redis/S3 관련 Config
│     └── exception  # Exception Response, Error Code, Custom Exception
│         └── handler 
│     └── model      # BaseTimeEntity
├ domains  # NewFit 도메인, auth 패키지를 제외한 나머지 패키지는 하위 구조 동일 
│     └── auth     
│         └── domain     # OAuthHistory, RefreshToken, Provider
│         └── repository # OAuthHistory, RefreshToken Repository
│     └── authority
│         └── controller 
│         └── domain     
│         └── dto        
│         └── repostiory 
│         └── service    
│     └── credit
│     └── dev
│     └── equipment
│     └── gym
│     └── reservation
│     └── routine
└     └── user
```

## 💻 Developers
<table >
    <tr align="center">
        <td><B>Lead•Backend</B></td>
        <td><B>Backend</B></td>
        <td><B>Backend</B></td>
    </tr>
    <tr align="center">
        <td><B>조상욱</B></td>
        <td><B>이승훈</B></td>
        <td><B>유호윤</B></td>
    </tr>
    <tr align="center">
        <td>
            <img src="https://github.com/Sangwook02.png?size=100">
            <br>
            <a href="https://github.com/Sangwook02"><I>Sangwook02</I></a>
        </td>
        <td>
            <img src="https://github.com/Lee-sh98.png?size=100" width="100">
            <br>
            <a href="https://github.com/Lee-sh98"><I>Lee-sh98</I></a>
        </td>
        <td>
            <img src="https://github.com/hoyun06.png?size=100" width="100">
            <br>
            <a href="https://github.com/hoyun06"><I>hoyun06</I></a>
        </td>
    </tr>
</table>
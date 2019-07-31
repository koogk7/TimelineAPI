# TimelineAPI
☘️D2 SUMMER CHALLENGE - Timeline API

### 개발환경

Back-End : Spring-Boot, Spring-Security,  JWT,  JPA, MySQL  
Cloud : AWS - RDS, S3  
Document : Swagger UI

### How To Build
1. 아마존 S3 서버정보 applications.properties에 작성
```properties
#----------------------AWS S3-------------------------------
# AWS 마스터 계정 또는 IAMUSER 정보
cloud.aws.credentials.accessKey=
cloud.aws.credentials.secretKey=
# AWS S3 bucket의 정보
cloud.aws.s3.bucket= 
cloud.aws.region.static= 
```
2. `TimelineApplication.java` Build And Run
3. http://localhost:8080/swagger-ui.html 접속

### 프로젝트 구조
![](https://user-images.githubusercontent.com/48513360/62139632-a8945a00-b324-11e9-994f-41ecc5556a71.png)
![](https://user-images.githubusercontent.com/48513360/62139760-daa5bc00-b324-11e9-8646-e7c13a7611e7.png)
`

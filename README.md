# SPRING-SECURITY

### 개요
이 프로젝트는 JWT를 사용하여 로그인 및 회원가입 기능을 구현하였으며, AWS EC2를 통해 배포된 백엔드 서버입니다. 개발에는 IntelliJ를 사용하였고, Java JDK 17, Spring Boot 3.3.5, MySQL을 주요 기술 스택으로 채택하였습니다.

### 기술 스택
- **IDE**: IntelliJ
- **Java JDK**: 17
- **Spring Boot**: 3.3.5
- **Database**: MySQL
- **Deployment**: AWS EC2

### 기능 시나리오
- **JWT 인증**: 
  - 액세스 토큰은 1시간, 리프레시 토큰은 2주 동안 유효하며, 이를 이용해 로그인 및 회원가입 기능을 제공합니다.
- **로그인/회원가입**: 회원은 이름, 비밀번호, 닉네임으로 가입하며, 로그인 후 JWT 토큰을 통해 인증된 접근이 가능합니다.

### 주요 기능 체크리스트
- [x] Spring Boot로 백엔드 로직 작성
- [x] 회원가입 구현
- [x] 로그인 구현
- [x] AWS EC2를 통한 배포
- [x] Swagger UI를 통한 API 접속 가능

### 스크린샷
> 각 기능의 스크린샷을 아래에 첨부하였습니다.

- **AWS EC2 배포**

  ![AWS EC2 배포](https://github.com/user-attachments/assets/9a0f7e7a-1419-4887-bc36-8fee78f29749)

- **Swagger UI 접속**

  ![Swagger UI 접속](https://github.com/user-attachments/assets/b9d6e203-ba7f-4573-bc59-c30d91c660fe)
  ![Swagger UI 접속](https://github.com/user-attachments/assets/0bdcb87f-6578-49ee-8518-64796447928c)

- **DB 저장**

  ![회원 정보](https://github.com/user-attachments/assets/7a6888dd-dc75-4b6d-a751-4c02fd6fbfde)
  ![리프레쉬 토큰](https://github.com/user-attachments/assets/f8fec5bd-7a08-4697-a0ac-972859701c89)

- **포스트맨**

  ![로그인](https://github.com/user-attachments/assets/cfd98fa8-77a8-4793-93ec-9a62884c21b1)
  ![회원가입](https://github.com/user-attachments/assets/0395c0ba-41ff-4d24-82c1-c7f076cf36bd)

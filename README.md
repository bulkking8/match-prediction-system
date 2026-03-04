🏆 AI 기반 실시간 스포츠 경기 승리 예측 시스템
 네이버 스포츠 응원톡 감성 분석을 통한 실시간 승률 시각화 플랫폼

📌 프로젝트 소개
본 프로젝트는 네이버 스포츠 응원톡의 실시간 데이터를 수집하여 팬들의 여론을 분석하고, 경기 흐름을 실시간 승리 확률로 변환하여 시각화하는 풀스택 대시보드 시스템입니다.

---

## 🚀 주요 기능

### [데이터 분석 엔진]
- **Selenium 기반 동적 크롤링**: 과거 댓글 히스토리 및 실시간 신규 댓글 수집
- **감성 분석**: 팀별 긍정/부정 키워드 매칭을 통한 실시간 스코어링
- **데이터 정제**: 정규표현식을 활용한 특수문자/이모지 제거

### [백엔드 & 예측 서비스]
- **실시간 데이터 동기화**: Python-Java REST API 통신을 통한 실시간 로그 및 분석 데이터 전송
- **승률 산출 알고리즘**: 누적 감성 데이터를 바탕으로 실시간 승리 확률 및 우세 팀 도출
- **DB 적재**: MySQL `ON DUPLICATE KEY UPDATE`를 활용한 실시간 데이터 Upsert

### [프론트엔드 대시보드]
- **실시간 모니터링 UI**: 분석 엔진의 프로세스 상태 실시간 텍스트 출력
- **데이터 시각화**: Chart.js 기반 지표 업데이트 및 프로그레스 바 형태의 확률 게이지 구현
- **자동 갱신**: 5초 주기 Polling 방식을 통한 UI 동기화

---

## 🛠 Tech Stack
- **Language**: Java 17, Python 3.9
- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.0
- **Library**: Selenium, Chart.js, PyMySQL, Requests
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla JS)

---

## 💡 Troubleshooting & Problem Solving

### 1. 실시간 데이터 파이프라인 지연 문제
- **현상**: 대량의 댓글 수집 시 서버 부하 및 로그 누락 발생
- **해결**: Python 측에 적절한 `timeout` 설정과 Spring Boot의 `Thread-safe` 리스트 관리를 통해 동시성 이슈 해결 및 안정적인 로그 전송 보장

### 2. 동적 웹 구조 대응
- **현상**: 네이버 스포츠 응원톡의 '더보기' 버튼 및 자동 업데이트 로직 제어의 어려움
- **해결**: Selenium의 `WebDriverWait`와 `execute_script`를 활용하여 버튼 가려짐 현상을 방지하고 안정적인 데이터 로드 구현

---


## 📁 Project Structure
```text
.
├── match-prediction-server/        # Spring Boot 백엔드 및 웹 프런트엔드
│   ├── src/main/java/org/duhyeok/
│   │   ├── controller/             # Log/Predict API 엔드포인트
│   │   ├── service/                # 분석 엔진 실행 및 데이터 처리 로직
│   │   └── model/                  # 경기 데이터 DTO 및 로그 객체
│   ├── src/main/resources/
│   │   ├── static/                 # Frontend 자산 (JS, CSS, Images)
│   │   └── templates/              # Thymeleaf HTML (index.html)
│   └── pom.xml                     # Maven 의존성 설정
│
├── analysis-engine/                # Python 기반 실시간 분석 엔진
│   ├── crawler.py                  # Selenium 크롤링 및 감성 분석 메인 로직
│   └── requirements.txt            # Python 라이브러리 의존성
│
└── sql/
    └── schema.sql                  # MySQL 테이블 생성 스크립트
```
## 🖥 화면 구성 (Dashboard)


<img width="627" height="221" alt="image" src="https://github.com/user-attachments/assets/74b666f1-1cc9-437e-a71f-c2dd156f2e0e" />
<img width="400" height="379" alt="image" src="https://github.com/user-attachments/assets/fe05e07a-62c3-4dba-88d2-8825ea9cbd90" />
<img width="461" height="471" alt="image" src="https://github.com/user-attachments/assets/5b7ef76b-bbc1-41df-8a4e-7c0123905fdf" />
<img width="436" height="406" alt="image" src="https://github.com/user-attachments/assets/08a2938d-939c-492e-b97a-476adbbe05ab" />
<img width="627" height="439" alt="image" src="https://github.com/user-attachments/assets/baa178fb-2a97-43cd-a82e-0a7efdd667c2" />



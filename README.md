# 프로젝트 산출물

---

## 목차

- [1. 필수 산출물 정의 및 작성 기준](#1-필수-산출물-정의-및-작성-기준)
  - [1.1 프로젝트 기획서 (Project Proposal)](#11-프로젝트-기획서-project-proposal)
  - [1.2 요구사항 기능 명세서 (PRD - Product Requirement Document)](#12-요구사항-기능-명세서-prd---product-requirement-document)
  - [1.3 시스템 아키텍처 구성도 (System Architecture Diagram)](#13-시스템-아키텍처-구성도-system-architecture-diagram)
  - [1.4 데이터베이스 ERD (Entity Relationship Diagram)](#14-데이터베이스-erd-entity-relationship-diagram)
  - [1.5 화면 설계서 (UI Wireframe)](#15-화면-설계서-ui-wireframe)
  - [1.6 REST API 명세서 및 더미 데이터 규격서 (API Specification)](#16-rest-api-명세서-및-더미-데이터-규격서-api-specification)
  - [1.7 트러블슈팅 및 기술 문제 해결 기록 (Troubleshooting Log)](#17-트러블슈팅-및-기술-문제-해결-기록-troubleshooting-log)

---

# 1. 필수 산출물 정의 및 작성 기준

## 1.1 프로젝트 기획서 (Project Proposal)

### 1.1.1 정의

프로젝트의 출발점으로서 개발하고자 하는 SNS 서비스의 목적, 배경, 핵심 비즈니스 가치, 크리에이터 구독 모델, 팀원별 도메인 역할 분담을 명확히 규정하는 기본 기획 문서.

### 1.1.2 작성 필요성 및 목적

- 프로젝트 방향성 통일: 팀원 간 서비스 목표와 비즈니스 모델(일반 콘텐츠 및 크리에이터 구독 콘텐츠)에 대한 이해도를 일치시키고 기획 혼선을 방지
- 개발 범위 명확화: 2주 개발 일정에 최적화된 현실적인 서비스 스코프를 설정하고 프론트엔드와 백엔드의 연동 목표를 확립

### 1.1.3 필수 포함 항목

- 프로젝트명 및 한 줄 서비스 소개
- 프로젝트 기획 배경 및 목적
- 서비스 주요 특징
- 주요 기능 요구사항 요약
- 팀 구성 및 도메인별 역할 분담표
- 💻 프로젝트 기획서 샘플: [docs/01_planning/01_proposal.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/01_planning/01_proposal.md)

---

## 1.2 요구사항 기능 명세서 (PRD - Product Requirement Document)

### 1.2.1 정의

시스템이 제공해야 하는 모든 세부 기능을 도출하고, 각 기능별 구현 조건, 담당자, 우선순위를 목록화하여 관리하는 문서.

### 1.2.2 작성 필요성 및 목적

- 개발 스코프 제어: 2주 개발 일정 동안 구현할 필수 기능과 선택 기능을 명확하게 구분
- 개발 구현 기준 제공: 기능별 입력/처리/출력 조건과 예외 규칙을 구체화하여 개발 과정에서 발생하는 구현 오차를 감소
- 인증 및 결제 요구사항 명시: JWT 인증, OAuth 로그인, 크리에이터 구독 및 결제 처리와 관련된 세부 요구조건을 사전에 정립

### 1.2.3 필수 포함 항목 및 작성 방법

- 요구사항 식별 ID: `REQ-USER-01`, `REQ-POST-01`, `REQ-FOLLOW-01`, `REQ-SUB-01`, `REQ-ADMIN-01` 등 도메인별 고유 코드 부여
- 구분: 회원, 피드, 인터랙션(댓글/좋아요/북마크), 결제/구독 등 도메인 명시
- 기능명 및 담당자: 기능을 담당하여 개발할 팀원 지정
- 우선순위: 1순위(필수 구현), 2순위(주요 권장), 3순위(추가 선택) 구분
- 상세 내용: 기능의 세부 동작 조건 및 입력 데이터 검증 규칙 명시
- 💻 요구사항 기능 명세서 샘플: [docs/01_planning/02_prd.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/01_planning/02_prd.md)

---

## 1.3 시스템 아키텍처 구성도 (System Architecture Diagram)

### 1.3.1 정의

React SPA 클라이언트부터 Spring Boot REST API 백엔드, MySQL 데이터베이스, AWS 클라우드 인프라 및 결제/구독 시스템 간의 전체적인 시스템 구조와 데이터 흐름을 시각화한 구성도.

### 1.3.2 작성 필요성 및 목적

- 클라이언트-서버 분리 아키텍처 이해: React 프론트엔드와 Spring Boot 백엔드 간 REST API 통신 및 인증 구조를 명확화
- AWS 클라우드 배포 구조 파악: S3 + CloudFront를 이용한 프론트엔드 배포, EC2 + Docker 기반 백엔드 배포 및 S3 파일 저장소 구성 확인
- 외부 결제 흐름 정립: 프론트엔드 결제 요청부터 백엔드 결제 검증 및 구독 정보 저장까지의 데이터 흐름을 명확하게 정의

### 1.3.3 필수 포함 항목

- 프론트엔드 호스팅 영역: AWS S3, CloudFront (CDN), 브라우저 React SPA
- 백엔드 애플리케이션 영역: AWS EC2, Docker, Spring Boot REST API
- 데이터베이스 영역: MySQL 컨테이너 및 데이터 볼륨
- 외부 연동 및 스토리지 영역: AWS S3 미디어 저장소, Toss Payments
- CI/CD 영역: GitHub Actions
- 💻 시스템 아키텍처 구성도 샘플: [docs/02_design/02_architecture.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/02_design/02_architecture.md)

---

## 1.4 데이터베이스 ERD (Entity Relationship Diagram)

### 1.4.1 정의

시스템에서 사용하는 데이터 테이블 구조, 컬럼 데이터 타입, PK/FK 제약조건 및 테이블 간의 연관 관계(1:1, 1:N, N:M)를 규정한 개체 관계도.

### 1.4.2 작성 필요성 및 목적

- 데이터 무결성 확보: 회원 상세 1:1 수직 분할, 해시태그 1NF 정규화, 좋아요/북마크 복합 유니크 제약을 통한 무결성 유지
- MyBatis SQL 매핑 효율 증대: 테이블 간 관계를 사전에 정립하여 조인 쿼리 및 매퍼 데이터 매핑 오류 최소화

### 1.4.3 필수 포함 항목

- 테이블명 및 컬럼 명세: PK, FK, Not Null, Unique, Default 제약조건 명시
- 핵심 도메인 테이블: member, post, post_image, post_like, reply, reply_like
- 사용자 관계 테이블: block, follow, report
- 결제/구독 테이블: subscription, payment
- 작성 방식: 마크다운 내부 Mermaid `erDiagram` 문법 구문을 사용하여 시각적 다이어그램으로 렌더링되도록 작성

### 1.4.4 Mermaid ERD 주요 문법

- Mermaid ERD 관계 표현 기호 규칙:
  - 1:1 관계 (필수 1 대 1): `||--||` (양쪽 모두 1개 필수)
  - 1:1 관계 (필수 1 대 선택 0 또는 1): `||--o|` (한쪽은 1개 필수, 반대쪽은 0개 또는 1개)
  - 1\:N 관계 (필수 1 대 1 이상 N): `||--|{` (한쪽은 1개 필수, 반대쪽은 1개 이상 필수)
  - 1\:N 관계 (필수 1 대 0 이상 N): `||--o{` (한쪽은 1개 필수, 반대쪽은 0개 이상 선택)
  - N\:M 관계 (1 이상 N 대 1 이상 M): `}|--|{` (양쪽 모두 1개 이상 필수)
  - N\:M 관계 (1 이상 N 대 0 이상 M): `}|--o{` (한쪽은 1개 이상 필수, 반대쪽은 0개 이상 선택)
- 관계선 기호 가이드:
  - `||`: 정확히 1개 (Exactly one)
  - `|{`: 1개 이상 (One or more)
  - `o{`: 0개 이상 (Zero or more)
  - `o|`: 0개 또는 1개 (Zero or one)
- 기호 구성 요소 해설:
  - Straight Line (`|`): 최소 1개 이상 존재해야 함 (Mandatory 필수 조건)
  - Circle (`o`): 0개일 수 있음 (Optional 선택 조건, 없어도 됨)
  - Crow Foot (`}` 또는 `{`): 다수 (Many, N개)를 의미
- 💻 데이터베이스 ERD 샘플: [docs/02_design/04_erd.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/02_design/04_erd.md)

---

## 1.5 화면 설계서 (UI Wireframe)

### 1.5.1 정의

사용자 관점에서의 SNS 서비스 화면 레이아웃, 게시글 카드, 댓글, 사용자 프로필, 구독 및 결제 화면, 관리자 화면과 화면 간 이동 흐름을 시각적으로 구성한 화면 뼈대 설계 문서.

### 1.5.2 작성 필요성 및 목적

- AI 바이브 코딩 연계 최적화: 시각적 와이어프레임과 구성 요소 명세를 AI에게 전달하여 신속하고 일관된 React 컴포넌트 생성 유도
- 기획 오차 감소: 메인 피드, 게시글 작성 및 상세, 댓글, 프로필, 구독 및 관리자 화면의 컴포넌트 배치를 미리 정의하여 프론트엔드 재작업 방지

### 1.5.3 필수 포함 항목

- 주요 화면 목록: 로그인/회원가입, 메인 피드, 게시글 상세 및 댓글, 게시글 작성, 사용자 프로필, 검색, 팔로우, 구독/결제, 관리자 페이지
- 출력 데이터 항목(Output Data) 및 화면 제어/동작 규칙(Behavior Rules) 수록

### 1.5.4 와이어프레임 도구 및 산출물 관리 가이드

- tldraw 웹사이트: https://www.tldraw.com/

- Excalidraw 웹사이트: https://excalidraw.com/

- 제작 팁:
  - 복잡한 픽셀 단위 작업 대신 사각형 박스와 텍스트를 활용한 와이어프레임을 구성하여 화면 구조와 사용자 흐름을 명확하게 표현
  - 이미지 파일은 `docs/images/` 폴더에 저장하고 마크다운에서 상대 경로로 링크하여 관리
- 💻 화면 설계서 샘플: [docs/02_design/03_ui_wireframe.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/02_design/03_ui_wireframe.md)

---

## 1.6 REST API 명세서 및 더미 데이터 규격서 (API Specification)

### 1.6.1 정의

프론트엔드와 백엔드가 JSON 비동기 통신을 수행하기 위해 사전에 합의하는 엔드포인트 URI, HTTP 메서드, 요청/응답 DTO 및 프론트 더미 데이터 규격 문서.

### 1.6.2 작성 필요성 및 목적

- 병렬 개발 보장: 백엔드 API 완성을 기다리지 않고, 프론트엔드가 정의된 API 규격을 기준으로 UI와 기능을 먼저 구현할 수 있도록 지원
- 손쉬운 연동 전환: 실제 API 구현 시 사전에 정의한 엔드포인트와 요청/응답 규격을 기준으로 프론트엔드와 백엔드 연동 가능
- 데이터 규격 통일: 공통 성공 응답 및 에러 응답 규격을 사전에 정의하여 프론트엔드 예외 처리의 일관성 확보

### 1.6.3 필수 포함 항목

- 공통 응답 규격: 성공 포맷 (DTO 단건 객체 또는 List 컬렉션 직접 반환), 실패/에러 포맷 (ApiErrorResponse - code, message, status, timestamp, errors)

- 인증 API: 로그인 (/api/v1/auth/login), 액세스 토큰 갱신 RTR (/api/v1/auth/refresh)

- 회원 API: 내 프로필 조회 (/api/v1/members/me)

- 피드 API: 피드 목록/검색 (GET /api/v1/posts), 피드 등록 (POST /api/v1/posts), 단건 상세, 수정, 삭제

- 인터랙션 API: 댓글 등록/삭제, 좋아요 토글, 북마크 토글

- 결제/구독 API: 결제 사전 검증 (/api/v1/payments/prepare), 사후 검증 (/api/v1/payments/complete), VIP 정기 구독 (/api/v1/subscriptions), 결제 웹훅
- 💻 REST API 명세서 샘플: API 명세서 작성 시 프로젝트의 실제 API 엔드포인트 및 DTO 규격을 기준으로 작성

---

## 1.7 트러블슈팅 및 기술 문제 해결 기록 (Troubleshooting Log)

### 1.7.1 정의

개발 및 배포 과정에서 직면한 런타임 오류, 로직 에러, 클라우드 환경 설정 문제와 그 발생 원인, 해결 방법, 시사점을 기록하는 문서.

### 1.7.2 작성 필요성 및 목적

- 문제 재발 방지: 작성된 해결 기록을 공유하여 팀원들이 동일한 버그로 시간을 낭비하는 현상 차단
- 기술적 자산화: CORS 에러, JWT 인증 및 사용자 식별 문제, MyBatis 쿼리 최적화, OAuth 설정, 파일 처리, 결제/구독 등 실무 수준의 기술 문제 해결 경험을 면접 및 포트폴리오 핵심 자산으로 활용

### 1.7.3 필수 포함 항목 및 작성 템플릿

- 작성자별 식별자 규칙: 동시 편집 충돌 방지를 위한 TS-[작성자영문]-[순번] 태그 사용 (예: TS-YONG-01, TS-HARU-01)
- 복사용 표준 템플릿:
```
## TS-[작성자영문]-[순번]: [발생한 문제 및 에러 제목]

### 1) 발생 현상 및 에러
- 런타임 현상 설명 및 발생한 에러 로그 원문/메시지 수록.

### 2) 원인 분석
- 해당 문제나 버그가 발생한 근본 원인 및 코드 흐름 분석.

### 3) 해결 방법
- 문제를 해결하기 위해 변경하거나 새로 적용한 해결 코드 반영.

```java
// 해결 핵심 소스 코드 반영

### 4) 시사점 및 배운 점
- 버그 해결을 통해 새로 배우게 된 원리나 향후 재발 방지를 위한 시사점 작성.
```

- 💻 트러블슈팅 작성 샘플: [docs/03_reports/troubleshooting.md](https://github.com/likelion-backend-25th/linkup/blob/dev/docs/03_reports/troubleshooting.md)

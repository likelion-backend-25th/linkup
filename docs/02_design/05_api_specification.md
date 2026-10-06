## 목차
- [1. 공통 응답 규격](#1-공통-응답-규격)
  - [1.1 공통 요청 규칙](#11-공통-요청-규칙)
  - [1.2 성공 응답 포맷](#12-성공-응답-포맷)
- [2, 인증 api](#2-인증-api)
  - [2.1 소셜 로그인 시작](#21-소셜-로그인-시작)
  - [2.2 소셜 로그인 콜백](#22-소셜-로그인-콜백)
  - [2.3 Access Token 발급 및 갱신](#23-access-token-발급-및-갱신)
  - [2.4 로그아웃](#24-로그아웃)
- [3. 회원 및 프로필 API](#3-회원-및-프로필-api)
  - [3.1 내 프로필 조회](#31-내-프로필-조회)
  - [3.2 내 프로필 수정](#32-내-프로필-수정)
  - [3.3 다른 사용자 프로필 조회](#33-다른-사용자-프로필-조회)
  - [3.4 추천 사용자 조회](#34-추천-사용자-조회)
  - [3.5 회원 차단](#35-회원-차단)
  - [3.6 회원 차단 해제](#36-회원-차단-해제)
  - [3.7 차단 회원 목록 조회](#37-차단-회원-목록-조회)
- [4. 게시글 및 피드 API](#4-게시글-및-피드-api)
  - [4.1 게시글 피드 조회](#41-게시글-피드-조회)
  - [4.2 게시글 상세 조회](#42-게시글-상세-조회)
  - [4.3 게시글 등록](#43-게시글-등록)
  - [4.4 게시글 수정](#44-게시글-수정)
  - [4.5 게시글 삭제](#45-게시글-삭제)
- [5. 댓글·좋아요 API](#5-댓글좋아요-api)
  - [5.1 댓글 목록 조회](#51-댓글-목록-조회)
  - [5.2 댓글 등록](#52-댓글-등록)
  - [5.3 댓글 수정](#53-댓글-수정)
  - [5.4 댓글 삭제](#54-댓글-삭제)
  - [5.5 게시글 좋아요ㅕ](#55-게시글-좋아요)
- [6. 팔로우 및 검색 API](#6-팔로우-및-검색-api)
  - [6.1 팔로우 및 언팔로우](#61-팔로우-및-언팔로우)
  - [6.2 사용자 검색](#62-사용자-검색)
  - [6.3 게시글 검색](#63-게시글-검색)
- [7. 사용자 차단 API](#7-사용자-차단-api)
  - [7. 사용자 차단](#71-사용자-차단)
  - [7. 차단 사용자 목록 조회](#72-차단-사용자-목록-조회)
  - [7. 사용자 차단 해제](#73-사용자-차단-해제)
- [8. 크리에이터 API](#8-크리에이터-api)
  - [8.1 크리에이터 승인](#81-크리에이터-승인)
  - [8.2 내 구독자 목록 조회](#82-내-구독자-목록-조회)
- [9. 구독 및 결제 API](#9-구독-및-결제-api)
  - [9. 구독 등록](#91-구독-등록)
  - [9. 내 구독 목록 조회](#92-내-구독-목록-조회)
  - [9. 구독 내역 상세 조회](#93-구독-내역-상세-조회)
  - [9. 구독 시작일 검증](#94-구독-시작일-검증)
  - [9. 정기 구독 해지](#95-정기구독-해지)
  - [9. 구독 환불](#96-구독-환불)
- [10. 신고 API](#10-신고-api)
  - [10.1 게시글·댓글 신고](#101-게시글댓글-신고)
- [11. 관리자 API](#11-관리자-api)
  - [11. 신고 목록 및 상세 조회](#111-신고-목록-및-상세-조회)
  - [11. 신고 처리](#112-신고-처리)
  - [11. 회원 목록 및 상세 조회](#113-회원-목록-및-상세-조회)
  - [11. 전체 구독·결제 조회](#114-전체-구독결제-조회)
  - [11. 환불 내역 조회](#115-환불-내역-조회)

# 1. 공통 응답 규격

## 1.1 공통 요청 규칙

- Base URI: `/api/v1`
- 기본 Content-Type: `application/json`
- 인증이 필요한 요청:

```
Authorization: Bearer <accessToken>
```

- 작성자·구매자 등 요청자 정보는 인증 정보에서 확인하며 Request Body로 받지 않음.
- 차단 관계, 게시글 공개 범위, 구독 상태는 서버에서 검증.
- 페이징 요청의 page는 0부터 시작.
- size는 요청에 따라 다름

## 1.2 성공 응답 포맷

- 별도의 공통 래퍼 없이 응답 DTO를 직접 반환.
- 조회·수정 성공: `200 OK`
- 생성 성공: `201 Created`
- 응답 본문이 없는 삭제·해제 성공: `204 No Content`

단건 응답 샘플:

```json
{
  "id":101,
  "content":"구독자 여러분께 공유하는 개발 자료입니다.",
  "visibility":"SUBSCRIBERS",
  "hidden":false,
  "likeCount":12,
  "commentCount":3,
  "createdAt":"2026-09-21T09:00:00+09:00"
}
```

목록은 페이징 정보를 포함한 DTO로 반환:

```json
{
  "content": [
    {
      "id":101,
      "content":"구독자 여러분께 공유하는 개발 자료입니다.",
      "visibility":"SUBSCRIBERS",
      "hidden":false,
      "likeCount":12,
      "commentCount":3,
      "createdAt":"2026-09-21T09:00:00+09:00"
    }
  ],
  "cursor":0,
  "size":20,
}
```

- `cursor`: 다음에 들어오기 시작할 컬럼 값
- `size`: 페이지당 조회 개수, 최소 3·최대 20

---

# 2. 인증 API

## 2.1 소셜 로그인 시작

- Method: `GET`
- URI: `/api/v1/oauth2/authorization/{provider}`
- 인증 필요 여부: 불필요
- Path Parameters:
  - `provider`: `google`, `kakao`
- Response: `302 Found`
- 처리 규칙:
  - 선택한 소셜 로그인 인증 화면으로 이동.
  - 최초 로그인 시 회원 등록.
  - 기존 회원은 소셜 서비스와 서비스 내 사용자 식별자를 기준으로 확인.

## 2.2 소셜 로그인 콜백

- Method: `GET`
- URI: `/api/v1/login/oauth2/code/{provider}`
- 인증 필요 여부: 불필요
- Query Parameters:
  - `code`: 소셜 서비스에서 발급한 인가 코드
  - `state`: 로그인 요청 검증값
- 처리 규칙:
  - 서버에서 소셜 인증 결과를 검증.
  - 프론트엔드 로그인 완료 화면으로 리다이렉트.
  - 프론트엔드는 토큰 갱신 API를 호출하여 Access Token을 발급받음.
  - 토큰을 리다이렉트 URL에 포함하지 않음.

> Refresh Token의 SameSite, CORS 및 CSRF 정책은 실제 프론트엔드·백엔드 배포 환경에 맞춰 설정한다.

- SameSite : 다른 사이트에서 해당 Cookie를 자동으로 보내도 되는지를 제어하는 옵션
- CORS : 백엔드로 오는 프론트 URL 요청을 허용하는 설정
- CSRF : 인증된 사용자의 권한을 도용해 사용자가 의도하지 않은 상태 변경 요청(수정, 삭제, 송금 등)을 특정 웹사이트에 보내게 만드는 웹 보안 취약점

정리 : Refresh Token을 Cookie로 관리할 경유, 프론트와 백엔드가 실제 배포된 도메인/Origin 관계를 확인한 후 Cookie의 SameSite 설정, CORS 허용 범위, CSRF 방어 방식을 결정하는 의미
>

## 2.3 Access Token 발급 및 갱신

- Method: `POST`
- URI: `/api/v1/auth/refresh`
- 인증 필요 여부**:** Refresh Token 검증
- Request Body:

```json
{
  "refreshToken": "Refresh Token"
}
```

- 처리 규칙
  - Request Body로 전달받은 Refresh Token을 검증합니다.
  - 검증에 성공하면 새로운 Access Token을 발급합니다.
  - 새로운 Refresh Token을 함께 발급하는 경우 해당 토큰을 Response Body로 반환합니다.
  - 현재 구현에서는 Refresh Token을 쿠키로 설정하지 않고 Request Body로 전달받습니다.
- Response Body**:** `200 OK`

    ```json
    {
      "message": "Access Token 재발급 성공",
      "token": "새로운 Access Token",
      "refreshToken": "새로운 Refresh Token"
    }
    
    ```


## 2.4 로그아웃

- Method: `POST`
- URI: `/api/v1/auth/logout`
- 인증 필요 여부: Refresh Token 쿠키 검증
- Request Body: 없음
- Response: `204 No Content`
- 처리 규칙:
  - 해당 로그인 세션의 Refresh Token 무효화.
  - Refresh Token 쿠키 제거.
  - 프론트엔드에서 Access Token 제거.

---

# 3. 회원 및 프로필 API

## 3.1 내 프로필 조회

- Method: `GET`
- URI: `/api/v1/member/me`
- 인증 필요 여부: 필수
- Response Body: `200 OK`

    ```json
    {
      "id": 1,
      "email": "user@example.com",
      "name": "닉네임",
      "uniqueId": "backend_name",
      "profileImage": "https://cdn.example.com/profiles/1.jpg",
      "introduction": "Java와 Spring을 공부하는 개발자입니다.",
      "postCount": 12,
      "followerCount": 120,
      "followingCount": 35
    }
    ```

- `id`: 서버 내부 회원 식별자

## 3.2 내 프로필 수정

- Method: `PUT`
- URI: `/api/v1/member/me`
- 인증 필요 여부: 필수
- Request
  - `request`: `MemberUpdateRequest`
  - `profileImage`: `MultipartFile` (선택), 프로필 이미지가 전달된 경우 기존 프로필 이미지를 변경
- Response: `200 OK`
- Response Body: 없음

## 3.3 다른 사용자 프로필 조회

- Method: `GET`
- URI: `/api/v1/members/{memberId}`
- 인증 필요 여부: 선택
- Response Body: `200 OK`

    ```json
    {
      "id": 2,
      "uniqueId": "daily_creator",
      "name": "데일리",
      "profileImage": "https://cdn.example.com/profiles/2.jpg",
      "introduction": "매일 새로운 콘텐츠를 공유합니다.",
      "role": "ROLE_CREATOR",
      "postCount": 30,
      "followerCount": 250,
      "followingCount": 20,
      "subscribedStatus": "SUBSCRIBED"
    }
    ```

- 타인 이메일 등 비공개 정보는 반환하지 않음.
- 차단 관계인 경우 일반 프로필 조회 제한.

## 3.4 추천 사용자 조회

- URI: `/api/v1/members/recommendations`
- 인증 필요 여부: 선택
- 처리 규칙:
  - 팔로워 수 내림차순으로 최대 4명 조회.
  - 로그인 상태에서는 본인과 차단 관계 사용자 제외.
- Response Body: `200 OK`

    ```jsx
    [
      {
        "id":2,
        "unique_id":"daily_creator",
        "name":"데일리",
        "profileImage":"https://cdn.example.com/profiles/2.jpg",
        "followerCount":251,
        "following":false
      }
    ]
    ```


## 3.5 회원 차단

- Method: `POST`
- URI: `/api/v1/member/{memberId}/block`
- 인증 필요 여부: 필수
- Response: `200 OK`
- Response Body: 없음
- 처리 규칙: 로그인한 회원이 특정 회원을 차단합니다.
- Path Variable
  - `memberId`: 차단할 회원의 ID
- Request Body: 없음
- Response Body**:** `200 OK`

---

## 3.6 회원 차단 해제

- Method: `DELETE`
- URI: `/api/v1/member/{memberId}/block`
- 인증 필요 여부: 필수
- Response: `200 OK`
- Response Body: 없음
- 처리 규칙: 로그인한 회원이 특정 회원의 차단을 해제합니다.
- Path Variable
  - `memberId`: 차단 해제할 회원의 ID
- Request Body: 없음
- Response Body**:** `200 OK`

## 3.7 차단 회원 목록 조회

- Method: `GET`
- URI: `/api/v1/member/blocks`
- 인증 필요 여부: 필수
- Response: `200 OK`
- 처리 규칙: 로그인한 회원이 차단한 회원 목록을 조회합니다.
- Response Body

    ```json
    [
      {
        "id": 2,
        "uniqueId": "daily_creator",
        "name": "데일리",
        "profileImage": "https://cdn.example.com/profiles/2.jpg"
      },
      {
        "id": 5,
        "uniqueId": "backend_dev",
        "name": "백엔드개발자",
        "profileImage": "https://cdn.example.com/profiles/5.jpg"
      }
    ]
    ```

- `id`: 차단한 회원의 ID
- `uniqueId`: 차단한 회원의 고유 아이디
- `name`: 차단한 회원의 이름
- `profileImage`: 차단한 회원의 프로필 이미지 URL

---

# 4. 게시글 및 피드 API

## 4.1 게시글 피드 조회

- Method: `GET`
- URI: `/api/v1/feeds`
- 인증: 선택
- Query Parameters
  - cursor: 다음 페이지 조회용 게시글 ID 커서
  - size 조회 개수
- Response: `200 OK` (`FeedResponse[]`)

```json
{
  "posts": [
    {
      "postId": 1,
      "memberId": 1,
      "memberName": "홍길동",
      "uniqueId": "hong123",
      "profileImageUrl": "https://bucket.s3.amazonaws.com/profiles/1.jpg",
      "content": "Spring Boot 프로젝트 개발 중입니다.",
      "mainImageUrl": "https://bucket.s3.amazonaws.com/posts/1-1.jpg",
      "likeCount": 3,
      "replyCount": 2,
      "likedByMe": false,
      "subscriberOnly": false,
      "createdAt": "2026-09-20T10:00:00"
    }
  ],
  "nextCursor": 1,
  "hasNext": true
}
```

## 4.2 게시글 상세 조회

- Method: `GET`
- URI: `/api/v1/posts/{id}`
- Path: `id` 게시글 ID
- 인증: 선택 (좋아요 여부 `likedByMe`는 로그인 시)
- Response: `200 OK` (`PostDetailResponse`)
- 오류: `403` 구독 전용 / `404` 없음

```json
{
  "id": 11,
  "memberId": 1,
  "name": "홍길동",
  "uniqueId": "gildong",
  "profileImage": "https://bucket.s3.amazonaws.com/members/1.jpg",
  "content": "Spring Boot 프로젝트를 개발하고 있습니다.",
  "fileUrl": "https://bucket.s3.amazonaws.com/files/document.pdf",
  "likeCount": 0,
  "subscriberOnly": false,
  "likedByMe": false,
  "images": [
    {
      "id": 21,
      "imageUrl": "https://bucket.s3.amazonaws.com/posts/image-1.jpg",
      "imageOrder": 1
    },
    {
      "id": 22,
      "imageUrl": "https://bucket.s3.amazonaws.com/posts/image-2.jpg",
      "imageOrder": 2
    }
  ],
  "createdAt": "2026-09-22T10:00:00",
  "updatedAt": "2026-09-22T10:00:00"
}
```

## 4.3 게시글 등록

- Method: `POST`
- URI: `/api/v1/posts`
- 인증: 필수
- Content-Type: `multipart/form-data`

| Part | 타입 | 설명 |
| --- | --- | --- |
| `request` | JSON (`application/json`) | { "content": string, "subscriberOnly": boolean } |
| `images` | File[] | 이미지. 여러 장이면 같은 키로 반복 |
| `file` | File | 첨부파일. 없으면 생략 |
- `request` 예시

```json
{
  "content": "Spring Boot 프로젝트를 개발하고 있습니다.",
  "subscriberOnly": false
}
```

- Response: `201 Created`
- Header: `Location: /api/v1/posts/11`
- Body:

```json
{
  "id": 11
}
```

## 4.4 게시글 수정

- Method: `PUT`
- URI: `/api/v1/posts/{id}`
- 인증: 필수
- 권한: 작성자
- Content-Type: `multipart/form-data`

| Part | 타입 | 설명 |
| --- | --- | --- |
| `request` | JSON | 아래 스키마 |
| `newImages` | File[] | 새로 추가할 이미지 |
| `file` | File | 새 첨부. 없으면 생략 |
- request

```json
{
  "content": "게시글 내용을 수정했습니다.",
  "subscriberOnly": true,
  "removeFile": false,
  "imageRequest": [
    { "imageId": 21, "newImageIndex": null },
    { "imageId": null, "newImageIndex": 0 }
  ]
}
```

- `imageId`: 유지할 기존 이미지 ID
- `newImageIndex`: `newImages` 배열 인덱스. 신규면 `imageId`는 `null`
- `removeFile`: 기존 첨부 삭제
- Response: `200 OK`

## 4.5 게시글 삭제

- Method: `DELETE`
- URI: `/api/v1/posts/{id}`
- 인증: 필수
- 권한: 작성자 또는 관리자
- Response: `204 No Content`

---

# 5. 댓글·좋아요 API

## 5.1 댓글 목록 조회

- Method: `GET`
- URI: `/api/v1/posts/{postId}/replies`
- 인증: 선택 (`likedByMe`는 로그인 시)

Path

- `postId`: 게시글 ID

Query

- `cursor`: 다음 페이지 커서. 첫 페이지는 생략
- `size`: 기본 `10`, 최소 `1`, 최대 `20`

Response: `200 OK` (`ReplyPageResponse`)

```json
{
  "replies": [
    {
      "id": 1,
      "memberId": 2,
      "name": "홍길동",
      "uniqueId": "gildong",
      "profileImage": "https://bucket.s3.amazonaws.com/members/2.jpg",
      "content": "좋은 글 잘 읽었습니다!",
      "likeCount": 3,
      "likedByMe": false,
      "createdAt": "2026-09-22T10:00:00",
      "updatedAt": "2026-09-22T10:00:00"
    }
  ],
  "nextCursor": 1,
  "hasNext": true
}
```

마지막 페이지면 `nextCursor`는 `null`, `hasNext`는 `false`.

## 5.2 댓글 등록

- Method: `POST`
- URI: `/api/v1/posts/{postId}/replies`
- 인증: 필수
- 정책: 게시글 접근 권한·차단 여부 확인 후 저장
- Body (`ReplyCreateRequest`, `content` 최대 500자)

```json
{
  "content": "좋은 자료 감사합니다!"
}
```

- Response: `200 OK`
- Body: 생성된 댓글 ID (`number`)

```json
1
```

## 5.3 댓글 수정

- Method: `PUT`
- URI: `/api/v1/posts/{postId}/replies/{replyId}`
- 인증: 필수
- 권한: 작성자
- Body (`ReplyUpdateRequest`)

```json
{
  "content": "댓글 내용을 수정했습니다."
}
```

- Response: `200 OK` (바디 없음)

## 5.4 댓글 삭제

- Method: `DELETE`
- URI: `/api/v1/posts/{postId}/replies/{replyId}`
- 인증: 필수
- 권한: 작성자 또는 관리자
- Response: `200 OK` (바디 없음)

## 5.5 게시글 좋아요

- 등록: `POST /api/v1/posts/{postId}/likes`
- 취소: `DELETE /api/v1/posts/{postId}/likes`
- 인증: 필수
- Response: `200 OK` (바디 없음)

## 5.6 댓글 좋아요

- 등록: `POST /api/v1/posts/{postId}/replies/{replyId}/likes`
- 취소: `DELETE /api/v1/posts/{postId}/replies/{replyId}/likes`
- 인증: 필수
- Response: `200 OK` (바디 없음)

---

# 6. 팔로우 및 검색 API

## 6.1 팔로우 및 언팔로우

| 기능 | Method | URI | 인증 | 성공 응답 |
| --- | --- | --- | --- | --- |
| 팔로우 | POST | `/api/v1/members/{targetId}/follow` | 필수 | 200 |
| 언팔로우 | DELETE | `/api/v1/members/{targetId}/follow` | 필수 | 200 |
- Request Body: 없음
- 처리 규칙:
  - 자기 자신 및 차단 관계 사용자 팔로우 제한.
  - 중복 요청으로 관계나 인원이 중복 반영되지 않도록 처리.
- 팔로우 Response Body:

```json
{
  "targetId":2,
  "isFollowing":true,
  "followerCount":251,
  "memberId":5,
  "followingCount":121
}
```

- 언팔로우 Response Body:

```json
{
  "targetId":2,
  "isFollowing":false,
  "followerCount":250,
  "memberId":5,
  "followingCount":120,
}
```

## 6.2 사용자 검색

- Method: `GET`
- URI: `/api/v1/search/members`
- 인증 필요 여부: 선택
- Query Parameters:
  - `keyword`: 검색어, 필수
  - `page`, `size`
- 처리 규칙:
  - 닉네임 또는 사용자 아이디를 기준으로 검색.
  - 빈 검색어 요청 거부.
  - 로그인 상태에서는 차단 관계 사용자는 검색 결과에서 제외.
- Response Body (HTTP 200, MemberSearchResponse)

```json
{
	"content": [
		{
			"id": 1,
			"name": "스프링러버",
			"uniqueId": "springlover",
			"profileImage": "https://bucket.s3.amazonaws.com/profile/1.jpg"
		},
		{
			"id": 2,
			"name": "스프링개발자",
			"uniqueId": "springdeveloper",
			"profileImage": "https://bucket.s3.amazonaws.com/profile/2.jpg"
		}
	]
}
```

## 6.3 게시글 검색

- Method: `GET`
- URI: `/api/v1/search/posts`
- 인증 필요 여부: 선택
- Query Parameters:
  - `keyword`: 검색어, 필수
  - `page`, `size`
- 처리 규칙:
  - 본문 검색을 기준으로 제안.
  - 조회자가 접근 가능한 게시글만 반환.
  - 삭제·차단 관계 게시글 제외.
  - 구독자 전용 게시글은 해당 게시글에 접근 가능한 회원에게만 반환한다.
- Response Body (HTTP 200, PostSearchResponse)

```json
{
  "posts": [
    {
      "postId": 1,
      "memberId": 1,
      "memberName": "홍길동",
      "uniqueId": "hong123",
      "profileImageUrl": "https://bucket.s3.amazonaws.com/profiles/1.jpg",
      "content": "Spring Boot 프로젝트 개발 중입니다.",
      "mainImageUrl": "https://bucket.s3.amazonaws.com/posts/1-1.jpg",
      "likeCount": 3,
      "replyCount": 2,
      "likedByMe": false,
      "subscriberOnly": false,
      "createdAt": "2026-09-20T10:00:00"
    }
  ],
  "nextCursor": 1,
  "hasNext": true
}
```

---

# 7. 사용자 차단 API

## 7.1 사용자 차단

- Method: `PUT`
- URI: `/api/v1/members/{memberId}/block`
- 인증 필요 여부: 필수
- Request Body: 없음
- Response: `204 No Content`
- 처리 규칙:
  - 자기 자신 차단 불가.
  - 이미 차단한 경우 추가 저장하지 않음.
  - 양방향 팔로우 관계가 존재하면 해제.
  - 양측의 프로필·콘텐츠 노출 및 상호작용 제한.

## 7.2 차단 사용자 목록 조회

- Method: `GET`
- URI: `/api/v1/members/me/blocks`
- 인증 필요 여부: 필수
- Query Parameters: `page`, `size`
- Response Body: `200 OK`

```json
{
  "content": [
    {
      "memberId":8,
      "handle":"blocked_user",
      "nickname":"사용자8",
      "profileImageUrl":"https://cdn.example.com/profiles/8.jpg",
      "blockedAt":"2026-09-21T11:00:00+09:00"
    }
  ],
  "page":0,
  "size":20,
  "totalElements":1,
  "totalPages":1,
  "hasNext":false
}
```

- 설정 페이지에서 차단 해제에 필요한 최소 정보 제공.
- 차단 목록에 표시되어도 일반 프로필 접근은 제한.

## 7.3 사용자 차단 해제

- Method: `DELETE`
- URI: `/api/v1/members/{memberId}/block`
- 인증 필요 여부: 필수
- Response: `204 No Content`
- 처리 규칙:
  - 본인이 생성한 차단 관계만 해제.
  - 상대방도 본인을 차단한 상태라면 상대방의 차단은 유지.
  - 기존 팔로우 관계는 자동 복구하지 않음.

---

# 8. 크리에이터 API

## 8.1 크리에이터 승인

- Method: `POST`
- URI: `/api/v1/creators`
- 인증 필요 여부: 필수
- Request Body: 없음
- 처리 규칙:
  - 사용자의 크리에이터 신청을 승인한다.
  - 일정 조건(팔로워 10명 이상)을 충족시켜야 승인될 수 있다.
- Response: `204 No Content` (바디 없음)

## 8.2 내 구독자 목록 조회

- Method: `GET`
- URI: `/api/v1/creators`
- 인증 필요 여부: 필수
- 처리 규칙
  - 인증 정보의 사용자 ID로 해당 크리에이터의 구독자 목록을 조회한다.
  - 크리에이터가 아닌 사용자는 리스트가 없이 출력된다.
- Query Parameters:

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `cursor` | integer (int64) | 아니오 | 다음 페이지 커서 값. 첫 조회 시 생략 |
| `size` | integer (int32) | 아니오 | 기본 `10`, 최소 `3`, 최대 `20` |
- Response: `200 OK` (`PagingSubscriberListResponse`)

```json
{
  "subscriberList": [
    {
      "subscriptionId": 40,
      "creatorId": 2,
      "memberId": 6,
      "memberName": "구독자",
      "memberUniqueId": "subscriber_name",
      "profileImage": "<https://cdn.example.com/profiles/6.jpg>",
      "startDate": "2026-09-21T12:00:00",
      "endDate": "2026-10-21T12:00:00",
      "nextBillingAt": "2026-10-21T12:00:00"
    }
  ],
  "subscriberCount": 20,
  "nextCursor": 40,
  "hasNext": true
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `subscriberList` | `SubscriberResponse[]` | 구독자 목록 |
| `subscriberCount` | integer (int32) | 구독자 수 |
| `nextCursor` | integer (int64) | 다음 조회에 사용할 커서 |
| `hasNext` | boolean | 다음 페이지 존재 여부 |

---

# 9. 구독 및 결제 API

## 9.1 구독 등록

- Method: `POST`
- URI: `/api/v1/subscriptions`
- 인증 필요 여부: 필수
- 처리 규칙: 인증 정보와 요청 본문을 기반으로 구독을 등록한다.
- Request Body: 필수, `application/json` (`CreateSubscriptionRequest`)

```json
{
  "creatorId": 2,
  "customerKey": "customer-key-example",
  "authKey": "auth-key-example"
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `creatorId` | integer (int64) | 구독 대상 크리에이터 ID |
| `customerKey` | string | 결제 고객 키 |
| `authKey` | string | 결제 인증 키 |
- 요청 본문 자체는 필수이며, DTO의 개별 필드 필수 여부는 명세에 별도로 지정되어 있지 않다.
- Response: `201 Created` (`CreateSubscriptionResponse`)

```json
{
  "subscriptionId": 40,
  "orderName": "월 정기구독",
  "status": "DONE",
  "totalAmount": 4900,
  "approvedAt": "2026-09-21T12:00:00+09:00"
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `subscriptionId` | integer (int64) | 생성된 구독 ID |
| `orderName` | string | 주문명 |
| `status` | string | 처리 상태 |
| `totalAmount` | integer (int32) | 총 금액 |
| `approvedAt` | string | 승인 일시 |

## 9.2 내 구독 목록 조회

- Method: `GET`
- URI: `/api/v1/subscriptions`
- 인증 필요 여부: 필수
- 처리 규칙: 인증 정보의 사용자 ID로 구독 중인 크리에이터 목록을 조회한다.
- Query Parameters:

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `cursor` | integer (int64) | 아니오 | 다음 페이지 커서 값. 첫 조회 시 생략 |
| `size` | integer (int32) | 아니오 | 기본 `9`, 최소 `3`, 최대 `20` |
- Response: `200 OK` (`PagingSubListResponse`)

```json
{
  "subCreatorList": [
    {
      "subscriptionId": 43,
      "memberId": 6,
      "creatorId": 32,
      "creatorName": "주혜원",
      "creatorUniqueId": "hyewon.photo",
      "profileImage": "<https://cdn.example.com/profiles/32.jpg>",
      "introduction": "빛이 좋은 오후와 필름 사진을 좋아해요.",
      "status": "ACTIVE"
    }
  ],
  "subCreatorCount": 28,
  "nextCursor": 43,
  "hasNext": true
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `subCreatorList` | `SubscribeCreatorListResponse[]` | 구독 중인 크리에이터 목록 |
| `subCreatorCount` | integer (int32) | 구독 중인 크리에이터 수 |
| `nextCursor` | integer (int64) | 다음 조회에 사용할 커서 |
| `hasNext` | boolean | 다음 페이지 존재 여부 |

## 9.3 구독 내역 상세 조회

- Method: `GET`
- URI: `/api/v1/subscriptions/{id}`
- 인증 필요 여부: 필수
- Path Parameters: `id` — 구독 내역 ID, 필수, integer (int64)
- Request Body: 없음
- Response: `200 OK` (`SubscriptionDetailResponse`)

```json
{
  "subscriptionId": 43,
  "name": "주혜원",
  "uniqueId": "hyewon.photo",
  "email": "hyewon@example.com",
  "price": 4900,
  "status": "ACTIVE",
  "startDate": "2026-09-18T12:00:00",
  "endDate": "2026-10-18T12:00:00",
  "nextBillingAt": "2026-10-18T12:00:00"
}
```

- 구독 ID, 이름·사용자 아이디·이메일, 가격·상태, 구독 시작일·종료일·다음 결제 일시를 반환한다.
- `startDate`, `endDate`, `nextBillingAt`은 `date-time` 형식이다.

## 9.4 구독 시작일 검증

- Method: `GET`
- URI: `/api/v1/subscriptions/cancel/{id}`
- 인증 필요 여부: 필수
- Path Parameters: `id` — 구독 내역 ID, 필수, integer (int64)
- Request Body: 없음
- 처리 규칙: 구독 ID로 구독 시작일을 검증한다.
- Response: `200 OK` (`CheckBillingDateResponse`)

```json
{
  "billingDate": "2026-09-18T12:00:00"
}
```

- `billingDate`: 검증 결과로 반환하는 일시 (`string`). 환불 가능 여부를 나타내는 boolean 필드는 명세에 없다.

## 9.5 정기구독 해지

- Method: `POST`
- URI: `/api/v1/subscriptions/cancel/{id}`
- 인증 필요 여부: 필수
- Path Parameters: `id` — 구독 내역 ID, 필수, integer (int64)
- Request Body: 없음
- 처리 규칙: 구독 ID로 구독을 해지한다.
- Response: `202 Accepted` (`CancelSubscriptionResponse`)

```json
{
  "endDate": "2026-10-18T12:00:00"
}
```

- `endDate`: 구독 종료 일시 (`date-time`).

## 9.6 구독 환불

- Method: `POST`
- URI: `/api/v1/subscriptions/cancel/{id}/refund`
- 인증 필요 여부: 필수
- Path Parameters: `id` — 구독 내역 ID, 필수, integer (int64)
- Request Body: 없음
- 처리 규칙: 구독 ID로 구독을 환불한다.
- Response: `202 Accepted` (`RefundSubscriptionResponse`)

```json
{
  "totalAmount": 4900,
  "canceledAt": "2026-09-21T16:00:00+09:00"
}
```

- `totalAmount`: 환불 응답의 총 금액 (integer, int32).
- `canceledAt`: 결제 취소 일시 (`string`).

---

# 10. 신고 API

## 10.1 게시글·댓글 신고

- Method: `POST`
- URI: `/api/v1/reports`
- 인증 필요 여부: 필수
- Request Body:

```json
{
  "targetType":"POST",
  "targetId":101,
  "reason":"SPAM",
  "description":"동일한 광고가 반복해서 게시되고 있습니다."
}
```

- `targetType`: `POST`, `COMMENT`
- `reason`: `SPAM`, `ABUSE`, `INAPPROPRIATE`, `OTHER`
- 처리 규칙:
  - 신고 대상 존재 여부 및 접근 권한 검증.
  - 본인 콘텐츠 신고 제한.
  - 동일 회원의 동일 대상에 대한 처리 대기 신고 중복 접수 제한.
- Response Body: `201 Created`

```json
{
  "id":801,
  "status":"PENDING",
  "createdAt":"2026-09-21T14:00:00+09:00"
}
```

- 프론트 처리
  - 신고 기능은 별도의 화면 Route를 만들지 않고 게시글 또는 댓글 신고 버튼에서 팝업으로 처리한다.
    1. 신고 버튼 > 신고 사용 선택 > “신고하시겠습니까?” > [취소]
    2. 신고 버튼 > 신고 사용 선택 > “신고하시겠습니까?” > [신고]
  - 팝업 신고 및 신고 확인 UI : FrontEnd
  - 신고 데이터 저장 및 중복 신고 검증 : BackEnd

---

# 11. 관리자 API

## 11.1 신고 목록 및 상세 조회

| 기능 | Method | URI |
| --- | --- | --- |
| 신고 목록 조회 | GET | `/api/v1/admin/reports` |
| 신고 상세 조회 | GET | `/api/v1/admin/reports/{reportId}` |
- 목록 Query Parameters:
  - `targetType`: `POST`, `COMMENT`
  - `status`: `PENDING`, `REJECTED`, `RESOLVED`
  - `page`, `size`
- 상세 응답:
  - 신고자 및 신고 대상 작성자 정보
  - 신고 대상 본문·이미지
  - 신고 사유·상세 내용
  - 접수 일시·처리 상태
- Response: `200 OK`

## 11.2 신고 처리

- Method: `PATCH`
- URI: `/api/v1/admin/reports/{reportId}`
- 인증 필요 여부 : 필수
- 권한 :  `ADMIN`
- Path Parameter : `reportId : 신고 고유 ID`
- Request Body:

```json
{
  "status":"RESOLVED",
  "actionReason":"신고 내용을 확인하고 처리했습니다."
}
```

- `status`: `REJECTED`, `RESOLVED`
- 처리 대기 상태(PENDING)의 신고만 처리 가능
- 신고 처리 상태 변경과 콘텐츠 숨김·삭제는 별도 동작.
- 관리자·처리 시각·처리 사유 기록.
- Response Body: `200 OK`

```json
{
  "id":801,
  "status":"RESOLVED",
  "processedAt":"2026-09-21T15:00:00+09:00"
}
```

## 11.3 회원 목록 및 상세 조회

| 기능 | Method | URI |
| --- | --- | --- |
| 회원 목록 조회 | GET | `/api/v1/admin/members` |
| 회원 상세 조회 | GET | `/api/v1/admin/members/{memberId}` |
- 목록 Query Parameters:
  - `keyword`: 닉네임 또는 아이디
  - `memberStatus`: 회원 상태
  - `creatorStatus`: 크리에이터 상태
  - `page`, `size`
- Response: `200 OK`
- 상세 정보에 이메일, 가입일, 크리에이터 상태, 구독 상품 및 구독자 수 포함.

## 11.4 전체 구독·결제 조회

| 기능 | Method | URI |
| --- | --- | --- |
| 구독 목록 조회 | GET | `/api/v1/admin/subscriptions` |
| 구독 상세 조회 | GET | `/api/v1/admin/subscriptions/{subscriptionId}` |
| 결제 목록 조회 | GET | `/api/v1/admin/payments` |
| 결제 상세 조회 | GET | `/api/v1/admin/payments/{paymentId}` |
- 목록 Query Parameters:
  - `keyword`: 구매자 또는 판매자 닉네임·아이디
  - `status`: 구독 또는 결제 상태
  - `from`, `to`: 조회 기간
  - `page`, `size`
- Response: `200 OK`
- 결제 상세에는 주문번호, 결제 상태, 금액, 구매자·판매자, 연결된 구독 정보 포함.

## 11.5 환불 내역 조회

- Method: `GET`
- URI: `/api/v1/admin/payment`
- 인증 필요 여부: 필수
- 권한: `ADMIN`
- Query Parameters:
  - `paymentStatus`: `CANCELED`
  - `page`
  - `size`
- 처리 규칙:
  - 환불 처리된 결제는 기존 `payment` 데이터의 `status`를 `CANCELED`로 관리한다.
  - 별도의 환불 데이터를 생성하지 않고 기존 결제 내역에서 환불 상태를 조회한다.
  - 환불 내역 클릭 시 기존 결제 상세 조회 API를 사용한다.
- Response: `200 OK`
- Response Body 예시:

```json
{
  "id":901,
  "paymentId":90,
  "amount":4900,
  "status":"COMPLETED",
  "refundedAt":"2026-09-21T16:00:00+09:00"
}
```
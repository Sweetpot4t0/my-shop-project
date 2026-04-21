# 감자몰 (Potato Mall)
스프링 부트와 JPA를 활용한 감자 전문 쇼핑몰 프로젝트입니다.

## 주요 기능
- **사용자**: 회원가입/로그인, 상품 주문, 마이페이지(주문 내역 확인)
- **관리자**: 전용 대시보드, 전체 주문 현황 관리 (배송 상태 확인 가능)

## Tech Stack
- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA
- **Database**: PostgreSQL 18.3
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla JS)

## 💡 주요 해결 과제 (Troubleshooting)
### 1. JPA N+1 문제 해결 (Fetch Join)
- 관리자 페이지에서 전체 주문을 불러올 때, 주문자 정보를 가져오기 위해 수많은 쿼리가 발생하는 문제를 `JOIN FETCH`를 사용하여 1번의 쿼리로 최적화했습니다.

### 2. JSON 직렬화 이슈 (`@JsonIgnore`)
- `Order`와 `Member` 객체 참조 시 발생하는 이슈를 해결하기 위해 `@JsonIgnoreProperties`를 활용하여 필요한 데이터만 프론트엔드로 전달했습니다.

### 3. Role 기반 접근 제어
- 세션(Session) 정보를 활용하여 일반 사용자와 관리자의 UI/UX를 분리하고 보안을 강화했습니다.

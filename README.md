# 도메인 개념
## 지하철 역(station)
- 지하철 역 속성: 이름(name)

## 지하철 구간(section)
- 지하철 (상행 방향)역과 (하행 방향)역 사이의 연결 정보
지하철 구간 속성: 길이(distance)

## 지하철 노선(line)
- 지하철 구간의 모음으로 구간에 포함된 지하철 역의 연결 정보
지하철 노선 속성: 노선 이름(name), 노선 색(color)

# Step3 - 지하철 구간 등록 기능 구현
## 요구 사항
- [ ] 기능 구현 전 인수 테스트 작성
- [ ] 예외 케이스 처리 인수 테스트 작성

## 기능 구현 목록
- [x] 구간 등록하기 기능
  - [x] 인수 테스트 및 단위 테스트 구현
    - [x] 케이스1. 구간 요청 DTO 하행과 기존 구간 데이터의 상행 동일
    - [x] 케이스2. 구간 요청 DTO 상행과 기존 구간 데이터의 하행 동일
    - [x] 케이스3. 구간 요청 DTO 상행과 기존 구간 데이터의 상행 동일
    - [x] 케이스4. 구간 요청 DTO 하행과 기존 구간 데이터의 하행 동일
    - [x] 예외1. 구간에 중간 역 추가시 부족한 역간 거리
    - [x] 예외2. 연결 되지 않은 구간 등록
    - [x] 예외3. 이미 등록된 구간 등록
  
  - [x] 기능 구현
    - [x] 케이스1. 구간 요청 DTO 하행과 기존 구간 데이터의 상행 동일
    - [x] 케이스2. 구간 요청 DTO 상행과 기존 구간 데이터의 하행 동일
    - [x] 케이스3. 구간 요청 DTO 상행과 기존 구간 데이터의 상행 동일
    - [x] 케이스4. 구간 요청 DTO 하행과 기존 구간 데이터의 하행 동일
    - [x] 예외1. 구간에 중간 역 추가시 부족한 역간 거리
    - [x] 예외2. 연결 되지 않은 구간 등록
    - [x] 예외3. 이미 등록된 구간 등록
  
  - [x] 리팩터링
    - [X] 케이스1. 구간 요청 DTO 하행과 기존 구간 데이터의 상행 동일
    - [X] 케이스2. 구간 요청 DTO 상행과 기존 구간 데이터의 하행 동일
    - [X] 케이스3. 구간 요청 DTO 상행과 기존 구간 데이터의 상행 동일
    - [X] 케이스4. 구간 요청 DTO 하행과 기존 구간 데이터의 하행 동일
    - [X] 예외1. 구간에 중간 역 추가시 부족한 역간 거리
    - [X] 예외2. 연결 되지 않은 구간 등록
    - [X] 예외3. 이미 등록된 구간 등록
    - [x] 기존 테스트 코드 리팩토링

## 피드백 반영 목록
- [x] 구간 추가의 유효성 검사 내부 로직 내 else 키워드를 쓰는 결과 확인 메소드 추출
- [ ] 연결가능 역 수 확인 기능의 filter 내 메소드의 메소드 추출
- [ ] isOverlappedUpStationBetweenSection, connectUpStationBetweenSection 등 메소드 본문이 겹치는 메소드의 중복 제거
- [ ] Stations 내 stream 반환 로직 제거
- [ ] 구간 추가에서 사용되는 여러 메소드에 단위 테스트 구현

# Step2 - 인수 테스트 리팩터링
## 요구사항
- [x] 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
  - [x] 두 종점역은 구간의 형태로 관리되어야 함
- [x] 노선 객체가 구간 정보를 관리해야함
- [x] 노선 조회 시 응답 결과에 역 목록 추가하기
  - [x] 상행역 부터 하행역 순으로 정렬되어야 함
  
## 기능 구현 목록
- [x] 인수 테스트 수정
  - [x] 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
  - [x] 노선 조회 시 응답 결과에 역 목록 추가하기

- [x] 기능 구현
  - [x] 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
  - [x] 노선 조회 시 응답 결과에 역 목록 추가하기
  
- [x] 기능 리팩토링
  - [x] 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
  - [x] 노선 조회 시 응답 결과에 역 목록 추가하기

## 1차 피드백 반영 필요 목록
- [x] 상행역부터 하행역까지 순서대로 읽어오는 방법 고민후 적용
- [x] LineService 내 Repository 관련 참조 변수에 final 키워드 붙이기
- [x] LineService 내 Exception Throw 부분 중복 제거
- [x] StationResponse의 팩토리 메소드명 변경

## 2차 피드백 반영 목록
- [x] EntityNotFoundException 원복
  - [x] 예외 메시지 상수 처리
- [x] Distance 클래스 내 value 접근 지정자 변경
- [x] 구간을 톻한 역 목록 조회 구조 개선
- [x] @ResponseBody @ControllerAdvice -> @RestControllerAdvice
- [x] 미사용 메소드 삭제
- [x] findByIdWithUnWrapped 메소드의 이름을 getById로 변경

## 추가 개선 목록
- [x] 지하철 역 인수 테스트 내 주요 메소드의 별도 클래스 분리
---

# Step1 - 지하철 노선 관리
## 요구사항
- [x] 지하철 노선 관련 기능의 인수 테스트를 작성하기(LineAcceptanceTest)
  - [x] 기능 목록: 생성 / 목록 조회 / 조회 / 수정 / 삭제 
  - [x] 기능 구현 전 인수 테스트 작성 
  - [x] 기능 구현 후 인수 테스트 리팩터링

## 기능 구현 목록
- [x] 인수 테스트 작성
  - [x] 노선 생성
  - [x] 노선 생성 예외, 중복된 이름
  - [x] 노선 목록 조회
  - [x] 노선 목록 조회 예외, 데이터 없는 경우
  - [x] 노선 목록 검색
  - [x] 노선 목록 검색 예외, 데이터 없는 경우
  - [X] 노선 ID 조건 조회
  - [X] 노선 ID 조건 조회 예외, 데이터 없는 경우
  - [x] 노선 수정
  - [x] 노선 수정 예외, 데이터 없는 경우
  - [x] 노선 수정 예외, 중복된 이름
  - [x] 노선 삭제
    
- [x] 기능 구현
  - [x] 노선 생성
  - [x] 노선 생성 예외, 중복된 이름
  - [x] 노선 목록 조회
  - [x] 노선 목록 조회 예외, 데이터 없는 경우
  - [x] 노선 목록 검색
  - [x] 노선 목록 검색 예외, 데이터 없는 경우
  - [X] 노선 ID 조건 조회
  - [X] 노선 ID 조건 조회 예외, 데이터 없는 경우
  - [x] 노선 수정
  - [x] 노선 수정 예외, 데이터 없는 경우
  - [x] 노선 수정 예외, 중복된 이름
  - [x] 노선 삭제
    
- [x] 기능 리팩토링
  - [x] 노선 생성
  - [x] 노선 생성 예외, 중복된 이름
  - [x] 노선 목록 조회
  - [x] 노선 목록 조회 예외, 데이터 없는 경우
  - [x] 노선 목록 검색
  - [x] 노선 목록 검색 예외, 데이터 없는 경우
  - [x] 노선 ID 조건 조회
  - [x] 노선 ID 조건 조회 예외, 데이터 없는 경우
  - [x] 노선 수정
  - [x] 노선 수정 예외, 데이터 없는 경우
  - [x] 노선 수정 예외, 중복된 이름
  - [x] 노선 삭제

- [x] 테스트 리팩토링
  - [x] ExtractableResponse
    - [x] 단일 응답에 대해 Location 헤더를 사용한 id 추출 기능 분리 및 적용
    - [x] 여러 응답에 대해 Location 헤더를 사용한 id 추출 기능 분리 및 적용
  - [x] LinesResponse
    - [x] getIds() 구현 및 적용
  
# 피드백 목록
- [x] Line id 조건 조회 절에 NPE 대신 EntityNotFoundException 적용
- [x] EntityNotFoundException에 해당하는 ControllerAdvice 메소드 구현
- [x] Line 서비스에서 update 처리하도록 수정
- [x] Bean Validation 적용
- [X] Line 목록 조회시 204 No content 대신 빈 배열 반환 처리
- [x] LineControllerTest 클래스 명을 LineControllerTestSnippet 으로 변경
- [x] 불필요한 getIds() 제거
---

<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.

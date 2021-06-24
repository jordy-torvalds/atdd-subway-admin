package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LinesResponse;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.ExtractableResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.ui.LineControllerTestSnippet.*;
import static nextstep.subway.station.ui.StationControllerTestSnippet.지하철_역_생성_요청;
import static nextstep.subway.utils.ExtractableResponseUtil.extractIdInResponses;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private final int 기본_역간_거리 = 30;

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 서울대입구역;
    private StationResponse 신도림역;
    private StationResponse 사당역;
    private StationResponse 영등포구청역;

    @BeforeEach
    void 지하철_역_생성() {
        강남역 = 지하철_역_생성_요청(new StationRequest("강남역")).as(StationResponse.class);
        역삼역 = 지하철_역_생성_요청(new StationRequest("역삼역")).as(StationResponse.class);
        서울대입구역 = 지하철_역_생성_요청(new StationRequest("서울대입구역")).as(StationResponse.class);
        신도림역 = 지하철_역_생성_요청(new StationRequest("신도림역")).as(StationResponse.class);
        사당역 = 지하철_역_생성_요청(new StationRequest("사당역")).as(StationResponse.class);
        영등포구청역 = 지하철_역_생성_요청(new StationRequest("영등포구청역")).as(StationResponse.class);
    }

    @DisplayName("지하철_중복_노선_생성_예외_중복된_이름")
    @Test
    void 지하철_중복_노선_생성_예외_중복된_이름() {
        // given
        ExtractableResponse<Response> firstResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));

        // when
        ExtractableResponse<Response> secondResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));

        // then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철_노선_구간정보와_함께_생성_성공")
    @Test
    void 지하철_노선_생성_성공() {
        // When
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        LineResponse createdLine = createLineResponse.as(LineResponse.class);
        List<Long> stationIdsInLine = createdLine.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createLineResponse.header("Location")).isNotBlank();
        assertThat(stationIdsInLine).containsAll(Arrays.asList(강남역.getId(), 역삼역.getId()));
    }

    @DisplayName("지하철_노선_목록_조회_성공")
    @Test
    void 지하철_노선_목록_조회_성공() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(new LineRequest("2호선", "00FF00", 신도림역.getId(), 서울대입구역.getId(), 기본_역간_거리));

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        List<Long> expectedLineIds = extractIdInResponses(createResponse1, createResponse2);
        List<Long> resultLineIds = response.jsonPath().getList("lineResponses", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철_노선_목록_조회_성공_데이터없음")
    @Test
    void 지하철_노선_목록_조회_성공_데이터없음() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();
        LinesResponse expectedResult = new LinesResponse(response.jsonPath().getList("lineResponses", LineResponse.class));

        // then
        assertThat(expectedResult.size()).isZero();
        assertThat(expectedResult.isEmpty()).isTrue();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철_노선_검색_성공")
    @Test
    void 지하철_노선_검색_성공() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(new LineRequest("2호선", "00FF00", 신도림역.getId(), 서울대입구역.getId(), 기본_역간_거리));
        ExtractableResponse<Response> createResponse3 = 지하철_노선_생성_요청(new LineRequest("3호선", "00FF00", 강남역.getId(), 서울대입구역.getId(), 기본_역간_거리));
        List<Long> expectedResult = extractIdInResponses(createResponse2, createResponse3);

        // when
        ExtractableResponse<Response> response = 지하철_노선_검색_요청(new LineRequest("", "00FF00"));
        List<Long> actualResult = response.jsonPath().getList("lineResponses", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualResult).containsAll(expectedResult);
    }

    @DisplayName("지하철_노선_검색_성공_데이터없음")
    @Test
    void 지하철_노선_검색_성공_데이터없음() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_검색_요청(new LineRequest("", "00FF00"));
        LinesResponse expectedResult = new LinesResponse(response.jsonPath().getList("lineResponses", LineResponse.class));

        // then
        assertThat(expectedResult.size()).isZero();
        assertThat(expectedResult.isEmpty()).isTrue();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철_노선_PK_조건_조회_성공")
    @Test
    void 지하철_노선_PK_조건_조회_성공() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));

        LineResponse expectedResult = createResponse.jsonPath().getObject(".", LineResponse.class);
        Long savedId = ExtractableResponseUtil.extractIdInResponse(createResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_PK_조건_조회_요청(savedId);
        LineResponse actualResult = response.jsonPath().getObject(".", LineResponse.class);
        List<Long> stationIdsInLine = actualResult.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
                () -> assertThat(actualResult.getId()).isEqualTo(expectedResult.getId()),
                () -> assertThat(actualResult.getName()).isEqualTo(expectedResult.getName()),
                () -> assertThat(actualResult.getColor()).isEqualTo(expectedResult.getColor())
        );

        assertThat(stationIdsInLine).containsAll(Arrays.asList(강남역.getId(), 역삼역.getId()));
    }

    @DisplayName("지하철_노선_PK_조건_조회_성공_데이터없음")
    @Test
    void 지하철_노선_PK_조건_조회_성공_데이터없음() {
        // given
        Long targetId = Long.MAX_VALUE;

        // when
        ExtractableResponse<Response> response = 지하철_노선_PK_조건_조회_요청(targetId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철_노선_수정_성공")
    @Test
    void 지하철_노선_수정_성공() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        Long savedId = ExtractableResponseUtil.extractIdInResponse(createResponse);

        // when
        LineRequest updateRequest = new LineRequest("1호선", "0000FF");
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(savedId, updateRequest);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateResponse.jsonPath().<String>get("name")).isEqualTo(updateRequest.getName());
        assertThat(updateResponse.jsonPath().<String>get("color")).isEqualTo(updateRequest.getColor());
    }

    @DisplayName("지하철_노선_수정_예외_존재하지_않는_PK")
    @Test
    void 지하철_노선_수정_예외_존재하지_않는_PK() {
        // when
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(Long.MAX_VALUE, new LineRequest("1호선", "0000FF"));

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철_노선_수정_예외_중복된_이름")
    @Test
    void 지하철_노선_수정_예외_수정_중복된_이름() {
        // given
        지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));

        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(new LineRequest("2호선", "00FF00", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        Long savedId2 = ExtractableResponseUtil.extractIdInResponse(createResponse2);

        // when
        LineRequest updateRequest = new LineRequest("1호선", "0000FF");
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(savedId2, updateRequest);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철_노선_제거_성공")
    @Test
    void 지하철_노선_제거_성공() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        Long savedId = ExtractableResponseUtil.extractIdInResponse(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(savedId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철_노선_제거_예외_존재하지_않는_PK")
    @Test
    void 지하철_노선_제거_예외_존재하지_않는_PK() {
        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(Long.MAX_VALUE);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철_노선_구간_추가")
    @Test
    void 지하철_노선_구간_추가() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        Long savedLineId = ExtractableResponseUtil.extractIdInResponse(createResponse);

        // when
        ExtractableResponse<Response> addingSectionResponse = 지하철_노선_구간_추가_요청(new SectionRequest(savedLineId, 서울대입구역.getId(), 역삼역.getId(), 기본_역간_거리));

        // then
        assertThat(addingSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // 정렬된 노선 반환 확인 테스트
    }

    @DisplayName("지하철_노선_여러_구간_추가시_역_정렬_확인")
    @Test
    void 지하철_노선_여러_구간_추가시_역_정렬_확인() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(new LineRequest("1호선", "FF0000", 영등포구청역.getId(), 강남역.getId(), 기본_역간_거리));
        Long savedLineId = ExtractableResponseUtil.extractIdInResponse(createResponse);

        // when
        지하철_노선_구간_추가_요청(new SectionRequest(savedLineId, 강남역.getId(), 역삼역.getId(), 기본_역간_거리));
        지하철_노선_구간_추가_요청(new SectionRequest(savedLineId, 역삼역.getId(), 신도림역.getId(), 기본_역간_거리));
        지하철_노선_구간_추가_요청(new SectionRequest(savedLineId, 신도림역.getId(), 서울대입구역.getId(), 기본_역간_거리));
        ExtractableResponse<Response> addingSectionResponse = 지하철_노선_구간_추가_요청(new SectionRequest(savedLineId, 서울대입구역.getId(), 사당역.getId(), 기본_역간_거리));

        // then
        // 영등포구청역-강남역-역삼역-신도림역-서울대입구역-사당역 순서로 정렬 되어 있는지 확인
    }
}

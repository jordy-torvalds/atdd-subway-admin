package nextstep.subway.line.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineControllerTest {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse 지하철_노선_검색_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .when()
                .formParam("name", lineRequest.getName())
                .formParam("color", lineRequest.getColor())
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse 지하철_노선_PK_조건_조회_요청(Long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse 지하철_노선_수정_요청(Long id, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/"+ id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse 지하철_노선_삭제_요청(LineRequest deleteRequest) {
        return RestAssured.given().log().all()
                .body(deleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines")
                .then().log().all()
                .extract();
    }
}
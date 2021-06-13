package nextstep.subway.line.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nextstep.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LinesResponse {

    private List<LineResponse> lineResponses;

    public LinesResponse(List<LineResponse> lineResponses) {
        this.lineResponses = lineResponses;
    }

    public static LinesResponse of(List<Line> lines) {
        List<LineResponse> lineResponses = lines.stream()
                .map((line) -> (LineResponse.of(line)))
                .collect(Collectors.toList());

        return new LinesResponse(lineResponses);
    }

    public int size() {
        return this.lineResponses.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.lineResponses.isEmpty();
    }

    @JsonIgnore
    public List<Long> getIds() {
        return lineResponses.stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    public List<LineResponse> getLineResponses() {
        return lineResponses;
    }
}

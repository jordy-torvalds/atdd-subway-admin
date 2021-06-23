package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LinesResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new LineNotFoundException(request.getUpStationId()));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new LineNotFoundException(request.getDownStationId()));
        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    public LineResponse getLineById(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        return LineResponse.of(line.orElseThrow(() -> new LineNotFoundException(id)));
    }

    public LinesResponse getLines(LineRequest lineRequest) {
        return LinesResponse.of(lineRepository.findByNameContainingAndColorContaining(lineRequest.getName(), lineRequest.getColor()));
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Optional<Line> line = lineRepository.findById(id);
        Line updatingLine = line.orElseThrow(() -> new LineNotFoundException(id));
        updatingLine.update(lineRequest.getName(), lineRequest.getColor());

        return LineResponse.of(lineRepository.save(updatingLine));
    }

    public void deleteLine(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        Line deletingLine = line.orElseThrow(() -> new LineNotFoundException(id));
        lineRepository.delete(deletingLine);
    }
}

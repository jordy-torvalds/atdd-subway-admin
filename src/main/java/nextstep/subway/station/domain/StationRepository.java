package nextstep.subway.station.domain;

import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    default Station findByIdWithUnWrapped(Long id) {
        Optional<Station> optionalStation = findById(id);
        return optionalStation.orElseThrow(()-> new StationNotFoundException(id));
    }
}
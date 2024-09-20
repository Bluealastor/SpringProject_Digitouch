package it.digitouch.videonoleggio.service;

import it.digitouch.videonoleggio.dto.FilmNoleggioDTO;
import it.digitouch.videonoleggio.model.FilmModel;
import it.digitouch.videonoleggio.model.FilmNoleggioModel;
import it.digitouch.videonoleggio.model.NoleggioModel;
import it.digitouch.videonoleggio.repository.FilmNoleggioRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class FilmNoleggioService {

    private final FilmNoleggioRepository filmNoleggioRepository;

    public FilmNoleggioModel saveFilmNoleggio(FilmNoleggioDTO filmNoleggioDTO) {
        FilmNoleggioModel filmNoleggio = new FilmNoleggioModel();
        filmNoleggio.setFilm(new FilmModel(filmNoleggioDTO.getFilmId()));
        filmNoleggio.setNoleggio(new NoleggioModel(filmNoleggioDTO.getNoleggioId()));
        return filmNoleggioRepository.save(filmNoleggio);
    }
}


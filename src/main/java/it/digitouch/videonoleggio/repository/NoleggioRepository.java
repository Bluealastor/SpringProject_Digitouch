package it.digitouch.videonoleggio.repository;

import it.digitouch.videonoleggio.model.FilmModel;
import it.digitouch.videonoleggio.model.NoleggioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoleggioRepository extends JpaRepository<NoleggioModel, Long>{
    Optional<NoleggioModel> findByhashNoleggio(String hashCode);
    void deleteByHashNoleggio(String hashCode);
}

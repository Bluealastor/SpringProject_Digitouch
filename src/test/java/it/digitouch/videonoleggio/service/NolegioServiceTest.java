package it.digitouch.videonoleggio.service;

import it.digitouch.videonoleggio.dto.NoleggioDTO;
import it.digitouch.videonoleggio.exception.ElementAlreadyFoundException;
import it.digitouch.videonoleggio.exception.ElementNotFoundException;
import it.digitouch.videonoleggio.model.NoleggioModel;
import it.digitouch.videonoleggio.repository.NoleggioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NolegioServiceTest {

    @Mock
    private NoleggioRepository noleggioRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    private NoleggioService noleggioService;


    @Test
    void simpleTest(){
        Assertions.assertTrue(true);
    }

    /********************************
     *   INIZIO TEST SAVE NOLEGGIO  *
     ********************************/

    @Test
    void saveNoleggio_ok() {


        when(noleggioRepository.findByhashNoleggio(ArgumentMatchers.any())).thenReturn(Optional.empty());
        when(modelMapper.map(ArgumentMatchers.any(), eq(NoleggioModel.class))).thenReturn(getNoleggioModel());
        when(modelMapper.map(ArgumentMatchers.any(), eq(NoleggioDTO.class))).thenReturn(getNoleggioDTO());

        NoleggioDTO result = noleggioService.saveNoleggio(getNoleggioDTO());

        // verify SPECIFICO PER CONTROLLO CHE IL METODO VENGA CHIAMATO
        // DAL MOCK DI NOLEGGIO REPOSITORY E CHE VENGA ESEGUITO CORRETTAMENTE
        // CONTROLLO IN PIù DA FARE
        verify(noleggioRepository).save(getNoleggioModel());
        verify(noleggioRepository).save(ArgumentMatchers.any());
        //TODO DA APPROFONDIRE CON VERIFY times(), never(), atLeast()


        assertNotNull(result);
    }


    @Test
    void saveNoleggio_ko() {

        when(noleggioRepository.findByhashNoleggio(ArgumentMatchers.any())).thenReturn(Optional.empty());
        when(modelMapper.map(ArgumentMatchers.any(), eq(NoleggioModel.class))).thenReturn(getNoleggioModel());

        when(noleggioRepository.save(ArgumentMatchers.any()))
                .thenThrow(new ElementAlreadyFoundException("Noleggio Con codice YmVsbG9QYXBlcmlub1BhcGVyb3BvbGk= esiste già"));

        ElementAlreadyFoundException exception = assertThrows(ElementAlreadyFoundException.class,
                () -> noleggioService.saveNoleggio(getNoleggioDTO()));

        assertEquals("Noleggio Con codice " + getNoleggioDTO().getHashNoleggio() + " esiste già", exception.getMessage());

    }

    /********************************
     *   FINE TEST SAVE NOLEGGIO    *
     ********************************/

    /************************************************************************************/

    /********************************
     *   INIZIO TEST GET ALL FILMS    *
     *********************************/
    @Test
    void getAllNoleggi_ok() {
        // Noleggi di esempio
        NoleggioModel noleggio1 = new NoleggioModel();
        NoleggioModel noleggio2 = new NoleggioModel();

        // Lista di noleggi
        List<NoleggioModel> noleggioList = Arrays.asList(noleggio1, noleggio2);

        // Configura il Pageable e la pagina
        Pageable pageable = PageRequest.of(0, 10);
        Page<NoleggioModel> noleggioPage = new PageImpl<>(noleggioList, pageable, noleggioList.size());

        // Configurazione del mock per restituire la pagina
        when(noleggioRepository.findAll(pageable)).thenReturn(noleggioPage);

        // Configura il mapper per mappare ogni NoleggioModel a NoleggioDTO
        when(modelMapper.map(noleggio1, NoleggioDTO.class)).thenReturn(getNoleggioDTO());
        when(modelMapper.map(noleggio2, NoleggioDTO.class)).thenReturn(getNoleggioDTO());

        // Esegui il metodo con paginazione
        List<NoleggioDTO> result = noleggioService.getAllNoleggi(pageable);

        // Verifica che la pagina sia stata restituita correttamente
        verify(noleggioRepository, times(1)).findAll(pageable);

        // Verifiche
        assertNotNull(result);  // Verifica che il risultato non sia null
        assertEquals(2, result.size());  // Verifica che la dimensione sia corretta
    }


    @Test
    void getAllNoleggi_ko() {
        NoleggioModel noleggio1 = new NoleggioModel();
        NoleggioModel noleggio2 = new NoleggioModel();
        List<NoleggioModel> noleggioList = Arrays.asList(noleggio1, noleggio2);

        // Configura il Pageable e la pagina
        Pageable pageable = PageRequest.of(0, 10);
        Page<NoleggioModel> noleggioPage = new PageImpl<>(noleggioList, pageable, noleggioList.size());

        // Configurazione del mock per restituire la pagina
        when(noleggioRepository.findAll(pageable)).thenReturn(noleggioPage);

        when(modelMapper.map(noleggio1, NoleggioDTO.class)).thenThrow(new RuntimeException("Mapping Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            noleggioService.getAllNoleggi(pageable);
        });

        assertEquals("Mapping Error", exception.getMessage());

        verify(noleggioRepository).findAll(pageable);
    }

    /********************************
     *   FINE TEST GET ALL FILMS    *
     ********************************/

    /************************************************************************************/

    /********************************
     * INIZIO TEST GET ID NOLEGGIO  *
     ********************************/

    @Test
    void getNoleggioById_ok() {

        when(noleggioRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(getNoleggioModel()));
        when(modelMapper.map(ArgumentMatchers.any(),eq(NoleggioDTO.class))).thenReturn(getNoleggioDTO());

        NoleggioDTO result = noleggioService.getNoleggioById(1L);

        assertNotNull(result);
}

    @Test
    void getNoleggioById_ko() {

        when(noleggioRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            noleggioService.getNoleggioById(ArgumentMatchers.anyLong());
        });

        assertEquals("Noleggio: " + 0l + " Not Found", exception.getMessage());

        verify(noleggioRepository, times(1)).findById(ArgumentMatchers.anyLong());
    }

    /********************************
     *  FINE TEST GET ID NOLEGGIO   *
     ********************************/

    @Test
    void deleteFilmByHash_ok(){
        var optionalOngoingStubbing = when(noleggioRepository.findByhashNoleggio(ArgumentMatchers.anyString())).thenReturn(Optional.of(getNoleggioModel()));
        noleggioService.deleteNoleggioByHash(ArgumentMatchers.anyString());
        // verify utilizzato per verificare se il metodo viene chiamato
        // con times indico quante volte deve essere chiamate
        verify(noleggioRepository, times(1)).deleteByHashNoleggio(ArgumentMatchers.anyString());

    }



    /************************************************************************************/

    /********************************
     * INIZIO TEST DELETE ID FILM   *
     ********************************/
    @Test
    void deleteFilmById_ok(){
        when(noleggioRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getNoleggioModel()));
        noleggioService.deleteNoleggioById(ArgumentMatchers.anyLong());
        // verify utilizzato per verificare se il metodo viene chiamato
        // con times indico quante volte deve essere chiamate
        verify(noleggioRepository, times(1)).deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    public void deleteFilmById_ko() {

        when(noleggioRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            noleggioService.deleteNoleggioById(ArgumentMatchers.anyLong());
        });

        assertEquals("Noleggio con id: 0 non trovato", exception.getMessage());
    }

    /********************************
     *   FINE TEST DELETE ID FILM   *
     ********************************/


    /********************************************************************/

    /********************************
     *INIZIO TEST UPDATE ID NOLEGGIO*
     ********************************/

    @Test
    void updateNoleggio_ok() {
        NoleggioDTO noleggioDTO = new NoleggioDTO();
        noleggioDTO.setNome("Nuovo Nome");
        noleggioDTO.setTitolare("Nuovo Titolare");
        noleggioDTO.setCitta("Nuova Città");

        NoleggioModel noleggioModel = new NoleggioModel();
        noleggioModel.setNome("Vecchio Nome");
        noleggioModel.setTitolare("Vecchio Titolare");
        noleggioModel.setCitta("Vecchia Città");

        when(noleggioRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(noleggioModel));

        when(noleggioRepository.findByhashNoleggio(noleggioDTO.getHashNoleggio()))
                .thenReturn(Optional.empty());

        when(modelMapper.map(ArgumentMatchers.any(), eq(NoleggioDTO.class))).thenReturn(noleggioDTO);

        // Eseguiamo l'aggiornamento
        NoleggioDTO result = noleggioService.updateNoleggio(ArgumentMatchers.anyLong(), noleggioDTO);

        // Verifichiamo che il risultato contenga i nuovi valori
        assertNotNull(result);
        assertEquals(noleggioDTO.getNome(), result.getNome());
        assertEquals(noleggioDTO.getTitolare(), result.getTitolare());
        assertEquals(noleggioDTO.getCitta(), result.getCitta());
        assertEquals(noleggioDTO.getHashNoleggio(), result.getHashNoleggio());

        // Verifichiamo che il noleggio esistente sia stato aggiornato correttamente
        assertEquals("Nuovo Nome", noleggioModel.getNome());
        assertEquals("Nuovo Titolare", noleggioModel.getTitolare());
        assertEquals("Nuova Città", noleggioModel.getCitta());
        assertEquals("TnVvdm8gTm9tZU51b3ZvIFRpdG9sYXJlTnVvdmEgQ2l0dMOg", noleggioModel.getHashNoleggio());

        // Verifichiamo che il noleggio sia stato salvato nel repository
        verify(noleggioRepository).save(noleggioModel);
    }


    @Test
    void updateNoleggio_ko() {
        when(noleggioRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            noleggioService.updateNoleggio(ArgumentMatchers.anyLong(), getNoleggioDTO());});

        assertEquals("Noleggio: " + 0L + " Not Found", exception.getMessage());

        verify(noleggioRepository, times(1)).findById(ArgumentMatchers.anyLong());
        verify(noleggioRepository, never()).save(any());
    }

    /********************************
     * FINE TEST UPDATE ID NOLEGGIO *
     ********************************/




    private NoleggioDTO getNoleggioDTO(){
        return NoleggioDTO
                .builder()
                .nome("bello")
                .titolare("Paperino")
                .citta("Paperopoli")
                .build();
    }


    private NoleggioModel getNoleggioModel(){
        return NoleggioModel
                .builder()
                .nome("bello")
                .titolare("Paperino")
                .citta("Paperopoli")
                .build();
    }


}

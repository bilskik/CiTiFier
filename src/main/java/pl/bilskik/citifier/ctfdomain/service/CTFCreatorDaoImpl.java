package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.exception.CTFCreatorException;
import pl.bilskik.citifier.ctfdomain.mapper.CTFCreatorMapper;
import pl.bilskik.citifier.ctfdomain.repository.CTFCreatorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CTFCreatorDaoImpl implements CTFCreatorDao {

    private final CTFCreatorRepository ctfCreatorRepository;
    private final CTFCreatorMapper mapper;

    @Override
    public List<CTFCreatorDTO> findAll() {
        return ctfCreatorRepository
                .findAll()
                .parallelStream()
                .map(mapper::toCTFCreatorDTO)
                .toList();
    }

    @Override
    public CTFCreatorDTO findById(Long id) {
        Optional<CTFCreator> optionalCTFCreator = ctfCreatorRepository.findById(id);

        return optionalCTFCreator.map(mapper::toCTFCreatorDTO).orElse(null);
    }

    @Override
    public CTFCreatorDTO findByLogin(String login) {
        Optional<CTFCreator> optionalCTFCreatorDTO = ctfCreatorRepository.findByLogin(login);

        return optionalCTFCreatorDTO.map(mapper::toCTFCreatorDTO).orElse(null);
    }

    @Override
    @Transactional
    public CTFCreatorDTO createCTFCreator(CTFCreatorDTO ctfCreatorDTO) {
        log.info("Creating new CTF user");
        CTFCreator ctfCreator = mapper.toCTFCreator(ctfCreatorDTO);
        if(ctfCreator == null) {
            throw new CTFCreatorException("CTFCreatorDTO cannot be null");
        }

        ctfCreatorRepository.save(ctfCreator);
        log.info("CTFCreator has been saved!");

        return ctfCreatorDTO;
    }
}

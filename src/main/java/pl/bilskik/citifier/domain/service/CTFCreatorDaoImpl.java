package pl.bilskik.citifier.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.domain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.domain.entity.CTFCreator;
import pl.bilskik.citifier.domain.exception.CTFCreatorException;
import pl.bilskik.citifier.domain.mapper.CTFCreatorMapper;
import pl.bilskik.citifier.domain.repository.CTFCreatorRepository;

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

        return optionalCTFCreatorDTO.map(mapper::toCTFCreatorDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public CTFCreatorDTO createCTFCreator(CTFCreatorDTO ctfCreatorDTO) {
        log.info("Creating new CTF creator");
        CTFCreator ctfCreator = mapper.toCTFCreator(ctfCreatorDTO);
        if(ctfCreator == null) {
            throw new CTFCreatorException("CTFCreatorDTO cannot be null");
        }

        ctfCreatorRepository.save(ctfCreator);
        log.info("CTFCreator has been saved!");

        return ctfCreatorDTO;
    }
}

package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;

import java.util.List;

public interface CTFCreatorDao {

    CTFCreatorDTO findById(Long id);

    CTFCreatorDTO findByLogin(String login);

    List<CTFCreatorDTO> findAll();

    CTFCreatorDTO createCTFCreator(CTFCreatorDTO ctfCreatorDTO);

}

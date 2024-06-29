package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;

import java.util.List;

public interface CTFCreatorDao {

    List<CTFCreatorDTO> findAll();

    CTFCreatorDTO findById(Long id);

    CTFCreatorDTO createCTFCreator(CTFCreatorDTO ctfCreatorDTO);

}

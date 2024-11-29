package pl.bilskik.citifier.domain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.domain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.domain.entity.CTFCreator;

@Mapper(componentModel = "spring")
public interface CTFCreatorMapper {

    CTFCreatorDTO toCTFCreatorDTO(CTFCreator ctfCreator);

    CTFCreator toCTFCreator(CTFCreatorDTO ctfCreatorDTO);

}

package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;

@Mapper(componentModel = "spring")
public interface CTFCreatorMapper {

    CTFCreatorDTO toCTFCreatorDTO(CTFCreator ctfCreator);

    CTFCreator toCTFCreator(CTFCreatorDTO ctfCreatorDTO);

}

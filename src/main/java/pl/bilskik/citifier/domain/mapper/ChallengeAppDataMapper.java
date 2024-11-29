package pl.bilskik.citifier.domain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.domain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.domain.entity.ChallengeAppData;

@Mapper(componentModel = "spring")
public interface ChallengeAppDataMapper {

    ChallengeAppDataDTO toChallengeAppDataDTO(ChallengeAppData challengeAppData);

    ChallengeAppData toChallengeAppData(ChallengeAppDataDTO challengeAppDataDTO);
}

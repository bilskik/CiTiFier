package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.ctfcreator.challengedetails.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.entity.ChallengeAppData;

@Mapper(componentModel = "spring")
public interface ChallengeAppDataMapper {

    ChallengeAppDataDTO toChallengeAppDataDTO(ChallengeAppData challengeAppData);

    ChallengeAppData toChallengeAppData(ChallengeAppDataDTO challengeAppDataDTO);
}

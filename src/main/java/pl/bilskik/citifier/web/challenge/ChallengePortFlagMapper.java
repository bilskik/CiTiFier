package pl.bilskik.citifier.web.challenge;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class ChallengePortFlagMapper {

    private final static int FLAG_LENGTH = 8;

    public Map<Integer, String> map(int initialPort, int numberOfApp) {
        Set<String> flags = new HashSet<>();

        while(flags.size() < numberOfApp) {
            String flag = generateFlag();
            flags.add(flag);
        }

        Map<Integer, String> portFlags = new HashMap<>();
        List<String> flagList = new ArrayList<>(flags);
        for(int i=0; i<flagList.size(); i++) {
            portFlags.put(initialPort+i, flagList.get(i));
        }

        return portFlags;
    }

    private String generateFlag() {
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int i=0; i<FLAG_LENGTH; i++) {
            int randomAscii = secureRandom.nextInt(93) + 33; //<33;125>
            char randomChar = (char)randomAscii;
            sb.append(randomChar);
        }

        return sb.toString();
    }
}

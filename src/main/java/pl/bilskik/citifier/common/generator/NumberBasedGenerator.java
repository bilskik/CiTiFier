package pl.bilskik.citifier.common.generator;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service("numberBasedGenerator")
public class NumberBasedGenerator implements IdGenerator {

    private final static int MINIMUM = 1000000;
    private final static int MAXIMUM = 10000000;

    @Override
    public String generateId(List<String> codes) {
        Integer generatedCode = 0;
        do {
            generatedCode = ThreadLocalRandom.current().nextInt(MINIMUM, MAXIMUM);
        } while (codes.contains(generatedCode.toString()));

        return generatedCode.toString();
    }
}

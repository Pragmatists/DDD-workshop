package pl.pragmatists.workshop.users.infrastructure;

import org.springframework.stereotype.Component;
import pl.pragmatists.workshop.users.domain.IdGenerator;

import java.util.UUID;

@Component
public class UidIdGenerator implements IdGenerator {

    @Override
    public String id() {
        return UUID.randomUUID().toString();
    }
}

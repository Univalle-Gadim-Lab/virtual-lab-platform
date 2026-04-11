package edu.univalle.gadim.virtual_lab_platform.commons.tool;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component("uuid")
public class UuidGenerator implements UniqueIdGenerator {
  public String generate() {
    return UUID.randomUUID().toString();
  }
}


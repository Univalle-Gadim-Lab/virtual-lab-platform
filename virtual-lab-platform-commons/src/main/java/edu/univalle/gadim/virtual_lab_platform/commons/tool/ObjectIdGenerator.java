package edu.univalle.gadim.virtual_lab_platform.commons.tool;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("objectId")
public class ObjectIdGenerator implements UniqueIdGenerator {

  @Override
  public String generate() {
    return new ObjectId().toHexString();
  }
}

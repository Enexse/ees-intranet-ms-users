package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesNews;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EesNewsRepository extends MongoRepository<EesNews,String> {
}

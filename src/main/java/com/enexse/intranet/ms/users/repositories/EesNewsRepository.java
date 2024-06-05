package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesNews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesNewsRepository extends MongoRepository<EesNews,String> {
}

package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserAddressRepository extends MongoRepository<EesUserAddress,String> {
}

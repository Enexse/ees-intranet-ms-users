package com.enexse.intranet.ms.users.repositories.partials;

import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesCloudinaryRepository extends MongoRepository<EesCloudinaryDoc, String> {

    EesCloudinaryDoc findByPublicId(String publicId);

    EesCloudinaryDoc findByEesUploadType(String eesUploadType);
}

package com.enexse.intranet.ms.users.repositories.partials;

import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EesCloudinaryRepository extends MongoRepository<EesCloudinaryDoc, String> {

    EesCloudinaryDoc findByPublicId(String publicId);

    @Query("SELECT doc FROM EesCloudinaryDoc summary WHERE doc.cloudinaryId = ?1 AND doc.userId = ?2")
    EesCloudinaryDoc findByCloudinaryIdAndUserId(String cloudinaryId, String userId);

    List<EesCloudinaryDoc> findAllByUserId(String userId);
    List<EesCloudinaryDoc> findAllByUserIdAndEesUploadType(String userId, String eesUploadType);
    EesCloudinaryDoc findByEesUploadType(String eesUploadType);
}

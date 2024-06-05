package com.enexse.intranet.ms.users.repositories.partials;

import com.enexse.intranet.ms.users.models.partials.EesCareerComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesCareerCommentRepository extends MongoRepository<EesCareerComment, String> {
}

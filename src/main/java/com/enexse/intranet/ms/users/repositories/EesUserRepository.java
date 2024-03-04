package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserDepartment;
import com.enexse.intranet.ms.users.models.EesUserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EesUserRepository extends MongoRepository<EesUser, String> {

    @Query("{'personalEmail': ?0}")
    Optional<EesUser> findByPersonalEmail(String personalEmail);

    @Query("{'enexseEmail': ?0}")
    Optional<EesUser> findByEnexseEmail(String enexseEmail);

    @Query("{'matricule': ?0}")
    Optional<EesUser> findByMatricule(String matricule);

    @Query("{'pseudo': ?0}")
    EesUser findByPseudo(String pseudo);

    Optional<EesUser> findByPseudoAndUserId(String pseudo, String userId);

    @Query("{'userId': ?0}")
    Optional<EesUser> findByUserId(String userId);

    List<EesUser> findByEesDepartment(Optional<EesUserDepartment> eesDepartment);

    List<EesUser> findByEesEntity(Optional<EesUserEntity> eesEntity);

    @Query("{ createdAt: { $lte: ?0 } }")
    List<EesUser> findLastThreeUsersAfterDate(String startOfDay, Pageable pageable);
}






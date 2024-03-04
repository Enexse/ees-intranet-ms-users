package com.enexse.intranet.ms.users.utils;

import com.enexse.intranet.ms.users.enums.EesStatusUser;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.partials.EesUserInfo;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.repositories.partials.EesUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@StepScope
public class CheckPasswordExpirationTasklet implements Tasklet {

    @Autowired
    private EesUserInfoRepository userInfoRepository;

    @Autowired
    private EesUserRepository userRepository;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String DateNow = EesCommonUtil.generateCurrentDateUtil();
        Date currentDate = new Date();

        List<EesUserInfo> usersInfo = null;
        usersInfo = userInfoRepository.findAll();
        for( EesUserInfo userInfo : usersInfo){
            if(userInfo.getPasswordChangedAt() != null && datePassedLimit(userInfo.getPasswordChangedAt())){
                Optional<EesUser> user = getUserById(userInfo.getCollaboratorId());
                if(user.isPresent()){
                    user.get().setStatus(EesStatusUser.DISABLED);
                    user.get().setUpdatedAt(DateNow);
                    userRepository.save(user.get());
                }
            }

        }

        return RepeatStatus.FINISHED;
    }

    public Optional<EesUser> getUserById(@NonNull String id) {
        Optional<EesUser> eesUser = userRepository.findById(id);
        return eesUser;
    }

    public static boolean datePassedLimit(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime inputDate = LocalDateTime.parse(date, formatter);

        LocalDateTime currentDate = LocalDateTime.now();

        long days = java.time.Duration.between(inputDate,currentDate).toDays();

        return days > 90;
    }
}

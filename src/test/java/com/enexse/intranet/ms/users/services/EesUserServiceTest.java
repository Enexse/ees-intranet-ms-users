package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.models.*;
import com.enexse.intranet.ms.users.payload.request.EesUserRequest;
import com.enexse.intranet.ms.users.repositories.EesUserDepartmentRepository;
import com.enexse.intranet.ms.users.repositories.EesUserEntityRepository;
import com.enexse.intranet.ms.users.repositories.EesUserProfessionRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EesUserServiceTest {


    private EesUserService underTest;

    @Mock
    private EesUserRepository userRepository;

    @Autowired
    private EesUserEntityRepository userEntityRepository;
    @Autowired
    private EesUserDepartmentRepository userDepartmentRepository;
    @Autowired
    private EesUserProfessionRepository userProfessionRepository;

    @BeforeEach
    void setUp() {
        underTest = new EesUserService(userRepository);
    }


    @Test
    void cangetAllCollaborators() {

        //when
        underTest.getAllCollaborators();

        //then
        //we dont want to test the userRepository because we know it works, so we mock its implementation inside the userServiceTest
        // and by that our unit test is now fast(No queries for insert, delete, update...)
        verify(userRepository).findAll();
    }


    @Test
    void canInsertUser() {

        //given
        EesUserAddress userAddress = new EesUserAddress().builder()
                .country("Tunisia")
                .state("Sfax")
                .build();

        EesUserContact userContact = new EesUserContact().builder()
                .phoneNumber("51873380")
                .build();

        EesUserEntity entity = new EesUserEntity().builder()
                .entityCode("EES-ENTITY-TLS")
                .build();
        userEntityRepository.save(entity);

        EesUserDepartment department = new EesUserDepartment().builder()
                .departmentCode("EES-DEPARTMENT-ICT")
                .build();
        userDepartmentRepository.save(department);

        EesUserProfession profession = new EesUserProfession().builder()
                .professionCode("EES-PROFESSION-DEV")
                .build();
        userProfessionRepository.save(profession);

        EesUserRequest userRequest = new EesUserRequest().builder()
                .firstName("yosri")
                .lastName("harrabi")
                .gender("Male")
                .entityCode("EES-ENTITY-TLS")
                .departmentCode("EES-DEPARTMENT-ICT")
                .professionCode("EES-PROFESSION-DEV")
                .userAddress(userAddress)
                .userContact(userContact)
                .personalEmail("harrabiyosri2018@gmail.com")
                .build();

        //when
        //in underTest we pass the userRequest
        underTest.insertUser(userRequest);

        //then
        ArgumentCaptor<EesUser> userArgumentCaptor =
                ArgumentCaptor.forClass(EesUser.class);

        //in the actual service we pass the capturedUser
        verify(userRepository)
                .save(userArgumentCaptor.capture());

        EesUser capturedUser = userArgumentCaptor.getValue();

        //now we compare the userRequest (the one that the underTest recieves) and the capturedUser (the one that the actual service recieves)
        //the idea here is that we test whether the userRequest that we passed in the parameter of the request is the same that is passed in the repository, if yes the our service is correct
        assertThat(capturedUser).isEqualTo(userRequest);

    }

    @Test
    void getUserById() {
    }


    @Test
    void verifyPersonalEmail() {
    }
}
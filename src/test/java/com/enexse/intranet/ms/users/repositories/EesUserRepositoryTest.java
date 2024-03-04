package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EesUserRepositoryTest {

    @Autowired
    private EesUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByPersonalEmail() {

        //given
        String email = "harrabiyosri2021@gmail.com";
        EesUser user = new EesUser()
                .builder()
                .firstName("yosri")
                .lastName("harrabi")
                .gender("Male")
                .eesEntity(null)
                .eesDepartment(null)
                .eesProfession(null)
                .personalEmail(email)
                .userAddress(null)
                .userContact(null)
                .build();

        underTest.save(user);

        //when
        Optional<EesUser> eesUser = underTest.findByPersonalEmail(email);
        boolean expected = eesUser.isPresent();

        //then
        assertThat(expected).isFalse();
    }

    @Test
    void findByEnexseEmail() {
        //given
        String email = "harrabi.yosri@enexse.com";
        EesUser user = new EesUser()
                .builder()
                .firstName("yosri")
                .lastName("harrabi")
                .gender("Male")
                .enexseEmail(email)
                .build();

        underTest.save(user);

        //when
        Optional<EesUser> eesUser = underTest.findByEnexseEmail(email);
        boolean expected = eesUser.isPresent();

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void findByPseudoAndUserId() {
    }
}
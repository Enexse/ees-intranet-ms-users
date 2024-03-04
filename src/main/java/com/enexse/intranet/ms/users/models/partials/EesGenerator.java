package com.enexse.intranet.ms.users.models.partials;

import com.enexse.intranet.ms.users.models.EesUser;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.Locale;
import java.util.Random;

public class EesGenerator extends AbstractMongoEventListener<EesUser> {

    public static final String generatorName = "eesGenerator";

    private static char rndChar() {
        int rnd = (int) (Math.random() * 52);
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);

    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<EesUser> event) {
        super.onBeforeConvert(event);
        EesUser document = event.getSource();

        if (document.getUserId() == null) {
            // generate the custom ID here
            String id = "";
            char ch1 = rndChar();
            id += ch1;
            char ch2 = rndChar();
            id += ch2;
            Random rand = new Random();
            int upperbound = 10;

            Integer int1 = rand.nextInt(upperbound);
            String ch3 = int1.toString();
            id += ch3;
            Integer int2 = rand.nextInt(upperbound);
            String ch4 = int2.toString();
            id += ch4;
            Integer int3 = rand.nextInt(upperbound);
            String ch5 = int3.toString();
            id += ch5;
            Integer int4 = rand.nextInt(upperbound);
            String ch6 = int4.toString();
            id += ch6;
            Integer int5 = rand.nextInt(upperbound);
            String ch7 = int5.toString();
            id += ch7;
            String customId = id;
            document.setUserId(customId.toUpperCase(Locale.ROOT));
        }
    }
/*
    @Override
    public String generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        String id = "";
        char ch1 = rndChar();
        id+=ch1;
        char ch2 = rndChar();
        id+=ch2;
        Random rand = new Random();
        int upperbound = 10;


        Integer int1 = rand.nextInt(upperbound);
        String ch3 = int1.toString();
        id+=ch3;
        Integer int2 = rand.nextInt(upperbound);
        String ch4 = int2.toString();
        id+=ch4;
        Integer int3 = rand.nextInt(upperbound);
        String ch5 = int3.toString();
        id+=ch5;
        Integer int4 = rand.nextInt(upperbound);
        String ch6 = int4.toString();
        id+=ch6;
        Integer int5 = rand.nextInt(upperbound);
        String ch7 = int5.toString();
        id+=ch7;
        return id;
    }*/
}

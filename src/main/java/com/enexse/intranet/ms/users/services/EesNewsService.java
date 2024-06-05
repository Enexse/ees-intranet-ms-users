package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesNews;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesNewsRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesNewsRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EesNewsService {

    private EesNewsRepository eesNewsRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> addNew(EesNewsRequest request) {
        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        EesNews news = EesNews
                .builder()
                .newsTitle(request.getNewsTitle())
                .newsDescription(request.getNewsDescription())
                .newsStatus(Boolean.FALSE)
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .createdBy(user.get())
                .build();

        eesNewsRepository.save(news);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_NEWS_CREATED), HttpStatus.CREATED);
    }

    public List<EesNews> getAllNews() {
        return eesNewsRepository.findAll()
                .stream().sorted(Comparator.comparing(EesNews::getCreatedAt).reversed()).collect(Collectors.toList());
    }

    public ResponseEntity<Object> getNewById(String id) {
        Optional<EesNews> news = eesNewsRepository.findById(id);
        if (news.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_NEWS_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<Object> updateANew(String id, EesNewsRequest request) {
        Optional<EesNews> newsOptional = eesNewsRepository.findById(id);
        if (newsOptional.isPresent()) {
            Optional<EesUser> user = null;
            user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

            if (user.isEmpty()) {
                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
            }
            EesNews newsRequest = newsOptional.get();
            newsRequest.setNewsTitle(request.getNewsTitle());
            newsRequest.setNewsDescription(request.getNewsDescription());
            newsRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            newsRequest.setCreatedBy(user.get());
            eesNewsRepository.save(newsRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_NEWS_UPDATED)), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_NEWS_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> changeStatus(String id, boolean newStatus) {
        Optional<EesNews> newsOptional = eesNewsRepository.findById(id);
        if (newsOptional.isPresent()) {
            EesNews news = newsOptional.get();
            boolean previousStatus = news.isNewsStatus();
            newStatus = previousStatus ? false : true;
            news.setNewsStatus(newStatus);
            eesNewsRepository.save(news);
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_NEWS_UPDATED)), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_NEWS_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteTheNew(String id) {

        Optional<EesNews> news = eesNewsRepository.findById(id);
        if (!news.isPresent()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_NEWS_NOT_FOUND + id), HttpStatus.NOT_FOUND);
        } else {
            eesNewsRepository.delete(news.get());
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_NEWS_DELETED), HttpStatus.OK);
        }
    }
}

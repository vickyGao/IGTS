package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Image;

public interface ImageRepository extends MyRepository<Image, String> {

    @Query("from Image i where i.userId=:userId and i.deletedYN='N'")
    List<Image> getImagesByUserId(@Param("userId") String userId);

    @Query("from Image i where i.uri=:uri and i.deletedYN='N'")
    List<Image> getImagesByUri(@Param("uri") String uri);
}

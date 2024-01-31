package com.appsdeveloperblog.app.ws.io.repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//@Repository is special type of @Component, it annotates classes at the persistence layer,
//which will act as a database repository
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>,
        PagingAndSortingRepository<UserEntity, Long>
{
    //These are querying methods: (names or characters following "By" must match that in the fileds under UserEntity.class)

    //findBy referring return 1 record, if want all records then findAllBy.
    //findByEmail is the required format writing in spring data JPA.
    //fetching data from database based on certain input, must starting "findBy" + column field name in target database;
    UserEntity findByEmail(String email);


    UserEntity findByUserId(String userId);


}

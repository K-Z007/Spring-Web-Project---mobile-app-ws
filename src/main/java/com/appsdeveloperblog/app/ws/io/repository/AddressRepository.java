package com.appsdeveloperblog.app.ws.io.repository;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository is special type of @Component, it annotates classes at the persistence layer,
//which will act as a database repository
@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>
{

    AddressEntity findByAddressId(String addressId);

    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    //To make following method works: Field Names much match
    //Ensure that AddressEntity has fields named UserDetails and addressId, and they are of type String.
    //Also, make sure these field names are spelled and capitalized in the same way as in the findByUserIdAndAddressId method.
    AddressEntity findByUserDetailsAndAddressId(UserEntity userEntity, String addressID);

}

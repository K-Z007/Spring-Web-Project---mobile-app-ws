package com.appsdeveloperblog.app.ws.service.implementation;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessageTypes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service   //@Service is special type of @Component, it annotates classes at the service layer.
public class AddressServiceImpl implements AddressService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAddresses(String userID)
    {
        List<AddressDTO> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userID);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        Iterable<AddressEntity> addressesEntity = addressRepository.findAllByUserDetails(userEntity);

        for (AddressEntity addressEntity: addressesEntity)
        {
            returnValue.add(new ModelMapper().map(addressEntity, AddressDTO.class));
        }

        return returnValue;
    }

    @Override
    public AddressDTO getAddress(String addressId)
    {
        AddressDTO returnValue = new AddressDTO();

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if(addressEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(addressEntity, returnValue);

        return returnValue;
    }

    @Override
    public AddressDTO getUserAddress(String userId, String addressId)
    {
        AddressDTO returnValue = new AddressDTO();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        AddressEntity addressEntity = addressRepository.findByUserDetailsAndAddressId(userEntity, addressId);

        if(addressEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(addressEntity, returnValue);

        return returnValue;
    }
}

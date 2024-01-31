package com.appsdeveloperblog.app.ws.service;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService
{
    List<AddressDTO> getAddresses(String userID);
    AddressDTO getAddress(String addressID);
    AddressDTO getUserAddress(String userID, String addressID);
}

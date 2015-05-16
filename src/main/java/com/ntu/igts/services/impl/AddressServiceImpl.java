package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.ntu.igts.model.Address;
import com.ntu.igts.repository.AddressRepository;
import com.ntu.igts.services.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressRepository addressRepository;

    @Override
    public Address create(Address address) {
        return addressRepository.create(address);
    }

    @Override
    public Address update(Address address) {
        return addressRepository.update(address);
    }

    @Override
    public boolean delete(String addressId) {
        addressRepository.delete(addressId);
        Address address = addressRepository.findById(addressId);
        if (address == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Address getById(String addressId) {
        return addressRepository.findById(addressId);
    }

    @Override
    public List<Address> getByUserId(String userId) {
        return addressRepository.getByUserId(userId);
    }

}

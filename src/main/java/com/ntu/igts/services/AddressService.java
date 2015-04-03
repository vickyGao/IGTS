package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Address;

public interface AddressService {

    public Address create(Address address);

    public Address update(Address address);

    public boolean delete(String addressId);

    public Address getById(String addressId);

    public List<Address> getByUserId(String userId);
}

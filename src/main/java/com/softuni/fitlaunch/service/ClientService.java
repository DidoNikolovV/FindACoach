package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.mappers.UserMapper;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;

    private final UserMapper userMapper;

    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper, UserMapper userMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
    }

    public void registerClient(UserEntity user) {
        ClientEntity client = userMapper.mapUserToClient(user);
        clientRepository.save(client);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public ClientEntity getClientEntityByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
    }
}

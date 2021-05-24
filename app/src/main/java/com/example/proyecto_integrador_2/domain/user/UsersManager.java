package com.example.proyecto_integrador_2.domain.user;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.example.proyecto_integrador_2.data.network.services.LoginResult;
import com.example.proyecto_integrador_2.data.network.services.RegisterResult;

import javax.inject.Inject;

public class UsersManager {

    private UsersRepository usersRepository;

    @Inject
    public UsersManager(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean emailIsInvalid(String email) {
        return email.isEmpty();
    }

    public boolean passwordIsInvalid(String password) {
        return password.isEmpty();
    }

    public boolean nameIsInvalid(String name) {
        return name.isEmpty() || name.length() > 50;
//        if(name.isEmpty()){
//            return false;
//        } else if (name.length() > 50){
//            return false;
//        } else {
//            return true;
//        }
    }

    public boolean phoneIsInvalid(String phone) {
        return phone.length() != 10;
//        if(phone.isEmpty()){
//            return false;
//        } else if(phone.length() != 10){
//            return false;
//        } else {
//            return true;
//        }
    }

    public void signIn(String email, String password, LoginResult loginResult) {

        usersRepository.signIn(email, password, loginResult);
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        usersRepository.getUserInfo(userId, loginResult);
    }

    public void saveUser(UserEntity userEntity) {
        usersRepository.saveUser(userEntity);
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        usersRepository.createUser(userEntity, password, registerResult);
    }
}

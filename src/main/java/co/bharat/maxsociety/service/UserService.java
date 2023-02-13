package co.bharat.maxsociety.service;

import java.util.Optional;

import co.bharat.maxsociety.entity.Users;

public interface UserService {

	   Users createUser(Users user);
	   
	   Optional<Users> getUser(String id);

	   Users updateUser(String userId, Users user);

	   void deleteUser(String id);
	}

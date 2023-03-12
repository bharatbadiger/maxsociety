package co.bharat.maxsociety.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.maxsociety.entity.Users;
import co.bharat.maxsociety.repository.UserRepository;
import co.bharat.maxsociety.service.UserService;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Users createUser(Users user) {
    return userRepository.save(user);
  }

  @Override
  public Optional<Users> getUser(String id) {
	  Optional<Users> user = userRepository.findById(id);
	  String flatNo=user.get().getFlats().getFlatNo();
	  user.get().setFamilyMembersCount(userRepository.countByFlats_FlatNo(flatNo));
      return user;
  }

  @Override
  public Users updateUser(String userId, Users user) {
	Optional<Users> updateUser = userRepository.findById(userId);
    return userRepository.save(updateUser);
  }

  @Override
  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }
  
}

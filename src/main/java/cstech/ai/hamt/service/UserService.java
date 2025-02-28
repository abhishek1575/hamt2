package cstech.ai.hamt.service;

import cstech.ai.hamt.dto.UserDto;
import cstech.ai.hamt.entity.User;
import cstech.ai.hamt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //Function used for Login (finding user by email it grants the respective authority)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }


    //Function used for finding user by email
    public User findUserbyEmail(String email){
        Optional<User> user1 = userRepository.findByEmail(email);
        return user1.get();
    }

    //Function used for adding new user.
    public String addUser(User user) {
        Optional<User> user1 = userRepository.findByEmail(user.getEmail());
        if(user1.isPresent()){
            return "User Already Present";
        }else {
            userRepository.save(user);
            return "User Added Successfully";
        }
    }

    //Function used for get user by Id.
    public User getUserById(long Id) {
        Optional<User> userInfo = userRepository.findById(Id);
        return userInfo.get();
    }

    //Function used for get all users.
    public List<UserDto> getAll(){
        List<User> users = userRepository.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }

        return userDtos;
    }
    //Function used to get the list of all users.
    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    //Function used to get the list of all users.
    public List<UserDto> getAllAdmin(){
        List<User> users = userRepository.getAllAdmins();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    //Function used to get the list of all users.
    public List<UserDto> getAllSuperAdmin(){
        List<User> users = userRepository.getAllSuperAdmins();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

}


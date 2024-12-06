package mapper;

import model.User;
import model.builder.UserBuilder;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO convertUserToUserDTO(User user) {
        return new UserDTOBuilder().setUsername(user.getUsername()).build();
    }

    public static User convertUserDTOToUser(UserDTO userDTO){
        return new UserBuilder().setUsername(userDTO.getUsername()).build();
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users) {
        return users.parallelStream().map(UserMapper::convertUserToUserDTO).collect(Collectors.toList());
    }

    public static List<User> convertUserDTOListToUserList(List<UserDTO> userDTOS) {
        return userDTOS.parallelStream().map(UserMapper::convertUserDTOToUser).collect(Collectors.toList());
    }

}

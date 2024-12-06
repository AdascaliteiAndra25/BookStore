package service.user;
import model.Role;
import model.User;
import model.builder.UserBuilder;

import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;

import static database.Constants.Roles.CUSTOMER;
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private User user;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password, String role) {

        Notification<Boolean> userRegisterNotification = new Notification<>();

        if(userRepository.existsByUsername(username)){
            userRegisterNotification.addError("This username is already registered. Please try with another username.");
            userRegisterNotification.setResult(Boolean.FALSE);
            return userRegisterNotification;

        }

        Role atribRole = rightsRolesRepository.findRoleByTitle(role);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(atribRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid=userValidator.validate();


        if(!userValid){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(hashPassword(password));
            userRegisterNotification.setResult(userRepository.save(user));
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
       // return userRepository.findByUsernameAndPassword(username, hashPassword(password));
        Notification<User> notification = userRepository.findByUsernameAndPassword(username, hashPassword(password));

        if (notification.getResult() != null) {
            this.user = notification.getResult();
            //System.out.println("OK");
        }
        user.setId(userRepository.findIdByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with username: " + username + " was not found.")));
        return notification;
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public Long getUserLoggedId() {
        if(user != null){
            return user.getId();
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            // SHA = Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

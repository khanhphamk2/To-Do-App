package org.khanhpham.todo.service.implement;

import org.khanhpham.todo.entity.User;
import org.khanhpham.todo.exception.ResourceNotFoundException;
import org.khanhpham.todo.payload.dto.UserDTO;
import org.khanhpham.todo.repository.UserRepository;
import org.khanhpham.todo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a User entity to a UserDTO object.
     *
     * @param user the User entity to be converted
     * @return the corresponding UserDTO object
     */
    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Converts email and username into a User entity.
     *
     * @param email the email of the user
     * @param username the username of the user
     * @return the created User entity
     */
    public User convertToEntity(String email, String username) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        return user;
    }

    /**
     * Finds a UserDTO based on the identity, which can be either username, email, or id.
     *
     * @param identity the username, email, or id of the user
     * @return the corresponding UserDTO if found
     * @throws ResourceNotFoundException if no user is found with the given identity
     */
    @Override
    public UserDTO findByIdentity(String identity) {
        Optional<User> user = userRepository.findByUsernameOrEmail(identity, identity);
        if (user.isEmpty()) {
            try {
                Long id = Long.parseLong(identity);
                user = userRepository.findById(id);
            } catch (NumberFormatException e) {
                throw new ResourceNotFoundException("User", "identity", identity);
            }
        }
        return user.map(this::convertToDTO).orElseThrow(() -> new ResourceNotFoundException("User", "identity", identity));
    }

    /**
     * Finds a UserDTO based on the user ID.
     *
     * @param id the ID of the user
     * @return the corresponding UserDTO if found
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDTO(user);
    }

    /**
     * Creates a new User entity based on email and username.
     *
     * @param email the email of the user
     * @param username the username of the user
     * @return the newly created User entity
     */
    @Override
    public User createUser(String email, String username) {
        User user = convertToEntity(email, username);
        return userRepository.save(user);
    }

    /**
     * Finds a UserDTO based on either username or email.
     *
     * @param email the email of the user
     * @param username the username of the user
     * @return an Optional containing the UserDTO if found, or empty if not found
     */
    @Override
    public UserDTO findByUsernameOrEmail(String email, String username) {
        Optional<User> user = userRepository.findByUsernameOrEmail(username, email);
        return user.map(this::convertToDTO).orElse(null);
    }

    @Override
    public void changePassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        user.setPassword(password);
        convertToDTO(userRepository.save(user));
    }
}

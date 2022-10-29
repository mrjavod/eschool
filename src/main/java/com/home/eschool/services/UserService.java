package com.home.eschool.services;

import com.home.eschool.entity.Roles;
import com.home.eschool.entity.States;
import com.home.eschool.entity.Teachers;
import com.home.eschool.entity.Users;
import com.home.eschool.entity.enums.RoleEnum;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.dto.PasswordUpdateDto;
import com.home.eschool.repository.UserRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final StateService stateService;

    public UserService(UserRepo userRepo,
                       RoleService roleService,
                       StateService stateService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.stateService = stateService;
    }

    /**
     * @param roles  List
     * @param states List
     */
    public void createDefaultUser(List<Roles> roles, List<States> states) {

        if (userRepo.count() == 0) {
            Roles role = roles.stream().filter(e -> e.getLabel().equals(RoleEnum.ROLE_ADMIN))
                    .findFirst().orElse(null);

            States state = states.stream().filter(e -> e.getLabel().equals(StateEnum.ACTIVE))
                    .findFirst().orElse(null);

            Users user = new Users();
            user.setId(UUID.randomUUID());
            user.setLogin("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setFullName("Adminov Admin");
            user.setRole(role);
            user.setCreateUser(null);
            user.setChangeUser(null);
            user.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            user.setState(state);

            this.userRepo.save(user);
        }
    }

    /**
     * @param login String
     * @return Users
     */
    public Users findUserByLogin(String login) {

        Optional<Users> users = userRepo.findByLoginAndStateLabel(login, StateEnum.ACTIVE);

        return users.orElse(null);
    }

    /**
     * @param teachers Teachers
     * @return Users
     */
    Users createProfile(Teachers teachers) {

        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setLogin(teachers.getPhoneNumber());
        user.setPassword(new BCryptPasswordEncoder().encode("Teacher123"));
        user.setFullName(String.format("%s %s %s",
                teachers.getLastName(),
                teachers.getFirstName(),
                teachers.getSureName()));
        user.setRole(roleService.getRoleByLabel(RoleEnum.ROLE_TEACHER));
        user.setState(stateService.getStateByLabel(StateEnum.ACTIVE));
        user.setCreateUser(Settings.getCurrentUser());
        user.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

        return userRepo.save(user);
    }

    /**
     * @param teachers Teachers
     */
    void updateProfile(Teachers teachers) {

        Users user = teachers.getProfile();
        user.setLogin(teachers.getPhoneNumber());
        user.setFullName(String.format("%s %s %s",
                teachers.getLastName(),
                teachers.getFirstName(),
                teachers.getSureName()));
        user.setChangeUser(Settings.getCurrentUser());
        user.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        userRepo.save(user);
    }

    /**
     * @param profile Users
     */
    void deleteUser(Users profile) {
        profile.setState(stateService.getStateByLabel(StateEnum.DELETED));
        userRepo.save(profile);
    }

    public void updatePassword(PasswordUpdateDto dto) {

        Users user = Settings.getCurrentUser();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Eski parol noto'g'ri kiritilgan !");
        }

        if (!dto.getNewPassword().equals(dto.getConfirm())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Parol tasdiqlanmadi !");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);
    }
}

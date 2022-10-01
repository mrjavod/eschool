package com.home.eschool.services;

import com.home.eschool.entity.Roles;
import com.home.eschool.entity.States;
import com.home.eschool.entity.Users;
import com.home.eschool.entity.enums.RoleEnum;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.repository.UserRepo;
import com.home.eschool.utils.Settings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        Roles role = roles.stream().filter(e -> e.getLabel().equals(RoleEnum.ADMIN))
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
        user.setCrateDate(Timestamp.valueOf(LocalDateTime.now()));
        user.setState(state);

        this.userRepo.save(user);
    }

    /**
     * @return List
     */
    public List<Users> getUsersList() {
        return userRepo.findAll();
    }

    /**
     * Bu method faqat ADMIN va MANAGER uchun
     *
     * @param newUser Users
     * @return Users
     */
    public Users createUser(Users newUser) {
        newUser.setId(UUID.randomUUID());
        newUser.setCrateDate(Timestamp.valueOf(LocalDateTime.now()));
        newUser.setRole(roleService.getRoleByLabel(newUser.getRole().getLabel()));
        newUser.setState(stateService.getStateByLabel(StateEnum.ACTIVE));
        newUser.setCreateUser(Settings.getCurrentUser());

        return userRepo.save(newUser);
    }

    /**
     * Bu method faqat ADMIN uchun
     *
     * @param ids Users uuid
     */
    public void deleteUsers(List<UUID> ids) {

        for (UUID id : ids) {

            // o'zini o'zi o'chirishi mumkin emas, keyinchalik tekshirish kerak

            Users user = userRepo.findByIdAndStateLabel(id, StateEnum.ACTIVE).orElse(null);

            if (user != null) {
                user.setState(stateService.getStateByLabel(StateEnum.DELETED));
                user.setChangeUser(Settings.getCurrentUser());
                user.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));

                userRepo.save(user);
            }
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
}

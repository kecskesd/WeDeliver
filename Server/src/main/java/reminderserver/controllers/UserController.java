package reminderserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import reminderserver.models.User;
import reminderserver.service.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    @Autowired
    UserService userService;

    //definovanie REST volani pre user

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<User> create(@RequestBody final User user){
        userService.create(user);
        return userService.getAll();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<User> update(@RequestBody final User user){
        userService.update(user);
        return userService.getAll();
    }

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping(path = "email/{email}")
    public Optional<User> findByEmail(@PathVariable("email") String email){
        return userService.findByEmail(email);
    }

    @GetMapping(path = "active/{active}")
    public List<User> findByActive(@PathVariable("active") Boolean active){
        return userService.findByActive(active);
    }
}

package com.lino.secondkill.service;


import com.lino.secondkill.dao.UserDao;
import com.lino.secondkill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    public UserDao userDao;


    public User getById(Integer id) {
        return userDao.getById(id);

    }
    @Transactional
    public boolean testInsert() {
        User u1 = new User();
        u1.setUsername("lino");
        User u2 = new User();
        u2.setUsername("lulu");


        userDao.testInsert(u1);



        return true;
    }
}

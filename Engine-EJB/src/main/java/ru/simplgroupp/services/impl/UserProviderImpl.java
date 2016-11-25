package ru.simplgroupp.services.impl;

import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.services.UserProvider;

import javax.annotation.Resource;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import java.security.Principal;

/**
 * User provider
 */
@Singleton
public class UserProviderImpl implements UserProvider {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private UsersDAO usersDAO;

    @Override
    public UsersEntity getAuthorizedUser() throws ObjectNotFoundException, AuthenticationException {
        Principal principal = sessionContext.getCallerPrincipal();
        if (principal == null) {
            throw new AuthenticationException("Authentication is required");
        }

        UsersEntity user = usersDAO.findUserByLogin(principal.getName());
        if (user == null) {
            throw new ObjectNotFoundException("User with username " + principal.getName() + " not found");
        }

        return user;
    }
}

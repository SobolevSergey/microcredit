package ru.simplgroupp.services;

import ru.simplgroupp.persistence.UsersEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.naming.AuthenticationException;

/**
 * User provider
 */
@Local
public interface UserProvider {

    UsersEntity getAuthorizedUser() throws ObjectNotFoundException, AuthenticationException;
}

package ru.simplgroupp.interfaces;

import javax.ejb.Remote;

@Remote
public interface UsersBeanRemote {
    public  void  testMethod(String name);
}

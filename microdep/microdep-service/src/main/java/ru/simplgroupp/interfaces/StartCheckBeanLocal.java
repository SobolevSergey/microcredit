package ru.simplgroupp.interfaces;

import java.util.List;

import ru.simplgroupp.exception.ExceptionInfo;

public interface StartCheckBeanLocal {

	public void check(boolean bCheckDatabase, boolean bCheckProcesses);

	boolean isOk();

	List<ExceptionInfo> getStatusMessages();

}

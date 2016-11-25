package ru.simplgroupp.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.Users;

public class BizActionUtils {

	/**
	 * Нужен ли основной процесс
	 * @param bizAct
	 * @return
	 */
	public static boolean needProcess(BizActionEntity bizAct) {
		return (bizAct.getIsRequired() == 1 || StringUtils.isNotBlank( bizAct.getSchedule()));
	}
	
	/**
	 * Вернуть ключ основного процесса
	 * @param bizAct
	 * @param businessObjectId
	 * @return
	 */
	public static String getBusinessKey(BizActionEntity bizAct, Object businessObjectId) {
		String bkey = BizAction.class.getName() + ":" + bizAct.getId().toString();
		if (bizAct.getForMany() == 1 && businessObjectId == null) {
			return bkey;
		} else {
			return bkey + ":" + bizAct.getBusinessObjectClass() + ":" + businessObjectId.toString();
		}
	}
	
	/**
	 * Может ли данный пользователь запустить данное действие
	 * @param bizAct
	 * @param auser
	 * @return
	 */
	public static boolean canExecute(BizAction bizAct, Users auser) {
		String[] roles = bizAct.getCandidateGroups().split(",");
		return auser.hasRoleAny(roles);
	}
	
	/**
	 * Есть ли данная роль в списке ролей для этого действия
	 * @param bizAct
	 * @param arole
	 * @return
	 */
	public static boolean hasRole(BizActionEntity bizAct, String arole) {
		String[] roles = bizAct.getCandidateGroups().split(",");
		int n = Arrays.binarySearch(roles, arole);
		return (n >= 0);
	}
	
	/**
	 * Добавляем роль в список ролей для данного действия, если этой роли там не было.
	 * @param bizAct
	 * @param arole
	 * @return true - если роли не было и она добавлена, false - если роль уже была в списке
	 */
	public static boolean setRole(BizActionEntity bizAct, String arole) {
		String[] roles = bizAct.getCandidateGroups().split(",");
		int n = Utils.searchText(roles, arole, true, false);
		if (n >= 0) {
			return false;
		}
		roles = Arrays.copyOf(roles, roles.length + 1);
		roles[roles.length - 1] = arole;
		String ss = Convertor.toStringSafe(roles);
		bizAct.setCandidateGroups(ss);
		return true;
	}

	/**
	 * Удаляем роль из списка ролей для данного действия, если она там была.
	 * @param bizAct
	 * @param arole
	 * @return true - если роль была, и она удалена, false - если роли в списке не было
	 */
	public static boolean removeRole(BizActionEntity bizAct, String arole) {
		String[] roles = bizAct.getCandidateGroups().split(",");
		int n = Utils.searchText(roles, arole, true, false);
		if (n < 0) {
			return false;
		}
		roles = ArrayUtils.remove(roles, n);
		String ss = Convertor.toStringSafe(roles);
		bizAct.setCandidateGroups(ss);
		return true;
	}
	
}

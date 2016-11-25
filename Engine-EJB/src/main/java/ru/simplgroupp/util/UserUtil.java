package ru.simplgroupp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.simplgroupp.transfer.Roles;

public class UserUtil {
	
	/**
	 * ищем роли по id
	 * @param source - список ролей пользователя
	 * @param roleId - id роли
	 * @return
	 */
	public static Roles findRoleById(List<Roles> source, int roleId) {
		for (Roles role: source) {
			if (roleId == role.getId()) {
				return role;
			}
		}
		return null;
	}
	
	/**
	 * Пересекаются ли данные роли (roleNames) со списком ролей.
	 * @param roles - список ролей
	 * @param roleNames - роли для проверки, есть ли они в списке
	 * @param bAll - если true, то все роли должны быть в списке, если false - то хотя бы одна
	 * @return есть или нет данные роли в списке
	 */
	public static boolean hasRoles(List<Roles> roles, String[] roleNames, boolean bAll) {
		if (roleNames.length == 0) {
			return false;
		}
		
		if (roles == null) {
			return false;
		}
		int n = 0;
		for (Roles role: roles) {
			if (Arrays.binarySearch(roleNames, role.getName()) >= 0) {
				n++;
			}
		}
		if (bAll) {
			return (n == roleNames.length);
		} else {
			return (n > 0);
		}
	}
	
	/**
	 * роли списком
	 * @param source - список ролей
	 * @return
	 */
	public static List<String> rolesToStrings(List<Roles> source) {
		List<String> roleNames = new ArrayList<String>(5);
		for (Roles role: source ) {
			roleNames.add(role.getName());
		}		
		return roleNames;
	}
}

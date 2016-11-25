package ru.simplgroupp.util;

import java.util.Collection;

import ru.simplgroupp.transfer.Reference;

public class ReferenceUtil {

	public static Reference find(Collection<Reference> source, Integer codeInteger, String code) {
		for (Reference ref: source) {
			if (codeInteger != null && codeInteger == ref.getCodeInteger()) {
				return ref;
			}
			if (code != null && code.equals(ref.getCode())) {
				return ref;
			}
		}
		return null;
	}
}

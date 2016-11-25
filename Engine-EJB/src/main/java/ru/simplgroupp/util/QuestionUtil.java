package ru.simplgroupp.util;

import java.util.List;

import ru.simplgroupp.transfer.QAutoAnswer;

public class QuestionUtil {
	
	public static QAutoAnswer findAutoAnswer(List<QAutoAnswer> source, String questionKey) {
		for (QAutoAnswer ans: source) {
			if (ans.getQuestionKey().equals(questionKey)) {
				return ans;
			}
		}
		return null;
	}
}

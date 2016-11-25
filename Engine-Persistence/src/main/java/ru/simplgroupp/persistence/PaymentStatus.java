package ru.simplgroupp.persistence;

/**
 * Статусы платежа
 */
public enum PaymentStatus {
    NEW, // 0
    SUCCESS, // 1
    ERROR, // 2
    SENDED, // 3
    REVOKED, // 4
    DELETED; // 5
    
    public static String getStatusName(PaymentStatus status) {
    	switch (status) {
    		case NEW: return "Новый";
    		case SUCCESS: return "Успешно выполнен";
    		case ERROR: return "Ошибка";
    		case SENDED: return "Отправлен";
    		case REVOKED: return "Возвращён";
    		case DELETED: return "Удалён";
    	}
    	return null;
    }
}

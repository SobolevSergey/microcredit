package ru.simplgroupp.toolkit.common;

/**
 * Call-back интерфейс для передачи данных о ходе длительного процесса инициатору процесса
 * @author irina
 *
 */
public interface ExecutionProgress {
	/**
	 * Вызывается, когда выполнен очередной шаг процесса
	 * @param indicator - индикатор хода процесса
	 */
	public void progress(Object indicator);
	
	/**
	 * Процесс завершён
	 */
	public void finished();
	
	/**
	 * Проверяем, есть ли сигнал для остановки процесса
	 * @return
	 */
	public boolean finishRequest();
}

package ru.simplgroupp.ejb;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.interfaces.BIKUpdaterLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.toolkit.common.ZipUtils;
import ru.simplgroupp.transfer.Bank;
import ru.simplgroupp.util.DBFUtils;
import ru.simplgroupp.util.HTTPUtils;

@Stateless
public class BIKUpdaterBean implements BIKUpdaterLocal {
	
	@Inject Logger log;
	
	@Resource(name="downloadBIKUrl")
	private String downloadBIKUrl;
	
	@EJB
	ReferenceBooksLocal refrenceBooks;
	
	@Schedule(dayOfWeek = "3",  hour = "0", minute = "0", second = "0", persistent = false, info ="Every wednesday midnight")
	public void handleTimer(Timer timer) {
		updateBanks();
	}
	
	@Override
	public void updateBanks(byte[] dbf) throws UnsupportedEncodingException {
		ArrayList<Bank> banks = parseDBFBanks(dbf);
	    refrenceBooks.updateBanks(banks);
	}

	@Override
	public void updateBanks() {
		log.info("Обработка обновления BIK");
		File updateArchive = null;
		try {
			// Загружаем файл обновлений
			updateArchive = File.createTempFile("bik", ".zip");
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String url = null;
			// поиск за последние семь дней до текущего, так как изменения за
			// текущий могут быть еще не актуальны
			for (int i = 1; i <= 7; i++) {
				Date d = DateUtils.addDays(new Date(), -i);
				String formatDate = sdf.format(d.getTime());
				url = String.format(downloadBIKUrl, formatDate);
				try {
					log.info("Загрузка файла " + url + "...");
					HTTPUtils.downloadFile(url, updateArchive);
					break;
				} catch (Exception e) {
					log.severe("Произошла ошибка при загрузке файла обновления "+e.getMessage());
				}
			}
			if (url != null) {
				log.info("Файл " + url + " загружен в " + updateArchive.getAbsolutePath());
				log.info("Распаковываем файл обновлений bnkseek.dbf из архива...");
				byte[] bnkseek = ZipUtils.unzipSingleFile(updateArchive, "bnkseek.dbf");

				// Обновляем БД
				updateBanks(bnkseek);

				log.info("Обновление BIK обработано.");
			} else {
				log.info("Обновления BIK не найдено.");
			}

		} catch (Exception e) {
			log.severe("Ошибка при обновлении BIK: " + e.getMessage());
		}
	}
	
	/**
	 * Парсинг справочника БИК ЦБ 
	 * @param dbf представление справочника БИК ЦБ в виде массива 
	 * @return список банков
	 * @throws UnsupportedEncodingException
	 */
	public ArrayList<Bank> parseDBFBanks(byte[] dbf) throws UnsupportedEncodingException {
		ArrayList<Bank> banks = new ArrayList<Bank>();
		HashMap<Integer, Class<?>> fieldTypes = new HashMap<Integer, Class<?>>();
		//NAMEP - полное наименование
		fieldTypes.put(10, String.class);
		//NNP - населенный пункт
		fieldTypes.put(7, String.class);
		//ADR - адрес
		fieldTypes.put(8, String.class);
		//NEWNUM - БИК
		fieldTypes.put(12, String.class);
		//KSNP - кор.счет
		fieldTypes.put(23, String.class);
		
		List<HashMap<Integer, Object>> rows = DBFUtils.parseDBF(dbf, "cp866", fieldTypes);
		for(HashMap<Integer, Object> row : rows) {
			Bank b = new Bank();
			String addr = (String) row.get(7);
			if(StringUtils.isNotEmpty(addr))
				addr += ", ";
			b.setAddress(addr + row.get(8));
			b.setBik((String) row.get(12));
			b.setCorAccount((String) row.get(23));
			b.setIsActive(1);
			b.setIsBank(1);
			b.setName((String) row.get(10));
			banks.add(b);
		}
		return banks;
	}

}

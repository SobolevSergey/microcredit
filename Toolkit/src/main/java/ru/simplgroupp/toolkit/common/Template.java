package ru.simplgroupp.toolkit.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * Шаблонизатор
 *
 * @author cobalt
 * @since 20131119
 */
public class Template {

    private static Logger log = Logger.getLogger(Template.class.getName());

    private String content;

    public Template() {
        content = "";
    }

    public Template(File tpl) {
        readFile(tpl);
    }

    public Template(String path) {
        File tpl = new File(path);
        if (tpl.exists() && tpl.isFile()) {
            readFile(tpl);
        } else {
            content = path;
        }
    }

    /**
     * Читает содержимое шаблона из указанного файла
     *
     * @param tpl файл
     */
    public void readFile(File tpl) {
        BufferedReader fin;
        content = "";
        try {
            fin = new BufferedReader(new FileReader(tpl));
            String line;
            while ((line = fin.readLine()) != null) {
                content += line + "\n";
            }
            fin.close();
        } catch (IOException e) {
            log.severe("Template: " + e);
        }
    }

    /**
     * Заменяет все ключи на значения как есть
     *
     * @param key
     * @param value
     */
    public void override(String key, String value) {
        Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL | Pattern.UNIX_LINES);
        Matcher m = pattern.matcher(content);
        if (m.find()) {
            content = m.replaceAll(value);
        }
    }

    /**
     * Заменяет все чанки по ключам на значения
     *
     * @param pattern
     * @param value
     */
    public void replace(String pattern, String value) {
        if (value == null) {
            value = "";
        }
        content = content.replaceAll("\\{::" + pattern + "::\\}", Matcher.quoteReplacement(value));
    }

    /**
     * Заменяет все чанки по ключам на значения
     *
     * @param pattern
     * @param value
     */
    public void replaceData(String pattern, String value) {
        content = content.replaceAll(pattern, Matcher.quoteReplacement(value));
    }

    /**
     * Заменяет все чанки по ключам на значения
     *
     * @param patterns массив чанков
     * @param values массив значений
     * @throws Exception
     */
    public void replace(String[] patterns, String[] values) {
        if (patterns.length != values.length) {
            log.severe("Patterns lenght not equal Values lenghts");
        }
        int i = 0;
        for (String p : patterns) {
            replace(p, values[i]);
            i++;
        }
    }

    /**
     * Парсит все чанки если они есть
     *
     * @param controller
     */
//	public void parseChunks(Controller controller){
//		List<String> chunks = getChunks();
//		for(String c: chunks){
//			ChunkHandler h = new ChunkHandler(c,controller);
//			replace("Chunk_"+c, h.getResult());
//		}
//	}
    /**
     * Заменяет данные из системного конфига
     */
//	public void parseConfig(){
//		Map<String,String> cfg = Configuration.getMap();
//		for(String key: cfg.keySet()){
//			replace(key,cfg.get(key));
//		}
//	}
    /**
     * Возвращает текущий шаблон
     *
     * @return
     */
    public String getContent() {
        //parseConfig();
        return content;
    }

    /**
     * Устанавливает текущий шаблон
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}

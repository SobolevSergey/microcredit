package ru.simplgroupp.fias.utils;

import com.github.junrar.extract.ExtractArchive;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import ru.simplgroupp.fias.exception.FiasException;

/**
 * Утилиты для работы с локальной файловой системой.
 * @author CVB
 */
public class FiasFileUtils {

  public static File createTempDir() throws FiasException {
    File dir;
    try {
      dir = File.createTempFile("fias", Long.toString(System.nanoTime()));
      dir.createNewFile();
    } catch (IOException e) {
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Ошибка при создании временного каталога.", e);
    }
    if (!dir.delete())
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Не удалось удалить временный файл/каталог " + dir.getAbsolutePath());
    if (!dir.mkdir())
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Не удалось создать временную папку " + dir.getAbsolutePath());
    return dir;
  }

  public static void deleteTempDir(File tempDir, Logger logger) {
    if (tempDir == null) return;
    try {
      FileUtils.cleanDirectory(tempDir);
    } catch (IOException ex) {
      if (logger != null)
        logger.error("Не удалось очистить временную папку.", ex);
      return;
    }
    if (tempDir.delete()) {
      if (logger != null)
        logger.info("Временная папка " + tempDir.getAbsolutePath() + " удалена.");
    } else {
      if (logger != null)
        logger.error("Не удалось удалить временную папку " + tempDir.getAbsolutePath() + ".");
    }
  }
  
  public static void unrarFile(File rarFile, File destFolder, String externalUnrarCommand) throws FiasException {
    if (!rarFile.exists())
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Файл " + rarFile.getName() + " не найден.");
    if (false /*&& rarFile.length() < Integer.MAX_VALUE*/) {
      // Почему-то библиотека не освобождает ресурс, что не позволяет в дальнейшем удалить файл. Нужно разобраться.
      ExtractArchive extractArchive = new ExtractArchive();
      extractArchive.extractArchive(rarFile, destFolder);
    } else {
      try {
        ProcessBuilder pb = new ProcessBuilder(externalUnrarCommand, "x", rarFile.getAbsolutePath());
        pb.directory(destFolder);
        Process process = pb.start();
        if (process.waitFor() != 0)
          throw new IOException("Ошибка при выполнении внешней команды.");
      } catch (IOException | InterruptedException ex) {
        throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Ошибка при распаковке архивного файла.", ex);
      }
    }
  }
  
}

package ru.simplgroupp.interfaces;

import javax.ejb.Local;




import ru.simplgroupp.persistence.MessagesEntity;
import ru.simplgroupp.persistence.PaymentMessageEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.transfer.Messages;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Local
public interface MailBeanLocal {
    /**
     * посылаем email просто текст
     *
     * @param subject заголовок
     * @param text    текст
     * @param toEmail кому
     */
    public void send(String subject, String text, String toEmail);

    /**
     * посылаем email с вложением
     *
     * @param subject  заголовок
     * @param text     текст
     * @param toEmail  кому
     * @param fileName имя файла
     */
    public void sendAttachment(String subject, String text, String toEmail, String fileName);

    /**
     * посылаем смс с использованием SSL
     *
     * @param phoneNumber № телефона
     * @param text        текст
     */
    public boolean sendSMS(String phoneNumber, String text);

    /**
     * Отправка СМС по новому, используя smstraffic.ru
     *
     * @param phoneNumber № телефона
     * @param text        текст
     * @return true если все ОК
     */
    boolean sendSMSV2(String phoneNumber, String text);

    /**
     * Отправка СМС по новому, используя digital-direct.ru
     *
     * @param phoneNumber № телефона
     * @param text        текст
     * @return true если все ОК
     */
    boolean sendSMSV3(String phoneNumber, String text);

    /**
     * Отправка СМС по новому, используя kazinfoteh.kz
     *
     * @param phoneNumber № телефона
     * @param text        текст
     * @return true если все ОК
     */
    boolean sendSMSV4(String phoneNumber, String text);

    /**
     * сохраняем сообщение
     *
     * @param peopleId    id человека
     * @param userId      id пользователя
     * @param messageType вид сообщения
     * @param messageWay  вид отправки
     * @param messageDate дата сообщения
     * @param header      заголовок сообщение
     * @param body        текст сообщения
     */
    public void saveMessage(Integer peopleId, Integer userId, Integer messageType,
                            Integer messageWay, Date messageDate, String header, String body);

    /**
     * сохраняем сообщение
     *
     * @param peopleId    id человека
     * @param userId      id пользователя
     * @param messageType вид сообщения
     * @param messageWay  вид отправки
     * @param contactID   ID контакта
     * @param messageDate дата сообщения
     * @param header      заголовок сообщение
     * @param body        текст сообщения
     */
    public void saveMessage(Integer peopleId, Integer userId, Integer messageType,
                            Integer messageWay, Integer contactID, Date messageDate, String header, String body);
    
    /**
     * возвращает сообщение по id
     * @param messageId - id сообщения
     * @return
     */
    public MessagesEntity getMessage(Integer messageId);

    /**
     * возвращает сообщение по id трансферный
     * @param messageID - id сообщения
     * @return
     */
    public Messages getMessages(Integer messageID);

    /**
     * удаляем сообщение
     * @param messageId - id сообщения
     */
    public void removeMessage(Integer messageId);

    /**
     * Метод достает сообщения
     * @param peopleID кому сообщения
     * @param userID чьи сообщения
     */
    List<Messages> getMessagesByPeopleAndUserID(Integer peopleID, Integer userID);

    /**
     * Метод достает список сообщений всех сообщений человека
     * входящие или исходящие
     *
     * @param peopleID  - чьи сообщения
     * @param typeCode  - вид сообщения SMS/EMAIL/MESSAGE
     */
    List<Messages> getMessagesByPeopleID(Integer peopleID, Integer typeCode);

    /**
     * Метод достает новые сообщения для сотрудников
     *
     * @param typeCode - вид сообщения SMS/EMAIL/MESSAGE
     * @param options  - что инициализируем
     * @return список сообщений
     */
    List<Messages> getNewMessageList(Integer typeCode, Set options);

    /**
     * Метод меняет статус сообщения
     *
     * @param messageID - ID сообщения
     * @param status    - статус сообщения
     */
    void changeMessageStatus(Integer messageID, Integer status);

    /**
     * Достаем сообщения для определеного сотрудника
     *
     * @param userID  - сотрудник
     * @param options - что инициализируем
     * @return список сообщений
     */
    List<Messages> getMessagesByUserID(Integer userID, Integer typeCode, Set options);

    /**
     * Достаем все сообщения по типу
     *
     * @param typeCode - вид сообщения SMS/EMAIL/MESSAGE
     * @param options  - что инициализируем
     * @return список сообщений
     */
    List<Messages> getMessagesByTypeCode(Integer typeCode, Set options);

    /**
     * Метод ставит пользователя сообщению
     *
     * @param messageID - ID сообщения
     * @param userID    - ID пользователя кто взял на себя сообщение
     */
    void takeMessage(Integer messageID, Integer userID);

    /**
     * Метод отправляет сообщение
     * если исходящее ставим peopleMainID - кто отправляет
     * если входящее ставим peopleMainID - кому сообщение и userID кто отправитель
     * признак входящего/исходящего inOut
     *
     * @param messages - сообщение
     * @return созданное сообщение
     */
    Messages sendMessage(Messages messages);

    /**
     * Метод достает все сообщения по человеку
     *
     * @param peopleID ID чьи сообщения достаем
     * @param options параметры для инициализации
     * @return список сообщений
     */
    List<Messages> getMessagesByPeopleID(Integer peopleID, Set options);

    /**
     * Метод достает все сообщение по человеку
     *
     * @param peopleID ID чьи сообщения достаем
     * @param options  параметры для инициализации
     * @param wayID    тип сообщения ручной/авто
     * @return список сообщений
     */
    List<Messages> getMessagesByPeopleIDAndWayID(Integer peopleID, Integer wayID, Set options);

    /**
     * генерируем код для подтверждения по смс или email
     * @return
     */
    String generateCodeForSending();
    /**
     * генерируем пароль пользователя
     * @return
     */
    String generatePasswordForSending();

    /**
     * Сохраняет запись отправки сообщения о низком балансе платежной системы
     *
     * @param paymentSystemCode код платежной системы
     * @param date              дата отправки сообщения
     */
    void savePaymentMessage(Integer paymentSystemCode, Date date);

    /**
     * Удаляет запись отправки сообщения
     *
     * @param paymentSystemCode код платежной системы
     */
    void deletePaymentMessageByPaymentCode(Integer paymentSystemCode);

    /**
     * Ищет запись об отправленом сообщении
     *
     * @param paymentSystemCode код платежной системы
     * @return найденая запись или null
     */
    PaymentMessageEntity findPaymentMessageByPaymentCode(Integer paymentSystemCode);
}

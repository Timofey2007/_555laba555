package org.example._555laba555.utils;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForPasswords { // здесь по сути будем создавать уникальный хэш за счет digest, который будет получать список битов из пароля записанного и кодировать его с помощью алгоритма SHA-256
    private String password;

    public static String hashPassword(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] hashing = md.digest(password.getBytes());
            StringBuilder sixSevenStr = new StringBuilder(); // выделяет буфер в памяти, 16 символов по базе,
            for (byte b : hashing){ // проходимся по всем битам из списка
                String hex = Integer.toHexString(0xff & b); //формат int, стандартизированный формат для перевода из массивов байтов в шестнадцатиричный строковый формат
                if (hex.length() == 1) sixSevenStr.append('0'); // заполняет пустой контейнер один нулем
                sixSevenStr.append(hex);
            }
            return sixSevenStr.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    } // проверка

}

package org.example._555laba555;

import java.util.Objects;

/**
 * Класс, с названием машины
 * <p>
 * Поле name - название машины
 * Это поле не может быть null
 * </p>
 *
 * @author Timofey2007 & ...
 * @version 17.0.12
 * @see HumanBeing
 */
public class Car {
    /**
     * <p>
     * Поле name - название машины
     * Это поле не может быть null
     * </p>
     */
    private String name; //Поле может быть null

    /**
     * Конструктор машины
     * @param name - название машины
     */
    public Car(String name){
        setName(name);
    }
    //TODO КОНСТРУКТОР ДЛЯ КОПРИРОВАНИЯ НУЖЕН ИЛИ НЕТ

    /**
     * Сеттер для имени с валидацией
     * @param name - название машины
     * @throws IllegalArgumentException значение должно быть заполнено
     */
    public void setName(String name)throws IllegalArgumentException {
        if (name == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.name = name;
    }
    /**
     * Создаёт копию объекта Car
     * @return новый объект Car с теми же значениями полей
     */
    public Car copy() {
        return new Car(this.name);
    }


    /**
     * Стандартный геттер для name
     * @return Возвращает значение name
     */
    public String getName() {
        return name;
    }


    /**
     * Сравнивает объекты
     * @param obj  - сравниваемый объект
     * @return true, если одинаковые объекты, false, если нет
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car obj1 = (Car) obj;
        return name == obj1.name;
    }

    /**
     *  Красивый вывод информации по машине
     * @return информация о машине
     */
    @Override
    public String toString() {
        return "Название машины: " + name;
    }

    /**
     * Возвращает хэш-код машины
     * @return хэш-код машины
     */
    @Override
    public int hashCode() {
        return 31*(int)name.hashCode();
    }
}

package org.example._555laba555.lastvt;

import java.util.Objects;

/**
 * Класс, представляющий координаты организации
 * <p>
 * Используется для хранения координат (x, y).
 * Координата x не может быть null
 * Координата y не может быть null
 * </p>
 *
 * @author Timofey2007 & ...
 * @version 17.0.12
 * @see HumanBeing
 */
public class Coordinates {
    /**
     * Координата X
     * <p>
     * Не может быть null
     * </p>
     */
    private Float x; //Поле не может быть null
    /**
     * Координата Y
     * <p>
     * Не может быть null
     * </p>
     */
    private Double y; //Поле не может быть null

    /**
     * Конструктор координаты
     * @param x - координата x
     * @param y - координата y
     */
    public Coordinates(Float x, Double y){
        setY(y);
        setX(x);
    }
    //TODO подумть


    /**
     * Обновляет значение y
     * @throws IllegalArgumentException значение должно быть заполнено
     * @param y - координата y
     */
    public void setY(Double y)throws  IllegalArgumentException{
        if (y == null){
            throw new IllegalArgumentException("поле Y не может быть null");

        }
        this.y = y;
    }

    /**
     * Стандартный геттер для Y
     * @return Возвращает значение y в координатах
     */
    public Double getY(){
        return this.y;
    }

    /**
     * Обновляет значение x
     * @throws IllegalArgumentException значение должно быть заполнено
     * @param x - координата x
     */
    public void setX(Float x)throws  IllegalArgumentException{
        if (x == null){
            throw new IllegalArgumentException("поле X не может быть null");
        }
        this.x = x;
    }

    /**
     * Стандартный геттер для X
     * @return Возвращает значение x в координатах
     */
    public Float getX(){
        return this.x;
    }

    /**
     * Красивый вывод координат
     * @return Возвращает интересующие координаты
     */
    @Override
    public String toString() {
        return "Координата: x: "+  x + "; y: "+ y;
    }

    /**
     * Сравнивает текущие координаты с другим объектом
     * @param obj  - объект для сравнения
     * @return true если координаты равны, false, если нет
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates obj1 = (Coordinates) obj;
        return y == obj1.y && Objects.equals(x, obj1.x);
    }

    /**
     *Возвращает хеш-код координат
     * @return хеш-код координат
     */
    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = (int) (31 * result + y);
        return result;

    }

    /**
     * Создает копию текущих координат.
     *
     * @return новый объект Coordinates с теми же значениями
     */
    public Coordinates copy() {
        return new Coordinates(this.x, this.y);
    }

}
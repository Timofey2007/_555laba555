package org.example._555laba555.lastvt;

import java.time.LocalDate;

import java.util.Objects;


/**
 *
 */
public class HumanBeing implements Comparable<HumanBeing> {
    /**
     * Статический счётчик для генерации уникальных ID.
     * <p>
     * Увеличивается при создании каждой новой организации.
     * Используется только при создании объектов, а не при загрузке из файла.
     * </p>
     */
    private static long idCounter = 1;
    /**
     *
     */
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля
    // должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     *
     */
    private static Long maxId = 0L;
    /**
     *
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     *
     */
    private Coordinates coordinates; //Поле не может быть null
    /**
     *
     */
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно
    // генерироваться автоматически
    /**
     *
     */
    private boolean realHero;
    /**
     *
     */
    private Boolean hasToothpick; //Поле может быть null
    /**
     *
     */
    private int impactSpeed;
    /**
     *
     */
    private WeaponType weaponType; //Поле не может быть null
    /**
     *
     */
    private Mood mood; //Поле не может быть null
    /**
     *
     */
    private Car car; //Поле может быть null
//TODO я сделал везде только null исключение, а на полях car и hasToothpick оно не имеет смысла потому что там не
// требуется(надо переписать по умному)
    /**
     * Конструктор для HumanBeing
     */
    public HumanBeing(Car car, Long id, Mood mood, WeaponType weaponType, int impactSpeed, Boolean hasToothpick,
                      boolean realHero, Coordinates coordinates, String name ){
        this.id = idCounter++;
        this.creationDate = java.time.LocalDate.now();
        setCar(car);
        setCoordinates(coordinates);
        setId(id);
        setImpactSpeed(impactSpeed);
        setHasToothpick(hasToothpick);
        setMood(mood);
        setWeaponType(weaponType);
        setRealHero(realHero);
        setName(name);
    }
    /**
     * Конструктор для HumanBeing при создании персон из загруженного файла
     * Используется в методе copy()
     */
    public HumanBeing(Long id, String name, Coordinates coordinates, LocalDate creationDate,
                      boolean realHero, Boolean hasToothpick, int impactSpeed,
                      WeaponType weaponType, Mood mood, Car car) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.weaponType = weaponType;
        this.mood = mood;
        this.car = car;

        if (this.id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("имя не может быть null или пустым");
        }
        //TODO доделать валидацию
    }

    /**
     * Геттер для id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Сеттер с валидацией для id
     * @param id - id человека
     */
    private void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id организации не может быть null");
        }
        if (id < maxId) {
            throw new IllegalArgumentException(
                    String.format("id (%d) не может быть меньше максимального существующего id (%d)",
                            id, maxId)
            );
        }
        this.id = id;
        if (id > maxId) { //обновление максимального id
            maxId = id;
        }
    }
    /**
     * Сбрасывает счётчик id
     * <p>
     * Используется для тестирования
     * В обычной работе программы не вызывается
     * </p>
     */
    public static void resetIdCounter() {
        idCounter = 1;
        maxId = 0L;
    }
    /**
     * Возвращает текущее значение счётчика id
     *
     * @return текущее значение счётчика id
     */
    public static long getIdCounter() {
        return idCounter;
    }

    /**
     * Управление временем
     * @return время занесения человека в базу данных
     */
    public java.time.LocalDate getCreationDate() {
        return creationDate;
    }
    /**
     * Устанавливает дату создания
     * <p>
     * Используется только при загрузке из файла
     * </p>
     */
    private void setCreationDate(java.time.LocalDate creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null");
        }
        this.creationDate = creationDate;
    }

    /**
     * Стандартный геттер для имени
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Сеттер для имени с валидацией
     * @param name - имя человека1
     * @throws IllegalArgumentException значение должно быть заполнено
     */
    public void setName(String name)throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("поле не может быть null или пустым");
        }
        this.name = name;
    }

    /**
     * Геттер для координат
     * @return coordinates {@link Coordinates}
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Сеттер для координат
     * @param coordinates {@link Coordinates}
     */
    public void setCoordinates(Coordinates coordinates)throws IllegalArgumentException {
        if (coordinates == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.coordinates = coordinates;
    }

    /**
     * Проверяет настоящий ли герой указанный человек
     * @return true, если соответствует условиям героя, false, если нет
     */
    public boolean isRealHero() {
        return realHero;
    }

    /**
     * Проверяет существование героя
     * @param realHero - является ли персонаж героем
     */
    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }

    //TODO придумать интересную реализацию проверки героичности человека и додумать сеттер

    /**
     * Узнает есть ли зубочистка у человека
     * @return true, если человек имеет зубочистку, false, если нет
     */
    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    /**
     * Задает наличие зубочистки у человека
     * @param hasToothpick - НАЛИЧИЕ ЗУБОЧИСТКИ
     */
    public void setHasToothpick(Boolean hasToothpick) {
        if (hasToothpick == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.hasToothpick = hasToothpick;
    }

    /**
     * Геттер для скорости человека
     * @return скорость человека
     */
    public int getImpactSpeed() {
        return impactSpeed;
    }

    /**
     * Сеттер для скорости человека
     * @param impactSpeed - СКОРОСТЬ
     */
    public void setImpactSpeed(int impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    /**
     * Геттер для типа оружия
     * @return weaponType - тип оружия {@link WeaponType}
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Сеттер для типа оружия
     * @param weaponType - тип оружия {@link WeaponType}
     */
    public void setWeaponType(WeaponType weaponType) {
        if (weaponType == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.weaponType = weaponType;
    }

    /**
     * Геттер для настроения человека
     * @return mood - настроение человека {@link Mood}
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Сеттер для настроения человека
     * @param mood - настроение человека {@link Mood}
     */
    public void setMood(Mood mood) {
        if (mood == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.mood = mood;
    }

    /**
     * Геттер для машины
     * @return название машины {@link Car}
     */
    public Car getCar() {
        return car;
    }

    /**
     * Сеттер для машины
     * @param car {@link Car}
     */
    public void setCar(Car car) {
        if (car == null){
            throw new IllegalArgumentException("поле не может быть null");
        }
        this.car = car;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HumanBeing that = (HumanBeing) obj;
        return Objects.equals(id, that.id);
    }

    /**
     * Вывод информации о персоне
     * @return информация о персоне
     */
    @Override
    public String toString() {
        return String.format(
                "HumanBeing:%n\n"+
                "id:%d%n\n"+
                "Name: %s%n\n"+
                "Coordinates: %s%n\n"+
                "Created: %s%n\n"+
                "Real Hero:%b%n\n"+
                "Has Toothpick:%b%n\n"+
                "ImpactSpeed:%d%n\n"+
                "WeaponType:%s%n\n"+
                "Mood:%s%n"+
                "Car:%s%n", id, name, coordinates, creationDate, realHero, hasToothpick, impactSpeed,
                weaponType, mood, car);
    }


    /**
     * Сравнивает двух человек по дате занесения их данных в базу
     * @param o - объект с которым будет сравниваться другой
     * @return  Отрицательное число = текущий объект меньше объекта o
     * Ноль = объекты равны
     * Положительное число = текущий объект больше объекта o
     */
    @Override
    public int compareTo(HumanBeing o) {
        if (o == null) return 1;
        int nameCompare = this.name.compareTo(o.name);
        if (nameCompare != 0) {
            return nameCompare;
        }
        return this.creationDate.compareTo(o.creationDate);
    }

    public HumanBeing copy() {
        return new HumanBeing(
                this.id,
                this.name,
                this.coordinates.copy(),
                this.creationDate,
                this.realHero,
                this.hasToothpick,
                this.impactSpeed,
                this.weaponType,
                this.mood,
                this.car != null ? this.car.copy() : null
        );
    }
}

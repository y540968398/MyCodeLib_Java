package com.robert.designmodel.objectcreate.singleton;

/**
 * java23中设计模式 - 对象创建模式 - 单例模式 - 饿汉式.
 * 
 * @author Robert.Gao 2016年3月7日13:35:49
 * 
 * @description: 在该类被虚拟机加载时就实例化该类，在调用 getInstance 方法时，将该对象的引用返回给调用方。
 *
 */
public class CreateOnInit
{
    /**
     * 声明一个静态变量(一个类，在运行期间，该变量只会有一个实例。即多个对象中的该变量使用的是同个实例。)用于存储当前类的实例.
     * 
     * @description: 在类加载时，初始化该对象。
     */
    private static Object instance = new CreateOnInit();

    /**
     * 提供一个获得当前类实例的方法.
     * 
     * @return 当前类的实例
     */
    public static Object getInstance()
    {
        return instance;
    }

}

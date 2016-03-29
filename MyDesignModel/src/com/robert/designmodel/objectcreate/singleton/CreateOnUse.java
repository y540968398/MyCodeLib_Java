package com.robert.designmodel.objectcreate.singleton;

/**
 * java23中设计模式 - 对象创建模式 - 单例模式 - 懒汉式.
 * 
 * @author Robert.Gao 2016年3月7日13:35:49
 * 
 * @description: 使用枚举的特性来实现一个 单例模式的例子，借助枚举是一个 final class ，其中的对象是一个常量(只会有一个实例)。
 *
 */
public class CreateOnUse
{
    /**
     * 声明一个静态变量(一个类，在运行期间，该变量只会有一个实例。即多个对象中的该变量使用的是同个实例。)用于存储当前类的实例.
     * 
     * @description: 在类加载时，该变量值为 null 。
     */
    private static Object instance;

    /**
     * 提供一个获得当前类实例的方法.
     * 
     * @return 当前类的实例
     */
    public static Object getInstance()
    {
        if (instance == null)
        {
            instance = new CreateOnInit();
        }
        return instance;
    }
}

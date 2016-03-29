package com.robert.designmodel.objectcreate.singleton;

/**
 * java23中设计模式 - 对象创建模式 - 单例模式 - 枚举式
 * 
 * @author Robert.Gao 2016年3月7日13:35:49
 * 
 * @description: 使用枚举的特性来实现一个 单例模式的例子，借助枚举是一个 final class ，其中的对象是一个常量(只会有一个实例)。
 *
 */
public enum CreateByEnum
{
    /**
     * 创建一个常量来保存当前类对象的实例.
     * 
     * @description： 不需要提供方法来供外部获取，直接以 类名.变量 就能获取该类的实例。
     */
    INSTANCE;

    /**
     * 对象具有的业务方法(多个).
     */
    public void getName()
    {

    }
}

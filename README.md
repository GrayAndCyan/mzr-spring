# mzr-spring
为加深理解Spring框架源码而完成的学习性项目，手写简易的Spring框架。

### step-01
* 实现简易的bean容器，编写bean定义类与bean工厂类。
* 使用`ConcurrentHashMap`作为bean的容器。

### step-02
* `AbstractBeanFactory`类使用**模板设计模式**（定义一个操作中的算法的骨架，而将一些步骤延迟到子类中）。模板方法是`getBean()`，它完成了逻辑骨架，并在其中调用了抽象方法 `createBean()` `getBeanDefinition()`，这些抽象方法延迟到子类实现，但不是由一个子类全部实现，而是由职责不同的两个子类实现，体现单一职责原则。
* 添加单例模块。懒性实例化bean（在`getBean()`中完成bean实例化，即用时再实例化），并将实例化后的bean存在单例缓存容器中一份，在获取bean时优先查单例缓存而无需再次实例化。

### step-03
* 实现含参构造器实例化bean,根据传入的参数粗略寻找匹配的构造器（未比对参数类型，仅根据参数数量来匹配）。
* 采用策略模式，提供jdk反射与cjlib两种方式创建实例。但cjlib创建实例方案在jdk17+无法使用下面是cglib官方给出的说明：

>IMPORTANT NOTE: cglib is unmaintained and does not work well (or possibly at all?) in newer JDKs, particularly JDK17+. If you need to support newer JDKs, we will accept well-tested well-thought-out patches... but you'll probably have better luck migrating to something like ByteBuddy.

因此增加基于`ByteBuddy`的bean实例化策略类`ByteBuddySubClassingInstantiationStrategy`。

### step-04
* 支持bean的属性与依赖注入。
* 在`BeanDefinition`中添加`PropertyValues`属性，表面bean需要注入的属性。
* 在属性注入时，属性分为两种，bean属性与其他属性。前者即当前bean对象依赖其他bean对象。bean属性使用`BeanReference`类型，该类型有一个`beanName`字段，表明了被依赖的bean的name。


### step-05
* 添加资源加载和XML文件解析实现，在`core.io`包下定义资源与资源加载器相关接口与实现类。资源加载器通过资源定位信息参数，返回具体资源。
bean定义读取器依赖资源加载器与注册表，通过加载器拿到资源后，从资源文件中读取bean定义，并通过注册表注册bean定义，放入IoC容器。
在首次获取这个bean时去实例化它，并放入单例缓存Map。
* 接口：`BeanDefinitionReader`、抽象类：`AbstractBeanDefinitionReader`、实现类：`XmlBeanDefinitionReader`，
这三部分内容主要是合理清晰的处理了资源读取后的注册 `Bean` 容器操作。
接口管定义，抽象类处理非接口功能外的注册 `Bean` 组件填充，最终实现类即可只关心具体的业务实现。

### step-06  应用上下文

* 完成`ClassPathXmlApplicationContext`类及其继承体系的编写:
    ![ClassPathXmlApplicationContext.png](img%2FClassPathXmlApplicationContext.png)
  `ClassPathXmlApplicationContext`类接收资源地址，并调用`refresh()`方法完成一系列操作，包括：
  1. bean工厂的创建；
  2. 资源的读取，读取开发者配置的bean定义，加载这些bean定义到上一步创建的bean工厂；
  3. 在Bean实例化之前，执行BeanFactoryPostProcessor的处理方法;
  4. 注册BeanPostProcessor；
  5. 提前实例化一遍`beanDefinitionMap`中定义的bean。
* 定义Spring中两个重要接口`BeanFactoryPostProcessor` 与`BeanPostProcessor`，
它们实现类的处理方法分别在「`Bean` 对象注册后但实例化之前」和「`Bean` 对象实例化并属性注入之后，执行初始化方法前后」时执行。
分别体现在`AbstractApplicationContext`类和`AbstractAutowireCapableBeanFactory`类中。
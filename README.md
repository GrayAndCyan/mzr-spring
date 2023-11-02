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
它们实现类的处理方法分别在「`Bean` 对象注册后但实例化之前」和「每个`Bean` 对象实例化并属性注入之后，执行初始化方法前后」时执行。
分别体现在`AbstractApplicationContext`类和`AbstractAutowireCapableBeanFactory`类中。


### step-07  bean初始化与销毁方法

1. bean初始化方法在bean实例化并属性注入后被调用。bean销毁方法在虚拟机关闭时调用。
2. 上述两个方法有两种方式声明：
   1. 让注册为bean的类实现`InitializingBean`  `DisposableBean` 接口，实现初始化和销毁方法，这样方法会被框架主动调用（也就是非反射的常规调用方式）。
   2. 在bean类中实现初始化和销毁方法，并在xml配置中指明方法名。这样方法会被框架以反射方式调用。
3. 一种值得一提的分层设计：A类实现B接口、继承C类。B接口定义的方法在C类中实现。
下图中的 `DefaultListableBeanFactory`类，`ConfigurableListableBeanFactory` 接口， `DefaultSingletonBeanRegistry` 类 便是这种关系，实现`destroySingletons()` 方法。维护了各司其职的单一职责原则。
![DefaultListableBeanFactory.png](img%2FDefaultListableBeanFactory.png)

4. **代理模式、装饰模式、适配器模式，傻傻分不清？**

    代理模式与装饰器模式的区别在于：谁持有引用，或者说用户是否持有原对象的引用。
试想大量应用装饰器模式的 Java IO 中的类，被装饰对象由用户持有引用并将其传入装饰器；而在代理模式，代理类目的就是对外屏蔽被代理对象，被代理对象是被代理类持有、使用和增强的。

    两者都是对原对象的增强。

    适配器模式与上面两个模式的区别在于：
    1. 为了达到适配目的，它一定是改变了被适配对象的接口定义的。在装饰器模式中并不强调接口与原对象的一致性，但在代理模式中，代理类是提供与被代理对象完全一致的接口的，让用户就像直接调用原对象一样地调用了代理对象。
    2. 适配器模式只做适配，不做功能增强或改变。目的不同，适配器的目的是适配，它不干扰被适配对象本该提供的功能。手机接口需要的是 Type-c 插头，我耳机是圆孔插头，那转接头（适配器）要做的只是接受圆孔插头， 对外提供 形为 Type-c 的服务，不干涉我耳机的原本功能。这在 Java 编码中体现为适配器类实现 Type-c 接口（定义了用户端需要的规范），实现 `offerTypeC()` 方法，供用户端调用。

    `Spring` 的 `DisposableBeanAdapter` 类是使用了适配器模式的一个例子：

    被适配对象是 `beanDefinition` ，调用方想要只是调用 `destroy()` 就完成「执行 `bean` 销毁方法」的工作。`beanDefinition` 提供的不是`destroy()` 这个接口，而是 `destroyMethodName` ，那么由 `DisposableBeanAdapter` 去做「持有功能提供者，统一对外接口」的工作。

    那么这里为什么不在 `BeanDefinition` 中实现 `destroy()` ，然后调用方去调它的这个方法？

    因为 `Spring` 提供多种 用户定义并指定`bean`销毁方法 的渠道，不止 “配置 `destroyMethodName` ”这一种，还有让 `bean` 类型实现 `DisposableBean` 接口的这种方式。这是不能在框架的调用处写死的，那不如在调用处只是调用 `DisposableBeanAdapter # destroy(Object bean, BeanDefinition beanDefinitio)`，而不必关心更多细节。
    
    这里的“调用处”，即供给 「注册的虚拟机关闭时的钩子函数」 的线程任务。

### step-08    Aware感知

赋予bean获取所属`BeanFactory,` `BeanClassLoader`, `ApplicationContext`, `BeanName` 的能力，只需要让 bean 实现对应的 `Aware` 接口即可。
具体做法如下：
1. 定义了`BeanFactoryAware,` `BeanClassLoaderAware`, `ApplicationContextAware`, `BeanNameAware` 四个接口，定义 `setXXX()` 方法。
在bean初始化时（更具体一点就是在执行bean后置处理器的beforeInitialization方法之前），判断bean是否继承了上述接口，如果是，框架调用上述接口定义的 `set`
方法，通过把需要的框架对象（bean所属的`BeanFactory,` `BeanClassLoader``BeanName`）传参的方式，让bean可以持有和使用这些对象。
2. bean所属`ApplicationContext` 的获取相对特殊，使用了一个继承 `BeanPostProcessor` 的 `ApplicationContextAwareProcessor`，它在 `postProcessBeforeInitialization()`
方法对实现`ApplicationContextAware`接口的bean进行所属应用上下文对象的注入。那这个bean后置处理器是怎么拿到应用上下文对象的呢？在 `AbstractApplicationContext`执行 `refresh()` 方法中，
创建了这个bean后置处理器，并在构造时给它提供了自身这个应用上下文对象。


现在可以相对完整地说一下Bean的生命周期了：
1. 以配置文件或注解的方式声明 `bean`；
2. 读取配置文件或注解，创建对应的 `beanDefinition`；
3. 注册 `bean` ,实际上是将对应的 `beanDefinition` 放入一个 `map` 容器中；
4. 执行 `BeanFactoryPostProcessor` 的处理方法操作已注册的 `beanDefinition`；
5. 实例化 `bean`；
6. 为 `bean` 填充属性（属性信息是存储在 `beanDefinition` 中的，另外，如果需要填充的属性是 bean引用 ， 那么先去执行这个新 bean 的创建工作（实例化、属性填充、感知、初始化、初始化前后处理方法））；
7. `Aware` 感知，即上文所说，使实现特定 `Aware` 接口的 `bean` 获取到 特定的框架对象。
8. 在调用 `bean` 自定义的初始化方法之前，先执行每个 `BeanPostProcessor` 的 `postProcessBeforeInitialization()` 方法；
9. 调用 `bean` 自定义的初始化方法;
10. 在调用 `bean` 自定义的初始化方法之后，执行每个 `BeanPostProcessor` 的 `postProcessAfterInitialization()` 方法；
11. 使用 `bean`；
12. 调用自定义的 `bean` 销毁方法，这发生在虚拟机关闭时的钩子函数中。
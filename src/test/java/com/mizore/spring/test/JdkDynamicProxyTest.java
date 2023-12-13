package com.mizore.spring.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class JdkDynamicProxyTest {

    public static void main(String[] args) {
        IVideoService videoService = new VideoServiceImpl();
//        videoService.saveVideo();
        IVideoService videoServiceP = (IVideoService) ProxyFactory.getProxyObject(videoService);
        System.out.println(videoServiceP.saveVideo());
    }

}

class ProxyFactory {

    public static Object getProxyObject(Object originObject) {

        return Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                originObject.getClass().getInterfaces(),
                new InvocationHandler() {
                    /**
                     *
                     * @param proxy the proxy instance that the method was invoked on
                     * 代理类实例
                     * @param method the {@code Method} instance corresponding to
                     * the interface method invoked on the proxy instance.  The declaring
                     * class of the {@code Method} object will be the interface that
                     * the method was declared in, which may be a superinterface of the
                     * proxy interface that the proxy class inherits the method through.
                     * 接口方法信息
                     * @param args an array of objects containing the values of the
                     * arguments passed in the method invocation on the proxy instance,
                     * or {@code null} if interface method takes no arguments.
                     * Arguments of primitive types are wrapped in instances of the
                     * appropriate primitive wrapper class, such as
                     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
                     * 调用代理对象方法时传入的参数
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println(method.getName()  + "开始了: " + LocalDateTime.now());
                        Object res = method.invoke(originObject, args);
                        System.out.println(method.getName()  + "执行结束: " + LocalDateTime.now());
                        return res;
                    }
                }
        );
    }
}


interface IVideoService{
    boolean saveVideo();
}

class VideoServiceImpl implements IVideoService{

    @Override
    public boolean saveVideo() {
        System.out.println("do save video...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
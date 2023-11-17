package com.mizore.spring.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.core.io.Resource;
import com.mizore.spring.core.io.ResourceLoader;
import com.mizore.spring.beans.PropertyValue;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanReference;
import com.mizore.spring.beans.factory.support.AbstractBeanDefinitionReader;
import com.mizore.spring.beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {
        super(resourceLoader, registry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            InputStream is = resource.getInputStream();
            doLoadBeanDefinition(is);
        } catch (IOException | ClassNotFoundException e) {
            throw  new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    protected void doLoadBeanDefinition(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (!(childNodes.item(i) instanceof  Element)) {
                // 非元素标签
                continue;
            }
            if (!"bean".equals(childNodes.item(i).getNodeName())) {
                // 非bean标签
                continue;
            }

            // 解析bean标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String initMethod = bean.getAttribute("init-method");
            String destroyMethod = bean.getAttribute("destroy-method");
            String beanScope = bean.getAttribute("scope");


            // 获取Class,方便获取类中的名称
            Class<?> clazz = Class.forName(className);
            // 优先级id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            // 定义bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethod);

            if (StrUtil.isNotEmpty(beanScope)) {
                // 判空是为了避免null覆盖默认值（默认单例）
                beanDefinition.setScope(beanScope);
            }
            // 读取属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                // 找到属性标签
                if (!((bean.getChildNodes().item(j)) instanceof Element)) continue;

                if (!("property".equals(bean.getChildNodes().item(j).getNodeName()))) continue;
                // 解析property标签
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
                // 获取属性值
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
                // 创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            // 注册beanDefinition
            getRegistry().registryBeanDefinition(beanName, beanDefinition);
        }
    }
}

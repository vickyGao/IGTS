package com.ntu.igts.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class OrderedRunner extends SpringJUnit4ClassRunner {

    private List<FrameworkMethod> testMethodList;

    public OrderedRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (testMethodList == null) {
            testMethodList = new ArrayList<FrameworkMethod>();
            testMethodList.addAll(super.computeTestMethods());
            Collections.sort(testMethodList, new Comparator<FrameworkMethod>() {
                @Override
                public int compare(FrameworkMethod sourceFrameworkMethod, FrameworkMethod targetFrameworkMethod) {
                    Order sourceOrder = sourceFrameworkMethod.getAnnotation(Order.class);
                    Order targetOrder = targetFrameworkMethod.getAnnotation(Order.class);
                    if (sourceOrder == null || targetOrder == null) {
                        return 0;
                    } else {
                        return sourceOrder.value() - targetOrder.value();
                    }
                }
            });
        }
        return testMethodList;
    }

}

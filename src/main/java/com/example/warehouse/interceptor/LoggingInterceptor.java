package com.example.warehouse.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Log
public class LoggingInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logMethodEntry(InvocationContext context) throws Exception {
        logger.info("Call to method " + context.getMethod().getName() + " with parameters: "
                + Arrays.toString(context.getParameters())
                + " in "
                + context.getMethod().getDeclaringClass());

        return context.proceed();
    }
}

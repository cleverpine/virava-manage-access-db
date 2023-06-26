package com.cleverpine.viravamanageaccessdb.aspect;

import com.cleverpine.viravamanageacesscore.aspect.TransactionalAspect;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
@RequiredArgsConstructor
public class DBTransactionalAspect extends TransactionalAspect {

    private final TransactionTemplate transactionTemplate;

    @Override
    public Object aroundAMTransactional(ProceedingJoinPoint joinPoint) throws Throwable {
        return transactionTemplate.execute(status -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        });
    }
}

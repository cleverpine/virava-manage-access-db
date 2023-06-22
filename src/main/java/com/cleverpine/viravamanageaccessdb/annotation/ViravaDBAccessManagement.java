package com.cleverpine.viravamanageaccessdb.annotation;

import com.cleverpine.viravamanageaccessdb.config.DBAppConfig;
import com.cleverpine.viravamanageacesscore.config.AMResponseEntityUtilConfig;
import com.cleverpine.viravamanageacesscore.init.ViravaAccessManagementInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({ViravaAccessManagementInitializer.class, AMResponseEntityUtilConfig.class, DBAppConfig.class})
public @interface ViravaDBAccessManagement {
}

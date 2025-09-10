package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.rag.config.VectorConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(VectorConfiguration.class)
@ComponentScan("com.dahuaboke.mpda.core")
public class CoreAutoConfiguration {
}

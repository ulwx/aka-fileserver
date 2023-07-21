package com.github.ulwx.aka.fileserver;

import com.github.ulwx.aka.webmvc.MyPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource(name="classpath*:aka-application-fileupload.yml"
        , value = {"classpath*:aka-application-fileupload.yml"},
        factory = MyPropertySourceFactory.class)

@Configuration("com.github.ulwx.aka.fileserver.AkaFileServerConfiguration")
public class AkaFileServerConfiguration {

}


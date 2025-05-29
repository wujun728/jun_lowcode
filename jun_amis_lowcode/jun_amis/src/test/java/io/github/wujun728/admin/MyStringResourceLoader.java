package io.github.wujun728.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
public class MyStringResourceLoader extends ResourceLoader {
    @Override
    public void init(ExtendedProperties configuration) {

    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        log.info("source:{}",source);

        String t = "";
        if(source.equals("test.vm")){
            t = "hello #parse('/abc.vm')";
        }else if(source.equals("/abc.vm")){
            t = "abc 123";
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(t.getBytes());
        return byteArrayInputStream;
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}

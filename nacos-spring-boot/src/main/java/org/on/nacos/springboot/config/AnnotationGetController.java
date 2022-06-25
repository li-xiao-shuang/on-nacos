/*
 * Copyright 2022 on-nacos open source organization.
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.on.nacos.springboot.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springboot/nacos/config")
@NacosPropertySource(dataId = "nacos-datasource.properties", autoRefreshed = true, before = SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, after = SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)
public class AnnotationGetController {
    
    @NacosValue(value = "${name:}", autoRefreshed = true)
    private String name;
    
    @NacosValue(value = "${url:}", autoRefreshed = true)
    private String url;
    
    @NacosValue(value = "${username:}", autoRefreshed = true)
    private String username;
    
    @NacosValue(value = "${password:}", autoRefreshed = true)
    private String password;
    
    @NacosValue(value = "${driverClassName:}", autoRefreshed = true)
    private String driverClassName;
    
    @GetMapping(path = "annotation/get")
    private Map<String, String> getNacosDataSource2() {
        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("url", url);
        result.put("username", username);
        result.put("password", password);
        result.put("driverClassName", driverClassName);
        return result;
    }
}

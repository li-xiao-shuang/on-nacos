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

package org.on.nacos.springcloud.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lixiaoshuang
 */
@RestController
public class RestTemplateController {
    
    @Autowired
    public RestTemplate restTemplate;
    
    @Bean
    @LoadBalanced
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }
    
    @GetMapping("/call/order/{orderid}")
    public String callEcho(@PathVariable String orderid) {
        // 访问应用 SPRING-CLOUD-CONFIG 的 REST "/order/{orderid}"
        return restTemplate.getForObject("http://SPRING-CLOUD-CONFIG/order/" + orderid, String.class);
    }
}
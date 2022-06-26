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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lixiaoshuang
 */
@RestController
public class OrderServiceController {
    
    @GetMapping("/order/{orderid}")
    public String echo(@PathVariable String orderid) {
        System.out.println("接到远程调用订单服务请求");
        return "[ORDER] : " + "订单id：[" + orderid + "] 的订单信息";
    }
}
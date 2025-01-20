package gientech.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication implements CommandLineRunner {

    private List<String> serviceList;

    @Value("${serviceList:servicea,serviceb}")
    private String serviceListProperty;

    @Value("${service:servicea}")
    private String service;

    private int code = 200;

    @Value("${serverHandleTime:0}")
    private int serverHandleTime;

    @Value("${printHeader:false}")
    private boolean printHeader;

    @PostConstruct
    public void init() {
        // Initialize serviceList from command line arguments if needed
        serviceList = Arrays.asList(serviceListProperty.split(","));
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public String handleRequest(HttpServletRequest request, HttpEntity<String> requestEntity) throws InterruptedException {
        Thread.sleep(serverHandleTime * 1000);

        if (printHeader) {
            System.out.println("Request Header =======");
            requestEntity.getHeaders().forEach((key, value) -> System.out.println("key: " + key + ", value: " + value));
            System.out.println("Request Header =======");
        }

        int serviceIndex = serviceList.indexOf(service);
        if (serviceIndex == -1 || serviceIndex == serviceList.size() - 1) {
            return System.getenv("POD_NAME");
        }

        String nextServiceName = serviceList.get(serviceIndex + 1);

        // 获取请求URI部分，例如 /aaa/bbb
        String requestURI = request.getRequestURI();

        // 获取查询字符串部分，例如 b=a
        String queryString = request.getQueryString();

        // 组合URI和查询字符串
        String fullURL = requestURI;
        if (queryString != null) {
            fullURL += "?" + queryString;
        }

        String url = "http://" + nextServiceName + ":8080"+fullURL;

        RestTemplate restTemplate = new RestTemplate();
        // 注意：这里直接使用 requestEntity.getMethod() 而不是解析方法名
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.resolve(request.getMethod()), // 直接使用 HttpMethod 枚举
                requestEntity,
                String.class
        );

        return System.getenv("POD_NAME") + "-->" + response.getBody();
    }

    @Override
    public void run(String... args) throws Exception {
        // Command line arguments can be parsed here if needed
    }
}
## Make the development of API services faster and more efficient (Only for springboot)

### 1、If you want to use request uniform response

Introducing dependencies to your pom.xml

```
<dependency>
    <groupId>com.lihansir.platform</groupId>
    <artifactId>lihansir-spring-boot-starter</artifactId>
    <version>${latest version}</version>
</dependency>
```
[click here to find the latest version](https://mvnrepository.com/artifact/com.lihansir.platform/lihansir-spring-boot-starter)

Just need two annotations

```
@EnableRestResult 

@UseRestResult
```
 
 For example:
```
@EnableRestResult  // Enable the uniform response
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@UseRestResult
@RestController
public class DemoController {}

@RestController
public class DemoController {

    @UseRestResult
    @PostMapping("create")
    public CreateOrderResultVO create(@Validated CreateOrderDTO createOrderDTO) {
        return ...
    }

}
```
Then all interfaces will return with a uniform response, like this
```
{
    "data": {}  // can be any object,such as String、List、Number...
    "errorCode": "success",
    "errorMessage": "success",
    "host": "127.0.0.1",
    "showType": 4,
    "success": true,
    "traceId": "04f3a87c-0d15-4832-8573-6676ea96546e"
}
```
You can use `@UseRestResult` on type or method

- If you use `@UseRestResult` on type, that means that all methods in the class use a uniform response

- If you use `@UseRestResult` on method, that means just this method use a uniform response

- If you use `@UseRestResult` on type, but you want one of these methods not to use a uniform response,
    you can use `@IgnoreRestResult` on this method to ignore the uniform response
    
### 2、Built in global exception handling

All exceptions will be caught and returned using a unified response
```
{
    "success": false,
    "data": null,
    "errorCode": "errorCode",
    "errorMessage": "errorMessage",
    "showType": 4,
    "traceId": "f07a25e9-f4aa-4f75-8eb7-41d6402d70e5",
    "host": "127.0.0.1"
}
```

Two exceptions (BusinessException, ParamException) are built in for developers to use in business code

You can implement `RestCode` to extended custom exception code

```
public enum CommonCode implements RestCode {

    FAILED("request_failed", "failed"),

    PARAM_CHECK_ERROR("param_check_error", "Parameter verification failed"),

    SERVLET_ERROR("bad_request", "Bad request"),

    ERROR_URL("request_path_not_found", "Request path error"),

    BUSINESS_EXECUTE_ERROR("business_execute_error", "Business logic processing exception"),

    ILLEGAL_ARGUMENT_ERROR("illegal_argument_error", "Illegal argument error"),

    PROGRAM_EXECUTION_EXCEPTION("program_execution_exception", "Program execution exception");

    private final String errorCode;

    private final String errorMessage;

    CommonCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
}
```

```
throw new BusinessException(CommonCode.PARAM_CHECK_ERROR);
```

Finally, I invite you to pay attention to my personal website:
[https://www.lihansir.com](https://www.lihansir.com)

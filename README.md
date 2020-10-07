## Make the development of API services faster and more efficient (Only for springboot)

### 1、If you want to use request uniform response

Just need two annotations

```
@EnableRestResult 

@UseRestResult
```
 
 For example:
```
@EnableRestResult  // Enable the uniform response
@SpringBootApplication
public class CloudPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPayApplication.class, args);
    }

}

@UseRestResult
@RestController
public class PayController {}

@RestController
public class PayController {

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
    code: 200,
    msg: "success",
    data: {}  // can be any object,such as String、List、Number...
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
    code: 30001, // error code
    msg: "error msg",
    data: null
}
```

Two exceptions (BusinessException, ParamException) are built in for developers to use in business code

You can implement `RestCode` to extended custom exception code

```
public enum CommonCode implements RestCode {

    OK(200, "success"),

    ERROR_TOKEN(4001, "Token verification failed"),

    PARAM_CHECK_ERROR(4002, "Parameter verification failed"),

    REQUEST_METHOD_ERROR(4003, "Unsupported request method"),

    ERROR_URL(4004, "Request path error"),

    BUSINESS_EXECUTE_ERROR(4005, "Business logic processing exception"),

    ILLEGAL_ARGUMENT_ERROR(4006, "Illegal argument error"),

    SERVER_ERROR(5000, "Server error"),

    REDIRECT_ERROR(5001, "Error in request redirection"),

    FAILED(50000, "failed");

    private final int code;

    private final String msg;

    CommonCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
```

```
throw new BusinessException(CommonCode.ERROR_TOKEN);
```

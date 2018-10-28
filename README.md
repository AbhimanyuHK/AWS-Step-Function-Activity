# AWS-Step-Function-Activity
### AWS Step Function Definition.
``` 
{
  "Comment": "An example using a Task state.",
  "StartAt": "Hello",
  "Version": "1.0",
  "TimeoutSeconds": 300,
  "States":
  {
    "Hello": {
      "Type": "Task",
      "Resource": "arn:aws:states:us-west-2:337226440425:activity:Hello",
      "End": true
    }
  }
}
```

### Run AWS Step Funcation Activity
- Compile Project
$ ``` mvn clean install ```

- Run application
$ ``` mvn spring-boot:run```


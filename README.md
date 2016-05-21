![alt text](https://github.com/Bjond/bjond-utilities/blob/master/images/bjondhealthlogo-whitegrey.png "Bjönd Inc.")

[![][travis img]][travis]

# bjond-utilties

 Utilities common to most Bjönd System Software.


## Build

Build jar file within ./build/libs/bjond-utilities.jar

```shell
$ gradle all
```

## Tests

Test report will be inserted into ./build/reports/tests/index.html

```shell
$ gradle test
```


## Publish Maven locally for testing

This will generate the default Maven POM file within ./build/publications/maven/pom-default.xml

```shell
$ gradle publishToMavenLocal
```



## Documentation

To generate the javadoc that will appear in ./build/docs/javadoc/index.html

```shell
$ gradle javadoc
```



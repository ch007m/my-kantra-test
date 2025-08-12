## Instructions

The following instrcutions have been created according to the documentation and updated: https://github.com/konveyor/kantra/blob/main/docs/rules-quickstart.md#writing-rules

To play with a new rule and test it, it is needed first to download, install kantra

```shell
set VERSION v0.8.0-alpha.2

rm -rf {rulesets,static-report}
mkdir rulesets static-report
set ID $(podman create --name kantra-download quay.io/konveyor/kantra:$VERSION)
podman cp $ID:/usr/local/bin/darwin-kantra .
podman cp $ID:/usr/local/etc/maven.default.index .
podman cp $ID:/usr/bin/fernflower.jar .
podman cp $ID:/jdtls .
podman cp $ID:/usr/local/static-report .
podman cp $ID:/opt/rulesets .
podman rm kantra-download
mv darwin-kantra kantra
./kantra version
```

To test the rules or a rule, run the [testrunner](https://github.com/konveyor/kantra/blob/main/docs/testrunner.md#running-tests)
```shell
RUN_LOCAL=1 ./kantra test ./tests/rule.test.yaml
```

To analyze the coolstore project, execute this command and open the generated report
```shell
./kantra analyze --run-local --skip-static-report --enable-default-rulesets=false \
  -i ./tests/data/coolstore \
  -o ./output \
  --rules ./coolstore-rule/rule.yaml \
  --overwrite  
```
Check the ouput.log generated
```shell
cat output/analysis.log | grep "com.redhat.coolstore.service.ProductService"
time="2025-08-12T15:46:24+02:00" level=info msg="language server log" line="!MESSAGE KONVEYOR_LOG: got: 2 search matches for com.redhat.coolstore.service.ProductService location 12 matches2" provider=java
time="2025-08-12T15:46:24+02:00" level=info msg="Symbols retrieved" cap=referenced conditionInfo="{\"Referenced\":{\"Pattern\":\"com.redhat.coolstore.service.ProductService\",\"Location\":\"FIELD\",\"annotated\":{\"pattern\":\"javax.inject.Inject\"},\"Filepaths\":null}}" provider=java symbols=2
```




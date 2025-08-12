## Instructions

Get the latest kantra distribution and unzip it instead of doing a go build `go build -o kantra`
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

To analyze a sample project, execute this command and open the generated report
```shell
 ./kantra analyze -i ../samples/coolstore/ -o  ../generated/ --overwrite -t quarkus
```

To test the new rule
```shell
./kantra analyze --run-local --skip-static-report --enable-default-rulesets=false \
  -i ./tests/data/coolstore \
  -o ./output \
  --rules ./rulesets/sample/rule.yaml \
  --overwrite
```



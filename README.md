## Instructions

The following instructions have been created according to the documentation and updated: https://github.com/konveyor/kantra/blob/main/docs/rules-quickstart.md#writing-rules as some parts were not correct

To play with rulesets, add a new and test it, it is needed first to download and install the `kantra` CLI able to [analyze](https://github.com/konveyor/analyzer-lsp) an existing project and to generate an analysis report with issues able to guide the user towards the changes needed to migrate by example their Spring Boot application into a Quarkus one.

```shell
set VERSION v0.8.0-alpha.2
#set VERSION latest

cd distro
rm -rf {rulesets,static-report}
mkdir rulesets static-report
set ID $(podman create --name kantra-download quay.io/konveyor/kantra:$VERSION)
podman cp $ID:/usr/local/bin/darwin-kantra .
podman cp $ID:/usr/local/etc/maven.default.index .
podman cp $ID:/usr/bin/fernflower.jar .
podman cp $ID:/jdtls .
podman cp $ID:/usr/local/static-report .
podman rm kantra-download
mv darwin-kantra kantra
./kantra version
```
**Remark**: Due to a bug discovered since release `v0.8.0-alpha.2`, the `rulesets` folder must be empty - https://github.com/konveyor/kantra/issues/525

To test a new rule created, run the [testrunner](https://github.com/konveyor/kantra/blob/main/docs/testrunner.md#running-tests) and pass as parameter the path of the rule created
```shell
pushd distro
RUN_LOCAL=1 ./kantra test ../rules/coolstore/tests/000-coolstore-rule.test.yaml
popd
```

To analyze the `coolstore` rule project, execute this command where you pass the name fo the rule folder like the text to search about within the generated report
```shell
./scripts/analyze-rule.sh coolstore 000-coolstore-rule "ProductService"
```




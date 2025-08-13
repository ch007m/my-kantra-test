
RULE_NAME=${1}
TEXT_TO_SEARCH=${2}

echo "##################################"
echo "Rule to be analyzed is: $RULE_NAME"
echo "##################################"

pushd distro
rm ../output/analysis.log
./kantra analyze --run-local --skip-static-report --enable-default-rulesets=false \
  -i ../rules/coolstore/tests/data \
  -o ../output \
  --rules ../rules/coolstore/rule.yaml \
  --overwrite

echo "####################################################################"
echo "Text to search within the generated report: $TEXT_TO_SEARCH"
echo "####################################################################"
cat ../output/analysis.log | grep "$TEXT_TO_SEARCH"
popd
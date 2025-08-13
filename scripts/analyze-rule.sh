
RULE_FOLDER_NAME=${1}
RULE_ID=${2}
TEXT_TO_SEARCH=${3}

pushd distro
rm ../output/analysis.log

echo "##################################"
echo "Testing the rule: $RULE_FOLDER_NAME"
echo "##################################"
RUN_LOCAL=1 ./kantra test ../rules/$RULE_FOLDER_NAME/tests/$RULE_ID.test.yaml

echo "##################################"
echo "Analysing now the project using the test data..."
echo "##################################"
./kantra analyze --run-local --skip-static-report --enable-default-rulesets=false \
  -i ../rules/$RULE_FOLDER_NAME/tests/data \
  -o ../output \
  --rules ../rules/$RULE_FOLDER_NAME/$RULE_ID.yaml \
  --overwrite \
  --json-output

echo "####################################################################"
echo "Text to search within the generated report: $TEXT_TO_SEARCH"
echo "####################################################################"
cat ../output/output.json | jq \
  --arg ruleId $RULE_ID \
  --arg textToSearch $TEXT_TO_SEARCH \
  '.[].violations[$ruleId].incidents | .[] | select(.message | contains($textToSearch))'

popd
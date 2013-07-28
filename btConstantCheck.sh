#!/bin/zsh

echo -n "Running Delite..."
./delight dsl.reactive.ConvertConstantsRunner &>/dev/null
echo "DONE"

CONSTANTS=$(grep 'Constant' generated/scala/kernels/*.scala | wc -l)

echo -n "Result: "

if [[ "3" == "$CONSTANTS" ]]; then
    echo "SUCCESS"
else
    echo "FAILURE"
fi
